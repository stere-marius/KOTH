package ro.marius.koth.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Items {

    public static final ItemStack KOTH_AREA_SELECTOR = new ItemBuilder(Material.WOODEN_AXE)
            .setLore("&eUsed to select the bounds of koth area", "&eRight click to set second position",
                                     "&eLeft click to set first position")
            .setDisplayName("&aKoth Area Selector")
            .setUnbreakable()
            .build();


    public static final ItemStack KIT_SELECTOR = new ItemBuilder(Material.CHEST)
            .setDisplayName("&aKit Selector")
            .setUnbreakable()
            .build();

    public static final ItemStack ARENA_LEAVE = new ItemBuilder(Material.RED_BED)
            .setDisplayName("&eLeave arena")
            .build();

}
