package de.craftsarmy.client.network;

import com.squareup.okhttp.*;
import de.craftsarmy.client.Client;
import de.craftsarmy.client.cosmetics.capes.statically.DiamondCape;
import de.craftsarmy.client.gui.overlays.ServerBackOnlineOverlay;
import de.craftsarmy.client.gui.overlays.ServerOfflineOverlay;
import net.minecraft.client.multiplayer.resolver.ServerAddress;

public class NetworkManager {

    private OkHttpClient client;
    private boolean online = true;

    private boolean loggedin = false;

    public NetworkManager init() {
        client = new OkHttpClient();
        return this;
    }

    public boolean isLoggedin() {
        return loggedin;
    }

    public void login(ServerAddress data) {
        if (!loggedin)
            try {
                send(post("/account/login", "{\"name\":\"" + Client.minecraft.getUser().getName() + "\", \"server\":\"" + data.getHost() + ":" + data.getPort() + "\"}")).close();
                loggedin = true;
                Client.cosmeticsManager.getCapeManager().setCape(DiamondCape.class);
            } catch (Exception ignored) {
            }
    }

    public void logout() {
        if (loggedin)
            try {
                send(delete("/account/logout", "{\"name\":\"" + Client.minecraft.getUser().getName() + "\"}")).close();
                loggedin = false;
            } catch (Exception ignored) {
            }
    }

    public void terminate() {
        loggedin = false;
    }

    public ResponseBody send(Request request) {
        try {
            ResponseBody body = client.newCall(request).execute().body();
            if (!online && Client.isInitialized() && Client.minecraft.level != null) {
                Client.overlayManager.hideOverlay(ServerOfflineOverlay.class);
                Client.overlayManager.showOverlay(ServerBackOnlineOverlay.class);
                if (Client.serverAddress != null)
                    login(Client.serverAddress);
                online = true;
            }
            return body;
        } catch (Exception e) {
            online = false;
        }
        return null;
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
        //return "http://localhost:25565" + (path.trim().startsWith("/") ? path.trim() : "/" + path.trim());
    }

    private RequestBody prepareData(String data) {
        return RequestBody.create(MediaType.parse("application/json"), data);
    }

}
