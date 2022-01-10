package ro.marius.koth.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import ro.marius.koth.arena.Kit;
import ro.marius.koth.match.KothMatch;
import ro.marius.koth.utils.ItemBuilder;
import ro.marius.koth.utils.StringUtils;

public class KitGUI extends GUI {

    private final KothMatch kothMatch;

    public KitGUI(KothMatch kothMatch) {
        this.kothMatch = kothMatch;
    }

    @Override
    public Inventory getInventory() {

        Inventory inventory = Bukkit.createInventory(this, 9, StringUtils.translate("&eSelect a kit"));

        for (int i = 0; i < kothMatch.getArena().getKits().size(); i++) {
            Kit kit = kothMatch.getArena().getKits().get(i);
            ItemStack item = new ItemBuilder(Material.IRON_SWORD)
                    .setDisplayName("&a" + kit.getName())
                    .setLore("", "&e&lRight click to select the kit", "", "&e&lMiddle click to view the items of the kit")
                    .build();
            inventory.setItem(i, item);
        }


        return inventory;
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();

        int clickedSlot = e.getSlot();

        if (clickedSlot >= kothMatch.getArena().getKits().size()) return;

        Kit clickedKit = kothMatch.getArena().getKits().get(clickedSlot);
        e.setCancelled(true);

        if (e.getClick() == ClickType.MIDDLE) {
            KitViewGUI kitViewGUI = new KitViewGUI(clickedKit, kothMatch);
            player.openInventory(kitViewGUI.getInventory());
            return;
        }

        kothMatch.getPlayerKit().put(player, clickedKit);
        player.sendMessage(StringUtils.translate("&aYou selected the kit " + clickedKit.getName() + " !"));
    }
}
