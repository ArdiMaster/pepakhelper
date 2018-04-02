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

package me.ardimaster.pepak.helper.hooks;

import cpw.mods.fml.common.Loader;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MFRHooks {
    private static Logger logger = LogManager.getLogger("PEpakHelper:MFRHooks");

    // Armor combinations that prevent potion effects when worn
    private static final String[][] protectiveCombos = {
            {"ic2.itemArmorRubBoots", "ic2.itemArmorHazmatLeggings", "ic2.itemArmorHazmatChestplate", "ic2.itemArmorHazmatHelmet"},
            {"item.npcX407Boots", "item.npcX407Legs", "item.npcX407Chest", "item.npcX407Head"}
    };

    public static boolean shouldFluidEffectsApply(String effectSource, EntityLivingBase entity) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            ItemStack[] armor = player.inventory.armorInventory;

            for (String[] aCombo : protectiveCombos) {
                if (itemStackArrayCompare(armor, aCombo)) {
                    logger.log(Level.TRACE, "Decided that " + player.getDisplayName() + " should not be affected by " + effectSource);
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean shouldSludgeBoilerEffectsApply(EntityLivingBase entity) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            ItemStack[] armor = player.inventory.armorInventory;

            for (String[] aCombo : protectiveCombos) {
                if (itemStackArrayCompare(armor, aCombo)) {
                    logger.log(Level.TRACE, "Decided that " + player.getDisplayName() + " should not be affected by the sludge boiler.");
                    return false;
                }
            }

            // Since the Sludge Boiler emits gasses, also check for a Galacticraft oxygen setup
            if (Loader.isModLoaded("GalacticraftCore")) {
                try {
                    Class<?> oxygenUtilClass = Class.forName("micdoodle8.mods.galacticraft.core.util.OxygenUtil");
                    Boolean result = (Boolean) oxygenUtilClass.getMethod("hasValidOxygenSetup", EntityPlayerMP.class).invoke(null, (EntityPlayerMP) player);
                    if (result) logger.log(Level.TRACE, "Decided that " + player.getDisplayName() + " should not be affected by the sludge boiler.");
                    return !result;  // Valid oxygen setup (true returned) means we shouldn't apply effects (returning false)
                } catch (Exception e) {
                    // Do nothing -- proceed to return true so potion effects will be applied
                    logger.log(Level.WARN, "Error checking Galacticraft oxygen setup.", e);
                }
            }
        }
        return true;
    }

    /**
     * Checks whether the names of the types in a given ItemStack array match the strings of the given string array.
     *
     * Name/String-based approach allows for comparing these without actually importing (= depending on) the mods
     * in question.
     * @param itemStack The ItemStack[] to check.
     * @param compareTo The String[] to check against
     * @return true if matching, false otherwise.
     */
    private static boolean itemStackArrayCompare(ItemStack itemStack[], String compareTo[]) {
        if (itemStack.length != compareTo.length) return false;
        try {
            for (int i = 0; i < itemStack.length; i++) {
                if (!itemStack[i].getUnlocalizedName().equals(compareTo[i])) {
                    return false;
                }
            }
            return true;
        } catch (NullPointerException e) {
            return false;
        }
    }
}
