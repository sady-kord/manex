package com.baman.manex.controls;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.baman.manex.R;
import com.baman.manex.util.PublicFunction;

public class VerificationCodeInput extends ConstraintLayout {

    public static final int LENGTH_DEFAULT = 5;

    private int mLength;

    private DigitInput[] mInputs;

    private int mCurrentFocusIndex = -1;

    private String mText = "";

    private OnInputCompletedListener mOnInputCompletedListener = null;
    private OnInputChangedListener mOnInputChangedListener = null;

    private int mSelectedItemColor, mUnselectedItemColor;
    private int mSelectedItemBackground, mUnselectedItemBackground;
    private Drawable mErrorDrawable;

    private boolean mErrorState = false;

    public VerificationCodeInput(Context context) {
        super(context);
        initialize(context, null, 0);
    }

    public VerificationCodeInput(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs, 0);
    }

    public VerificationCodeInput(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        setFocusable(true);
        setFocusableInTouchMode(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setDefaultFocusHighlightEnabled(false);
        }

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.VerificationCodeInput, defStyleAttr, 0);

        mLength = a.getInteger(R.styleable.VerificationCodeInput_codeLenght, LENGTH_DEFAULT);

        mSelectedItemColor = a.getColor(R.styleable.VerificationCodeInput_selectedItemTextColor,
                ContextCompat.getColor(context, R.color.verificationcodefield_textcolor_selected));
        mUnselectedItemColor = a.getColor(R.styleable.VerificationCodeInput_unselectedItemTextColor,
                ContextCompat.getColor(context, R.color.verificationcodefield_itembackground_unselected));

        mSelectedItemBackground =
                a.getResourceId(R.styleable.VerificationCodeInput_selectedItemBackground,
                        R.drawable.verificationcodefield_background_selected);
        mUnselectedItemBackground =
                a.getResourceId(R.styleable.VerificationCodeInput_unselectedItemBackground,
                        R.drawable.verificationcodefield_background_unselected);

        int errorDrawableRes = a.getResourceId(R.styleable.VerificationCodeInput_errorBackgroundDrawable,
                R.drawable.verificationcodefield_background_error);
        mErrorDrawable = ContextCompat.getDrawable(context, errorDrawableRes);

        a.recycle();

        mInputs = new DigitInput[mLength];

        LayoutInflater inflater = LayoutInflater.from(context);

        for (int i = 0; i < mLength; i++) {
            View view = inflater.inflate(R.layout.view_verificationcodeinput, this, false);
            view.setId(i + 1);

            LayoutParams params = (LayoutParams) view.getLayoutParams();

            if (i == 0) {
                params.leftToLeft = LayoutParams.PARENT_ID;
                params.rightToLeft = i + 2;
                params.setMargins(16,0,16,0);

                params.width = PublicFunction.convertDpToPixels(76f,context);
                params.height = PublicFunction.convertDpToPixels(42f,context);
            } else if (i == mLength - 1) {
                params.rightToRight = LayoutParams.PARENT_ID;
                params.leftToRight = i;
                params.setMargins(4,0,4,0);
                params.width = PublicFunction.convertDpToPixels(76f,context);
                params.height = PublicFunction.convertDpToPixels(42f,context);
            } else {
                params.rightToLeft = i + 2;
                params.leftToRight = i;
                params.setMargins(4,0,16,0);
                params.width = PublicFunction.convertDpToPixels(76f,context);
                params.height = PublicFunction.convertDpToPixels(42f,context);
            }

            addView(view);

            mInputs[i] = new DigitInput(i, view);

            mInputs[i].setFocused(false);
        }

        assignNextFocus(0);
    }

    public void setErrorState(boolean errorState) {
        if (mErrorState == errorState) {
            return;
        }
        mErrorState = errorState;
        for (int i = 0; i < mLength; i++) {
            mInputs[i].setError(errorState);
        }
    }

    public void setOnInputCompletedListener(OnInputCompletedListener listener) {
        mOnInputCompletedListener = listener;

        checkNotifyInputCompleted();
    }

    public void setOnInputChangedListener(OnInputChangedListener listener) {
        mOnInputChangedListener = listener;

        checkNotifyInputCompleted();
    }

    public String getText() {
        return mText;
    }

    private void assignNextFocus(int index) {
        if (mCurrentFocusIndex != -1) {
            mInputs[mCurrentFocusIndex].setFocused(false);
        }

        if (mCurrentFocusIndex != index) {
            if (index < mText.length()) {
                mCurrentFocusIndex = index;
            } else {
                mCurrentFocusIndex = Math.min(mText.length(), mLength - 1);
            }
        }

        mInputs[mCurrentFocusIndex].setFocused(true);

        requestFocusFromTouch();

        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(this, 0);
    }

    @Override
    public boolean onCheckIsTextEditor() {
        return true;
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        outAttrs.inputType = InputType.TYPE_CLASS_NUMBER; // 7540 in TextView
        outAttrs.imeOptions = EditorInfo.IME_ACTION_DONE;

        CodeInputConnection ic = new CodeInputConnection();

        outAttrs.initialSelStart = 0;
        outAttrs.initialSelEnd = 1;
        outAttrs.initialCapsMode = ic.getCursorCapsMode(outAttrs.inputType);

        return ic;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        setErrorState(false);

        if ((event.getFlags() & KeyEvent.FLAG_EDITOR_ACTION) != 0) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getWindowToken(), 0);

            mInputs[mCurrentFocusIndex].setFocused(false);

            checkNotifyInputCompleted();

            return true;
        }

        if (keyCode == KeyEvent.KEYCODE_DEL) {
            if (mCurrentFocusIndex == -1) {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindowToken(), 0);
            } else if (mInputs[mCurrentFocusIndex].hasValue() && mCurrentFocusIndex < mText.length() - 1) {
                mText = mText.substring(0, mCurrentFocusIndex) + mText.substring(mCurrentFocusIndex + 1);

                reassignValues();
            } else {
                mInputs[mCurrentFocusIndex].setFocused(false);
                mInputs[mCurrentFocusIndex].setValue("");

                mText = mText.substring(0, mCurrentFocusIndex);

                if (mCurrentFocusIndex != 0) {
                    mCurrentFocusIndex--;
                }

                if (mCurrentFocusIndex >= 0) {
                    mInputs[mCurrentFocusIndex].setFocused(true);
                }
            }

            checkNotifyInputCompleted();

            return true;
        }

        char number = event.getNumber();

        if (number < '0' || number > '9') {
            return false;
        }

        String key = "" + number;

        mInputs[mCurrentFocusIndex].setValue(key);
        mInputs[mCurrentFocusIndex].setFocused(false);

        if (mCurrentFocusIndex == mText.length()) {
            mText = mText.concat(key);
        } else if (mCurrentFocusIndex < mText.length()) {
            mText = mText.substring(0, mCurrentFocusIndex) + key + mText.substring(mCurrentFocusIndex + 1);
        }

        if (mCurrentFocusIndex != mLength-1) {
            mCurrentFocusIndex++;
        }

        if (mCurrentFocusIndex >= mLength) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getWindowToken(), 0);
        } else {
            mInputs[mCurrentFocusIndex].setFocused(true);
        }

        checkNotifyInputCompleted();

        return true;
    }

    private void checkNotifyInputCompleted() {
        if (mOnInputChangedListener != null) {
            mOnInputChangedListener.onInputChanged(this, mText);
        }
        if (mOnInputCompletedListener != null && mText.length() == mLength) {
            mOnInputCompletedListener.onInputCompleted(this, mText);
        }
    }

    public void setText(String text) {
        mText = text;

        reassignValues();
        checkNotifyInputCompleted();
    }

    private void reassignValues() {
        for (int i = 0; i < mLength; i++) {
            mInputs[i].setValue(i < mText.length() ? mText.substring(i, i + 1) : "");
        }
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        return new SavedInstanceState(super.onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);

        SavedInstanceState savedState = (SavedInstanceState) state;

        mCurrentFocusIndex = savedState.getCurrentFocusIndex();
        mText = savedState.getText();

        reassignValues();

        if (mCurrentFocusIndex >= 0) {
            mInputs[mCurrentFocusIndex].setFocused(true);
        }
    }

    private class CodeInputConnection extends BaseInputConnection {

        public CodeInputConnection() {
            super(VerificationCodeInput.this, false);
        }

    }

    private class DigitInput implements OnClickListener {

        private int mIndex;

        private TextView mView;

        protected DigitInput(int index, View view) {
            mIndex = index;

            mView = (TextView) view;

//            mView.addTextChangedListener(new DigitFormatter(getContext()));

            mView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            setErrorState(false);
            assignNextFocus(mIndex);
        }

        protected void setValue(String value) {
            mView.setText(value);
        }

        protected boolean hasValue() {
            return !mView.getText().toString().trim().isEmpty();
        }

        protected void setError(boolean error) {
            if (error) {
                mView.setBackground(mErrorDrawable);
            } else {
                setFocused(false);
            }
        }

        protected void setFocused(boolean focused) {
            mView.setTextColor(focused ? mSelectedItemColor : mUnselectedItemColor);
            mView.setBackgroundResource(
                    focused ? mSelectedItemBackground : mUnselectedItemBackground);
        }
    }

    private class SavedInstanceState extends BaseSavedState {

        private int mCurrentFocusIndex;
        private String mText;

        public SavedInstanceState(Parcel source) {
            super(source);

            read(source);
        }

        @TargetApi(Build.VERSION_CODES.N)
        public SavedInstanceState(Parcel source, ClassLoader loader) {
            super(source, loader);

            read(source);
        }

        public SavedInstanceState(Parcelable superState) {
            super(superState);

            mCurrentFocusIndex = VerificationCodeInput.this.mCurrentFocusIndex;
            mText = VerificationCodeInput.this.mText;
        }

        private void read(Parcel source) {
            mCurrentFocusIndex = source.readInt();
            mText = source.readString();
        }

        protected int getCurrentFocusIndex() {
            return mCurrentFocusIndex;
        }

        protected String getText() {
            return mText;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);

            out.writeInt(mCurrentFocusIndex);
            out.writeString(mText);
        }

    }

    public interface OnInputCompletedListener {
        void onInputCompleted(View input, String text);
    }

    public interface OnInputChangedListener {
        void onInputChanged(View input, String text);
    }
}