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
