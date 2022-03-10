package main.java.live.astrono.astronobot.sys.util;

import main.java.live.astrono.astronobot.AstronoBot;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpUtil {
    public static String getContentSync(String url) {
        HttpRequest request = HttpRequest.newBuilder(URI.create(url)).build();
        try {
            return AstronoBot.HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "";
        }
    }
}
