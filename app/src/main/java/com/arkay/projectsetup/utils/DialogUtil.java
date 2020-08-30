package com.arkay.projectsetup.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;

import com.arkay.projectsetup.R;
import com.arkay.projectsetup.callback.CallbackDialogButtonClick;
import com.arkay.projectsetup.callback.CallbackYesNo;
import com.arkay.projectsetup.customViews.CustomTextView;

public class DialogUtil {

    private Activity activity;
    private Context context;

    public DialogUtil(Activity activity) {
        this.activity = activity;
    }

    public DialogUtil(Context context) {
        this.context = context;
    }

    private Dialog buildDialogView(@LayoutRes int layout, boolean fullscreen) {
        final Dialog dialog = new Dialog(activity, R.style.DialogLevelCompleted);
        if (dialog.getWindow() != null) {
            if (fullscreen) {
                dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
            dialog.setContentView(layout);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setAttributes(lp);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#80000000")));
        }
        return dialog;
    }

    //Dialog Msg
    public Dialog buildDialogMessage(String message, boolean fullscreen, final CallbackDialogButtonClick callback) {
        final Dialog dialog = buildDialogView(R.layout.dialog_msg, fullscreen);
        ((TextView) dialog.findViewById(R.id.txt_msg)).setText(message);
        ((TextView) dialog.findViewById(R.id.txt_ok)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onButtonClick(dialog);
            }
        });

        return dialog;
    }

    //loading dialog
    public Dialog buildDialogLoading(String message, boolean fullscreen) {
        final Dialog dialog = buildDialogView(R.layout.dialog_loading, fullscreen);
        ((TextView) dialog.findViewById(R.id.txt_loading_msg)).setText(message);
        return dialog;
    }

    /*logout dialog*/
    public Dialog buildDialogLogout(String message, boolean fullscreen, final CallbackYesNo callback) {
        final Dialog dialog = buildDialogView(R.layout.dialog_logout, fullscreen);

        RelativeLayout rl_dialog_logout = dialog.findViewById(R.id.rl_dialog_logout);

        rl_dialog_logout.setOnClickListener(v -> {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        });

        ((CustomTextView) dialog.findViewById(R.id.txt_msg)).setText(message);
        ((CustomTextView) dialog.findViewById(R.id.txt_yes)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onYesClick(dialog);
            }
        });

        ((CustomTextView) dialog.findViewById(R.id.txt_no)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onNoClick(dialog);
            }
        });

        return dialog;
    }
}
