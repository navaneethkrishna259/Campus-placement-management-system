package com.placementhub.company.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * REST API client helper for desktop applications.
 * Handles HTTP requests, JSON serialization/deserialization, and authentication headers.
 */
public class ApiClient {

    private static final String BASE_URL = "http://localhost:8080";
    private static final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();
    private static final ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private static String authToken = null;

    public static void setAuthToken(String token) {
        authToken = token;
    }

    public static String getAuthToken() {
        return authToken;
    }

    public static void clearAuthToken() {
        authToken = null;
    }

    public static <T> T get(String endpoint, TypeReference<T> responseType) throws Exception {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Accept", "application/json")
                .timeout(Duration.ofSeconds(10))
                .GET();

        if (authToken != null) {
            builder.header("Authorization", "Bearer " + authToken);
        }

        HttpResponse<String> response = sendRequest(builder.build());
        return mapper.readValue(response.body(), responseType);
    }

    public static <T> T post(String endpoint, Object requestBody, TypeReference<T> responseType) throws Exception {
        String json = requestBody != null ? mapper.writeValueAsString(requestBody) : "";
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .timeout(Duration.ofSeconds(10))
                .POST(HttpRequest.BodyPublishers.ofString(json));

        if (authToken != null) {
            builder.header("Authorization", "Bearer " + authToken);
        }

        HttpResponse<String> response = sendRequest(builder.build());
        return mapper.readValue(response.body(), responseType);
    }

    public static <T> T patch(String endpoint, Object requestBody, TypeReference<T> responseType) throws Exception {
        String json = requestBody != null ? mapper.writeValueAsString(requestBody) : "";
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .timeout(Duration.ofSeconds(10))
                .method("PATCH", HttpRequest.BodyPublishers.ofString(json));

        if (authToken != null) {
            builder.header("Authorization", "Bearer " + authToken);
        }

        HttpResponse<String> response = sendRequest(builder.build());
        return mapper.readValue(response.body(), responseType);
    }

    public static <T> T put(String endpoint, Object requestBody, TypeReference<T> responseType) throws Exception {
        String json = requestBody != null ? mapper.writeValueAsString(requestBody) : "";
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .timeout(Duration.ofSeconds(10))
                .PUT(HttpRequest.BodyPublishers.ofString(json));

        if (authToken != null) {
            builder.header("Authorization", "Bearer " + authToken);
        }

        HttpResponse<String> response = sendRequest(builder.build());
        return mapper.readValue(response.body(), responseType);
    }

    public static <T> T delete(String endpoint, TypeReference<T> responseType) throws Exception {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Accept", "application/json")
                .timeout(Duration.ofSeconds(10))
                .DELETE();

        if (authToken != null) {
            builder.header("Authorization", "Bearer " + authToken);
        }

        HttpResponse<String> response = sendRequest(builder.build());
        return mapper.readValue(response.body(), responseType);
    }

    private static HttpResponse<String> sendRequest(HttpRequest request) throws Exception {
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new Exception("Cannot connect to the server. Please start the backend and try again.");
        }
    }
}
