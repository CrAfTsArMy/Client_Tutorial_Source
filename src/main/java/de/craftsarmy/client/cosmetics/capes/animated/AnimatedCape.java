package de.craftsarmy.client.cosmetics.capes.animated;

import net.minecraft.resources.ResourceLocation;

public class AnimatedCape extends AbstractAnimatedCape {

    private ResourceLocation location = new ResourceLocation("ytc/cosmetics/capes/diamond.png");

    public AnimatedCape(Class<?> from) {
        super(from);
    }

    @Override
    public void update(ResourceLocation location) {
        this.location = location;
    }

    public ResourceLocation getCape() {
        return location;
    }

    @Override
    public ResourceLocation get() {
        return location;
    }

}
