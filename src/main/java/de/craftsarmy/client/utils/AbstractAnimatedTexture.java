package de.craftsarmy.client.utils;

import net.minecraft.resources.ResourceLocation;

public abstract class AbstractAnimatedTexture {

    public abstract void update(ResourceLocation location);
    public abstract ResourceLocation get();

}
