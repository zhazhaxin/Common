package cn.lemon.jcourse.module.account;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
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

import cn.lemon.util.Utils;
import cn.lemon.common.base.ToolbarActivity;
import cn.lemon.common.net.ServiceResponse;
import cn.lemon.jcourse.R;
import cn.lemon.jcourse.config.Config;
import cn.lemon.jcourse.model.AccountModel;
import cn.lemon.jcourse.model.bean.Account;
import cn.lemon.jcourse.model.bean.Picture;
import io.reactivex.disposables.Disposable;
import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class UpdateInfoActivity extends ToolbarActivity implements View.OnClickListener {

    private TextInputLayout mName;
    private TextInputLayout mSign;
    private ImageView mAvatar;
    private Button mSubmit;
    private String mAvatarUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        useStatusPages(false);
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
                .load(mAvatarUrl = AccountModel.getInstance().getAccount().avatar)
                .placeholder(R.drawable.ic_upload)
                .into(mAvatar);
        mName.getEditText().setText(AccountModel.getInstance().getAccount().name);
        mSign.getEditText().setText(AccountModel.getInstance().getAccount().sign);
    }

    public void selectImage() {
        showDialog("请选择图片", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MultiImageSelector.create()
                        .single()
                        .start(UpdateInfoActivity.this, Config.REQUEST_IMAGE_CODE);
                dismissDialog();
            }
        });
    }

    public void updateAccountInfo(String name, String sign, String avatar) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(sign) || TextUtils.isEmpty(avatar)) {
            Utils.Toast("信息不能为空");
            return;
        }
        Account account = AccountModel.getInstance().getAccount();
        if (name.equals(account.name) && sign.equals(account.sign) && avatar.equals(account.avatar)) {
            Utils.Toast("信息没有更新");
            return;
        }
        AccountModel.getInstance().updateAccountInfo(name, sign, avatar, new ServiceResponse<Account>() {
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
                updateAccountInfo(mName.getEditText().getText().toString(),
                        mSign.getEditText().getText().toString(),
                        mAvatarUrl);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri imageUri;
        //选择图片
        if (requestCode == Config.REQUEST_IMAGE_CODE) {
            if (resultCode == RESULT_OK) {
                final String image = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT).get(0);
                imageUri = Uri.parse("file://" + image);
                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
            }
        }
        //剪切图片
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                final Uri resultUri = result.getUri();
                mAvatar.setImageURI(resultUri);
                final StringBuilder imagePath = new StringBuilder(String.valueOf(resultUri));
                imagePath.delete(0, 7); //删除file://
                BitmapFactory.Options op = new BitmapFactory.Options();
                op.inJustDecodeBounds = true;  //不分配内存设置
                BitmapFactory.decodeFile(imagePath.toString(), op);
                int width = op.outWidth;
                int height = op.outHeight;
                //上传图片
                AccountModel.getInstance().updateAvatar(new File(imagePath.toString()), width, height, new ServiceResponse<Picture>() {
                    @Override
                    public void onStart() {
                        showLoadingDialog();
                    }

                    @Override
                    public void onNext(Picture picture) {
                        super.onNext(picture);
                        dismissLoadingDialog();
                        Utils.Toast("上传成功");
                        mAvatarUrl = Config.SERVER_IMAGE_PATH + new File(imagePath.toString()).getName();
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Utils.Log(result.getError().getMessage());
                Utils.Toast("剪切失败");
            }

        }
    }
}
