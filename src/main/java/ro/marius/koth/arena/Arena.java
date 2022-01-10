package ro.marius.koth.arena;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import ro.marius.koth.KothPlugin;
import ro.marius.koth.match.KothTeam;
import ro.marius.koth.match.KothMatch;
import ro.marius.koth.utils.CuboidSelection;
import ro.marius.koth.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Arena {

    private final List<Kit> kits = new ArrayList<>();
    private final String name;
    private final CuboidSelection kothArea;
    private final int blocksToCapture;
    private final Set<KothTeam> teams = new HashSet<>();
    private final Set<Player> players = new HashSet<>();

    private KothMatch kothMatch;

    public Arena(String name, CuboidSelection kothArea, KothPlugin kothPlugin){
        this.name = name;
        this.kothArea = kothArea;
        this.kothMatch = new KothMatch(this, kothPlugin);
        this.blocksToCapture =  kothArea
                .getBlocks()
                .stream()
                .filter(b -> b.getType() == Material.WHITE_WOOL)
                .collect(Collectors.toSet()).size();
        Bukkit.getConsoleSender().sendMessage(StringUtils.translate("&a&lblocksToCapture = " + blocksToCapture));
    }

    public void setKothMatch(KothMatch kothMatch) {
        this.kothMatch = kothMatch;
    }

    public CuboidSelection getKothArea() {
        return kothArea;
    }

    public int getBlocksToCapture() {
        return blocksToCapture;
    }

    public KothMatch getKothMatch() {
        return kothMatch;
    }

    public List<Kit> getKits() {
        return kits;
    }

    public String getName() {
        return name;
    }

    public Set<KothTeam> getTeams() {
        return teams;
    }

    public Set<Player> getPlayers() {
        return players;
    }
}
