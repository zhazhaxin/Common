package cn.lemon.jcourse.module.java;

import android.content.Context;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import cn.lemon.jcourse.R;
import cn.lemon.jcourse.model.bean.JVideo;
import cn.lemon.view.adapter.BaseViewHolder;
import cn.lemon.view.adapter.RecyclerAdapter;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by linlongxin on 2016/9/14.
 */

public class JavaVideoAdapter extends RecyclerAdapter<JVideo> {

    private Context mContext;

    public JavaVideoAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public BaseViewHolder<JVideo> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return new VideoViewHolder(parent);
    }

    class VideoViewHolder extends BaseViewHolder<JVideo>{

        private JCVideoPlayerStandard mVideoPlayer;

        public VideoViewHolder(ViewGroup parent) {
            super(parent, R.layout.java_holder_video);
        }

        @Override
        public void onInitializeView() {
            super.onInitializeView();
            mVideoPlayer = findViewById(R.id.video_player);
        }

        @Override
        public void setData(JVideo object) {
            super.setData(object);
            mVideoPlayer.setUp(object.url,
                    JCVideoPlayer.SCREEN_LAYOUT_LIST,
                    object.title + " - " + object.subtitle);
            Glide.with(mContext)
                    .load(object.cover)
                    .into(mVideoPlayer.thumbImageView);
        }
    }
}
