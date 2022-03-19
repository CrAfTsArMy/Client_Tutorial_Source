package de.craftsarmy.client.cosmetics.capes;

import de.craftsarmy.client.Client;
import de.craftsarmy.client.utils.Touch;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.resources.ResourceLocation;
import net.optifine.Config;

public class CapeManager {

    private ICape cape;
    private Touch<ICape> touch;

    public CapeManager init() {
        touch = new Touch<>();
        setCape(DiamondCape.class);
        return this;
    }

    public void setCape(Class<?> clazz) {
        try {
            cape = touch.touch(clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reset() {
        setCape(ResetCape.class);
    }

    public void tick() {
        assert Client.minecraft.player != null;
        assert cape != null;
        AbstractClientPlayer player = Client.minecraft.player;
        SimpleTexture texture = new SimpleTexture(cape.getCape());
        ResourceLocation resourceLocation = new ResourceLocation("cosmetics/cape");
        Config.getTextureManager().register(resourceLocation, texture);
        player.setLocationOfCape(resourceLocation);
    }

}
