/*
 * Copyright 2017-18 Adrian "ArdiMaster" Welcker
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

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
    public static final String VERSION = "0.2.2-SNAPSHOT";
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
