package com.arkay.projectsetup.callback;

public interface CallbackTimePicker {
    void onSelectedTime(String time);

    void onException(String exception);
}
