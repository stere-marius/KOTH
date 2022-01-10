package ro.marius.koth.handlers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import ro.marius.koth.KothPlugin;
import ro.marius.koth.match.KothMatch;
import ro.marius.koth.match.KothMatchState;
import ro.marius.koth.utils.PlayerUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class KothMatchHandler {

    private final Map<UUID, KothMatch> playerMatch = new HashMap<>();

    public Map<UUID, KothMatch> getPlayerMatch() {
        return playerMatch;
    }


    public void handlePluginDisable(){

        Set<KothMatch> startedKothMatches = playerMatch
                .values()
                .stream()
                .filter(k -> k.getState() == KothMatchState.RUNNING || k.getState() == KothMatchState.STARTING)
                .collect(Collectors.toSet());

        startedKothMatches.forEach(match -> {
            match.getPlayers().forEach(player -> {
                match.getSpectators().forEach(s -> player.showPlayer(JavaPlugin.getPlugin(KothPlugin.class), s));
                PlayerUtils.resetPlayer(player, true, true);
                player.teleport(Bukkit.getWorld("world").getSpawnLocation());
                player.closeInventory();
                player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            });


            match.resetKothAreaWoolBlocks();
        });


    }
}
