package cn.lemon.jcourse.module.bbs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import cn.lemon.common.base.ToolbarActivity;
import cn.lemon.common.base.presenter.RequirePresenter;
import cn.lemon.jcourse.R;
import cn.lemon.jcourse.model.AccountModel;
import cn.lemon.jcourse.model.net.GlideCircleTransform;
import cn.lemon.multi.MultiView;

/**
 * Created by linlongxin on 2016/9/16.
 */

@RequirePresenter(PublishBBSPresenter.class)
public class PublishBBSActivity extends ToolbarActivity<PublishBBSPresenter> {

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bbs_publish,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.publish){

        }
        return super.onOptionsItemSelected(item);

    }
}
