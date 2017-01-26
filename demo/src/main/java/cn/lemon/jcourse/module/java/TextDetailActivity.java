package cn.lemon.jcourse.module.java;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import cn.alien95.util.Utils;
import cn.lemon.common.base.ToolbarActivity;
import cn.lemon.common.base.presenter.RequirePresenter;
import cn.lemon.jcourse.R;
import cn.lemon.jcourse.config.Config;
import cn.lemon.jcourse.model.bean.JavaCourse;

import static cn.lemon.jcourse.R.id.star;

/**
 * Created by linlongxin on 2016/8/7.
 */

@RequirePresenter(TextDetailPresenter.class)
public class TextDetailActivity extends ToolbarActivity<TextDetailPresenter> implements View.OnTouchListener, GestureDetector.OnGestureListener, View.OnClickListener {

    private ImageView mCover;
    private TextView mTitle;
    private TextView mSubtitle;
    private TextView mContent;
    private TextView mStarNum;
    private TextView mVisitNum;
    private Button mExercise;
    private FloatingActionButton mStar;

    private float mStartY;
    private float mCurrentY;

    private GestureDetector mGestureDetector;

    private boolean hasVisible = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        useStatusPages(false);

        setContentView(R.layout.java_activity_text_detail);
        setToolbarHomeBack(true);

        mCover = $(R.id.cover);
        mTitle = $(R.id.title);
        mSubtitle = $(R.id.subtitle);
        mContent = $(R.id.content);
        mStarNum = $(R.id.star_num);
        mVisitNum = $(R.id.visit_num);
        mExercise = $(R.id.exercise);
        ScrollView mScrollView = (ScrollView) findViewById(R.id.scroll_view);
        mStar = $(star);
        mStar.setOnClickListener(this);
        mExercise.setOnClickListener(this);
        mGestureDetector = new GestureDetector(this, this);
        assert mScrollView != null;
        mScrollView.setOnTouchListener(this);

    }

    public void setData(JavaCourse data) {
        Glide.with(this)
                .load(data.cover)
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_load_error)
                .into(mCover);
        mTitle.setText(data.title);
        mSubtitle.setText(data.subtitle);
        mContent.setText(data.content);
        mStarNum.setText(data.starNum + "");
        mVisitNum.setText(data.visitNum + "");
    }

    public ImageView getStarView() {
        return mStar;
    }
    public void setStarNumDec(){
        String num = mStarNum.getText().toString();
        mStarNum.setText(Integer.parseInt(num) - 1 + "");
    }
    public void setStarNumAdd(){
        String num = mStarNum.getText().toString();
        mStarNum.setText(Integer.parseInt(num) + 1 + "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.star:
                getPresenter().starViewClick();
                break;
            case R.id.exercise:
                Intent intent = new Intent(this,ExerciseActivity.class);
                int courseId = getPresenter().getCourseId();
                if(courseId != -1){
                    intent.putExtra(Config.EXERCISE_ID,courseId);
                    startActivity(intent);
                }else {
                    Utils.Toast("无习题练习");
                }

        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        mStartY = e.getY();
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        mCurrentY = e2.getY();
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    // e1：第1个ACTION_DOWN MotionEvent
    // e2：最后一个ACTION_MOVE MotionEvent
    // velocityX：X轴上的移动速度（像素/秒）
    // velocityY：Y轴上的移动速度（像素/秒）
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        //向上划
        ObjectAnimator mAlphaAnimator;
        ObjectAnimator mTranslationYAnimator;
        ObjectAnimator mTranslationXAnimator;
        AnimatorSet mAnimatorSet;
        if (mCurrentY - mStartY < 0 && velocityY < 2000 && hasVisible && Math.abs(velocityY) > Math.abs(velocityX)) {
            mAlphaAnimator = ObjectAnimator.ofFloat(mStar, "alpha", 1f, 0f);
            mTranslationXAnimator = ObjectAnimator.ofFloat(mStar, "scaleX", 1f, 0f);
            mTranslationYAnimator = ObjectAnimator.ofFloat(mStar, "scaleY", 1f, 0f);
            mAnimatorSet = new AnimatorSet();
            mAnimatorSet.play(mTranslationYAnimator).with(mAlphaAnimator).with(mTranslationXAnimator);
            mAnimatorSet.setDuration(200);
            mAnimatorSet.start();
            mStar.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mStar.setVisibility(View.GONE);
                }
            }, 200);
            hasVisible = false;
            //向下划
        } else if (mCurrentY - mStartY > 0 && velocityY > 1500 && !hasVisible && Math.abs(velocityY) > Math.abs(velocityX)) {
            mStar.setVisibility(View.VISIBLE);
            mAlphaAnimator = ObjectAnimator.ofFloat(mStar, "alpha", 0f, 1f);
            mTranslationXAnimator = ObjectAnimator.ofFloat(mStar, "scaleX", 0f, 1f);
            mTranslationYAnimator = ObjectAnimator.ofFloat(mStar, "scaleY", 0f, 1f);
            mAnimatorSet = new AnimatorSet();
            mAnimatorSet.play(mTranslationYAnimator).with(mAlphaAnimator).with(mTranslationXAnimator);
            mAnimatorSet.setDuration(200);
            mAnimatorSet.start();
            hasVisible = true;
        }
        return false;
    }
}
