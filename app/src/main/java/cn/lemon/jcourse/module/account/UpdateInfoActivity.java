package cn.lemon.jcourse.module.account;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;

import cn.alien95.util.Utils;
import cn.lemon.common.base.ToolbarActivity;
import cn.lemon.jcourse.R;
import cn.lemon.jcourse.config.Config;
import cn.lemon.jcourse.model.AccountModel;
import cn.lemon.jcourse.model.bean.Account;
import cn.lemon.jcourse.model.bean.Info;
import cn.lemon.jcourse.model.ServiceResponse;
import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class UpdateInfoActivity extends ToolbarActivity implements View.OnClickListener {

    private TextInputLayout mName;
    private TextInputLayout mSign;
    private ImageView mAvatar;
    private Button mSubmit;
    private String uploadImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_activity_update_info);

        mAvatar = (ImageView) findViewById(R.id.avatar);
        mName = (TextInputLayout) findViewById(R.id.name);
        mSign = (TextInputLayout) findViewById(R.id.sign);
        mSubmit = (Button) findViewById(R.id.submit);

        mAvatar.setOnClickListener(this);
        mSubmit.setOnClickListener(this);

        initData();
    }

    public void initData() {
        Glide.with(this)
                .load(AccountModel.getInstance().getAccount().avatar)
                .placeholder(R.drawable.ic_upload)
                .into(mAvatar);
        mName.getEditText().setText(AccountModel.getInstance().getAccount().name);
        mSign.getEditText().setText(AccountModel.getInstance().getAccount().sign);
    }

    public void selectImage() {
        showDialog("请选择图片", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MultiImageSelector.create(UpdateInfoActivity.this)
                        .single()
                        .start(UpdateInfoActivity.this, Config.REQUEST_IMAGE_CODE);
                dismissDialog();
            }
        });
    }

    public void updateAccountInfo(Account account) {
        AccountModel.getInstance().updateAccountInfo(account.name, account.sign, account.avatar, new ServiceResponse<Account>() {
            @Override
            public void onNext(Account account) {
                super.onNext(account);
                AccountModel.getInstance().saveAccount(account);
                Utils.Toast("更新成功");
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.avatar:
                selectImage();
                break;
            case R.id.submit:
                Account account = AccountModel.getInstance().getAccount();
                if (!TextUtils.isEmpty(uploadImageUrl)) {
                    account.avatar = uploadImageUrl;
                }
                if (!TextUtils.isEmpty(mName.getEditText().getText().toString())) {
                    account.name = mName.getEditText().getText().toString();
                }
                if (!TextUtils.isEmpty(mSign.getEditText().getText().toString())) {
                    account.sign = mSign.getEditText().getText().toString();
                }
                updateAccountInfo(account);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri imageUri;
        if (requestCode == Config.REQUEST_IMAGE_CODE) {
            if (resultCode == RESULT_OK) {
                final String image = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT).get(0);
                imageUri = Uri.parse("file://" + image);
                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                final Uri resultUri = result.getUri();
                mAvatar.setImageURI(resultUri);
                final StringBuilder imagePath = new StringBuilder(String.valueOf(resultUri));
                imagePath.delete(0,7); //删除file://
                AccountModel.getInstance().updateAvatar(new File(imagePath.toString()), new ServiceResponse<Info>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        showLoadingDialog();
                    }

                    @Override
                    public void onNext(Info info) {
                        super.onNext(info);
                        dismissLoadingDialog();
                        Utils.Toast("上传成功");
                        uploadImageUrl = Config.CACEH_IAMGE + new File(imagePath.toString()).getName();
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Utils.Log(result.getError().getMessage());
                Utils.Toast("剪切失败");
            }

        }
    }
}
