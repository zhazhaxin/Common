package cn.lemon.common.net;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import okhttp3.OkHttpClient;

/**
 * Created by linlongxin on 2016/7/26.
 */

public class SSLManager {

    private final String TAG = "SSLManager";
    private OkHttpClient mOkHttpClient;
    private OkHttpClient.Builder mClientBuilder;
    private static SSLManager instance;

    private SSLManager() {
        LogInterceptor mLogInterceptor = new LogInterceptor();
        mLogInterceptor.setLogTag("self_sign_https");
        mClientBuilder = new OkHttpClient.Builder()
                .addInterceptor(mLogInterceptor);
    }

    public static SSLManager getInstance() {
        if (instance == null) {
            instance = new SSLManager();
        }
        return instance;
    }

    public void setCertificates(InputStream... certificates) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));

                try {
                    if (certificate != null)
                        certificate.close();
                } catch (IOException e) {
                    Log.i(TAG,"certificate e : " + e.getMessage());
                }
            }

            SSLContext sslContext = SSLContext.getInstance("TLS");

            TrustManagerFactory trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

            trustManagerFactory.init(keyStore);
            sslContext.init(null,
                            trustManagerFactory.getTrustManagers(),
                            new SecureRandom());
            mClientBuilder.sslSocketFactory(sslContext.getSocketFactory());
            mOkHttpClient = mClientBuilder.build();

        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG,"e : " + e.getMessage());
        }
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }
}
