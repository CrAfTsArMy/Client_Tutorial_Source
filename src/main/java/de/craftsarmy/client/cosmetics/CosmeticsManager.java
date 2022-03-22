package de.craftsarmy.client.cosmetics;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import com.squareup.okhttp.ResponseBody;
import de.craftsarmy.client.Client;
import de.craftsarmy.client.cosmetics.capes.CapeManager;
import de.craftsarmy.client.gui.overlays.ServerOfflineOverlay;
import de.craftsarmy.client.network.NetworkPlayer;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CosmeticsManager {

    private CapeManager capeManager;

    private ConcurrentLinkedQueue<NetworkPlayer> players = new ConcurrentLinkedQueue<>();
    private long last = System.currentTimeMillis();

    public CosmeticsManager init() {
        this.capeManager = new CapeManager().init();
        return this;
    }

    public void tick() {
        capeManager.tick();
        /*if (last < System.currentTimeMillis()) {
            new Thread(() -> {
                try {
                    players = getPlayersOnClient();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Thread.currentThread().interrupt();
            }).start();
            last = System.currentTimeMillis() + (1000 * 10);
        }*/
    }

    public CapeManager getCapeManager() {
        return capeManager;
    }

    public ConcurrentLinkedQueue<NetworkPlayer> getPlayersOnClient() throws IOException {
        assert Client.minecraft.player != null;
        assert !Client.minecraft.isLocalServer();
        ResponseBody body = Client.networkManager.send(Client.networkManager.patch("/data", "{}"));
        if (body != null) {
            String response = body.string();
            System.out.println("[Cosmetics Sync Response]: " + response);
            JsonObject object = JsonParser.parseString(response).getAsJsonObject();
            ConcurrentLinkedQueue<NetworkPlayer> networkPlayers = new ConcurrentLinkedQueue<>();
            for (JsonElement element : object.getAsJsonArray("data")) {
                try {
                    if (element.isJsonObject()) {
                        JsonObject data = element.getAsJsonObject();
                        String name = data.get("name").getAsString();
                        ConcurrentLinkedQueue<String> cosmetics = new ConcurrentLinkedQueue<>();
                        for (JsonElement c : data.getAsJsonArray("data"))
                            cosmetics.add(c.getAsString());
                        networkPlayers.add(new NetworkPlayer(Client.minecraft.player.connection.getPlayerInfo(name), cosmetics, true));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return networkPlayers;
        } else {
            Client.overlayManager.showOverlay(ServerOfflineOverlay.class);
        }
        return new ConcurrentLinkedQueue<>();
    }

    public ConcurrentLinkedQueue<NetworkPlayer> getPlayers() {
        return players;
    }

}
