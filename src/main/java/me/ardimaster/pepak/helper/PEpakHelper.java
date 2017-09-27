package me.ardimaster.pepak.helper;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import me.ardimaster.pepak.helper.proxy.CommonProxy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = PEpakHelper.MODID, version = PEpakHelper.VERSION, name = PEpakHelper.NAME)
public class PEpakHelper {
    public static final String MODID = "pepakhelper";
    public static final String VERSION = "0.1.0-SNAPSHOT";
    public static final String NAME = "PEpakHelper";

    @Mod.Instance
    public static PEpakHelper modInstance;
    public static final Logger logger = LogManager.getLogger("pepakhelper");

    @SidedProxy(clientSide = "me.ardimaster.pepak.helper.proxy.ClientProxy", serverSide = "me.ardimaster.pepak.helper.proxy.CommonProxy")
    public static CommonProxy proxy;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
		proxy.preInit(event);
    }
}
