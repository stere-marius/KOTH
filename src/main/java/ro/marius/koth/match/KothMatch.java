package ro.marius.koth.match;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import ro.marius.koth.arena.Kit;
import ro.marius.koth.KothPlugin;
import ro.marius.koth.arena.Arena;
import ro.marius.koth.handlers.KothMatchHandler;
import ro.marius.koth.utils.ItemBuilder;
import ro.marius.koth.utils.Items;
import ro.marius.koth.utils.PlayerUtils;
import ro.marius.koth.utils.StringUtils;

import java.util.*;

public class KothMatch {

    private KothMatchState state = KothMatchState.WAITING;
    private int startingSeconds = 10;
    private int secondsLeft = 10;
    private final Arena arena;
    private final Set<Player> spectators = new HashSet<>();
    private final Map<Player, KothTeam> playerTeam = new HashMap<>();
    private final Map<Player, Kit> playerKit = new HashMap<>();
    private BukkitTask matchStartTask;
    private BukkitTask scoreboardTask;
    private BukkitTask matchRunningTask;

    private final KothPlugin plugin;

    public KothMatch(Arena arena, KothPlugin plugin) {
        this.arena = arena;
        this.plugin = plugin;
        this.startTasks();
    }

    public void startTasks() {
        this.scoreboardTask = Bukkit
                .getServer()
                .getScheduler()
                .runTaskTimer(plugin, new KothMatchScoreboardTask(this), 20L, 20L);
    }

    public void addPlayer(Player player) {

        if (plugin.getKothMatchHandler().getPlayerMatch().containsKey(player.getUniqueId())) {
            player.sendMessage(StringUtils.translate("&cYou are already in arena!"));
            return;
        }

        PlayerUtils.resetPlayer(player, true, true);
        KothTeam kothTeam = getAvailableTeam();
        kothTeam.getPlayers().add(player);
        this.playerTeam.put(player, kothTeam);
        this.playerKit.put(player, arena.getKits().get(0));
        player.sendMessage(StringUtils.translate("&eYou have been added to the arena. Waiting for players..."));
        player.getInventory().setItem(0, Items.KIT_SELECTOR);
        player.getInventory().setItem(8, Items.ARENA_LEAVE);
        plugin.getKothMatchHandler().getPlayerMatch().put(player.getUniqueId(), this);
        checkStartOfMatch();
    }

    public void removePlayer(Player player) {
        PlayerUtils.resetPlayer(player, true, true);
        player.teleport(Bukkit.getWorld("world").getSpawnLocation());
        KothTeam team = playerTeam.get(player);
        team.getPlayers().remove(player);
        plugin.getKothMatchHandler().getPlayerMatch().remove(player.getUniqueId());
        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        checkCancelOfStartingTask();
    }

    private void checkStartOfMatch() {
        boolean isEmptyTeam = arena.getTeams().stream().anyMatch(t -> t.getPlayers().isEmpty());

        if (isEmptyTeam) return;

        startMatch();
    }

    private void checkCancelOfStartingTask() {
        if (this.state != KothMatchState.STARTING) return;

        boolean isEmptyTeam = arena.getTeams().stream().anyMatch(t -> t.getPlayers().isEmpty());

        if (isEmptyTeam) {
            matchStartTask.cancel();
            setState(KothMatchState.WAITING);
            setStartingSeconds(15);
        }

    }

    public void startMatch() {
        setState(KothMatchState.STARTING);
        cancelStartTask();
        matchStartTask = Bukkit
                .getServer()
                .getScheduler()
                .runTaskTimer(plugin, new MatchStartTask(this), 20L, 20L);
    }


    public void endMatch() {
        this.resetSpectators();
        this.resetPlayers();
        this.teleportPlayers(new HashSet<>(getPlayers()));
        this.handleWinningTeam();
        this.cancelTasks();
        this.resetMatchToDefaultState();
    }

    private void cancelTasks() {
        this.matchRunningTask.cancel();
        this.matchStartTask.cancel();
    }

    private void resetPlayers() {
        getPlayers().forEach(player -> {
            PlayerUtils.resetPlayer(player, true, true);
            player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        });
    }

    private void teleportPlayers(Set<Player> players) {
        Bukkit.getScheduler().runTaskLater(JavaPlugin.getPlugin(KothPlugin.class), () -> {
            players.forEach(player -> {
                player.teleport(Bukkit.getWorld("world").getSpawnLocation());
                plugin.getKothMatchHandler().getPlayerMatch().remove(player.getUniqueId());
            });
        }, 40L);
    }

    private void handleWinningTeam() {

        boolean everyTeamHasTheSameScore = playerTeam
                .values()
                .stream()
                .map(KothTeam::getScore)
                .distinct()
                .count() <= 1;

        if (everyTeamHasTheSameScore) {
            getPlayers().forEach(p -> PlayerUtils.sendTitle(p, 20, 20, 20, "&e&lIT'S A TIE!", "", true));
            return;
        }

        KothTeam winningTeam = playerTeam
                .values()
                .stream()
                .max(Comparator.comparingInt(KothTeam::getScore)).get();

        sendMessage("&a&l" + winningTeam.getName() + " won the match");
        winningTeam.getPlayers().forEach(p -> {
            PlayerUtils.sendTitle(p, 20, 20, 20, "&a&lYOU WIN", "", true);
            p.getInventory().addItem(new ItemStack(Material.DIAMOND, 5));
        });

        Set<Player> lostPlayers = new HashSet<>(getPlayers());
        lostPlayers.removeAll(winningTeam.getPlayers());
        lostPlayers.forEach(p -> PlayerUtils.sendTitle(p, 20, 20, 20, "&c&lYOU LOST", "", true));

    }

    private void resetSpectators() {
        spectators.forEach(spectator -> {
            getPlayers().forEach(p -> p.showPlayer(JavaPlugin.getPlugin(KothPlugin.class), spectator));
        });
    }

    private void resetMatchToDefaultState() {
        getPlayerTeam().clear();
        getArena().getTeams().forEach(t -> {
            t.getPlayers().clear();
            t.resetScore();
        });

        state = KothMatchState.WAITING;
        setSecondsLeft(10);
    }

    public void startRunningMatchTask() {
        cancelMatchRunningTask();
        matchRunningTask = Bukkit
                .getServer()
                .getScheduler()
                .runTaskTimer(plugin, new KothMatchRunningTask(this), 20L, 20L);
    }

    public void addToRespawnTask(Player player) {
        PlayerUtils.resetPlayer(player, true, true);
        spectators.add(player);
        getPlayers().forEach(p -> p.hidePlayer(JavaPlugin.getPlugin(KothPlugin.class), player));
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
        return startingSeconds;
    }

    public void setStartingSeconds(int startingSeconds) {
        this.startingSeconds = startingSeconds;
    }

    public int getSecondsLeft() {
        return secondsLeft;
    }

    public void setSecondsLeft(int secondsLeft) {
        this.secondsLeft = secondsLeft;
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

    public Set<Player> getSpectators() {
        return spectators;
    }

    public void cancelStartTask() {
        if (matchStartTask == null || matchStartTask.isCancelled()) return;

        matchStartTask.cancel();
    }

    public void cancelMatchRunningTask() {
        if (matchRunningTask == null || matchRunningTask.isCancelled()) return;

        matchRunningTask.cancel();
    }
}
