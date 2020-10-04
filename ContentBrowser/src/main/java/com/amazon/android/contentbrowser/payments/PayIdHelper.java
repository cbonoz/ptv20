package com.amazon.android.contentbrowser.payments;

public class PayIdHelper {


    // The expected address of the gRPC server.
    static final String XRP_URL = "test.xrp.xpring.io:50051";
    static final String btcNetwork = "btc-testnet";

    static final String TEST_MNEMONIC = "abandon abandon abandon abandon abandon abandon abandon abandon abandon abandon abandon about";
    static final String ERROR = "Error";

    // The XRP Ledger network to resolve on.
//    static final XrplNetwork xrpNetwork = XrplNetwork.TEST;

    // The BTC network to resolve on.
//    static final XrpClient xrpClient = new XrpClient(XRP_URL, xrpNetwork);

    //    String payId = "alice$dev.payid.xpring.money";
    public static String submitPayment(String payId, PaymentType paymentType, String destinationPayId, String amount) throws Exception {
        // The Pay ID to resolve.

        final String txHash;

        switch (paymentType) {
            case BTC:
                txHash = sendBtc(payId, amount);
                break;
            case XRP:
                txHash = sendXrp(payId, "snYP7oArxKepd3GPDcrjMsJYiJeJB", destinationPayId, amount);
                break;
            default:
                txHash = ERROR;
                break;
        }


        return txHash;
    }

    private static String sendXrp(String payId, String walletSeed, String destinationPayId, String amount) throws Exception {

//        final boolean isTest = XrplNetwork.TEST.equals(xrpNetwork);
//        final Wallet wallet = new Wallet(walletSeed, isTest);
//
//        // The number of drops to send.
//        BigInteger dropsToSend = BigInteger.valueOf(Long.parseLong(amount));
//
//
//        // A client to resolve PayIDs on the XRP Ledger.
//        XrpPayIdClient xrpPayIdClient = new XrpPayIdClient(xrpNetwork);
//        System.out.println("Resolving PayID: " + payId);
//        System.out.println("On network: " + xrpNetwork);
//        String xrpAddress = xrpPayIdClient.xrpAddressForPayId(payId);
//        System.out.println("Resolved to " + xrpAddress);
//        System.out.println("");
//
//        XpringClient xpringClient = new XpringClient(xrpPayIdClient, xrpClient);
//
//        String hash = xpringClient.send(dropsToSend, destinationPayId, wallet);
//        return hash;
        return "";
    }

    private static String sendBtc(String payId, String amount) throws Exception {
        // A client to resolve PayIDs on any network..
//        PayIdClient payIdClient = new PayIdClient();
//
//        System.out.println("Resolving PayID: " + payId);
//        System.out.println("On network: " + btcNetwork);
//        CryptoAddressDetails btcAddressComponents = payIdClient.cryptoAddressForPayId(payId, btcNetwork);
//        System.out.println("Resolved to " + btcAddressComponents.getAddress());
//        System.out.println("");
//
//
//        System.out.println("Resolving All PayIDs for: " + payId);
//        List<Address> allAddresses = payIdClient.allAddressesForPayId(payId);
//        System.out.println("Resolved to:");
//        for (int i = 0; i < allAddresses.size(); i++) {
//            System.out.println(i + ") " + allAddresses.get(i));
//        }


        throw new Exception("BTC payment not supported yet");
    }
}
