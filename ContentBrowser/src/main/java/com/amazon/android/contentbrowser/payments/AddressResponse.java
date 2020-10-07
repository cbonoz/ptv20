package com.amazon.android.contentbrowser.payments;

import java.util.List;

import io.xpring.payid.generated.model.Address;
import io.xpring.payid.generated.model.CryptoAddressDetails;

class CustomAddress {
    public String paymentNetwork;
    public String environment;
    public CryptoAddressDetails details;
}

public class AddressResponse {
    public String payId;
    public List<CustomAddress> addresses;

}
