package me.ardimaster.pepak.helper.proxy;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import me.ardimaster.pepak.helper.PEpakHelper;
import org.lwjgl.opengl.Display;

public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        PEpakHelper.logger.info("Setting window title...");

        Display.setTitle("PEpak2");
    }
}
