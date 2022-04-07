package de.craftsarmy.client.cosmetics.capes;

import de.craftsarmy.craftscore.utils.Touch;
import net.minecraft.resources.ResourceLocation;

public abstract class AbstractCape extends Touch.TouchAble {

    public AbstractCape(Class<?> from) {
        super(from);
    }

    public abstract ResourceLocation getCape();

}
