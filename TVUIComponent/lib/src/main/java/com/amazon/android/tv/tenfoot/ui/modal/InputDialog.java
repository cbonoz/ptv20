package com.amazon.android.tv.tenfoot.ui.modal;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.view.ViewGroup;
import android.widget.EditText;

import com.amazon.android.tv.tenfoot.R;

import static com.amazon.android.contentbrowser.app.ContentBrowserApplication.setWalletSeed;

public class InputDialog {

    //    https://stackoverflow.com/questions/2795300/how-to-implement-a-custom-alertdialog-view
    public static Dialog createWalletInputDialog(Activity context) {

        ViewGroup subView = (ViewGroup) context.getLayoutInflater().// inflater view
                inflate(R.layout.wallet_input_dialog, null, false);

        return new AlertDialog.Builder(context)
                .setView(subView)
                .setTitle("Wallet information")
                .setPositiveButton("Ok", (dialogInterface, i) -> {
                    EditText text = subView.findViewById(R.id.wallet_seed_input);
                    setWalletSeed(text.toString());
                    dialogInterface.dismiss();
                })
                .setNegativeButton("Back", (dialogInterface, i) -> dialogInterface.cancel())
                .create();
    }


}
