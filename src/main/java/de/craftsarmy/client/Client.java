package de.craftsarmy.client;

import com.mojang.realmsclient.RealmsMainScreen;
import de.craftsarmy.Variables;
import de.craftsarmy.client.gui.overlays.FPSOverlay;
import de.craftsarmy.client.gui.overlays.OverlayManager;
import de.craftsarmy.client.gui.screens.WelcomeScreen;
import de.craftsarmy.discord.RPC;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.GenericDirtMessageScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.network.chat.TranslatableComponent;

public class Client {

    private static boolean initialized = false;

    public static Minecraft minecraft;
    public static OverlayManager overlayManager;

    public static String websiteUrl = "https://web1.craftsblock.de";

    public static void init(Minecraft minecraft) {
        Client.minecraft = minecraft;
        overlayManager = new OverlayManager();
        minecraft.setScreen(new WelcomeScreen());
        initialized = true;
    }

    public static void tick() {
        if(minecraft.level != null)
            overlayManager.showOverlay(FPSOverlay.class);
        else
            overlayManager.hideOverlay(FPSOverlay.class);
    }

    public static void resetRpc() {
        RPC.instance().update("Idling", "Main Menu", "", "");
        Variables.server = false;
    }

    public static void disconnect() {
        disconnect(true);
    }

    public static void disconnect(boolean titlescreen) {
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

    public static boolean isInitialized() {
        return initialized;
    }

}
