package ro.marius.koth.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import ro.marius.koth.arena.Kit;
import ro.marius.koth.match.KothMatch;
import ro.marius.koth.utils.ItemBuilder;
import ro.marius.koth.utils.StringUtils;

public class KitViewGUI extends GUI {

    private final Kit kit;
    private final KothMatch kothMatch;

    public KitViewGUI(Kit kit, KothMatch kothMatch) {
        this.kit = kit;
        this.kothMatch = kothMatch;
    }

    @Override
    public Inventory getInventory() {

        Inventory inventory = Bukkit.createInventory(this, 54, StringUtils.translate("&eKit preview"));

        for (int i = 0; i < kit.getArmor().length; i++) {
            inventory.setItem(i, kit.getArmor()[i]);
        }

        for (int i = 0; i < kit.getItems().length; i++) {
            inventory.setItem(9 + i, kit.getItems()[i]);
        }

        inventory.setItem(53, new ItemBuilder(Material.RED_WOOL)
                .setDisplayName("&eGo back to kit selector")
                .build());


        return inventory;
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);

        if (e.getSlot() == 53) {
            KitGUI kitGUI = new KitGUI(kothMatch);
            e.getWhoClicked().openInventory(kitGUI.getInventory());
        }
    }
}
