package ro.marius.koth.handlers;

import org.bukkit.entity.Player;
import ro.marius.koth.arena.Arena;
import ro.marius.koth.arena.ArenaSetup;

import java.util.*;

public class ArenaHandler {

    private final Set<Arena> arenas = new HashSet<>();

    private final Map<Player, ArenaSetup> playerArenaSetup = new HashMap<>();

    public Optional<Arena> findArenaByName(String name){
        return arenas.stream().filter(a -> a.getName().equals(name)).findFirst();
    }

    public Set<Arena> getArenas() {
        return arenas;
    }

    public Map<Player, ArenaSetup> getPlayerArenaSetup() {
        return playerArenaSetup;
    }
}
