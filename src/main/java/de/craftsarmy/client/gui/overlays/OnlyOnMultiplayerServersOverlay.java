package de.craftsarmy.client.gui.overlays;

import com.mojang.blaze3d.vertex.PoseStack;
import de.craftsarmy.client.Client;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;

public class OnlyOnMultiplayerServersOverlay implements IOverlay {

    @Override
    public void render(PoseStack pPostStack, Font font, int pMouseX, int pMouseY, float pPartialTick) {
        String s = "Cosmetics Sync ist nur im Multiplayer verfügbar!";
        font.draw(pPostStack, s, 2.5F, Client.minecraft.getWindow().getHeight() - 20.0F, 16731184);
    }

}
