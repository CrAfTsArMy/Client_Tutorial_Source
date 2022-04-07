package de.craftsarmy.client.utils;

import de.craftsarmy.craftscore.utils.Touch;
import net.minecraft.resources.ResourceLocation;

public abstract class AbstractAnimatedTexture extends Touch.TouchAble {

    public AbstractAnimatedTexture(Class<?> from) {
        super(from);
    }

    public abstract void update(ResourceLocation location);
    public abstract ResourceLocation get();

}
