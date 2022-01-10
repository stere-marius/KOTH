package ro.marius.koth.listeners;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import ro.marius.koth.handlers.KothMatchHandler;
import ro.marius.koth.match.KothMatch;
import ro.marius.koth.match.KothMatchState;

public class ArrowHitListener implements Listener {

    private final KothMatchHandler kothMatchHandler;

    public ArrowHitListener(KothMatchHandler kothMatchHandler) {
        this.kothMatchHandler = kothMatchHandler;
    }


    @EventHandler
    public void onArrowHit(ProjectileHitEvent e) {

        if (!(e.getEntity() instanceof Arrow))
            return;

        Arrow arrow = (Arrow) e.getEntity();

        if (!(arrow.getShooter() instanceof Player))
            return;

        Player player = (Player) arrow.getShooter();
        KothMatch playerMatch = kothMatchHandler.getPlayerMatch().get(player.getUniqueId());

        if (playerMatch == null) return;

        if (playerMatch.getState() != KothMatchState.RUNNING) return;

        arrow.remove();


    }
}
