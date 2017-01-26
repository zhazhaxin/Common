package cn.lemon.jcourse.module.java;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
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

public class ExerciseAdapter extends RecyclerAdapter {

    private final String TAG = "ExerciseAdapter";
    private final int TYPE_SUBMIT = 999;
    private boolean isCommitAnswer = false;

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
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isCommitAnswer = true;
                    notifyDataSetChanged();
                    Log.i(TAG, "ButtonViewHolder -- onItemViewClick");
                }
            });
            return new BaseViewHolder(button);
        }
        return new ExerciseViewHolder(parent);
    }

    /**
     * 单选或者多选Item
     */
    private class ExerciseViewHolder extends BaseViewHolder<Exercise> {

        private TextView mTitle;
        private RadioGroup mRadioGroup;
        private RadioButton A, B, C, D;

        ExerciseViewHolder(ViewGroup parent) {
            super(parent, R.layout.java_item_exercise);
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
        public void setData(final Exercise object) {
            super.setData(object);
            mTitle.setText(object.title);
            List<String> contentList = parseContent(object.contentList);
            A.setText(contentList.get(0));
            B.setText(contentList.get(1));
            C.setText(contentList.get(2));
            D.setText(contentList.get(3));
            mRadioGroup.clearCheck();
            if (object.choice != null && object.choice.size() > 0) {
                switch (object.choice.get(0)) {
                    case 0:
                        A.setChecked(true);
                        break;
                    case 1:
                        B.setChecked(true);
                        break;
                    case 2:
                        C.setChecked(true);
                        break;
                    case 3:
                        D.setChecked(true);
                        break;
                }
            }
            int answer = parseAnswer(object.answer).get(0);
            if (isCommitAnswer) {
                switch (answer) {
                    case 0:
                        A.setTextColor(Config.Color.BLUE);
                        break;
                    case 1:
                        B.setTextColor(Config.Color.BLUE);
                        break;
                    case 2:
                        C.setTextColor(Config.Color.BLUE);
                        break;
                    case 3:
                        D.setTextColor(Config.Color.BLUE);
                        break;
                }
            }
            mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (object.choice == null) {
                        object.choice = new ArrayList<>();
                    }
                    if (object.choice.size() > 0) {
                        object.choice.remove(0);
                    }
                    switch (checkedId) {
                        case R.id.radio_a:
                            object.choice.add(0);
                            break;
                        case R.id.radio_b:
                            object.choice.add(1);
                            break;
                        case R.id.radio_c:
                            object.choice.add(2);
                            break;
                        case R.id.radio_d:
                            object.choice.add(3);
                            break;
                    }
                }
            });
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
        //复用
        boolean isReuse = parent.getChildCount() > 0;
        //多选
        if (isMultiple) {
            CheckBox[] checkBoxes = null;
            if (isReuse) {
                checkBoxes = new CheckBox[4];
                parent.clearCheck();
                for (int i = 0; i < 4; i++) {
                    CheckBox ch = (CheckBox) parent.getChildAt(i);
                    ch.setTextColor(Color.BLACK);
                    checkBoxes[i] = ch;
                }
                parent.removeAllViews();
            }
            for (int i = 0; i < num; i++) {
                CheckBox checkBox;
                if (isReuse) {
                    checkBox = checkBoxes[i];
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
        } else {
            //单选
            RadioButton[] buttons = null;
            //复用
            if (isReuse) {
                buttons = new RadioButton[4];
                for (int i = 0; i < 4; i++) {
                    RadioButton ra = (RadioButton) parent.getChildAt(i);
                    ra.setChecked(false);
                    ra.setTextColor(Color.BLACK);
                    parent.setOnCheckedChangeListener(null);
                    buttons[i] = ra;
                }
                parent.removeAllViews();
            }
            int answer = parseAnswer(exercise.answer).get(0);
            for (int i = 0; i < num; i++) {
                RadioButton button;
                if (isReuse) {
                    button = buttons[i];
                } else {
                    button = new RadioButton(context);
                    button.setLayoutParams(params);
                }
                button.setText(data.get(i));
                parent.addView(button);
                if (exercise.choice != null) {
                    if (exercise.choice.get(0) == i) {
                        Log.i(TAG, "choice : " + exercise.choice.get(0));
                        button.setChecked(true);
                    }
                    if (answer == i && isCommitAnswer) {
                        button.setTextColor(Config.Color.BLUE);
                    }
                }
                parent.setOnCheckedChangeListener(new ChangeListener(i, exercise));
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

    private class ChangeListener implements RadioGroup.OnCheckedChangeListener {

        private int mChoice;
        private Exercise mExercise;

        public ChangeListener(int mChoice, Exercise mExercise) {
            this.mChoice = mChoice;
            this.mExercise = mExercise;
        }


        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            Log.i(TAG, "onCheckedChanged");
            if (mExercise.choice == null) {
                mExercise.choice = new ArrayList<>();
            }
            int childCount = group.getChildCount();
            for (int i = 0; i < childCount; i++) {
                int id = group.getChildAt(i).getId();
                Log.i(TAG, "checkedId : " + id);
                if (checkedId == id) {
                    if (mExercise.choice.size() > 0) {
                        mExercise.choice.remove(0);
                    }
                    mExercise.choice.add(mChoice);
                }
            }
        }
    }
}
