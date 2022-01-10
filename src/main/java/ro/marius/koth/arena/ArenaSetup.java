package ro.marius.koth.arena;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ro.marius.koth.KothPlugin;
import ro.marius.koth.match.KothTeam;
import ro.marius.koth.utils.CuboidSelection;
import ro.marius.koth.utils.ItemBuilder;
import ro.marius.koth.utils.PlayerUtils;
import ro.marius.koth.utils.StringUtils;

public class ArenaSetup {

    private final String arenaName;
    private Location firstTeamSpawn;
    private Location secondTeamSpawn;
    private Location kothAreaFirstPoint;
    private Location kothAreaSecondPoint;

    public ArenaSetup(String arenaName) {
        this.arenaName = arenaName;
    }

    public void sendStepsMessage(Player player) {

        boolean isCompletedSetup = true;

        player.sendMessage(StringUtils.translate("&e-----------------------------------------------"));
        player.sendMessage(" ");

        if (firstTeamSpawn == null) {
            PlayerUtils.sendSuggestCommandMessage(player, "/koth setFirstTeamSpawn", "&eClick me to set the first team spawn", "&eClick me");
            isCompletedSetup = false;
        }

        if (secondTeamSpawn == null) {
            PlayerUtils.sendSuggestCommandMessage(player, "/koth setSecondTeamSpawn", "&eClick me to set the second team spawn", "&eClick me");
            isCompletedSetup = false;
        }

        if (kothAreaFirstPoint == null) {
            PlayerUtils.sendSuggestCommandMessage(player, "/koth setKothAreaFirstPoint", "&eClick me to set the first point of the koth area", "&eClick me");
            isCompletedSetup = false;
        }

        if (kothAreaSecondPoint == null) {
            PlayerUtils.sendSuggestCommandMessage(player, "/koth setKothAreaSecondPoint", "&eClick me to set the second point of the koth area", "&eClick me");
            isCompletedSetup = false;
        }

        if (isCompletedSetup) {
            PlayerUtils.sendSuggestCommandMessage(player, "/koth finish", "&eClick me to finish the setup of the arena", "&eClick me");
        }

        player.sendMessage(" ");
        player.sendMessage(StringUtils.translate("&e-----------------------------------------------"));
    }


    public Arena createArena(KothPlugin kothPlugin) {

        CuboidSelection cuboidSelection = new CuboidSelection(kothAreaFirstPoint, kothAreaSecondPoint);
        cuboidSelection.assignValues();
        cuboidSelection.select();

        Arena arena = new Arena(arenaName, cuboidSelection, kothPlugin);

        KothTeam redTeam = new KothTeam("Red", firstTeamSpawn, Material.RED_WOOL);

        KothTeam blueTeam = new KothTeam("Blue",  secondTeamSpawn, Material.BLUE_WOOL);

        arena.getTeams().add(redTeam);
        arena.getTeams().add(blueTeam);

        arena.getKits().addAll(Kit.getDefaultKits());

        return arena;
    }

    public String getArenaName() {
        return arenaName;
    }

    public Location getFirstTeamSpawn() {
        return firstTeamSpawn;
    }

    public Location getSecondTeamSpawn() {
        return secondTeamSpawn;
    }

    public Location getKothAreaFirstPoint() {
        return kothAreaFirstPoint;
    }

    public Location getKothAreaSecondPoint() {
        return kothAreaSecondPoint;
    }

    public void setFirstTeamSpawn(Location firstTeamSpawn) {
        this.firstTeamSpawn = firstTeamSpawn;
    }

    public void setSecondTeamSpawn(Location secondTeamSpawn) {
        this.secondTeamSpawn = secondTeamSpawn;
    }

    public void setKothAreaFirstPoint(Location kothAreaFirstPoint) {
        this.kothAreaFirstPoint = kothAreaFirstPoint;
    }

    public void setKothAreaSecondPoint(Location kothAreaSecondPoint) {
        this.kothAreaSecondPoint = kothAreaSecondPoint;
    }
}
