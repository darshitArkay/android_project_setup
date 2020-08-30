package com.arkay.projectsetup.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arkay.projectsetup.R;
import com.arkay.projectsetup.broadcastReceiver.ConnectionReceiver;
import com.arkay.projectsetup.callback.CallbackDialogButtonClick;
import com.arkay.projectsetup.callback.CallbackOkCancel;
import com.arkay.projectsetup.utils.DialogUtil;
import com.arkay.projectsetup.utils.MyApplication;

import butterknife.ButterKnife;

/*Created by: Darshit Anjaria*/

public abstract class BaseActivity extends AppCompatActivity implements ConnectionReceiver.ConnectivityReceiverListener {

    Context context;
    private Dialog dialog;
    public static String TAG = "Pathya App";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        ButterKnife.bind(this);
        context = this;
    }

    protected abstract int getLayout();

    public void Fullscreen() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    protected void hideKeyboard(View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                View v = getCurrentFocus();
                if (v != null) {
                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (inputManager != null) {
                        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
                return false;
            }
        });

    }

    protected void showCustomAlert(Activity activity, boolean fullscreen, String message) {
        Dialog dialog = new DialogUtil(activity).buildDialogMessage(message, fullscreen,new CallbackDialogButtonClick(){
            @Override
            public void onButtonClick(Dialog dialog) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    protected void showAlert(String message, String positiveButtonText, String negativeButtonText, CallbackOkCancel callbackOkCancel) {
        new AlertDialog.Builder(context)
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
        Intent intent = new Intent(this, classname);
        startActivity(intent);
    }

    /*message type 0 -> default, 1 -> success, 2 -> error*/
    protected void showToast(String message, int messageType) {

        LayoutInflater inflater = getLayoutInflater();
        View layout;

        switch (messageType) {
            case 0:
            default:
                layout = inflater.inflate(R.layout.custom_toast_layout, (ViewGroup) findViewById(R.id.toast_layout_root));
                break;

            case 1:
                layout = inflater.inflate(R.layout.custom_success_toast_layout, (ViewGroup) findViewById(R.id.toast_layout_success_root));
                break;

            case 2:
                layout = inflater.inflate(R.layout.custom_error_toast_layout, (ViewGroup) findViewById(R.id.toast_layout_error_root));
        }

        TextView txt_toast_msg = (TextView) layout.findViewById(R.id.txt_toast_msg);
        txt_toast_msg.setText(message);
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.BOTTOM, 0, 0);
        toast.setView(layout);
        toast.show();
    }

    //orientation = 0 -> vertical, orientation = 1 -> horizontal
    protected void setRecyclerView(Context context, RecyclerView recyclerView, int orientation) {
        RecyclerView.LayoutManager layoutManager;
        switch (orientation) {
            case 1:
                layoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
                break;
            case 0:
            default:
                layoutManager = new LinearLayoutManager(context);
                break;
        }
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
    }

    protected void setRecyclerViewWithGridLayout(Context context, RecyclerView recyclerView, int colSpan) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(context, colSpan));
    }

    protected void FragmentMangerCall(int id, Fragment fragment, String tag) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(id, fragment, tag)
                .addToBackStack(tag)
                .commit();

    }

    protected void showLoading(String message, boolean fullscreen) {
        dialog = new DialogUtil(this).buildDialogLoading(message, fullscreen);
        dialog.show();
    }

    protected void hideLoading() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected) {
//            showToast(context.getString(R.string.internet_available), 1);
        } else {
            showToast(context.getString(R.string.internet_not_available), 2);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        MyApplication.getInstance().setConnectivityReceiverListener(this);
    }
}
