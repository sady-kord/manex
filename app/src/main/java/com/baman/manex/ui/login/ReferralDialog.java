package com.baman.manex.ui.login;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.baman.manex.R;
import com.baman.manex.controls.TextInputLayoutControl;

import org.jetbrains.annotations.NotNull;

public class ReferralDialog extends DialogFragment {

    private AlertDialog alertDialog;
    private DialogCallBack callBack;

    RelativeLayout confirmLayout , cancelLayout , close ;
    TextInputLayoutControl textInputLayoutControl;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_referral, null);
        builder.setView(view);
        alertDialog = builder.create();

        confirmLayout = view.findViewById(R.id.confirm_layout);
        cancelLayout = view.findViewById(R.id.cancel_layout);
        close = view.findViewById(R.id.close);
        textInputLayoutControl = view.findViewById(R.id.edit_text);

        close.setOnClickListener(v -> alertDialog.dismiss());

        confirmLayout.setOnClickListener(v -> {

            callBack.onCallBack(textInputLayoutControl.getValue());

            alertDialog.dismiss();
        });

        cancelLayout.setOnClickListener(v -> alertDialog.dismiss());

        return alertDialog;
    }

    public void setCallBack(DialogCallBack callBack) {
        this.callBack = callBack;
    }

    public interface DialogCallBack {
        void onCallBack(String referralCode);
    }
}
