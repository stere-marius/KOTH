package ro.marius.koth.handlers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import ro.marius.koth.KothPlugin;
import ro.marius.koth.arena.Arena;
import ro.marius.koth.arena.ArenaSetup;
import ro.marius.koth.match.KothMatch;
import ro.marius.koth.match.KothMatchState;
import ro.marius.koth.utils.PlayerUtils;

import java.util.*;
import java.util.stream.Collectors;

public class ArenaHandler {

    private final Set<Arena> arenas = new HashSet<>();

    private final Map<Player, ArenaSetup> playerArenaSetup = new HashMap<>();

    public Optional<Arena> findArenaByName(String name){
        return arenas.stream().filter(a -> a.getName().equals(name)).findFirst();
    }

    public void handlePluginDisable(){
        Set<KothMatch> waitingKothMatches = arenas
                .stream()
                .filter(a -> a.getKothMatch().getState() == KothMatchState.WAITING)
                .map(Arena::getKothMatch)
                .collect(Collectors.toSet());

        waitingKothMatches.forEach(match -> {
            match.getPlayers().forEach(player -> {
                match.getSpectators().forEach(s -> player.showPlayer(JavaPlugin.getPlugin(KothPlugin.class), s));
                PlayerUtils.resetPlayer(player, true, true);
                player.teleport(Bukkit.getWorld("world").getSpawnLocation());
                player.closeInventory();
                player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            });
        });
    }

    public Set<Arena> getArenas() {
        return arenas;
    }

    public Map<Player, ArenaSetup> getPlayerArenaSetup() {
        return playerArenaSetup;
    }
}
