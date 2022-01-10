package ro.marius.koth.match;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class KothTeam {

    private final String name;
    private final Set<Player> players = new HashSet<>();
    private final Location spawn;

    private int score;
    private int kothSecondsCaptured;

    public KothTeam(String name, Location spawn) {
        this.name = name;
        this.spawn = spawn;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public int getKothSecondsCaptured() {
        return kothSecondsCaptured;
    }

    public void incrementSecondsKothCaptured() {
        this.kothSecondsCaptured++;
    }

    public void resetKothSecondsCaptured() {
        this.kothSecondsCaptured = 0;
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

    public Location getSpawn() {
        return spawn;
    }

}
