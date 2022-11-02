package org.atm.util;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RequestCurrency {
    public static String response() throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().
                uri(new URI("https://www.cbr-xml-daily.ru/daily_json.js"))
                .GET()
                .build();
        HttpResponse<String> client = HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());

        return client.body();
    }
}
