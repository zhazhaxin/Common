package cn.lemon.common.base.widget;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import cn.lemon.common.R;

/**
 * MaterialDialog 提示框，可设置title，content，两个按钮监听器
 * Created by linlongxin on 2016/8/3.
 */

public class MaterialDialog extends AppCompatDialog {

    private final String TAG = "MaterialDialog";
    private TextView mTitle;
    private TextView mContent;
    private TextView mPositiveButton;
    private TextView mPassiveButton;

    public MaterialDialog(Context context) {
        this(context, R.style.MaterialDialogStyle);
        Log.i(TAG, "MaterialDialog");
    }

    public MaterialDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected MaterialDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        setCancelable(cancelable);
        setOnCancelListener(cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);

    }

    public void setTitle(CharSequence title) {
        if (!TextUtils.isEmpty(title)) {
            mTitle.setText(title);
        }
    }

    private void setContent(CharSequence content) {
        if (!TextUtils.isEmpty(content)) {
            mContent.setText(content);
            mContent.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 确认按钮监听器
     */
    private void setPositiveClickListener(final OnClickListener listener) {
        mPositiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(MaterialDialog.this, 0);
                }
            }
        });
    }

    /**
     * 取消安装监听器
     */
    private void setPassiveClickListener(final OnClickListener listener) {
        mPassiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (listener != null) {
                    listener.onClick(MaterialDialog.this, 1);
                }
            }
        });
    }

    public CharSequence getTitle() {
        return mTitle.getText();
    }

    public CharSequence getContent() {
        return mContent.getText();
    }

    private static class MaterialParam {

        boolean cancelable = true;
        CharSequence mTitle;
        CharSequence mPositiveText;
        CharSequence mPassiveText;
        OnClickListener mPositiveListener;
        OnClickListener mPassiveListener;
    }

    public static class Builder {

        private MaterialParam mMaterialParam;
        private Context mContext;

        public Builder(Context context) {
            mMaterialParam = new MaterialParam();
            mContext = context;
        }

        public Builder setTitle(CharSequence title) {
            mMaterialParam.mTitle = title;
            return this;
        }

        public Builder setPositiveText(CharSequence text) {
            mMaterialParam.mPositiveText = text;
            return this;
        }

        public Builder setPassiveText(CharSequence text) {
            mMaterialParam.mPassiveText = text;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            mMaterialParam.cancelable = cancelable;
            return this;
        }

        public Builder setOnPositiveClickListener(OnClickListener listener) {
            mMaterialParam.mPositiveListener = listener;
            return this;
        }

        public Builder setOnPassiveClickListener(OnClickListener listener) {
            mMaterialParam.mPassiveListener = listener;
            return this;
        }

        public MaterialDialog create() {
            MaterialDialog dialog = new MaterialDialog(mContext);
            View dialogContent = LayoutInflater.from(mContext).inflate(R.layout.dialog_material, null);

            dialog.mTitle = (TextView) dialogContent.findViewById(R.id.title);
            dialog.mContent = (TextView) dialogContent.findViewById(R.id.content);
            dialog.mPositiveButton = (TextView) dialogContent.findViewById(R.id.positive_button);
            dialog.mPassiveButton = (TextView) dialogContent.findViewById(R.id.passive_button);

            if (!TextUtils.isEmpty(mMaterialParam.mPositiveText)) {
                dialog.mPositiveButton.setText(mMaterialParam.mPositiveText);
            }
            if (!TextUtils.isEmpty(mMaterialParam.mPassiveText)) {
                dialog.mPassiveButton.setText(mMaterialParam.mPassiveText);
            }
            if (!TextUtils.isEmpty(mMaterialParam.mTitle)) {
                dialog.setTitle(mMaterialParam.mTitle);
            }
            dialog.setPositiveClickListener(mMaterialParam.mPositiveListener);
            dialog.setPassiveClickListener(mMaterialParam.mPassiveListener);
            dialog.setCancelable(mMaterialParam.cancelable);

            dialog.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(dialogContent,
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return dialog;
        }

        public void show() {
            create().show();
        }

    }
}
