package ro.marius.koth.utils;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ItemBuilder {

    protected ItemStack itemStack;

    public ItemBuilder(Material m, int amount, int data) {

        if (amount <= 0) {
            amount = 1;
        }

        this.itemStack = new ItemStack(m, amount);
    }


    public ItemBuilder(Material material) {

        this.itemStack = new ItemStack(material, 1);

    }

    public ItemBuilder(ItemStack m, int amount) {

        this.itemStack = m;
        this.itemStack.setAmount(amount);

    }

    public ItemBuilder(ItemBuilder builder) {

        this.itemStack = builder.getItemStack().clone();

    }

    public ItemBuilder(ItemStack parseItem) {

        this.itemStack = parseItem;

    }

    public ItemBuilder addEnchant(Enchantment enchant, int value) {
        ItemMeta meta = this.itemStack.getItemMeta();
        meta.addEnchant(enchant, value, true);
        this.itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addEnchants(Map<Enchantment, Integer> map) {

        ItemMeta meta = this.itemStack.getItemMeta();

        for (Map.Entry<Enchantment, Integer> entry : map.entrySet()) {
            meta.addEnchant(entry.getKey(), entry.getValue(), true);
        }

        this.itemStack.setItemMeta(meta);

        return this;
    }


    public ItemBuilder setColorLeather(Color color) {
        LeatherArmorMeta lh = (LeatherArmorMeta) this.itemStack.getItemMeta();
        lh.setColor(color);
        this.itemStack.setItemMeta(lh);
        return this;
    }

    public ItemBuilder addUnsafeEnchant(Enchantment enchant, int value) {
        this.itemStack.addUnsafeEnchantment(enchant, value);
        return this;
    }


    public ItemBuilder setLore(String... strings) {
        if (strings.length == 0) {
            return this;
        }

        List<String> lore = Arrays.asList(strings);

        ItemMeta meta = this.itemStack.getItemMeta();
        for (int i = 0; i < lore.size(); i++) {
            String s = lore.get(i).replace("&", "§");
            lore.set(i, s);
        }

        meta.setLore(lore);
        this.itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder glowingItem() {

        ItemMeta meta = itemStack.getItemMeta();
        meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
        itemStack.setItemMeta(meta);
        itemStack.addUnsafeEnchantment(Enchantment.LUCK, 1);

        return this;
    }


    @Override
    public ItemBuilder clone() {
        return new ItemBuilder(this);
    }

    public ItemBuilder setDisplayName(String name) {
        ItemMeta meta = this.itemStack.getItemMeta();
        meta.setDisplayName(StringUtils.translate(name));
        this.itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setUnbreakable() {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setUnbreakable(true);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder withAmount(int amount){
        this.itemStack.setAmount(amount);
        return this;
    }

    public ItemStack build() {
        return this.itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

}
