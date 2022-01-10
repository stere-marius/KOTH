package ro.marius.koth.match;

import ro.marius.koth.utils.PlayerUtils;

public class MatchStartTask implements Runnable {

    private final KothMatch kothMatch;

    public MatchStartTask(KothMatch kothMatch) {
        this.kothMatch = kothMatch;
    }

    @Override
    public void run() {
        kothMatch.setStartingSeconds(kothMatch.getStartingSeconds() - 1);

        if (kothMatch.getStartingSeconds() == 0) {
            kothMatch.teleportTeamPlayersToSpawn();
            kothMatch.givePlayersKit();
            kothMatch.sendMessage("&a&lKOTH started");
            kothMatch.setStartingSeconds(15);
            kothMatch.setState(KothMatchState.RUNNING);
            kothMatch.startRunningMatchTask();
            kothMatch.cancelStartTask();
            return;
        }


        if (kothMatch.getStartingSeconds() % 5 == 0) {
            kothMatch.sendMessage("&a&lKOTH is starting in " + kothMatch.getStartingSeconds());
            kothMatch.getPlayers().forEach(PlayerUtils::playSoundCountdown);
        }

        if (kothMatch.getStartingSeconds() < 5) {
            kothMatch.sendMessage("&a&lKOTH is starting in " + kothMatch.getStartingSeconds());
            kothMatch.getPlayers().forEach(PlayerUtils::playSoundCountdown);
        }

    }



}
