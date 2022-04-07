package de.craftsarmy.client.gui.overlays;

import com.mojang.blaze3d.vertex.PoseStack;
import de.craftsarmy.client.Client;
import net.minecraft.client.gui.Font;
import net.optifine.Config;

public class FPSOverlay extends AbstractOverlay {

    public FPSOverlay(Class<?> from) {
        super(from);
    }

    @Override
    public void render(PoseStack pPostStack, Font font, int pMouseX, int pMouseY, float pPartialTick) {
        if(Client.overlayConfig.getBoolean("overlay.fps.enabled")) {
            String fps = "FPS: " + Config.getFpsString();
            float pX = (float) Client.overlayConfig.getDouble("overlay.fps.x");
            float pY = (float) Client.overlayConfig.getDouble("overlay.fps.y");
            font.draw(pPostStack, fps, pX, pY, -2039584);
        }
    }

}
