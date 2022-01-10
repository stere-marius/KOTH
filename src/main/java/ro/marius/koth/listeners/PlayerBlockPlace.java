package ro.marius.koth.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import ro.marius.koth.handlers.KothMatchHandler;
import ro.marius.koth.match.KothMatch;

public class PlayerBlockPlace implements Listener {

    private final KothMatchHandler kothMatchHandler;

    public PlayerBlockPlace(KothMatchHandler kothMatchHandler) {
        this.kothMatchHandler = kothMatchHandler;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){
        Player p = e.getPlayer();
        KothMatch playerMatch = kothMatchHandler.getPlayerMatch().get(p.getUniqueId());

        if (playerMatch == null) {
            return;
        }

        e.setCancelled(true);
    }

}
