package com.amazon.android.contentbrowser.payments;

import com.google.gson.Gson;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.xpring.payid.PayIdClient;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class PayIdHelper {

    static final String TEST_AUTH = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6ImM0MTIwYzE5ODlhNWQzNmQxZjAxODQxYjM5YTVmNjE3NTM4ZTk2M2I1NDFmMTBmODRmYjExNzQ5YjEzOTQ3YjkifQ.eyJqdGkiOiI1MzEzOGE1Yi1mMDlhLTRlYzctOThiNS00ZTQzNGUxM2Y1OTIiLCJzdWIiOiIxOWY5N2QxMC1kZDE4LTQwNDEtODNhZS1hNTRhYWVmYzQzMDUiLCJpZCI6IjE5Zjk3ZDEwLWRkMTgtNDA0MS04M2FlLWE1NGFhZWZjNDMwNSIsImdpdGh1YklkIjoiMjM1MTA4NyIsInNhbmRib3hJZCI6ImU4NDYwZTAwLWMzZjQtNDgzOS1hYjlhLWVjZTRmZDExMDA2ZiIsImF1dGhUeXBlIjoidG9rZW4iLCJpYXQiOjE2MDIwMzg4MzgsImF1ZCI6Imh0dHBzOi8vcGF5aWQub3JnL3NhbmRib3giLCJpc3MiOiJodHRwczovL3BheWlkLm9yZy9zYW5kYm94LyJ9.R3YAas94N5xULobFT01Nkkvi0frfjSNPL0gCD0Ifh7DG0jwWaUWd03SZXWXXzgUcgIUUJCB_vIQqLCk1SiSze2CCAwu3k6curWZWUrUAS2v-D525aSqtu6g4Pz2fD2YjqJwFYRB8_dX2gtwMdk_3jXCoU7EYE49zb-hKHOaVnYlhgxwu-m76NmRAchRGdPGuUCTOxoArmENq4MQjQ0npSxP_AwBjf5bIY7FNas0lc522vRPZrKnPdFkUjteyHXvu89WgJdiutplfmE1E_f4hRj-zi6spa4bL9XXuqRIxDvFkepNRsIUTuA0Jq-1t5dsQccm7ialvvbGQObcdHmYlGiofzR0F-STWufMVmpW6oNCr3JyiqoiUx1glAg6tr6JdnXEMa3HvEwQgqnt2QyN3r8QakbFEIBNdv5_T8hkX1t6iuzyFeuGGmhRgyKQrU75kvWikpeE4WQfhHKhurVa7xVDm4nh3K5IwFpdG6P9GA2bv9kYEmoAHe56l--byFyvGCM-FPmX2OR77HBMvjkvnGToIhNxl4iUQ2qYu24NLF21s3dEz-BkSikO78zYGZ2ROTHSvwKlVz_P5WITnpAljIZZ4Q4HN2e2_vQxfM0l2fMuSDHdG1jVI34IB3ADQ9eGnJlaIufdACgFnw2VpdXGMgnoEBczcLALgWI15cmuGqaA";

    // The expected address of the gRPC server.
    static final String XRP_URL = "test.xrp.xpring.io:50051";
    static final String BTC_NETWORK = "btc-testnet";
    static final String PAYTV_SERVER = "paytv.sandbox.payid.org";

    public static String createPayIdUrl(String userName) {
        return String.format("%s$%s", userName, PAYTV_SERVER);
    }

    static final String TEST_MNEMONIC = "abandon abandon abandon abandon abandon abandon abandon abandon abandon abandon abandon about";
    static final String ERROR = "Error";
    //
//    // The XRP Ledger network to resolve on.
//    static final XrplNetwork xrpNetwork = XrplNetwork.TEST;
//
//    // The BTC network to resolve on.
//    static final XrpClient xrpClient = new XrpClient(XRP_URL, xrpNetwork);
    public static final OkHttpClient HTTP_CLIENT = getUnsafeOkHttpClient();

    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Call getAddresses(String payId, Callback callback) {
        String url = String.format("https://payid.org/sandbox/users/%s", payId);
        Request request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .addHeader("Authorization", "Bearer " + TEST_AUTH)
                .addHeader("Content-Type", "application/json")
                .build();
        Call call = HTTP_CLIENT.newCall(request);
        call.enqueue(callback);
        return call;
    }

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

        // The number of drops to send.
//        BigInteger dropsToSend = BigInteger.valueOf(Long.parseLong(amount));


//        // A client to resolve PayIDs on the XRP Ledger.
//        XrpPayIdClient xrpPayIdClient = new XrpPayIdClient(xrpNetwork);
//        System.out.println("Resolving PayID: " + payId);
//        System.out.println("On network: " + xrpNetwork);
//        String xrpAddress = xrpPayIdClient.xrpAddressForPayId(payId);
//        System.out.println("Resolved to " + xrpAddress);
//        System.out.println("");
//
//        XpringClient xpringClient = new XpringClient(xrpPayIdClient, xrpClient);

//        String hash = xpringClient.send(dropsToSend, destinationPayId, wallet);
        return "";
    }

    private static String sendBtc(String payId, String amount) throws Exception {
        // A client to resolve PayIDs on any network..
        PayIdClient payIdClient = new PayIdClient();

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
