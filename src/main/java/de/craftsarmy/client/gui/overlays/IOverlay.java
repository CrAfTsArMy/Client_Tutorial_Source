package de.craftsarmy.client.gui.overlays;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;

public interface IOverlay {

    void render(PoseStack pPostStack, Font font, int pMouseX, int pMouseY, float pPartialTick);

}
