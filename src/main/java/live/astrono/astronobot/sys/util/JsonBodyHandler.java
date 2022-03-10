package main.java.live.astrono.astronobot.sys.util;

import main.java.live.astrono.astronobot.AstronoBot;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.http.HttpResponse;

public class JsonBodyHandler<T> implements HttpResponse.BodyHandler<T> {

    private Class<T> clazz;

    JsonBodyHandler(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public HttpResponse.BodySubscriber<T> apply(HttpResponse.ResponseInfo responseInfo) {
        HttpResponse.BodySubscriber<InputStream> upstream = HttpResponse.BodySubscribers.ofInputStream();

        return HttpResponse.BodySubscribers.mapping(
                upstream,
                inputStream -> AstronoBot.GSON.fromJson(new InputStreamReader(inputStream), this.clazz));
    }
}
