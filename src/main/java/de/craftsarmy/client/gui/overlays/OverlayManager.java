package de.craftsarmy.client.gui.overlays;

import com.mojang.blaze3d.vertex.PoseStack;
import de.craftsarmy.client.Client;
import de.craftsarmy.craftscore.utils.Touch;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class OverlayManager {

    private final ConcurrentHashMap<Class<? extends AbstractOverlay>, AbstractOverlay> overlays = new ConcurrentHashMap<>();
    private Touch<AbstractOverlay> touch;

    public OverlayManager init() {
        touch = new Touch<>(this.getClass());
        return this;
    }

    public void showOverlay(Class<? extends AbstractOverlay> clazz) {
        try {
            overlays.put(clazz, Objects.requireNonNull(touch.touch(clazz)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideOverlay(Class<? extends AbstractOverlay> clazz) {
        overlays.remove(clazz);
    }

    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        for (Class<?> clazz : overlays.keySet())
            overlays.get(clazz).render(pPoseStack, Client.minecraft.font, pMouseX, pMouseY, pPartialTick);
    }

}
