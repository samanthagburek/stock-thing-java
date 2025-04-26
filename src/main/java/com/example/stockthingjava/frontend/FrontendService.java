package com.example.stockthingjava.frontend;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class FrontendService {

    private final HttpClient client = HttpClient.newHttpClient();
    private static final String BASE_URL = "http://localhost:8080";

    public void fetchDetectionRules(FrontendCallback callback) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/detection-rules"))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(callback::onSuccess)
                .exceptionally(ex -> {
                    callback.onError(ex);
                    return null;
                });
    }

    // Callback interface
    public interface FrontendCallback {
        void onSuccess(String response);
        void onError(Throwable throwable);
    }
}
