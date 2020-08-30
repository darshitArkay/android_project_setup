package com.arkay.projectsetup.callback;

public interface CallbackDatePicker {
    void onValidation(String msg);

    void onSelectedDate(String date);

    void onException(String exception);
}
