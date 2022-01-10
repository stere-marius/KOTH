package ro.marius.koth.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import ro.marius.koth.KothPlugin;
import ro.marius.koth.handlers.KothMatchHandler;
import ro.marius.koth.match.KothMatch;

public class PlayerDeathListener implements Listener {

    private final KothMatchHandler kothMatchHandler;

    public PlayerDeathListener(KothMatchHandler kothMatchHandler) {
        this.kothMatchHandler = kothMatchHandler;
    }

    @EventHandler
    public void onDeath(EntityDamageByEntityEvent e) {

        if (!(e.getEntity() instanceof Player)) return;

        Player player = (Player) e.getEntity();
        KothMatch kothMatch = kothMatchHandler.getPlayerMatch().get(player.getUniqueId());

        if (kothMatch == null) return;

        if (e.getFinalDamage() < player.getHealth()) return;

        e.setCancelled(true);
        Player killer = player.getKiller();

        if (killer != null) {
            kothMatch.sendMessage("&e" + player.getName() + " has been killed by " + killer.getName());
        }

        kothMatch.addToRespawnTask(player);
    }

}
