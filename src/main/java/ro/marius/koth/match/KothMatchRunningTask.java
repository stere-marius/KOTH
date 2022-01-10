package ro.marius.koth.match;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import ro.marius.koth.utils.PlayerUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class KothMatchRunningTask implements Runnable {

    private final KothMatch kothMatch;
    private final KothAreaAnimation kothAreaAnimation = new KothAreaAnimation();

    public KothMatchRunningTask(KothMatch kothMatch) {
        this.kothMatch = kothMatch;
    }

    @Override
    public void run() {
        kothMatch.setSecondsLeft(kothMatch.getSecondsLeft() - 1);

        if (kothMatch.getSecondsLeft() == 0) {
            kothMatch.endMatch();
            kothMatch.cancelMatchRunningTask();
            return;
        }

        if (kothZoneContainsTwoDifferentTeams()) return;

        kothMatch.getPlayerTeam().values().forEach(this::handleTeamKothZone);
    }

    private boolean kothZoneContainsTwoDifferentTeams() {
        Set<Player> playersInsideKothZone =
                kothMatch
                        .getPlayers()
                        .stream()
                        .filter(p -> kothMatch.getArena().getKothArea().isInsideCuboidSelection(p.getLocation()))
                        .collect(Collectors.toSet());

        Set<KothTeam> teamsInKothZone = new HashSet<>();

        playersInsideKothZone.forEach(p -> teamsInKothZone.add(kothMatch.getPlayerTeam().get(p)));

        return teamsInKothZone.size() >= 2;
    }

    private void handleTeamKothZone(KothTeam team) {
        boolean hasPlayerInsideKothZone = team
                .getPlayers()
                .stream()
                .anyMatch(p -> kothMatch.getArena().getKothArea().isInsideCuboidSelection(p.getLocation()));

        Set<Block> blocksCaptured = kothMatch
                .getArena()
                .getKothArea()
                .getBlocks()
                .stream()
                .filter(b -> b.getType() == team.getKothAreaMaterial())
                .collect(Collectors.toSet());

        team.setBlocksCaptured(blocksCaptured.size());

        if (!hasPlayerInsideKothZone) return;

        kothAreaAnimation.update(team, kothMatch.getArena().getKothArea());

        boolean hasCapturedAllBlocks = blocksCaptured.size() == kothMatch.getArena().getBlocksToCapture();

        if (!hasCapturedAllBlocks) return;

        team.incrementScore();
        kothMatch.teleportTeamPlayersToSpawn();
        kothMatch.getPlayers().forEach(p -> PlayerUtils.resetPlayer(p, true, true));
        kothMatch.givePlayersKit();
        kothMatch.sendMessage("&e" + team.getName() + " got a point for fully capturing the KOTH zone!");
        kothMatch.resetKothAreaWoolBlocks();

    }

}
