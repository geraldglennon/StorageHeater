package com.glennon.storageHeater.client;


import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class RestClient {

    private Client client;

    public RestClient() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = SSLContext.getInstance("SSL");

        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

        client = ClientBuilder.newBuilder()
                .sslContext(sslContext)
                .hostnameVerifier(new HostnameVerifier() {
                    public boolean verify(String s, SSLSession sslSession) {
                        return true;
                    }
                })
                .build();

    }

    public Client getClient() {
        return client;
    }

    private final TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(
                        final java.security.cert.X509Certificate[] arg0, final String arg1)
                        throws CertificateException {
                }

                public void checkServerTrusted(
                        final java.security.cert.X509Certificate[] arg0, final String arg1)
                        throws CertificateException {
                }

            }
    };
}
