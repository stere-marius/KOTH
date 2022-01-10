package ro.marius.koth.match;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import ro.marius.koth.scoreboard.ScoreboardAPI;

import java.util.UUID;

public class KothMatchScoreboardTask extends BukkitRunnable {

    private final KothMatch kothMatch;

    public KothMatchScoreboardTask(KothMatch kothMatch) {
        this.kothMatch = kothMatch;
    }

    @Override
    public void run() {
        kothMatch.getPlayers().forEach(this::setScoreboardPlayerByMatchState);
    }

    private void setScoreboardPlayerByMatchState(Player player) {
        if (kothMatch.isStarting()) {
            setScoreboardWaiting(player);
            return;
        }

        setScoreboardMatchRunning(player);
    }

    private void setScoreboardWaiting(Player player) {
        UUID uuid = player.getUniqueId();
        ScoreboardAPI scoreboardAPI = new ScoreboardAPI(uuid, "KOTH", "Koth");

        scoreboardAPI.clear();
        String startingDisplay = kothMatch.isStarting() ? "&a&lStarting KOTH in &e&l" + kothMatch.getStartingSeconds() : "&e&lSearching for players...";
        scoreboardAPI.addLine(startingDisplay);
        scoreboardAPI.addLine(" ");
        kothMatch.getPlayerTeam().values().forEach(t -> scoreboardAPI.addLine("&e" + t.getName() + " : " + t.getPlayers().size()));
        scoreboardAPI.addLine(" ");
        scoreboardAPI.updateScoreboard(player);
    }

    private void setScoreboardMatchRunning(Player player) {
        UUID uuid = player.getUniqueId();
        ScoreboardAPI scoreboardAPI = new ScoreboardAPI(uuid, "KOTH", "Koth");

        scoreboardAPI.clear();
        scoreboardAPI.addLine(" ");
        kothMatch.getPlayerTeam().values().forEach((team) -> scoreboardAPI.addLine("&e" + team.getName() + "'s score : " + team.getScore()));
        scoreboardAPI.addLine(" ");
        scoreboardAPI.updateScoreboard(player);
    }

}
