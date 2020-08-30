package com.arkay.projectsetup.utils;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextPaint;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.widget.DatePicker;

import androidx.core.content.FileProvider;
import androidx.core.graphics.drawable.DrawableCompat;

import com.arkay.projectsetup.BuildConfig;
import com.arkay.projectsetup.callback.CallbackDatePicker;
import com.arkay.projectsetup.callback.CallbackShareImage;
import com.arkay.projectsetup.callback.CallbackTimePicker;
import com.arkay.projectsetup.customViews.CustomEditText;
import com.arkay.projectsetup.customViews.CustomTextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Pattern;

public class Tools {

    public static Pattern letter = Pattern.compile("[a-zA-Z]");
    public static Pattern digit = Pattern.compile("[0-9]");
    public static Pattern special = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]");

    public static boolean isNetworkAvailable(Activity act) {
        ConnectivityManager connMgr = (ConnectivityManager) act.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }
        return networkInfo != null && networkInfo.isConnected();
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return model;
        } else {
            return manufacturer + " " + model;
        }
    }

    public static String getAndroidVersionName(Context ctx) {
        try {
            PackageManager manager = ctx.getPackageManager();
            PackageInfo info = manager.getPackageInfo(ctx.getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return e.getMessage();
        }
    }

    public static int getVersionCode(Context ctx) {
        try {
            PackageManager manager = ctx.getPackageManager();
            PackageInfo info = manager.getPackageInfo(ctx.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return -1;
        }
    }

    public static int getScreenWidth(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static int getScreenHeight(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public static int dpToPx(int dp, Activity activity) {
        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static int pxToDp(int px, Activity activity) {
        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static boolean isStringNull(String value) {
        boolean isStringNull = true;
        if (value != null && !value.equals("") && !value.equals("null")) {
            isStringNull = false;
        }
        return isStringNull;
    }

    public static String convertDateWithoutTime(String date, String flag) {
        String result = "";
        try {
            SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-mm-dd", Locale.getDefault());
            Date parsed = sourceFormat.parse(date);

            SimpleDateFormat destFormat = null;

           /* 1 - dd/mm/yyyy (31/12/2018)
            2 - dd/mm/yyyy (31/12/2018)
            3 - dd/yyyy/mm (31/2018/12)
            4 - dd/yyyy/mm (31/2018/12)
            5 - mm/dd/yyyy (12/31/2018)
            6 - mm/dd/yyyy (12/31/2018)
            7 - mm/yyyy/dd (12/2018/31)
            8 - mm/yyyy/dd (12/2018/31)
            9 - yyyy/dd/mm (2018/31/12)
            10 - yyyy/dd/mm (2018/31/12)
            11 - yyyy/mm/dd (2018/12/31)
            12 - yyyy/mm/dd (2018/12/31)
            13 - dd-mm-yyyy (31-12-2018)*/

            switch (flag) {
                case "1":
                    destFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    break;
                case "2":
                    destFormat = new SimpleDateFormat("dd/yyyy/MM", Locale.getDefault());
                    break;
                case "3":
                    destFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
                    break;
                case "4":
                    destFormat = new SimpleDateFormat("MM/yyyy/dd", Locale.getDefault());
                    break;
                case "5":
                    destFormat = new SimpleDateFormat("yyyy/dd/MM", Locale.getDefault());
                    break;
                case "6":
                    destFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
                    break;
                case "7":
                    destFormat = new SimpleDateFormat("dd-mm-yyyy", Locale.getDefault());
                    break;
                case "8":
                    destFormat = new SimpleDateFormat("dd MMMM YYYY", Locale.getDefault());
                    break;
                default:
                    destFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    break;
            }
            if (parsed != null) {
                result = destFormat.format(parsed);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void openDatePickerUnderAge(Activity activity, Calendar calendar, String validationMsg, int dateFlag, String selectedDate, CallbackDatePicker callbackDatePicker) {

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                Calendar userAge = new GregorianCalendar(year, month, dayOfMonth);
                Calendar minAdultAge = new GregorianCalendar();

                minAdultAge.add(Calendar.YEAR, -18);
                if (minAdultAge.before(userAge)) {
                    callbackDatePicker.onValidation(validationMsg);
                } else {
                    String format = "yyyy-MM-dd";
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.US);
                    callbackDatePicker.onSelectedDate(simpleDateFormat.format(calendar.getTime()));
                }
            }

        };

        int year = 0, month = 0, day = 0;
        if (dateFlag == 0) {
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
        } else {
            if (selectedDate != null && selectedDate.isEmpty()) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                Date d = null;
                try {
                    d = df.parse(selectedDate);
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
                if (d != null) {
                    calendar.setTime(d);
                }
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
            }
        }

        DatePickerDialog datePickerDialog;
        if (dateFlag == 0) {
            datePickerDialog = new DatePickerDialog(activity, dateSetListener, (year) - 18, month, day);
        } else {
            datePickerDialog = new DatePickerDialog(activity, dateSetListener, year, month, day);
        }
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();

    }

    public static void openDatePicker(Activity activity, Calendar calendar, int dateFlag, String selectedDate, CallbackDatePicker callbackDatePicker) {

        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            String format = "dd-MM-yyyy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.US);
            callbackDatePicker.onSelectedDate(simpleDateFormat.format(calendar.getTime()));

        };

        int year = 0, month = 0, day = 0;
        if (dateFlag == 0) {
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
        } else {
            if (selectedDate != null && !selectedDate.isEmpty()) {
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                Date d = null;
                try {
                    d = df.parse(selectedDate);
                } catch (ParseException ex) {
                    ex.printStackTrace();
                    callbackDatePicker.onException(ex.getMessage());
                }
                if (d != null) {
                    calendar.setTime(d);
                }
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
            }
        }

        DatePickerDialog datePickerDialog;
        datePickerDialog = new DatePickerDialog(activity, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();

    }

    public static void openTimePicker(Activity activity, Calendar calendar, int timeFlag, String selectedTime, CallbackTimePicker callbackTimePicker) {
        int hour = 0, minute = 0;
        if (timeFlag == 0) {
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            minute = calendar.get(Calendar.MINUTE);
        } else {
            if (selectedTime != null && !selectedTime.isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm", Locale.getDefault());
                Date date = null;
                try {
                    date = sdf.parse(selectedTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                    callbackTimePicker.onException(e.getMessage());
                }
                if (date != null) {
                    calendar.setTime(date);
                }

                hour = calendar.get(Calendar.HOUR_OF_DAY);
                minute = calendar.get(Calendar.MINUTE);
            }
        }

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(activity, (timePicker, selectedHour, selectedMinute) ->
                callbackTimePicker.onSelectedTime(selectedHour + ":" + selectedMinute), hour, minute, true);
        mTimePicker.show();
    }

    public static String calculateBMI(String height, String weight) {
        float heightValue = 0;
        float weightValue = 0;
        try {
            heightValue = Float.parseFloat(height) / 100;
            weightValue = Float.parseFloat(weight);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return String.valueOf(weightValue / (heightValue * heightValue));
    }

    public static String parseDate(String inputDateString, SimpleDateFormat inputDateFormat, SimpleDateFormat outputDateFormat) {
        Date date = null;
        String outputDateString = null;
        try {
            date = inputDateFormat.parse(inputDateString);
            if (date != null) {
                outputDateString = outputDateFormat.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputDateString;
    }

    public static String convertTimeToAMPM(String selectedTime) {

        SimpleDateFormat HHmmFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        SimpleDateFormat hhmmampmFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        String formattedTime = "";
        formattedTime = parseDate(selectedTime, HHmmFormat, hhmmampmFormat);
        return formattedTime;
    }

    public static String getDifferenceBetweenTwoTimes(Date startDate, Date endDate) {
        long difference = endDate.getTime() - startDate.getTime();
        int days = (int) (difference / (1000 * 60 * 60 * 24));
        int hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
        hours = (hours < 0 ? -hours : hours);

        return String.valueOf(hours);

    }

    public static BigDecimal round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd;
    }

    public static void scanFile(Context context, String path) {
        MediaScannerConnection.scanFile(context, new String[] { path }, null, (path1, uri) -> Log.d("Tag", "Scan finished. You can view the image in the gallery now."));
    }

    public static void openYouTubeChannel(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }

    public static void openGmail(Context context, String email) {

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + email));
        context.startActivity(intent);

    }

    public static int getDeviceScreenWidth(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static int getDeviceScreenHeight(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public static void setTextInGradient(CustomTextView text_view, String text, String colorCode1, String colorCode2) {
        text_view.setText(text);

        TextPaint textPaint = text_view.getPaint();
        float width = textPaint.measureText(text_view.getText().toString());


        Shader textShader = new LinearGradient(0, 0, width, text_view.getTextSize(),
                new int[]{
                        Color.parseColor(colorCode1),
                        Color.parseColor(colorCode2)
                }, null, Shader.TileMode.CLAMP);
        text_view.getPaint().setShader(textShader);
        text_view.setTextColor(Color.parseColor(colorCode1));

    }

    public static void showPassword(CustomEditText customEditText) {
        customEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
    }

    public static void hidePassword(CustomEditText customEditText) {
        customEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }

    public static Drawable setDrawableWithTint(Drawable my_drawable, int color) {
        Drawable drawable = my_drawable;
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, color);
        return drawable;
    }

    public static void shareImage(Context context, String filePath, CallbackShareImage callbackShareImage) {

        try {

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/png");
            File file = new File(filePath);
            Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID, file);
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            context.startActivity(Intent.createChooser(shareIntent, "Share Image"));
            callbackShareImage.onSuccess();
        } catch (Exception e) {
            Log.e("Tools", "Share Image with failure: ", e.getCause());
            callbackShareImage.onFailure(e.getMessage());
        }
    }

    public static void shareImageInWhatsapp(Context context, String filePath, String imageCaption, CallbackShareImage callbackShareImage) {

        try {

            Uri imgUri = Uri.parse(filePath);
            Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
            whatsappIntent.setType("text/plain");
            whatsappIntent.setPackage("com.whatsapp");
            whatsappIntent.putExtra(Intent.EXTRA_TEXT, imageCaption);
            whatsappIntent.putExtra(Intent.EXTRA_STREAM, imgUri);
            whatsappIntent.setType("image/jpeg");
            whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(whatsappIntent);
            callbackShareImage.onSuccess();

        } catch (android.content.ActivityNotFoundException ex) {
            Log.e("Tools", "Share Image with WhatsApp failure: ", ex.getCause());
            callbackShareImage.onFailure(ex.getMessage());
        }

    }

    public static void shareImageInInstagram(Context context, String filePath, String imageName, String imageDesc, CallbackShareImage callbackShareImage) {

        try {

            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setPackage("com.instagram.android");
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(), filePath, imageName, imageDesc)));
            shareIntent.setType("image/jpeg");
            context.startActivity(shareIntent);
            callbackShareImage.onSuccess();

        } catch (FileNotFoundException | ActivityNotFoundException e) {
            Log.e("Tools", "Share Image with WhatsApp failure: ", e.getCause());
            callbackShareImage.onFailure(e.getMessage());
        }
    }

    public static String getPathFromURI(Context context, Uri contentUri) {

        Cursor mediaCursor = null;

        try {

            String[] dataPath = { MediaStore.Images.Media.DATA };
            mediaCursor = context.getContentResolver().query(contentUri,  dataPath, null, null, null);
            int column_index = mediaCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            mediaCursor.moveToFirst();
            return mediaCursor.getString(column_index);

        } finally {

            if (mediaCursor != null) {
                mediaCursor.close();
            }
        }

    }

}
