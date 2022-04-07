package de.craftsarmy.client.cosmetics.capes.normal;

import de.craftsarmy.client.cosmetics.capes.AbstractCape;
import net.minecraft.resources.ResourceLocation;

public class DiamondCape extends AbstractCape {

    public DiamondCape(Class<?> from) {
        super(from);
    }

    @Override
    public ResourceLocation getCape() {
        return new ResourceLocation("ytc/cosmetics/capes/diamond.png");
    }

}
