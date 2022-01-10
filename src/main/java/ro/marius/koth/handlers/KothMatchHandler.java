package ro.marius.koth.handlers;

import org.bukkit.entity.Player;
import ro.marius.koth.match.KothMatch;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class KothMatchHandler {

    private final Map<UUID, KothMatch> playerMatch = new HashMap<>();

    public Map<UUID, KothMatch> getPlayerMatch() {
        return playerMatch;
    }
}
