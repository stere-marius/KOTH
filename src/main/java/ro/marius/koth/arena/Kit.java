package ro.marius.koth.arena;

import net.minecraft.server.v1_16_R3.PlayerInventory;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import ro.marius.koth.utils.ItemBuilder;

import java.util.HashSet;
import java.util.Set;

public class Kit {

    private final String name;
    private final ItemStack[] items;
    private final ItemStack[] armor;

    public Kit(String name, ItemStack[] items, ItemStack[] armor) {
        this.name = name;
        this.items = items;
        this.armor = armor;
    }

    public String getName() {
        return name;
    }

    public ItemStack[] getItems() {
        return items;
    }

    public ItemStack[] getArmor() {
        return armor;
    }

    public static Set<Kit> getDefaultKits() {

        Set<Kit> kits = new HashSet<>();

        Kit warriorKit = new Kit("Warrior", new ItemStack[]{
                new ItemBuilder(Material.STONE_SWORD).setUnbreakable().build()
        },
                new ItemStack[]{
                        new ItemBuilder(Material.DIAMOND_BOOTS).setUnbreakable().build(),
                        new ItemBuilder(Material.DIAMOND_LEGGINGS).setUnbreakable().build(),
                        new ItemBuilder(Material.DIAMOND_CHESTPLATE).setUnbreakable().build(),
                        new ItemBuilder(Material.DIAMOND_HELMET).setUnbreakable().build(),
                });

        Kit ninjaKit = new Kit("Ninja", new ItemStack[]{
                new ItemBuilder(Material.IRON_SWORD).setUnbreakable().build(),
                new ItemBuilder(Material.BOW).addEnchant(Enchantment.ARROW_DAMAGE, 5).setUnbreakable().build(),
                new ItemBuilder(Material.ARROW, 64).setUnbreakable().build()
        },
                new ItemStack[]{
                        new ItemBuilder(Material.IRON_BOOTS).setUnbreakable().build(),
                        new ItemBuilder(Material.IRON_LEGGINGS).setUnbreakable().build(),
                        new ItemBuilder(Material.IRON_CHESTPLATE).setUnbreakable().build(),
                        new ItemBuilder(Material.IRON_HELMET).setUnbreakable().build(),
                });


        kits.add(warriorKit);
        kits.add(ninjaKit);

        return kits;
    }

}
