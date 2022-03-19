package de.craftsarmy.client.cosmetics;

import de.craftsarmy.client.cosmetics.capes.CapeManager;

public class CosmeticsManager {

    private CapeManager capeManager;

    public CosmeticsManager init() {
        this.capeManager = new CapeManager().init();
        return this;
    }

    public CapeManager getCapeManager() {
        return capeManager;
    }

}
