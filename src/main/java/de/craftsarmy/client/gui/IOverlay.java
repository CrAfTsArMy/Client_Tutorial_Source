package de.craftsarmy.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;

public interface IOverlay {

    void render(PoseStack pPoseStack, Font pFont, int pMouseX, int pMouseY, float pPartialTick);

}
