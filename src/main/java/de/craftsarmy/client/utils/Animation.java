package de.craftsarmy.client.utils;

import de.craftsarmy.client.cosmetics.capes.animated.AbstractAnimatedCape;
import net.minecraft.resources.ResourceLocation;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Animation<E> {

    private final String base;
    private final Touch<E> touch;

    private boolean ready = false;
    private final List<AnimationTile<E>> tiles = new ArrayList<>();
    private int current = 0;

    public Animation(Class<E> clazz, String base, String prefix) {
        touch = new Touch<>();
        this.base = base;
        new Thread(() -> {
            ConcurrentLinkedQueue<ResourceLocation> locations = new ConcurrentLinkedQueue<>();
            for (int i = 0; i < 100; i++) {
                try {
                    ResourceLocation location = new ResourceLocation((base.trim().toLowerCase().endsWith("/") ? base.trim().toLowerCase() : base.trim().toLowerCase() + "/") + prefix + "_" + i);
                    locations.add(location);
                } catch (Exception ignored) {
                }
            }
            for (ResourceLocation location : locations) {
                try {
                    tiles.add(new AnimationTile<>(touch.touch(clazz), location));
                } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            ready = true;
            Thread.currentThread().interrupt();
        });
    }

    public String getBase() {
        return base;
    }

    public int getCurrent() {
        return current;
    }

    public void cycle() {
        assert ready;
        if (current >= tiles.size())
            current = 0;
        else
            current += 1;
    }

    public AnimationTile<E> get() {
        assert ready;
        return get(current);
    }

    private AnimationTile<E> get(int tile) {
        assert ready;
        return tiles.get(tile);
    }

    public AnimationTile<E> cycleGet() {
        assert ready;
        int temp = current;
        cycle();
        return get(temp);
    }

    public static class AnimationTile<E> {

        private final E data;

        public AnimationTile(E e, ResourceLocation location) {
            if (e instanceof AbstractAnimatedCape) {
                data = e;
                ((AbstractAnimatedTexture) e).update(location);
                return;
            }
            data = null;
        }

        public E getData() {
            return data;
        }

    }

}
