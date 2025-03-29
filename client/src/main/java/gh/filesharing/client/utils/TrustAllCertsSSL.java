package gh.filesharing.client.utils;

import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

/**
 * Clase utilitaria para configurar un contexto SSL que confía en todos los certificados.
 * Esta clase es útil para entornos de desarrollo donde se utilizan certificados autofirmados.
 */
public class TrustAllCertsSSL {

    /**
     * Obtiene un contexto SSL que confía en todos los certificados.
     *
     * @return Un objeto SSLContext configurado para confiar en todos los certificados.
     * @throws NoSuchAlgorithmException Si el algoritmo SSL no está disponible.
     * @throws KeyManagementException Si hay un problema al inicializar el contexto SSL.
     */
    public static SSLContext getSSLContext() throws NoSuchAlgorithmException, KeyManagementException {
        TrustManager[] trustAllCerts = new TrustManager[] {
            new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
        };

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        return sc;
    }
}