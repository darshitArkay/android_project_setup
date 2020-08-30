package com.arkay.projectsetup.callback;

import android.app.Dialog;

public interface CallbackYesNo {
    void onYesClick(Dialog dialog);

    void onNoClick(Dialog dialog);
}
