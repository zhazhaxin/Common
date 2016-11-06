package cn.lemon.common.base.widget;

import android.content.Context;
import android.support.v7.app.AppCompatDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import cn.lemon.common.R;


/**
 * Created by linlongxin on 2016/10/25.
 */

public class InputDialog extends AppCompatDialog {

    private Context mContext;

    private TextView mTitle;
    private EditText mInput;
    private TextView mPassiveText;
    private TextView mPositiveText;

    public InputDialog(Context context) {
        this(context, cn.lemon.common.R.style.MaterialDialogStyle);
    }

    public InputDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
        initView();
    }

    protected InputDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        setCancelable(cancelable);
        setOnCancelListener(cancelListener);
    }

    public void initView(){
        View dialogContent = LayoutInflater.from(mContext).inflate(R.layout.dialog_input, null);

        mTitle = (TextView) dialogContent.findViewById(R.id.title);
        mInput = (EditText) dialogContent.findViewById(R.id.input_content);
        mPassiveText = (TextView) dialogContent.findViewById(R.id.passive_button);
        mPositiveText = (TextView) dialogContent.findViewById(R.id.positive_button);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(dialogContent,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setPassiveClickListener(null);
    }

    public void setPositiveClickListener(final OnClickListener listener) {
        mPositiveText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(InputDialog.this, 0);
                }
            }
        });
    }

    public void setPassiveClickListener(final OnClickListener listener) {
        mPassiveText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(InputDialog.this, 1);
                }
            }
        });
    }

    public String getContent(){
        return mInput.getText().toString();
    }

    public EditText getInputView(){
        return mInput;
    }

    public TextView getPassiveButton(){
        return mPassiveText;
    }

    public TextView getPositiveText(){
        return mPositiveText;
    }

    public void setTitle(String content){
        mTitle.setText(content);
    }

    public void setHint(String content){
        mInput.setHint(content);
    }

    public void setContent(String content){
        mInput.setText(content);
    }

    public String getTitle(){
        return mTitle.getText().toString();
    }

}
