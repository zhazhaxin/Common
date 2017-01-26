package cn.lemon.jcourse.module.java;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.alien95.util.Utils;
import cn.lemon.jcourse.R;
import cn.lemon.jcourse.config.Config;
import cn.lemon.jcourse.model.bean.Exercise;
import cn.lemon.view.adapter.BaseViewHolder;
import cn.lemon.view.adapter.RecyclerAdapter;

/**
 * Created by linlongxin on 2017.1.22.
 */

public class ExerciseAdapter extends RecyclerAdapter implements CompoundButton.OnCheckedChangeListener {

    private final int TYPE_SUBMIT = 999;

    ExerciseAdapter(Context context) {
        super(context);
    }


    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return TYPE_SUBMIT;
        }
        return super.getItemViewType(position);

    }

    @Override
    public BaseViewHolder onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_SUBMIT) {
            Button button = new Button(parent.getContext());
            button.setTextColor(Config.Color.WHITE);
            button.setText("提交");
            button.setBackgroundResource(R.drawable.bg_button_login);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            int dpLength = Utils.dip2px(16);
            params.setMargins(dpLength, dpLength, dpLength, dpLength);
            button.setLayoutParams(params);
            return new ButtonViewHolder(button);
        }
        return new ExerciseViewHolder(parent);
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    /**
     * 单选或者多选Item
     */
    private class ExerciseViewHolder extends BaseViewHolder<Exercise> {

        private TextView mTitle;
        private RadioGroup mRadioGroup;

        ExerciseViewHolder(ViewGroup parent) {
            super(parent, R.layout.java_item_exercise);
        }

        @Override
        public void onInitializeView() {
            super.onInitializeView();
            mRadioGroup = findViewById(R.id.radio_group);
            mTitle = findViewById(R.id.title);
        }

        @Override
        public void setData(Exercise object) {
            super.setData(object);
            mTitle.setText(object.title);
            addRadioButtons(mRadioGroup, object, object.isMultipleChoice == 1);
        }
    }

    /**
     * 提交答案Item
     */
    private class ButtonViewHolder extends BaseViewHolder {

        public ButtonViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onItemViewClick(Object object) {
            super.onItemViewClick(object);
            notifyDataSetChanged();
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

    /**
     * 正确答案用蓝色标记，错误的用红色标记
     */
    private void addRadioButtons(RadioGroup parent, final Exercise exercise, boolean isMultiple) {
        Context context = parent.getContext();
        List<String> data = parseContent(exercise.contentList);
        int num = data.size();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int dpLength = Utils.dip2px(8);
        params.setMargins(dpLength, dpLength, dpLength, dpLength);
        //多选
        boolean isRecycle = parent.getChildCount() > 0;
        if (isMultiple) {
            for (int i = 0; i < num; i++) {
                CheckBox checkBox;
                if (isRecycle) {
                    checkBox = (CheckBox) parent.getChildAt(i);
                } else {
                    checkBox = new CheckBox(context);
                    checkBox.setLayoutParams(params);
                }
                checkBox.setText(data.get(i));
                parent.addView(checkBox);
                final int finalI = i;
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            exercise.choice.add(finalI);
                        } else {
                            exercise.choice.remove(finalI);
                        }
                    }
                });
                if (exercise.choice == null) {
                    exercise.choice = new ArrayList<>();
                } else if (exercise.choice.size() > 0) {
                    for (int ch : exercise.choice) {
                        if (ch == i) {
                            checkBox.setChecked(true);
                            checkBox.setTextColor(Config.Color.RED);
                        }
                    }
                    for (int an : parseAnswer(exercise.answer)) {
                        if (an == i) {
                            checkBox.setTextColor(Config.Color.BLUE);
                        }
                    }
                }
            }
        } else
            for (int i = 0; i < num; i++) {
                RadioButton button;
                if (isRecycle) {
                    button = (RadioButton) parent.getChildAt(i);
                } else {
                    button = new RadioButton(context);
                    button.setLayoutParams(params);
                }
                button.setText(data.get(i));
                parent.addView(button);
                final int finalI = i;
                button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            exercise.choice.add(0, finalI);
                        }
                    }
                });
                if (exercise.choice == null) {
                    exercise.choice = new ArrayList<>();
                } else if (exercise.choice.size() > 0) {
                    if (exercise.choice.get(0) == i) {
                        button.setChecked(true);
                    } else if (exercise.choice.get(0) != i && parseAnswer(exercise.answer).get(0) == i) {
                        button.setTextColor(Config.Color.BLUE);
                    }
                }
            }
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
