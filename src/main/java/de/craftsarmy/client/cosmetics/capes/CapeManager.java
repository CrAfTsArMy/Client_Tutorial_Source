package de.craftsarmy.client.cosmetics.capes;

import de.craftsarmy.client.Client;
import de.craftsarmy.client.cosmetics.capes.animated.ColorChangingCape;
import de.craftsarmy.craftscore.utils.Touch;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.resources.ResourceLocation;
import net.optifine.Config;

public class CapeManager {

    private AbstractCape cape;
    private Touch<AbstractCape> touch;

    public CapeManager init() {
        touch = new Touch<>(this.getClass());
        setCape(ColorChangingCape.class);
        //setCape(DiamondCape.class);
        return this;
    }

    public void setCape(Class<? extends AbstractCape> clazz) {
        try {
            cape = touch.touch(clazz);
            /*if (Client.networkManager.isLoggedin())
                Client.networkManager.send(Client.networkManager.put("/data", "{\"data\":[\"cape:" + cape.getCape().getPath() + "\"]}"));*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reset() {
        setCape(ResetCape.class);
    }

    private int i = 0;

    public void tick() {
        if (i < 4) {
            i++;
            return;
        }
        i = 0;
        assert Client.minecraft.player != null;
        assert cape != null;
        AbstractClientPlayer player = Client.minecraft.player;
        SimpleTexture texture = new SimpleTexture(cape.getCape());
        ResourceLocation resourceLocation = new ResourceLocation("cosmetics/cape");
        Config.getTextureManager().register(resourceLocation, texture);
        player.setLocationOfCape(resourceLocation);
    }

}
