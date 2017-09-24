package me.ardimaster.pepak.helper.proxy;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import me.ardimaster.pepak.helper.item.PEHItems;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        PEHItems.init();
    }
}
