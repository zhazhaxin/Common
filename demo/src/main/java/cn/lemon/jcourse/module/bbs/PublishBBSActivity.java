package cn.lemon.jcourse.module.bbs;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.alien95.util.Utils;
import cn.lemon.common.base.ToolbarActivity;
import cn.lemon.common.base.presenter.RequirePresenter;
import cn.lemon.jcourse.R;
import cn.lemon.jcourse.config.Config;
import cn.lemon.jcourse.model.AccountModel;
import cn.lemon.jcourse.model.net.GlideCircleTransform;
import cn.lemon.multi.MultiView;
import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * Created by linlongxin on 2016/9/16.
 */

@RequirePresenter(PublishBBSPresenter.class)
public class PublishBBSActivity extends ToolbarActivity<PublishBBSPresenter>
        implements View.OnClickListener {

    private ImageView mAvatar;
    private ImageView mAddImage;
    private EditText mTitle;
    private EditText mContent;
    private MultiView mMultiView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bbs_activity_publish);
        setToolbarHomeBack(true);

        mAvatar = $(R.id.avatar);
        mAddImage = $(R.id.add_img);
        mTitle = $(R.id.title);
        mContent = $(R.id.content);
        mMultiView = $(R.id.multi_view);

        Glide.with(this)
                .load(AccountModel.getInstance().getAccount().avatar)
                .transform(new GlideCircleTransform(this))
                .into(mAvatar);
        mAddImage.setOnClickListener(this);
        Utils.hideSoftInput(this);
    }

    public void setBitmaps(List<Bitmap> bitmaps) {
        mMultiView.clear();
        mMultiView.setBitmaps(bitmaps);
    }

    //选取图片
    public void selectImage() {
        showDialog("请选择图片", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MultiImageSelector.create(PublishBBSActivity.this)
                        .count(20)
                        .start(PublishBBSActivity.this, Config.REQUEST_IMAGE_CODE);
                dismissDialog();
            }
        });
    }

    public void publish() {
        Utils.hideSoftInput(this);
        getPresenter().publishBBS(mTitle.getText().toString(), mContent.getText().toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bbs_publish, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.publish) {
            publish();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_img:
                selectImage();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Config.REQUEST_IMAGE_CODE && resultCode == RESULT_OK) {
            List<String> paths = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
            getPresenter().dealPictures(paths);
        }
    }
}
