package de.craftsarmy.client.network;

import com.squareup.okhttp.*;
import de.craftsarmy.client.Client;

import java.io.IOException;

public class NetworkManager {

    private OkHttpClient client;

    public NetworkManager init() {
        client = new OkHttpClient();
        try {
            send(post("/account/login", "{\"name\":\""+Client.minecraft.getUser().getName()+"\"}")).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public ResponseBody send(Request request) throws IOException {
        return client.newCall(request).execute().body();
    }

    public Request get(String path) {
        return new Request.Builder()
                .url(prepareURL(path))
                .get()
                .build();
    }

    public Request post(String path, String data) {
        return new Request.Builder()
                .url(prepareURL(path))
                .post(prepareData(data))
                .build();
    }

    public Request put(String path, String data) {
        return new Request.Builder()
                .url(prepareURL(path))
                .put(prepareData(data))
                .build();
    }

    public Request patch(String path, String data) {
        return new Request.Builder()
                .url(prepareURL(path))
                .patch(prepareData(data))
                .build();
    }

    public Request delete(String path) {
        return new Request.Builder()
                .url(prepareURL(path))
                .get()
                .build();
    }

    public Request delete(String path, String data) {
        return new Request.Builder()
                .url(prepareURL(path))
                .delete(prepareData(data))
                .build();
    }

    private String prepareURL(String path) {
        return "http://endpoint.craftsblock.de:25565" + (path.trim().startsWith("/") ? path.trim() : "/" + path.trim());
    }

    private RequestBody prepareData(String data) {
        return RequestBody.create(MediaType.parse("application/json"), data);
    }

}
