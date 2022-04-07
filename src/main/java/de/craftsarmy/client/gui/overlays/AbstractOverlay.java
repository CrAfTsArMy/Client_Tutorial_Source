package de.craftsarmy.client.gui.overlays;

import com.mojang.blaze3d.vertex.PoseStack;
import de.craftsarmy.craftscore.utils.Touch;
import net.minecraft.client.gui.Font;

public abstract class AbstractOverlay extends Touch.TouchAble {

    public AbstractOverlay(Class<?> from) {
        super(from);
    }

    public abstract void render(PoseStack pPostStack, Font font, int pMouseX, int pMouseY, float pPartialTick);

}
