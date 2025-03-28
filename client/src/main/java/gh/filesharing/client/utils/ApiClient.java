package gh.filesharing.client.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.*;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.client5.http.ssl.TrustAllStrategy;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.core5.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.Map;

public class ApiClient {
    private static final String BASE_URL = "https://localhost:8080";
    private static final InputStream TRUSTSTORE_PATH = ApiClient.class.getClassLoader().getResourceAsStream("keystores/server-keystore.jks");

    private static final String TRUSTSTORE_PASSWORD = "grupo8";
    private static final ObjectMapper objectMapper = new ObjectMapper();


    // instance HttpClient with SSL context
    private static CloseableHttpClient createHttpClient() throws Exception {
        System.out.println("Classpath: " + System.getProperty("java.class.path"));

        try (InputStream trustStoreStream = ApiClient.class.getClassLoader().getResourceAsStream("gh/filesharing/client/keystores/client-truststore.jks")) {
            if (trustStoreStream == null) {
                throw new FileNotFoundException("Truststore not found in classpath.");
            }

            KeyStore trustStore = KeyStore.getInstance("JKS");
            trustStore.load(trustStoreStream, TRUSTSTORE_PASSWORD.toCharArray());

            SSLContext sslcontext = SSLContexts.custom()
                    .loadTrustMaterial(trustStore, new TrustAllStrategy())
                    .build();
            SSLConnectionSocketFactory sslSocketFactory = SSLConnectionSocketFactoryBuilder.create()
                    .setSslContext(sslcontext)
                    .build();
            HttpClientConnectionManager cm = PoolingHttpClientConnectionManagerBuilder.create()
                    .setSSLSocketFactory(sslSocketFactory)
                    .build();

            return HttpClients.custom().setConnectionManager(cm).evictExpiredConnections().build();
        }
    }


    public static String post(String endpoint, Map<String, String> params) throws Exception {
        String url = BASE_URL + endpoint;
        try (CloseableHttpClient client = createHttpClient()) {
            HttpPost post = new HttpPost(url);

            // Convert parameters to JSON
            String json = objectMapper.writeValueAsString(params);
            post.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
            post.setHeader("Content-Type", "application/json");

            try (var response = client.execute(post)) {
                return response.getEntity() != null ? response.getEntity().getContent().toString() : "";
            }
        }
    }

    public static String get(String endpoint) throws Exception {
        String url = BASE_URL + endpoint;
        try (CloseableHttpClient client = createHttpClient()) {
            HttpGet get = new HttpGet(url);
            get.setHeader("Accept", "application/json");

            try (var response = client.execute(get)) {
                return response.getEntity() != null ? response.getEntity().getContent().toString() : "";
            }
        }
    }
}
