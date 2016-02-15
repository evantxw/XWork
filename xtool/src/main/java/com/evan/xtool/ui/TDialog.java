package com.evan.xtool.ui;

import android.app.Dialog;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.evan.xtool.R;

/**
 * the Customer Dialog
 */
public class TDialog extends Dialog {

    private ImageView dialog_iv; //dialog显示图片
    private TextView dialog_tv; //dialog显示文本

    public TDialog(Context context) {
        this(context, 0);
    }

    public TDialog(Context context, int theme) {
        super(context, theme);
        init();
    }

    protected TDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    //初始化
    private void init() {
        setContentView(R.layout.tdialog);
    }

}
