package gh.filesharing.client.utils;

import javax.net.ssl.*;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class HttpsConnectionManager {

    public static SSLSocketFactory getSocketFactory() throws Exception {
        InputStream certificateInputStream = HttpsConnectionManager.class.getResourceAsStream("/gh/filesharing/client/keystore/app_certificate.cer");

        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate cert = (X509Certificate) cf.generateCertificate(certificateInputStream);

        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(null, null); // empty KeyStore
        trustStore.setCertificateEntry("server", cert);

        // TrustManager that trusts our certificate
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(trustStore);
        TrustManager[] trustManagers = tmf.getTrustManagers();

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustManagers, null);

        return sslContext.getSocketFactory();
    }
}