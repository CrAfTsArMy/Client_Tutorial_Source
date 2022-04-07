package de.craftsarmy.client;

import com.google.gson.GsonBuilder;
import com.mojang.realmsclient.RealmsMainScreen;
import de.craftsarmy.Variables;
import de.craftsarmy.client.cosmetics.CosmeticsManager;
import de.craftsarmy.client.gui.overlays.FPSOverlay;
import de.craftsarmy.client.gui.overlays.OverlayManager;
import de.craftsarmy.client.gui.overlays.ServerBackOnlineOverlay;
import de.craftsarmy.client.gui.overlays.ServerOfflineOverlay;
import de.craftsarmy.client.gui.screens.WelcomeScreen;
import de.craftsarmy.client.servers.FeaturedServerData;
import de.craftsarmy.craftscore.Core;
import de.craftsarmy.craftscore.api.config.AbstractConfig;
import de.craftsarmy.craftscore.api.threading.AbstractWorker;
import de.craftsarmy.craftscore.buildin.threading.Worker;
import de.craftsarmy.discord.RPC;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.GenericDirtMessageScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.network.chat.TranslatableComponent;

import java.io.File;

public class Client {

    private static boolean initialized = false;

    public static Minecraft minecraft;
    public static AbstractWorker worker;
    public static AbstractConfig overlayConfig;
    public static File overlayConfigFile;
    public static AbstractConfig clientConfig;
    public static File clientConfigFile;
    public static OverlayManager overlayManager;
    public static CosmeticsManager cosmeticsManager;

    public static String websiteUrl = "https://web1.craftsblock.de";
    public static ServerAddress serverAddress = null;

    public static void init(Minecraft minecraft) {
        Client.minecraft = minecraft;
        worker = Core.instance().getWorker();
        overlayManager = new OverlayManager().init();
        cosmeticsManager = new CosmeticsManager().init();

        overlayConfigFile = new File("./configs", "overlays.json");
        overlayConfig = Core.instance().getConfigParser().parse(overlayConfigFile);
        if (!overlayConfig.contains("overlay.fps")) {
            overlayConfig.setBoolean("overlay.fps.enabled", true);
            overlayConfig.setDouble("overlay.fps.x", 10.5);
            overlayConfig.setDouble("overlay.fps.y", 2.5);
            overlayConfig.save(overlayConfigFile);
        }

        clientConfigFile = new File("./configs", "client.json");
        clientConfig = Core.instance().getConfigParser().parse(clientConfigFile);
        if (!clientConfig.contains("welcome.screen.shown")) {
            clientConfig.setBoolean("welcome.screen.shown", false);
            clientConfig.save(clientConfigFile);
        }

        initialized = true;
    }

    public static boolean isFeaturedServer(ServerData data) {
        return (data instanceof FeaturedServerData);
    }

    public static void tick() {
        if (minecraft.level != null) {
            overlayManager.showOverlay(FPSOverlay.class);
            cosmeticsManager.tick();
        } else {
            overlayManager.hideOverlay(FPSOverlay.class);
            overlayManager.hideOverlay(ServerOfflineOverlay.class);
            overlayManager.hideOverlay(ServerBackOnlineOverlay.class);
        }
    }

    public static void resetRpc() {
        RPC.instance().update("Idling", "Main Menu", "", "");
        Variables.server = false;
    }

    public static void disconnect() {
        disconnect(true);
    }

    public static void disconnect(boolean titlescreen) {
        if (minecraft.level != null) {
            boolean flag = minecraft.isLocalServer();
            minecraft.level.disconnect();
            if (flag)
                minecraft.clearLevel(new GenericDirtMessageScreen(new TranslatableComponent("menu.savingLevel")));
            else
                minecraft.clearLevel();
            if (titlescreen) {
                TitleScreen ts = new TitleScreen();
                if (flag) {
                    minecraft.setScreen(ts);
                } else if (minecraft.isConnectedToRealms()) {
                    minecraft.setScreen(new RealmsMainScreen(ts));
                } else {
                    minecraft.setScreen(new JoinMultiplayerScreen(ts));
                }
            }
        }
    }

    public static boolean isInitialized() {
        return initialized;
    }

}
