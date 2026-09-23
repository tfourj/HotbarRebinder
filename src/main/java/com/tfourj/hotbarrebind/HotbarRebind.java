package com.tfourj.hotbarrebind;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = HotbarRebind.MODID, name = HotbarRebind.NAME, version = HotbarRebind.VERSION)
public class HotbarRebind {
    public static final String MODID = "hotbarrebind";
    public static final String NAME = "Hotbar Rebind";
    public static final String VERSION = "3.0.0";

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        System.out.println("Hotbar Rebind loaded");
    }
}
