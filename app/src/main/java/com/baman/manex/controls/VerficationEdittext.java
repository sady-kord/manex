package com.baman.manex.controls;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatEditText;

public class VerficationEdittext extends AppCompatEditText {

    private Context mContext;

    protected final String TAG = getClass().getName();

    public VerficationEdittext(Context context) {
        super(context);
        mContext = context;
    }

    public VerficationEdittext(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public VerficationEdittext(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            //hide keyboard
            InputMethodManager mgr = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(this.getWindowToken(), 0);

            //lose focus
            this.clearFocus();

            return true;
        }
        return false;
    }
}
