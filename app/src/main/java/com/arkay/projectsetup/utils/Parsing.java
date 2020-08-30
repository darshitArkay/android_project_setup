package com.arkay.projectsetup.utils;

/*
 * Created by Darshit Anjaria on 04/03/2020.
 */

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Parsing {

    /*check whether the given key is json array or not*/
    public static boolean checkIsJsonArray(JsonObject jsonObject, String key) {
        return jsonObject.has(key) && jsonObject.get(key).isJsonArray();
    }

    /*check whether the given json array is null or not*/
    public static boolean checkJsonArrayIsNull(JsonArray jsonArray) {
        return !jsonArray.isJsonNull();
    }

    /*check whether the given key is json object or not*/
    public static boolean checkIsJsonObject(JsonObject jsonObject, String key) {
        return jsonObject.has(key) && jsonObject.get(key).isJsonObject();
    }

    public static JsonArray parseJsonArray(JsonObject jsonObject, String key) {
        return jsonObject.getAsJsonArray(key);
    }

    public static JsonObject parseJsonObject(JsonObject jsonObject, String key) {
        return jsonObject.getAsJsonObject(key);
    }


    /*get string data from response*/
    public static String parseStringData(JsonObject jsonObject, String key) {
        if (jsonObject.has(key) && !jsonObject.get(key).isJsonNull() && !jsonObject.get(key).getAsString().equals("")) {
            return jsonObject.get(key).getAsString();
        }
        return "";
    }

    /*get int data from response*/
    public static int parseIntData(JsonObject jsonObject, String key) {
        if (jsonObject.has(key) && !jsonObject.get(key).isJsonNull()) {
            return jsonObject.get(key).getAsInt();
        }
        return 0;
    }

    public static float parseFloatData(JsonObject jsonObject, String key) {
        if (jsonObject.has(key) && !jsonObject.get(key).isJsonNull()) {
            return jsonObject.get(key).getAsFloat();
        }
        return 0;
    }

    /*get double data from response*/
    public static double parseDoubleData(JsonObject jsonObject, String key) {
        if (jsonObject.has(key) && !jsonObject.get(key).isJsonNull()) {
            return jsonObject.get(key).getAsDouble();
        }
        return 0;
    }

    /*get boolean data from response*/
    public static boolean parseBooleanData(JsonObject jsonObject, String key) {
        if (jsonObject.has(key) && !jsonObject.get(key).isJsonNull()) {
            return jsonObject.get(key).getAsBoolean();
        }
        return false;
    }
}
