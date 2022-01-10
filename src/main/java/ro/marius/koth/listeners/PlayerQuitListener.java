package ro.marius.koth.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import ro.marius.koth.KothPlugin;
import ro.marius.koth.match.KothTeam;
import ro.marius.koth.handlers.KothMatchHandler;
import ro.marius.koth.match.KothMatch;

public class PlayerQuitListener implements Listener {

    private final KothMatchHandler kothMatchHandler;

    public PlayerQuitListener(KothMatchHandler kothMatchHandler) {
        this.kothMatchHandler = kothMatchHandler;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {

        Player player = e.getPlayer();
        KothMatch kothMatch = kothMatchHandler.getPlayerMatch().get(player.getUniqueId());

        if (kothMatch == null) return;

        if (kothMatch.isSpectator(player)) {
            kothMatch.getPlayers().forEach(p -> p.showPlayer(JavaPlugin.getPlugin(KothPlugin.class), player));
            kothMatch.removeSpectator(player);
        }

        KothTeam playerTeam = kothMatch.getPlayerTeam().get(player);
        playerTeam.getPlayers().remove(player);
        kothMatch.getPlayerTeam().remove(player);
        kothMatchHandler.getPlayerMatch().remove(player.getUniqueId());
        kothMatch.checkMatchEnd();
    }
}
