package com.amazon.android.contentbrowser.payments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.ViewGroup;

import com.amazon.android.contentbrowser.R;

public class PaymentDialog {


    public static Dialog createPayIdInputDialog(Activity context, DialogInterface.OnClickListener onClickListener) {
        // TODO: custom layout to collect payId and amount.

        ViewGroup subView = (ViewGroup) context.getLayoutInflater().// inflater view
                inflate(R.layout.pay_id_input_dialog, null, false);

        return new AlertDialog.Builder(context)
                .setView(subView)
                .setTitle("Wallet information")
                .setPositiveButton("Ok", onClickListener)
                .setNegativeButton("Back", (dialogInterface, i) -> dialogInterface.cancel())
                .create();
    }
}
