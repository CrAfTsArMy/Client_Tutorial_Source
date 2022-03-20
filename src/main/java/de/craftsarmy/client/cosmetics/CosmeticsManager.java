package de.craftsarmy.client.cosmetics;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import de.craftsarmy.client.Client;
import de.craftsarmy.client.cosmetics.capes.CapeManager;
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
        if (last < System.currentTimeMillis()) {
            new Thread(() -> {
                try {
                    players = getPlayersOnClient();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Thread.currentThread().interrupt();
            }).start();
            last = System.currentTimeMillis() + (1000 * 10);
        }
    }

    public CapeManager getCapeManager() {
        return capeManager;
    }

    public ConcurrentLinkedQueue<NetworkPlayer> getPlayersOnClient() throws IOException {
        assert Client.minecraft.player != null;
        Collection<UUID> players = Client.minecraft.player.connection.getOnlinePlayerIds();
        StringWriter request = new StringWriter();
        JsonWriter writer = new JsonWriter(request);
        writer.beginObject().name("data").beginArray();
        for (UUID uuid : players)
            try {
                if (Objects.requireNonNull(Client.minecraft.player.connection.getPlayerInfo(uuid)).isOnClient())
                    writer.value(Objects.requireNonNull(Client.minecraft.player.connection.getPlayerInfo(uuid)).getProfile().getName());
            } catch (Exception ignored) {
            }
        writer.endArray().endObject().flush();
        String response = Client.networkManager.send(Client.networkManager.patch("/data", request.toString())).string();
        System.out.println("[Cosmetics Sync Response]: " + response);
        JsonObject object = JsonParser.parseString(response).getAsJsonObject();
        ConcurrentLinkedQueue<NetworkPlayer> networkPlayers = new ConcurrentLinkedQueue<>();
        for (JsonElement element : object.getAsJsonArray("data")) {
            if (element.isJsonObject()) {
                JsonObject data = element.getAsJsonObject();
                String name = data.get("name").getAsString();
                String message = data.get("message").getAsString();
                if (message.trim().equalsIgnoreCase("user.not.exists")) {
                    networkPlayers.add(new NetworkPlayer(Client.minecraft.player.connection.getPlayerInfo(name), null, false));
                } else {
                    ConcurrentLinkedQueue<String> cosmetics = new ConcurrentLinkedQueue<>();
                    for (JsonElement c : data.getAsJsonArray("data"))
                        cosmetics.add(c.getAsString());
                    networkPlayers.add(new NetworkPlayer(Client.minecraft.player.connection.getPlayerInfo(name), cosmetics, true));
                }
            }
        }
        return networkPlayers;
    }

    public ConcurrentLinkedQueue<NetworkPlayer> getPlayers() {
        return players;
    }
}
