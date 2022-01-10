package ro.marius.koth.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import ro.marius.koth.gui.GUI;
import ro.marius.koth.handlers.KothMatchHandler;
import ro.marius.koth.match.KothMatch;

public class PlayerClickInventory implements Listener {

    private final KothMatchHandler kothMatchHandler;

    public PlayerClickInventory(KothMatchHandler kothMatchHandler) {
        this.kothMatchHandler = kothMatchHandler;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        KothMatch playerMatch = kothMatchHandler.getPlayerMatch().get(p.getUniqueId());

        if (playerMatch == null) return;

        if (e.getCurrentItem() == null) return;

        if (e.getInventory().getHolder() == null) return;


        if (!(e.getView().getTopInventory().getHolder() instanceof GUI)) return;

        GUI extraInventory = (GUI) e.getView().getTopInventory().getHolder();
        extraInventory.onClick(e);
    }

}
