package ro.marius.koth.listeners;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import ro.marius.koth.handlers.KothMatchHandler;
import ro.marius.koth.match.KothMatch;

public class SpectatorDamage implements Listener {

    private final KothMatchHandler kothMatchHandler;

    public SpectatorDamage(KothMatchHandler kothMatchHandler) {
        this.kothMatchHandler = kothMatchHandler;
    }


    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player)) {
            return;
        }
        Player p = (Player) e.getDamager();
        KothMatch kothMatch = kothMatchHandler.getPlayerMatch().get(p.getUniqueId());

        if (kothMatch == null) return;

        if (!kothMatch.getSpectators().contains(p)) {
            return;
        }

        e.setCancelled(true);
    }


    @EventHandler
    public void onEntityDamageArrow(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        if (!(e.getDamager() instanceof Arrow)) {
            return;
        }
        if (!(((Arrow) e.getDamager()).getShooter() instanceof Player)) {
            return;
        }

        Player p = (Player) ((Arrow) e.getDamager()).getShooter();
        KothMatch kothMatch = kothMatchHandler.getPlayerMatch().get(p.getUniqueId());

        if (kothMatch == null) return;

        if (!kothMatch.getSpectators().contains(p)) {
            return;
        }

        e.setCancelled(true);
    }

    @EventHandler
    public void onSpectatorDamageByOther(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        Player p = (Player) e.getEntity();
        KothMatch kothMatch = kothMatchHandler.getPlayerMatch().get(p.getUniqueId());

        if (kothMatch == null) return;

        if (!kothMatch.getSpectators().contains(p)) {
            return;
        }

        e.setCancelled(true);
    }

}
