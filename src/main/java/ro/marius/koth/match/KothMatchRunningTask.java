package ro.marius.koth.match;

import org.bukkit.scheduler.BukkitRunnable;
import ro.marius.koth.utils.PlayerUtils;

public class KothMatchRunningTask extends BukkitRunnable {

    private final KothMatch kothMatch;

    private int secondsLeft = 60 * 15;

    public KothMatchRunningTask(KothMatch kothMatch) {
        this.kothMatch = kothMatch;
    }

    @Override
    public void run() {
        this.secondsLeft--;

        if (secondsLeft == 0) {
            kothMatch.endMatch();
            this.cancel();
            return;
        }

        kothMatch.getPlayerTeam().values().forEach(this::handleTeamKothZone);
    }

    private void handleTeamKothZone(KothTeam team) {
        boolean hasPlayerInsideKothZone = team
                .getPlayers()
                .stream()
                .anyMatch(p -> kothMatch.getArena().getKothArea().isInsideCuboidSelection(p.getLocation()));

        if (hasPlayerInsideKothZone) {
            team.incrementSecondsKothCaptured();
            return;
        }

        if (team.getKothSecondsCaptured() == 3) {
            kothMatch.sendMessage("&e" + team.getName() + " has captured the KOTH zone");
            return;
        }

        if (team.getKothSecondsCaptured() == 20) {
            team.incrementScore();
            kothMatch.teleportTeamPlayersToSpawn();
            kothMatch.getPlayers().forEach(p -> PlayerUtils.resetPlayer(p, true, true));
            kothMatch.getPlayerTeam().values().forEach(KothTeam::resetKothSecondsCaptured);
            kothMatch.givePlayersKit();
            kothMatch.sendMessage("&e" + team.getName() + " got a point for capturing the KOTH zone for more than 20 seconds");
            return;
        }

        team.resetKothSecondsCaptured();
    }

    public void setSecondsLeft(int secondsLeft) {
        this.secondsLeft = secondsLeft;
    }

    public int getSecondsLeft() {
        return secondsLeft;
    }
}
