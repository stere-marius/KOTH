package ro.marius.koth.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.projectiles.ProjectileSource;
import ro.marius.koth.match.KothTeam;
import ro.marius.koth.handlers.KothMatchHandler;
import ro.marius.koth.match.KothMatch;
import ro.marius.koth.match.KothMatchState;

public class PlayerDamageListener implements Listener {

    private final KothMatchHandler kothMatchHandler;

    public PlayerDamageListener(KothMatchHandler kothMatchHandler) {
        this.kothMatchHandler = kothMatchHandler;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {

        Entity entity = e.getEntity();
        Entity dmg = e.getDamager();

        if (!(dmg instanceof Player)) {
            return;
        }
        if (!(entity instanceof Player)) {
            return;
        }

        KothMatch playerMatch = kothMatchHandler.getPlayerMatch().get(entity.getUniqueId());
        KothMatch damagerMatch =  kothMatchHandler.getPlayerMatch().get(dmg.getUniqueId());

        if ((playerMatch == null) || (damagerMatch == null)) {
            return;
        }

        if (!playerMatch.getArena().getName().equals(damagerMatch.getArena().getName())) {
            return;
        }

        if (damagerMatch.getState() == KothMatchState.WAITING) {
            return;
        }

        Player damager = (Player) dmg;
        Player p = (Player) entity;

        KothTeam damagerTeam = playerMatch.getPlayerTeam().get(Bukkit.getPlayer(damager.getUniqueId()));

        if (!damagerTeam.getPlayers().contains(p)) {
            return;
        }

        e.setCancelled(true);

    }

    @EventHandler
    public void onEntityDamageArrow(EntityDamageByEntityEvent e) {

        Entity entity = e.getEntity();
        Entity damager = e.getDamager();

        if (!(damager instanceof Arrow)) {
            return;
        }
        if (!(entity instanceof Player)) {
            return;
        }

        KothMatch playerMatch = kothMatchHandler.getPlayerMatch().get(entity.getUniqueId());

        if (playerMatch == null) {
            return;
        }

        Arrow arrow = (Arrow) damager;
        ProjectileSource shooter = arrow.getShooter();

        if (!(shooter instanceof Player)) {
            return;
        }

        Player p = (Player) entity;

        KothTeam pTeam = playerMatch.getPlayerTeam().get(p);

        if (!pTeam.getPlayers().contains(shooter)) {
            return;
        }

        e.setCancelled(true);

    }
}
