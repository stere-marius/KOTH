package ro.marius.koth.match;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class KothTeam {

    private final String name;
    private final ChatColor chatColor;
    private final Set<Player> players = new HashSet<>();
    private final Location spawn;
    private final Material kothAreaMaterial;

    private int score;
    private int blocksCaptured;

    public KothTeam(String name, Location spawn, Material kothAreaMaterial, ChatColor chatColor) {
        this.name = name;
        this.spawn = spawn;
        this.kothAreaMaterial = kothAreaMaterial;
        this.chatColor = chatColor;
    }

    public ChatColor getChatColor() {
        return chatColor;
    }

    public int getScore() {
        return score;
    }

    public Material getKothAreaMaterial() {
        return kothAreaMaterial;
    }

    public void resetScore() {
        this.score = 0;
    }

    public void incrementScore() {
        this.score++;
    }

    public String getName() {
        return name;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public int getBlocksCaptured() {
        return blocksCaptured;
    }

    public void setBlocksCaptured(int blocksCaptured) {
        this.blocksCaptured = blocksCaptured;
    }

    public Location getSpawn() {
        return spawn;
    }

}
