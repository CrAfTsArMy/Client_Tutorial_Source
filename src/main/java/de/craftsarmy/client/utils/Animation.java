package de.craftsarmy.client.utils;

import de.craftsarmy.client.Client;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Animation<E extends AbstractAnimatedTexture> {

    private final String base;
    private final String prefix;
    private final Touch<E> touch;

    private boolean ready = false;
    private final List<AnimationTile<E>> tiles = new ArrayList<>();
    private int current = 0;

    private final Class<? extends E> clazz;

    public Animation(Class<? extends E> clazz, String base, String prefix) {
        touch = new Touch<>();
        this.base = base;
        this.prefix = prefix;
        this.clazz = clazz;
        Client.worker.submit(StartupTask.class);
    }

    private final class StartupTask implements Worker.Task {

        @Override
        public void run() {
            ConcurrentLinkedQueue<ResourceLocation> locations = new ConcurrentLinkedQueue<>();
            for (int i = 0; i < 100; i++)
                try {
                    ResourceLocation location = new ResourceLocation((base.trim().toLowerCase().endsWith("/") ? base.trim().toLowerCase() : base.trim().toLowerCase() + "/") + prefix + "_" + i + ".png");
                    locations.add(location);
                } catch (Exception ignored) {
                }
            for (ResourceLocation location : locations)
                try {
                    tiles.add(new AnimationTile<>(touch.touch(clazz), location));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            ready = true;
        }

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

    public static class AnimationTile<K extends AbstractAnimatedTexture> {

        private final K data;

        public AnimationTile(K k, ResourceLocation location) {
            if (k != null) {
                data = k;
                data.update(location);
                return;
            }
            data = null;
        }

        public K getData() {
            return data;
        }

    }

}
