package cn.lemon.jcourse.module.java;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import cn.lemon.common.base.presenter.RequirePresenter;
import cn.lemon.common.base.ToolbarActivity;
import cn.lemon.jcourse.R;
import cn.lemon.jcourse.config.Config;
import cn.lemon.jcourse.model.bean.JavaCourse;
import cn.lemon.view.RefreshRecyclerView;
import cn.lemon.view.adapter.Action;
import cn.lemon.view.adapter.BaseViewHolder;
import cn.lemon.view.adapter.RecyclerAdapter;

@RequirePresenter(CourseUnitListPresenter.class)
public class CourseUnitListActivity extends ToolbarActivity<CourseUnitListPresenter> {

    private JavaDirAdapter mAdapter;
    private RefreshRecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        useStatusPages(true);
        setContentView(R.layout.java_activity_course_unit_list);

        mAdapter = new JavaDirAdapter(this);
        mRecyclerView = (RefreshRecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setRefreshAction(new Action() {
            @Override
            public void onAction() {
                getPresenter().getData(true);
            }
        });
        mRecyclerView.setLoadMoreAction(new Action() {
            @Override
            public void onAction() {
                getPresenter().getData(false);
            }
        });
    }

    public JavaDirAdapter getAdapter(){
        return mAdapter;
    }
    public RefreshRecyclerView getRecyclerView(){
        return mRecyclerView;
    }

    class JavaDirAdapter extends RecyclerAdapter<JavaCourse> {

        public JavaDirAdapter(Context context) {
            super(context);
        }

        @Override
        public BaseViewHolder<JavaCourse> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
            return new JavaDirViewHolder(parent);
        }
    }

    class JavaDirViewHolder extends BaseViewHolder<JavaCourse> {

        private ImageView mCover;
        private TextView mSubitle;

        public JavaDirViewHolder(ViewGroup parent) {
            super(parent, R.layout.java_holder_dir);
        }

        @Override
        public void onInitializeView() {
            super.onInitializeView();
            mCover = findViewById(R.id.cover);
            mSubitle = findViewById(R.id.subtitle);
        }

        @Override
        public void setData(JavaCourse object) {
            super.setData(object);
            Glide.with(itemView.getContext())
                    .load(object.cover)
                    .into(mCover);
            mSubitle.setText(object.subtitle);
        }

        @Override
        public void onItemViewClick(JavaCourse object) {
            super.onItemViewClick(object);
            Intent intent = new Intent(itemView.getContext(), TextDetailActivity.class);
            intent.putExtra(Config.JAVA_COURSE_DETAIL, object);
            startActivity(intent);
        }
    }
}
