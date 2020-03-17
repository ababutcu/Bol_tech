package com.idyllix.bol_tech.utils;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;

import java.util.Objects;

public class HideKeyboard {
    public static void hide(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(
                    Objects.requireNonNull(activity.getCurrentFocus()).getWindowToken(), 0);
        }
    }
}
