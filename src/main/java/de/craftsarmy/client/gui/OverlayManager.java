package de.craftsarmy.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import de.craftsarmy.client.Client;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ConcurrentHashMap;

public class OverlayManager {

    private final ConcurrentHashMap<Class<?>, IOverlay> overlays = new ConcurrentHashMap<>();

    public void showOverlay(Class<?> clazz) {
        try {
            IOverlay overlay = (IOverlay) touch(clazz);
            assert overlay != null;
            overlays.put(clazz, overlay);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideOverlay(Class<?> clazz) {
        overlays.remove(clazz);
    }

    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        for(Class<?> clazz : overlays.keySet())
            overlays.get(clazz).render(pPoseStack, Client.minecraft.font, pMouseX, pMouseY, pPartialTick);
    }

    private Object touch(Class<?> clazz) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        boolean correct = false;
        for (Class<?> interfaze : clazz.getInterfaces()) {
            if (interfaze.getDeclaredConstructor().newInstance() instanceof IOverlay) {
                correct = true;
                break;
            }
        }
        if (correct)
            return clazz.getDeclaredConstructor().newInstance();
        return null;
    }

}
