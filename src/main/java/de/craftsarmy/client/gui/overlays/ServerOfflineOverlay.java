package de.craftsarmy.client.gui.overlays;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;

public class ServerOfflineOverlay implements IOverlay {

    @Override
    public void render(PoseStack pPostStack, Font font, int pMouseX, int pMouseY, float pPartialTick) {
        String s = "Cosmetics Server nicht Verbunden!";
        font.draw(pPostStack, s, 2.5F, 15.5F, 16731184);
    }

}
