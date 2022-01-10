package ro.marius.koth.match;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import ro.marius.koth.scoreboard.ScoreboardAPI;
import ro.marius.koth.utils.StringUtils;

import java.util.*;

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
        kothMatch.getArena().getTeams().forEach(t -> scoreboardAPI.addLine("&e" + t.getName() + " : " + t.getPlayers().size()));
        scoreboardAPI.addLine(" ");
        scoreboardAPI.updateScoreboard(player);
        playerScoreboard.putIfAbsent(player, scoreboardAPI);
    }

    private String getDisplayCapturedKoth() {
        StringBuilder stringBuilder = new StringBuilder();

        int totalBlocksToBeCaptured = kothMatch.getArena().getBlocksToCapture();

        int totalCubes = 10;

        List<KothTeam> teams = new ArrayList<>(kothMatch.getPlayerTeam().values());
        KothTeam redTeam = teams.get(0);
        KothTeam blueTeam = teams.get(1);

        double redTeamCubesPercentage = (double) redTeam.getBlocksCaptured() / totalBlocksToBeCaptured * 100;
        int redTeamCubesNumber = (int) (redTeamCubesPercentage  / 10);

        double blueTeamCubesPercentage = (double) blueTeam.getBlocksCaptured() / totalBlocksToBeCaptured * 100 ;
        int blueTeamCubesNumber = (int) (blueTeamCubesPercentage / 10);

        for (int i = 0; i < redTeamCubesNumber; i++) {
            stringBuilder.append(redTeam.getChatColor()).append("⬛");
        }

        for (int i = redTeamCubesNumber; i < totalCubes - blueTeamCubesNumber; i++) {
            stringBuilder.append("&7⬛");
        }

        for (int i = totalCubes - blueTeamCubesNumber; i < totalCubes ; i++) {
            stringBuilder.append(blueTeam.getChatColor()).append("⬛");
        }


        return stringBuilder.toString();
    }

    private void setScoreboardMatchRunning(Player player) {

        UUID uuid = player.getUniqueId();
        ScoreboardAPI scoreboardAPI = playerScoreboard.getOrDefault(player, new ScoreboardAPI(uuid, "KOTH", "Koth"));

        scoreboardAPI.clear();
        scoreboardAPI.addLine(" ");
        scoreboardAPI.addLine("&eTime Left: &a" + StringUtils.formatIntoHHMMSS(kothMatch.getSecondsLeft()));
        scoreboardAPI.addLine(" ");
        kothMatch.getArena().getTeams().forEach((team) -> scoreboardAPI.addLine("&e" + team.getName() + "'s score : " + team.getScore()));
        scoreboardAPI.addLine(" ");
        scoreboardAPI.addLine("&aKoth Info");
        scoreboardAPI.addLine(" ");
        scoreboardAPI.addLine(getDisplayCapturedKoth());
        scoreboardAPI.addLine(" ");
        scoreboardAPI.updateScoreboard(player);

        playerScoreboard.putIfAbsent(player, scoreboardAPI);
    }

}
