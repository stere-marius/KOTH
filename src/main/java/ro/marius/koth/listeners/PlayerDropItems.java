package ro.marius.koth.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import ro.marius.koth.handlers.KothMatchHandler;
import ro.marius.koth.match.KothMatch;

public class PlayerDropItems implements Listener {

    private final KothMatchHandler kothMatchHandler;

    public PlayerDropItems(KothMatchHandler kothMatchHandler) {
        this.kothMatchHandler = kothMatchHandler;
    }


    @EventHandler
    public void onDropItem(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        KothMatch kothMatch = kothMatchHandler.getPlayerMatch().get(player.getUniqueId());

        if (kothMatch == null) return;

        e.setCancelled(true);
    }

}
