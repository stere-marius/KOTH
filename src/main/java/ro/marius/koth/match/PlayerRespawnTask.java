package ro.marius.koth.match;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import ro.marius.koth.KothPlugin;
import ro.marius.koth.arena.Kit;
import ro.marius.koth.utils.PlayerUtils;

public class PlayerRespawnTask extends BukkitRunnable {

    private int secondsLeft = 5;
    private final Player player;
    private final KothMatch kothMatch;

    public PlayerRespawnTask(Player player, KothMatch kothMatch) {
        this.player = player;
        this.kothMatch = kothMatch;
    }

    @Override
    public void run() {

        if (!kothMatch.getPlayers().contains(player)) {
            this.cancel();
            return;
        }

        if (this.secondsLeft == 0) {
            respawnPlayer();
            cancel();
            return;
        }


        if (secondsLeft == 5) {
            PlayerUtils.sendTitle(player, 20, 20 * 5, 20,
                    "&cYOU DIED!", "&eYou will be respawned in " + secondsLeft + " seconds!",
                    true);
        }

        PlayerUtils.sendTitle(player, 20, 20, 20, null,
                "&eYou will be respawned in " + secondsLeft + " seconds!", false);


        this.secondsLeft--;
    }

    private void respawnPlayer() {
        KothTeam team = kothMatch.getPlayerTeam().get(player);
        kothMatch.getPlayers().forEach(p -> p.showPlayer(JavaPlugin.getPlugin(KothPlugin.class), player));
        player.teleport(team.getSpawn());
        kothMatch.removeSpectator(player);
        PlayerUtils.resetPlayer(player, true, true);
        Kit playerKit = kothMatch.getPlayerKit().get(player);
        player.getInventory().setContents(playerKit.getItems());
        player.getInventory().setArmorContents(playerKit.getArmor());
    }
}
