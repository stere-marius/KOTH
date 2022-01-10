package ro.marius.koth.match;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import ro.marius.koth.arena.Kit;
import ro.marius.koth.KothPlugin;
import ro.marius.koth.arena.Arena;
import ro.marius.koth.utils.PlayerUtils;
import ro.marius.koth.utils.StringUtils;

import java.util.*;

public class KothMatch {

    private KothMatchState state = KothMatchState.WAITING;
    private final Arena arena;
    private final Set<Player> spectators = new HashSet<>();
    private final Map<Player, KothTeam> playerTeam = new HashMap<>();
    private final Map<Player, Kit> playerKit = new HashMap<>();
    private final MatchStartTask matchStartTask;
    private final KothMatchScoreboardTask kothMatchScoreboardTask;
    private final KothMatchRunningTask kothMatchRunningTask;

    private final KothPlugin plugin;

    public KothMatch(Arena arena, KothPlugin plugin) {
        this.arena = arena;
        this.matchStartTask = new MatchStartTask(this);
        this.kothMatchScoreboardTask = new KothMatchScoreboardTask(this);
        this.kothMatchRunningTask = new KothMatchRunningTask(this);
        this.plugin = plugin;
        this.startTasks();
    }

    public void startTasks() {
        this.kothMatchScoreboardTask.runTaskTimer(plugin, 20L, 20L);
    }

    public void addPlayer(Player player) {
        this.playerTeam.put(player, getAvailableTeam());
        player.sendMessage(StringUtils.translate("&eYou have been added to the arena. Waiting for players..."));
        checkStartOfMatch();
    }

    private void checkStartOfMatch() {
        boolean isEmptyTeam = arena.getTeams().stream().anyMatch(t -> t.getPlayers().isEmpty());

        if (!isEmptyTeam) return;

        startMatch();
    }

    public void startMatch() {
        matchStartTask.runTaskTimer(plugin, 20L, 20L);
    }


    // TODO: Refactor this method
    public void endMatch() {

        this.resetSpectators();


        getPlayers().forEach( player -> {
            PlayerUtils.resetPlayer(player, true, true);
            player.teleport(Bukkit.getWorld("world").getSpawnLocation());
        });

        this.handleWinningTeam();
        this.resetMatchToDefaultState();
    }

    private void handleWinningTeam(){

        boolean everyTeamHasTheSameScore = playerTeam
                .values()
                .stream()
                .map(KothTeam::getScore)
                .distinct()
                .count() <= 1;

        if (everyTeamHasTheSameScore) {
            getPlayers().forEach(p -> PlayerUtils.sendTitle(p, 20, 20, 20, "&e&lIT'S A TIE!", "", true));
        } else {
            KothTeam winningTeam = playerTeam
                    .values()
                    .stream()
                    .max(Comparator.comparingInt(KothTeam::getScore)).get();

            sendMessage("&a&l" + winningTeam.getName() + " won the match");
            winningTeam.getPlayers().forEach(p -> {
                PlayerUtils.sendTitle(p, 20, 20, 20, "&a&lYOU WIN", "", true);
                p.getInventory().addItem(new ItemStack(Material.DIAMOND, 5));
            });
        }
    }

    private void resetSpectators(){
        spectators.forEach(spectator -> {
            getPlayers().forEach(p -> p.showPlayer(JavaPlugin.getPlugin(KothPlugin.class), spectator));
        });
    }

    private void resetMatchToDefaultState(){
        getPlayerTeam().clear();
        getArena().getTeams().forEach(t -> {
            t.resetKothSecondsCaptured();
            t.resetScore();
        });

        kothMatchRunningTask.setSecondsLeft(60 * 15);
        kothMatchScoreboardTask.cancel();
        state = KothMatchState.WAITING;
    }

    public void addToRespawnTask(Player player) {
        spectators.add(player);
        getPlayers().forEach(p -> p.hidePlayer(JavaPlugin.getPlugin(KothPlugin.class), player));
        PlayerUtils.resetPlayer(player, true, true);
        new PlayerRespawnTask(player, this).runTaskTimer(plugin, 20L, 20L);
    }

    public void sendMessage(String message) {
        playerTeam.keySet().forEach(p -> p.sendMessage(StringUtils.translate(message)));
    }

    public Map<Player, Kit> getPlayerKit() {
        return playerKit;
    }

    public Map<Player, KothTeam> getPlayerTeam() {
        return playerTeam;
    }

    public int getStartingSeconds() {
        return matchStartTask.getStartingSeconds();
    }

    public int getSecondsLeft() {
        return kothMatchRunningTask.getSecondsLeft();
    }

    public boolean isStarting() {
        return state == KothMatchState.STARTING;
    }

    public KothMatchState getState() {
        return state;
    }

    public Set<Player> getPlayers() {
        return playerTeam.keySet();
    }

    public void givePlayersKit() {
        getPlayerKit().forEach((p, k) -> {
            p.getInventory().setContents(k.getItems());
            p.getInventory().setArmorContents(k.getArmor());
        });
    }

    public void teleportTeamPlayersToSpawn() {
        getPlayerTeam().forEach((p, t) -> p.teleport(t.getSpawn()));
    }

    public Arena getArena() {
        return arena;
    }

    public boolean isSpectator(Player player) {
        return spectators.contains(player);
    }

    public void removeSpectator(Player player) {
        spectators.remove(player);
    }

    public void checkMatchEnd() {
        boolean isOnlyOneTeamLeft = getPlayerTeam()
                .values()
                .stream()
                .filter(t -> t.getPlayers().isEmpty())
                .count() == 1;

        if (isOnlyOneTeamLeft) {
            this.endMatch();
        }

    }

    public void setState(KothMatchState state) {
        this.state = state;
    }

    private KothTeam getAvailableTeam() {

        return arena
                .getTeams()
                .stream()
                .min(Comparator.comparing(t -> t.getPlayers().size()))
                .get();
    }


}
