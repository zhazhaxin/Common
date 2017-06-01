package cn.lemon.jcourse.module.java;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.lemon.util.Utils;
import cn.lemon.jcourse.R;
import cn.lemon.jcourse.config.Config;
import cn.lemon.jcourse.model.bean.Exercise;
import cn.lemon.view.adapter.BaseViewHolder;

/**
 * Created by linlongxin on 2017.1.22.
 */

public class ExerciseAdapter extends RecyclerView.Adapter {

    private final String TAG = "ExerciseAdapter";
    private final int TYPE_SUBMIT = 999;
    private boolean isCheckAnswer = false;
    private List<Exercise> mData = new ArrayList<>();
    private String CHECK_ANSWER = "查看答案";
    private String HIDE_ANSWER = "隐藏答案";

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_SUBMIT) {
            Button button = new Button(parent.getContext());
            button.setTextColor(Config.Color.WHITE);
            button.setText(CHECK_ANSWER);
            button.setBackgroundResource(R.drawable.bg_button_login);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            int dpLength = Utils.dip2px(16);
            params.setMargins(dpLength, dpLength, dpLength, dpLength);
            button.setLayoutParams(params);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 隐藏答案
                    if(isCheckAnswer){
                        ((Button)v).setText(CHECK_ANSWER);
                        isCheckAnswer = false;
                    }else {
                        ((Button)v).setText(HIDE_ANSWER);
                        isCheckAnswer = true;
                    }
                    notifyDataSetChanged();
                }
            });
            return new BaseViewHolder(button);
        }
        return new ExerciseViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ExerciseViewHolder) {
            ((ExerciseViewHolder) holder).setData(mData.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return TYPE_SUBMIT;
        }
        return super.getItemViewType(position);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void addAll(Exercise[] data) {
        mData.addAll(Arrays.asList(data));
    }

    /**
     * 单选或者多选Item
     */
    private class ExerciseViewHolder extends BaseViewHolder<Exercise> {

        private TextView mTitle;
        private RadioGroup mRadioGroup;
        private RadioButton A, B, C, D;

        ExerciseViewHolder(ViewGroup parent) {
            super(parent, R.layout.java_holder_exercise);
        }

        @Override
        public void onInitializeView() {
            super.onInitializeView();
            mRadioGroup = findViewById(R.id.radio_group);
            mTitle = findViewById(R.id.title);
            A = findViewById(R.id.radio_a);
            B = findViewById(R.id.radio_b);
            C = findViewById(R.id.radio_c);
            D = findViewById(R.id.radio_d);
        }

        @Override
        public void setData(final Exercise data) {
            mTitle.setText(data.title);
            mRadioGroup.clearCheck();
            List<String> contentList = parseContent(data.contentList);
            A.setText(contentList.get(0));
            A.setChecked(data.selectIds[0]);
            B.setText(contentList.get(1));
            B.setChecked(data.selectIds[1]);
            C.setText(contentList.get(2));
            C.setChecked(data.selectIds[2]);
            D.setText(contentList.get(3));
            D.setChecked(data.selectIds[3]);

            if (isCheckAnswer) {
                setAnswer(parseAnswer(data.answer).get(0));
            }else {
                clearAnswer();
            }

            mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.radio_a:
                            data.selectIds[0] = true;
                            break;
                        case R.id.radio_b:
                            data.selectIds[1] = true;
                            break;
                        case R.id.radio_c:
                            data.selectIds[2] = true;
                            break;
                        case R.id.radio_d:
                            data.selectIds[3] = true;
                            break;
                    }
                }
            });
        }

        private void setAnswer(int answer) {
            clearAnswer();
            switch (answer) {
                case 0:
                    A.setTextColor(Config.Color.GREEN);
                    break;
                case 1:
                    B.setTextColor(Config.Color.GREEN);
                    break;
                case 2:
                    C.setTextColor(Config.Color.GREEN);
                    break;
                case 3:
                    D.setTextColor(Config.Color.GREEN);
                    break;
            }
        }
        private void clearAnswer(){
            A.setTextColor(Config.Color.TEXT_ANSWER_COLOR);
            B.setTextColor(Config.Color.TEXT_ANSWER_COLOR);
            C.setTextColor(Config.Color.TEXT_ANSWER_COLOR);
            D.setTextColor(Config.Color.TEXT_ANSWER_COLOR);
        }
    }

    private List<String> parseContent(String content) {
        if (content != null) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<String>>() {
            }.getType();
            return gson.fromJson(content, listType);
        } else
            return null;
    }


    private List<Integer> parseAnswer(String answer) {
        if (answer != null) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Integer>>() {
            }.getType();
            return gson.fromJson(answer, listType);
        } else
            return null;
    }

}
