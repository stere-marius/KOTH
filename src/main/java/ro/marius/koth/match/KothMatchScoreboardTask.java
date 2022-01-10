package ro.marius.koth.match;

import org.bukkit.entity.Player;
import ro.marius.koth.scoreboard.ScoreboardAPI;
import ro.marius.koth.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class KothMatchScoreboardTask implements Runnable {

    private final KothMatch kothMatch;
    private Map<Player, ScoreboardAPI> playerScoreboard = new HashMap<>();

    public KothMatchScoreboardTask(KothMatch kothMatch) {
        this.kothMatch = kothMatch;
    }

    @Override
    public void run() {
        kothMatch.getPlayers().forEach(this::setScoreboardPlayerByMatchState);
    }

    private void setScoreboardPlayerByMatchState(Player player) {
        if (kothMatch.getState() == KothMatchState.RUNNING) {
            setScoreboardMatchRunning(player);
            return;
        }

        setScoreboardWaiting(player);
    }

    private void setScoreboardWaiting(Player player) {
        UUID uuid = player.getUniqueId();
        ScoreboardAPI scoreboardAPI = playerScoreboard.getOrDefault(player, new ScoreboardAPI(uuid, "KOTH", "Koth"));

        scoreboardAPI.clear();
        String startingDisplay = kothMatch.isStarting() ? "&a&lStarting KOTH in &e&l" + kothMatch.getStartingSeconds() : "&e&lSearching for players...";
        scoreboardAPI.addLine("");
        scoreboardAPI.addLine(startingDisplay);
        scoreboardAPI.addLine(" ");
        kothMatch.getPlayerTeam().values().forEach(t -> scoreboardAPI.addLine("&e" + t.getName() + " : " + t.getPlayers().size()));
        scoreboardAPI.addLine(" ");
        scoreboardAPI.updateScoreboard(player);
        playerScoreboard.putIfAbsent(player, scoreboardAPI);
    }

    private void setScoreboardMatchRunning(Player player) {
        UUID uuid = player.getUniqueId();
        ScoreboardAPI scoreboardAPI = playerScoreboard.getOrDefault(player, new ScoreboardAPI(uuid, "KOTH", "Koth"));

        scoreboardAPI.clear();
        scoreboardAPI.addLine(" ");
        scoreboardAPI.addLine("&eTime Left: &a" + StringUtils.formatIntoHHMMSS(kothMatch.getSecondsLeft()));
        scoreboardAPI.addLine(" ");
        kothMatch.getPlayerTeam().values().forEach((team) -> scoreboardAPI.addLine("&e" + team.getName() + "'s score : " + team.getScore()));
        scoreboardAPI.addLine(" ");
        scoreboardAPI.updateScoreboard(player);

        playerScoreboard.putIfAbsent(player, scoreboardAPI);
    }

}
