package crawler.http;

import java.net.http.HttpClient;
import java.time.Duration;

public enum Client {
    INSTANCE;

    private final HttpClient httpClient;

    Client() {
        this.httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }
}