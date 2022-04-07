package de.craftsarmy.client.gui.overlays;

import com.mojang.blaze3d.vertex.PoseStack;
import de.craftsarmy.client.Client;
import net.minecraft.client.gui.Font;

public class ServerBackOnlineOverlay extends AbstractOverlay {

    private final long autoremove = System.currentTimeMillis() + (1000 * 20);

    public ServerBackOnlineOverlay(Class<?> from) {
        super(from);
    }

    @Override
    public void render(PoseStack pPostStack, Font font, int pMouseX, int pMouseY, float pPartialTick) {
        String s = "Cosmetics Server wieder Verbunden!";
        font.draw(pPostStack, s, 2.5F, 15.5F, 9697957);
        if (autoremove <= System.currentTimeMillis())
            Client.overlayManager.hideOverlay(this.getClass());
    }

}
