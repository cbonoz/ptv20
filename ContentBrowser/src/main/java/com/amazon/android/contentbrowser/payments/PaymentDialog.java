package com.amazon.android.contentbrowser.payments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amazon.android.contentbrowser.R;
import com.amazon.android.model.content.Content;

import java.util.Locale;

import static com.amazon.android.contentbrowser.ContentBrowser.PRICE_MAP;

public class PaymentDialog {


    public static Dialog createPayIdInputDialog(Activity context,
                                                Content content,
                                                String currency,
                                                double dollarConversion,
                                                DialogInterface.OnClickListener onClickListener)
            throws Exception
    {
        final double price;
        if (PRICE_MAP.containsKey(content.getId())) {
            String string = context.getString(PRICE_MAP.get(content.getId()));
            price = Double.parseDouble(string.substring(1)); // remove $.
        } else {
            throw new Exception("Could not find price for item: " + content.getId() + ". This needs to be added to the PRICE_MAP.");
        }

        ViewGroup subView = (ViewGroup) context.getLayoutInflater().// inflater view
                inflate(R.layout.pay_id_input_dialog, null, false);

        TextView purchaseText = subView.findViewById(R.id.pay_id_text);
        purchaseText.setText(String.format(Locale.US, "Enter your PayID to complete purchase of %s.", content.getTitle()));


        TextView conversionText = subView.findViewById(R.id.conversion_text);
        double total = price / dollarConversion;

        final String text = String.format(Locale.US, "Base Price: $%s\nConversion: %s %s/USD\nTotal: %s %s",
                price, dollarConversion, currency, total, currency);
        conversionText.setText(text);

        return new AlertDialog.Builder(context)
                .setView(subView)
                .setTitle("Enter your PayID")
                .setPositiveButton("Buy Now", onClickListener)
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel())
                .create();
    }
}
