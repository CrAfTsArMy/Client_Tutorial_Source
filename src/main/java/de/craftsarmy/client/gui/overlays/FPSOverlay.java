package de.craftsarmy.client.gui.overlays;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.optifine.Config;

public class FPSOverlay implements IOverlay {

    @Override
    public void render(PoseStack pPostStack, Font font, int pMouseX, int pMouseY, float pPartialTick) {
        String fps = "FPS: " + Config.getFpsString();
        font.draw(pPostStack, fps, 10.0F, 10.0F, -2039584);
    }

}
