package com.arkay.projectsetup.callback;

import android.app.Dialog;

public interface CallbackWaterGoalUpdateClick {
    void onUpdateClick(String newGoal, Dialog dialog);

    void onInvalidateClick(String msg);
}
