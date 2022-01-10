package ro.marius.koth.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import ro.marius.koth.gui.KitGUI;
import ro.marius.koth.handlers.KothMatchHandler;
import ro.marius.koth.match.KothMatch;
import ro.marius.koth.utils.Items;
import ro.marius.koth.utils.StringUtils;

public class PlayerInteractItems implements Listener {

    private final KothMatchHandler kothMatchHandler;

    public PlayerInteractItems(KothMatchHandler kothMatchHandler) {
        this.kothMatchHandler = kothMatchHandler;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        KothMatch kothMatch = kothMatchHandler.getPlayerMatch().get(player.getUniqueId());

        if (kothMatch == null) return;

        ItemStack handItem = e.getItem();

        if (handItem == null) return;

        if (handItem.isSimilar(Items.KIT_SELECTOR)) {
            KitGUI kitGUI = new KitGUI(kothMatch);
            player.openInventory(kitGUI.getInventory());
            e.setCancelled(true);
            return;
        }

        if (handItem.isSimilar(Items.ARENA_LEAVE)) {
            kothMatch.removePlayer(player);
            e.setCancelled(true);
            return;
        }

    }
}
