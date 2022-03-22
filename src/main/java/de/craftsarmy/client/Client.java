package de.craftsarmy.client;

import com.mojang.realmsclient.RealmsMainScreen;
import de.craftsarmy.Variables;
import de.craftsarmy.client.cosmetics.CosmeticsManager;
import de.craftsarmy.client.cosmetics.capes.ICape;
import de.craftsarmy.client.cosmetics.capes.animated.AbstractAnimatedCape;
import de.craftsarmy.client.cosmetics.capes.animated.AnimatedCape;
import de.craftsarmy.client.gui.overlays.*;
import de.craftsarmy.client.gui.screens.WelcomeScreen;
import de.craftsarmy.client.network.NetworkManager;
import de.craftsarmy.client.utils.AbstractAnimatedTexture;
import de.craftsarmy.client.utils.Animation;
import de.craftsarmy.discord.RPC;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.GenericDirtMessageScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.network.chat.TranslatableComponent;

public class Client {

    private static boolean initialized = false;

    public static Minecraft minecraft;
    public static OverlayManager overlayManager;
    public static CosmeticsManager cosmeticsManager;
    public static NetworkManager networkManager;

    public static String websiteUrl = "https://web1.craftsblock.de";
    public static ServerAddress serverAddress = null;

    public static void init(Minecraft minecraft) {
        Client.minecraft = minecraft;
        overlayManager = new OverlayManager().init();
        networkManager = new NetworkManager().init();
        cosmeticsManager = new CosmeticsManager().init();
        minecraft.setScreen(new WelcomeScreen());
        initialized = true;
    }

    public static void tick() {
        if (minecraft.level != null) {
            overlayManager.showOverlay(FPSOverlay.class);
            cosmeticsManager.tick();
        } else {
            overlayManager.hideOverlay(FPSOverlay.class);
            overlayManager.hideOverlay(ServerOfflineOverlay.class);
            overlayManager.hideOverlay(ServerBackOnlineOverlay.class);
            overlayManager.hideOverlay(OnlyOnMultiplayerServersOverlay.class);
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
        networkManager.logout();
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
