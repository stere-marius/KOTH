package ro.marius.koth.match;

import org.bukkit.scheduler.BukkitRunnable;

public class MatchStartTask extends BukkitRunnable {

    private final KothMatch kothMatch;
    private int startingSeconds;
    private KothMatchState matchState =  KothMatchState.WAITING;

    public MatchStartTask(KothMatch kothMatch) {
        this.kothMatch = kothMatch;
    }

    @Override
    public void run() {
        startingSeconds--;

        if(startingSeconds == 0){
            kothMatch.teleportTeamPlayersToSpawn();
            kothMatch.givePlayersKit();
            kothMatch.sendMessage("&a&lKOTH started");
            return;
        }

        if (startingSeconds % 5 == 0) {
            kothMatch.sendMessage("&a&lKOTH starts in " + startingSeconds);
        }

    }

    public int getStartingSeconds() {
        return startingSeconds;
    }

    public KothMatchState getState() {
        return matchState;
    }

    public void setMatchState(KothMatchState matchState) {
        this.matchState = matchState;
    }



}
