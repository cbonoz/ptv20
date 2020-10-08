package com.amazon.android.contentbrowser.payments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amazon.android.contentbrowser.R;
import com.amazon.android.model.content.Content;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.amazon.android.contentbrowser.ContentBrowser.PRICE_MAP;
import static com.amazon.android.contentbrowser.app.ContentBrowserApplication.GSON;
import static com.amazon.android.contentbrowser.payments.PayIdHelper.HTTP_CLIENT;
import static com.amazon.android.contentbrowser.payments.PayIdHelper.PAYTV_SERVER;
import static com.amazon.android.contentbrowser.payments.PayIdHelper.createPayIdUrl;
import static com.amazon.android.contentbrowser.payments.PayIdHelper.getAddresses;

public class PaymentDialog {


    public static void createPayIdInputDialog(Activity context,
                                              Content content,
                                              DialogInterface.OnClickListener onClickListener)
            throws Exception {
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
        purchaseText.setText(String.format(Locale.US, "Scan with your mobile wallet to complete purchase of %s.", content.getTitle()));

        TextView conversionText = subView.findViewById(R.id.conversion_text);
        final String payId = createPayIdUrl(content.getPayIdUserName());
        final String text = String.format(Locale.US, "Base Price: $%.2f\nUser: %s\nServer: %s\n\nPay for this item by scanning one of the below QR codes:",
                price, content.getPayIdUserName(), PAYTV_SERVER);
        conversionText.setText(text);

        getAddresses(payId, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                AddressResponse addressResponse = GSON.fromJson(response.body().string(), AddressResponse.class);

                String btcAddress = null;
                String xrpAddress = null;
                for (CustomAddress address : addressResponse.addresses) {
                    String current = address.details.address;
                    switch (address.paymentNetwork) {
                        case "BTC":
                            btcAddress = current;
                            break;
                        case "XRPL":
                            xrpAddress = current;
                            break;
                        // TODO: support other accounts.
                    }
                }

                String finalBtcAddress = btcAddress;
                String finalXrpAddress = xrpAddress;
                Picasso picasso = new Picasso.Builder(context).downloader(new OkHttp3Downloader(HTTP_CLIENT)).build();
                picasso.setLoggingEnabled(true);
                new Handler(Looper.getMainLooper()).post(() -> {
                    if (finalBtcAddress != null) {
                        String url = "https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=" + finalBtcAddress;
                        ImageView v = subView.findViewById(R.id.btcImage);
                        picasso.load(url).into(v);
                    }

                    if (finalXrpAddress != null) {
                        String url = "https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=" + finalXrpAddress;
                        ImageView v = subView.findViewById(R.id.xrpImage);
                        picasso.load(url).into(v);
                    }


                    new AlertDialog.Builder(context)
                            .setView(subView)
                            .setTitle("Scan address to complete purchase")
                            .setPositiveButton("Done", onClickListener)
//                            .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel())
                            .show();

                });
            }
        });


    }
}
