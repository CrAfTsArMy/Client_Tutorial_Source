package de.craftsarmy.client.mods;

public interface IModule {

    default void onEnable(Core core, String name) {
        System.out.print("[Mods] Enableing " + getClass());
    }

    default void onDisable() {
    }

    default void onUpdate() {
    }

    default boolean isOverlayAble() {
        return true;
    }

}
