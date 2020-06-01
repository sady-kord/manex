package com.baman.manex.controls.buttonlistsheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.res.ResourcesCompat;

import com.baman.manex.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ButtonListSheet extends BottomSheetDialogFragment {

    private static final String KEY_BUTTON_ITEMS = "key_button_items";

    public interface ClickListener {
        void onButtonClicked(BottomSheetDialogFragment sheet, int position);
    }

    private List<ButtonItem> buttonItems;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        buttonItems = getArguments().getParcelableArrayList(KEY_BUTTON_ITEMS);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        LinearLayoutCompat linearLayout = new LinearLayoutCompat(getContext());
        linearLayout.setOrientation(LinearLayoutCompat.VERTICAL);

        for (int i = 0; i < buttonItems.size(); i++) {
            ButtonItem buttonItem = buttonItems.get(i);

            View itemView = inflater.inflate(R.layout.item_buttonlistsheet, linearLayout, false);
            TextView textLabel = itemView.findViewById(R.id.text_label);
            ImageView imageIcon = itemView.findViewById(R.id.image_icon);

            if (null != buttonItem.getIcon()) {
                imageIcon.setImageResource(buttonItem.getIcon());
            }

            textLabel.setText(buttonItem.getLabel());

            if (null != buttonItem.getTextColor()) {
                textLabel.setTextColor(buttonItem.getTextColor());
            }

            if (null != buttonItem.getFontFamily()) {
                textLabel.setTypeface(ResourcesCompat.getFont(getContext(), buttonItem.getFontFamily()));
            }

            int finalI = i;
            itemView.setOnClickListener(v -> buttonItem.getListener().onButtonClicked(this, finalI));

            linearLayout.addView(itemView);
        }

        return linearLayout;
    }

    public static class Builder {
        private ButtonListSheet mInstance;
        private ArrayList<ButtonItem> mButtonItems;

        public Builder() {
            this.mInstance = new ButtonListSheet();
            this.mButtonItems = new ArrayList<>(2);
        }

        public Builder addButtonItem(ButtonItem item) {
            mButtonItems.add(item);
            return this;
        }

        public Builder addButtonItem(@DrawableRes Integer icon, @NonNull String label,
                                     @NonNull ClickListener listener) {
            mButtonItems.add(new ButtonItem(icon, label, listener));
            return this;
        }

        public Builder addButtonItem(@DrawableRes Integer icon, @NonNull String label,
                                     @Nullable Integer textColor, @Nullable Integer fontFamily,
                                     @NonNull ClickListener listener) {
            mButtonItems.add(new ButtonItem(icon, label, textColor, fontFamily, listener));
            return this;
        }

        public ButtonListSheet build() {
            Bundle args = new Bundle();
            args.putParcelableArrayList(KEY_BUTTON_ITEMS, mButtonItems);
            mInstance.setArguments(args);
            return mInstance;
        }

        @NotNull
        public Builder setTitle(@StringRes int titleRes) {
            return null; // todo: implement this
        }

        @NotNull
        public Builder setSubtitle(@StringRes int subtitleRes) {
            return null; // todo: imlement this
        }
    }
}
