package ro.marius.koth.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import ro.marius.koth.handlers.KothMatchHandler;
import ro.marius.koth.match.KothMatch;

public class PlayerClickArmor implements Listener {

    private final KothMatchHandler kothMatchHandler;

    public PlayerClickArmor(KothMatchHandler kothMatchHandler) {
        this.kothMatchHandler = kothMatchHandler;
    }


    @EventHandler
    public void onArmorSlot(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        KothMatch playerMatch = kothMatchHandler.getPlayerMatch().get(p.getUniqueId());

        if (playerMatch == null) {
            return;
        }
        if (e.getSlotType() != InventoryType.SlotType.ARMOR) {
            return;
        }

        e.setCancelled(true);
    }

}

