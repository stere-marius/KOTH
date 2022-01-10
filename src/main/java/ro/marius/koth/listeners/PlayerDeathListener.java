package ro.marius.koth.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import ro.marius.koth.handlers.KothMatchHandler;
import ro.marius.koth.match.KothMatch;

public class PlayerDeathListener implements Listener {

    private final KothMatchHandler kothMatchHandler;

    public PlayerDeathListener(KothMatchHandler kothMatchHandler) {
        this.kothMatchHandler = kothMatchHandler;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        KothMatch kothMatch = kothMatchHandler.getPlayerMatch().get(player.getUniqueId());

        if (kothMatch == null) return;

        kothMatch.addToRespawnTask(player);
        e.setDeathMessage(null);
        e.getDrops().clear();
    }

}
