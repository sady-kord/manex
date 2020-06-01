package com.baman.manex.controls;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.content.res.ResourcesCompat;

import com.baman.manex.R;
import com.baman.manex.util.PublicFunction;

import static android.view.Gravity.CENTER_VERTICAL;
import static android.view.Gravity.RIGHT;

public class CheckBoxControl extends AppCompatCheckBox {

    public CheckBoxControl(Context context, AttributeSet attrt) {
        super(context , attrt);

        this.setTextSize(14);

        Typeface typeface = ResourcesCompat.getFont(context, R.font.iranyekan);
        this.setTypeface(typeface);

        this.setButtonDrawable(new StateListDrawable());

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        this.setGravity(CENTER_VERTICAL | RIGHT);
        params.setMargins(25, 25, 25, 25);

        this.setLayoutParams(params);


        this.setCompoundDrawablePadding(25);
        this.setPadding(25, 25, 25, 25);
        this.setBackgroundDrawable(PublicFunction.getDrawable(R.drawable.background_radio_button_un_check, this.getContext()));

        setChecked_Selector(this);
    }

    public void setChecked_Selector(CheckBox view) {
        try {
            Drawable pressed = PublicFunction.getDrawable(R.drawable.ic_radio_check, view.getContext());
            Drawable normal = PublicFunction.getDrawable(R.drawable.ic_radio_uncheck, view.getContext());

            StateListDrawable states = new StateListDrawable();
            states.addState(new int[]{android.R.attr.state_checked,}, pressed);
            states.addState(new int[]{android.R.attr.state_pressed}, pressed);

            states.addState(new int[]{android.R.attr.state_checked, android.R.attr.state_enabled}, pressed);
            states.addState(new int[]{android.R.attr.state_checked, -android.R.attr.state_enabled}, pressed);

            states.addState(new int[]{}, normal);

            view.setCompoundDrawablesWithIntrinsicBounds(states, null, null, null);

        } catch (Exception e) {
        }
    }

    public void setBackGround() {
        this.setBackgroundDrawable(PublicFunction.getDrawable(R.drawable.background_item_filter_selected, this.getContext()));
        this.setTextColor(getResources().getColor(R.color.colorPrimary));
    }

    public void clearBackGround() {
        this.setBackgroundDrawable(PublicFunction.getDrawable(R.drawable.background_item_filter_unselected, this.getContext()));
        this.setTextColor(getResources().getColor(R.color.gray));

    }
}
