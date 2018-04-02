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

package me.ardimaster.pepak.helper.item;

import cpw.mods.fml.common.registry.GameRegistry;
import me.ardimaster.pepak.helper.PEpakHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSword;
import net.minecraftforge.common.util.EnumHelper;

public final class PEHItems {
    public static Item.ToolMaterial materialA = EnumHelper.addToolMaterial("materialA", 4, 1, 25.0F, 340282347.0F, 0);

    public static ItemSword swordA;

    public static final void init() {
        swordA = new ItemSword(materialA);
        swordA.setUnlocalizedName("swordA");
        swordA.setTextureName(PEpakHelper.MODID + ":swordA");
        GameRegistry.registerItem(swordA, "swordA");
    }
}
