package de.craftsarmy.discord;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import de.craftsarmy.Variables;

public class RPC {

    private final DiscordRPC lib = DiscordRPC.INSTANCE;
    private final String largeImageKey$final;
    private final String largeImageText$final;
    private String largeImageKey;
    private String largeImageText;
    private RPCCache rpcCache;
    private RPCParty rpcParty;
    private boolean party = false;
    private static boolean created = false;
    private long start;
    private Thread worker;

    public RPC(String largeImageKey, String largeImageText) {
        this.largeImageKey$final = largeImageKey;
        this.largeImageText$final = largeImageText;
        this.largeImageKey = largeImageKey;
        this.largeImageText = largeImageText;
        this.start = -1;
    }

    public RPC create() {
        String applicationId = "947109582482837524";
        String steamId = "";
        DiscordEventHandlers handlers = new DiscordEventHandlers();
        handlers.ready = (user) -> System.out.println("Ready!");
        lib.Discord_Initialize(applicationId, handlers, true, steamId);
        worker = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                lib.Discord_RunCallbacks();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {
                }
            }
        }, "RPC-Callback-Handler");
        worker.start();
        return this;
    }

    public void destroy() {
        worker.interrupt();
        worker = null;
        party = false;
        rpcCache = null;
        rpcParty = null;
        start = -1;
        lib.Discord_Shutdown();
    }

    public RPC update(String state, String details, String smallKey, String smallText) {
        DiscordRichPresence presence = new DiscordRichPresence();
        if (start == -1)
            start = System.currentTimeMillis() / 1000;
        presence.startTimestamp = start;
        presence.state = state;
        presence.details = details;
        presence.largeImageKey = largeImageKey;
        presence.largeImageText = largeImageText;
        presence.smallImageKey = smallKey;
        presence.smallImageText = smallText;
        if (party) {
            presence.partyId = rpcParty.getId();
            presence.partySize = rpcParty.getSize();
            presence.partyMax = rpcParty.getMax();
            presence.joinSecret = rpcParty.getJoinsecret();
        }
        lib.Discord_UpdatePresence(presence);
        rpcCache = new RPCCache(state, details, smallKey, smallText);
        return this;
    }

    public RPC setupParty(RPCParty rpcParty) {
        this.rpcParty = rpcParty;
        Variables.rpcParty = rpcParty;
        party = true;
        update(rpcCache.getState(), rpcCache.getDetails(), rpcCache.getSmallKey(), rpcCache.getSmallText());
        return this;
    }

    public void destroyParty() {
        this.rpcParty = null;
        Variables.rpcParty = null;
        party = false;
        update(rpcCache.getState(), rpcCache.getDetails(), rpcCache.getSmallKey(), rpcCache.getSmallText());
    }

    public void setLargeImage(String key) {
        this.largeImageKey = key;
    }

    public void resetLargeImage() {
        this.largeImageKey = this.largeImageKey$final;
    }

    public void setLargeImageText(String text) {
        this.largeImageText = text;
    }

    public void resetLargeImageText() {
        this.largeImageText = this.largeImageText$final;
    }

    public static RPC instance() {
        if(!created)
            return Variables.rpc.create();
        return Variables.rpc;
    }

}
