package com.arkay.projectsetup.callback;

import android.app.Dialog;

public interface CallbackOkCancel {
    void onOkClick(Dialog dialog);
    void onCancelClick(Dialog dialog);
}
