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

package me.ardimaster.pepak.helper.proxy;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import me.ardimaster.pepak.helper.PEpakHelper;
import org.lwjgl.opengl.Display;

public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        PEpakHelper.logger.info("Setting window title...");

        // Even though the license doesn't require it (I think), please do me a favor
        // and change this to something else if you're reusing this mod/file for your own project.
        Display.setTitle("PEpak2");
    }
}
