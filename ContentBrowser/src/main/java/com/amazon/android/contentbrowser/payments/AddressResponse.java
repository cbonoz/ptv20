package com.amazon.android.contentbrowser.payments;

import java.util.List;


class Address {
    public String tag;
    public String address;
}

class CustomAddress {
    public String paymentNetwork;
    public String environment;
    public Address details;
}

public class AddressResponse {
    public String payId;
    public List<CustomAddress> addresses;

}
