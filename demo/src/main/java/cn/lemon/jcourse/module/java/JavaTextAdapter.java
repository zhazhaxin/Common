package cn.lemon.jcourse.module.java;

import android.content.Context;
import android.content.Intent;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import cn.lemon.jcourse.R;
import cn.lemon.jcourse.config.Config;
import cn.lemon.jcourse.model.bean.JavaCourse;
import cn.lemon.view.adapter.BaseViewHolder;
import cn.lemon.view.adapter.RecyclerAdapter;

class JavaTextAdapter extends RecyclerAdapter<JavaCourse> {

    public JavaTextAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder<JavaCourse> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return new JavaTextViewHolder(parent);
    }

    static class JavaTextViewHolder extends BaseViewHolder<JavaCourse> {

        private ImageView mCover;
        private TextView mTitle;
        private TextView mSubtitle;
        private TextView mContent;

        public JavaTextViewHolder(ViewGroup parent) {
            super(parent, R.layout.main_holder_java_text);
        }

        @Override
        public void onInitializeView() {
            mTitle = findViewById(R.id.title);
            mSubtitle = findViewById(R.id.subtitle);
            mCover = findViewById(R.id.cover);
            mContent = findViewById(R.id.content);
        }

        @Override
        public void setData(JavaCourse object) {
            super.setData(object);
            mTitle.setText(object.title);
            mSubtitle.setText(object.subtitle);
            mContent.setText(object.content);
            Glide.with(mTitle.getContext())
                    .load(object.cover)
                    .placeholder(R.drawable.ic_place_holder)
                    .error(R.drawable.ic_load_error)
                    .into(mCover);
        }

        @Override
        public void onItemViewClick(JavaCourse data) {
            Intent intent = new Intent(itemView.getContext(), JavaTextDetailActivity.class);
            intent.putExtra(Config.JAVA_COURSE_DETAIL, data);
            itemView.getContext().startActivity(intent);
        }
    }
}

