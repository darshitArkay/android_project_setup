package com.arkay.projectsetup.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arkay.projectsetup.R;
import com.arkay.projectsetup.callback.CallbackDialogButtonClick;
import com.arkay.projectsetup.callback.CallbackOkCancel;
import com.arkay.projectsetup.utils.DialogUtil;


public class BaseFragment extends Fragment {

    private Dialog dialog;
    public static String TAG = "Pathya App";

    public void Fullscreen() {
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    protected void hideKeyboard(View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                View v = getActivity().getCurrentFocus();
                if (v != null) {
                    InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (inputManager != null) {
                        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
                return false;
            }
        });

    }

    protected void showCustomAlert(boolean fullscreen, String message) {
        Dialog dialog = new DialogUtil(getActivity()).buildDialogMessage(message, fullscreen, new CallbackDialogButtonClick() {
            @Override
            public void onButtonClick(Dialog dialog) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    protected void showAlert(String message, String positiveButtonText, String negativeButtonText, CallbackOkCancel callbackOkCancel) {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callbackOkCancel.onOkClick((Dialog) dialog);
                    }
                })
                .setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callbackOkCancel.onCancelClick((Dialog) dialog);
                    }
                })
                .setCancelable(false)
                .show();
    }

    public void startActivity(Class classname) {
        Intent intent = new Intent(getActivity(), classname);
        startActivity(intent);
    }

    /*message type 0 -> default, 1 -> success, 2 -> error*/
    protected void showToast(Activity activity, String message, int messageType) {

        LayoutInflater inflater = getLayoutInflater();
        View layout;

        switch (messageType) {
            case 0:
            default:
                layout = inflater.inflate(R.layout.custom_toast_layout, activity.findViewById(R.id.toast_layout_root));
                break;

            case 1:
                layout = inflater.inflate(R.layout.custom_success_toast_layout, (ViewGroup) activity.findViewById(R.id.toast_layout_success_root));
                break;

            case 2:
                layout = inflater.inflate(R.layout.custom_error_toast_layout, (ViewGroup) activity.findViewById(R.id.toast_layout_error_root));
        }

        TextView txt_toast_msg = (TextView) layout.findViewById(R.id.txt_toast_msg);
        txt_toast_msg.setText(message);
        Toast toast = new Toast(activity);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.BOTTOM, 0, 0);
        toast.setView(layout);
        toast.show();
    }

    //orientation = 0 -> vertical, orientation = 1 -> horizontal
    protected void setRecyclerView(Activity activity, RecyclerView recyclerView, int orientation) {
        RecyclerView.LayoutManager layoutManager;
        if (orientation == 0) {
            layoutManager = new LinearLayoutManager(activity.getApplicationContext());
        } else if (orientation == 1) {
            layoutManager = new LinearLayoutManager(activity.getApplicationContext(), RecyclerView.HORIZONTAL, false);
        } else {
            layoutManager = new LinearLayoutManager(activity.getApplicationContext());
        }
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
    }

    protected void showLoading(Context context, String message, boolean fullscreen) {
        dialog = new DialogUtil(context).buildDialogLoading(message, fullscreen);
        dialog.show();
    }

    protected void hideLoading() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
