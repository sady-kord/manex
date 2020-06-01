package com.baman.manex.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.widget.AppCompatEditText;


public class KeyBoardHelper {

    Activity activity;

    public KeyBoardHelper(Activity activity) {
        this.activity = activity;
    }

    public void closeKeyBoard() {
        InputMethodManager imm = null;

        try {
            imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            View view = activity.getCurrentFocus();
            if (view == null) {
                view = new View(activity);
            }
            if (imm == null) {
                return;
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            Log.e("closeKeyboard", e.getMessage());
        }
    }

    public void focusRequest(AppCompatEditText editText) {
        try {
            editText.requestFocus();
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm == null) {
                return;
            }
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        } catch (Exception e) {
            Log.e("", e.getMessage());
        }
    }

    public View.OnFocusChangeListener hideKeyboardThouchOutSide() {
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if (!focus) {
                    closeKeyBoard();
                }
            }
        };
    }
}
