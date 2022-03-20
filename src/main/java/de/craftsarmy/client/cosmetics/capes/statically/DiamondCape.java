package de.craftsarmy.client.cosmetics.capes.statically;

import de.craftsarmy.client.cosmetics.capes.ICape;
import net.minecraft.resources.ResourceLocation;

public class DiamondCape implements ICape {

    @Override
    public ResourceLocation getCape() {
        return new ResourceLocation("ytc/cosmetics/capes/diamond.png");
    }

}
