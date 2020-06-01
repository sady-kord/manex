package com.baman.manex.controls.buttonlistsheet;

import android.os.Parcel;
import android.os.Parcelable;

class ButtonItem implements Parcelable {

    private ButtonListSheet.ClickListener listener; //mandatory
    private Integer icon;
    private String label; //mandatory
    private Integer textColor;
    private Integer fontFamily;

    public ButtonItem(Integer icon, String label, ButtonListSheet.ClickListener listener) {
        this.icon = icon;
        this.label = label;
        this.listener = listener;
    }

    public ButtonItem(Integer icon, String label, Integer textColor, Integer fontFamily,
                      ButtonListSheet.ClickListener listener) {
        this.icon = icon;
        this.label = label;
        this.textColor = textColor;
        this.fontFamily = fontFamily;
        this.listener = listener;
    }

    protected ButtonItem(Parcel in) {
        if (in.readByte() == 0) {
            icon = null;
        } else {
            icon = in.readInt();
        }
        label = in.readString();
        if (in.readByte() == 0) {
            textColor = null;
        } else {
            textColor = in.readInt();
        }
        if (in.readByte() == 0) {
            fontFamily = null;
        } else {
            fontFamily = in.readInt();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (icon == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(icon);
        }
        dest.writeString(label);
        if (textColor == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(textColor);
        }
        if (fontFamily == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(fontFamily);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ButtonItem> CREATOR = new Creator<ButtonItem>() {
        @Override
        public ButtonItem createFromParcel(Parcel in) {
            return new ButtonItem(in);
        }

        @Override
        public ButtonItem[] newArray(int size) {
            return new ButtonItem[size];
        }
    };

    public ButtonListSheet.ClickListener getListener() {
        return listener;
    }

    public Integer getIcon() {
        return icon;
    }

    public String getLabel() {
        return label;
    }

    public Integer getTextColor() {
        return textColor;
    }

    public Integer getFontFamily() {
        return fontFamily;
    }
}
