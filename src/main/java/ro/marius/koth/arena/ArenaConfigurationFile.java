package ro.marius.koth.arena;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import ro.marius.koth.KothPlugin;
import ro.marius.koth.match.KothTeam;
import ro.marius.koth.utils.CuboidSelection;
import ro.marius.koth.utils.LocationUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ArenaConfigurationFile {

    private final File arenaFile;
    private final FileConfiguration arenaConfigurationFile;

    public ArenaConfigurationFile() {
        this.arenaFile = new File(JavaPlugin.getPlugin(KothPlugin.class).getDataFolder(), "arenas.yml");
        this.arenaConfigurationFile = YamlConfiguration.loadConfiguration(arenaFile);
    }

    public Set<Arena> getArenasFromConfiguration() {

        Set<Arena> arenas = new HashSet<>();

        ConfigurationSection configurationSection = arenaConfigurationFile.getConfigurationSection("Arenas");

        if (configurationSection == null) return Collections.emptySet();

        if (configurationSection.getKeys(false).isEmpty()) return Collections.emptySet();


        for (String arenaKey : configurationSection.getKeys(false)) {
            String configKey = "Arenas." + arenaKey;
            CuboidSelection kothArea = getKothAreaFromConfiguration(configKey);
            Set<KothTeam> kothTeams = getTeamsFromConfiguration(configKey + ".Teams");
            Arena arenaObject = new Arena(arenaKey, kothArea);
            arenaObject.getKits().addAll(Kit.getDefaultKits());
            arenaObject.getTeams().addAll(kothTeams);
        }

        return arenas;
    }

    public void saveArena(Arena arena) {
        String arenaKey = "Arenas." + arena.getName();

        Location firstPointKothArea = arena.getKothArea().getPositionOne();
        Location secondPointKothArea = arena.getKothArea().getPositionTwo();

        arenaConfigurationFile.set(arenaKey + ".FirstPointKothArea", LocationUtils.getConvertedStringToLocation(firstPointKothArea));
        arenaConfigurationFile.set(arenaKey + ".SecondPointKothArea", LocationUtils.getConvertedStringToLocation(secondPointKothArea));

        for (KothTeam team : arena.getTeams()) {
            arenaConfigurationFile.set(arenaKey + ".Teams." + team.getName() + ".Spawn", LocationUtils.getConvertedStringToLocation(team.getSpawn()));
        }

        saveFile();
    }


    private CuboidSelection getKothAreaFromConfiguration(String path) {
        String firstPointKothArea = arenaConfigurationFile.getString(path + ".FirstPointKothArea");
        String secondPointKothArea = arenaConfigurationFile.getString(path + ".SecondPointKothArea");
        Location firstPointKothAreaLocation = LocationUtils.getConvertedStringToLocation(firstPointKothArea);
        Location secondPointKothAreaLocation = LocationUtils.getConvertedStringToLocation(secondPointKothArea);

        CuboidSelection cuboidSelection = new CuboidSelection(firstPointKothAreaLocation, secondPointKothAreaLocation);
        cuboidSelection.assignValues();
        cuboidSelection.select();
        return cuboidSelection;
    }

    private Set<KothTeam> getTeamsFromConfiguration(String path) {
        Set<KothTeam> kothTeams = new HashSet<>();

        for (String teamName : arenaConfigurationFile.getConfigurationSection(path).getKeys(false)) {
            kothTeams.add(getTeamFromConfiguration(path + "." + teamName, teamName));
        }

        return kothTeams;
    }

    private KothTeam getTeamFromConfiguration(String path, String teamName) {
        Location teamSpawn = LocationUtils.getConvertedStringToLocation(arenaConfigurationFile.getString(path + ".Spawn"));
        return new KothTeam(teamName, teamSpawn);
    }


    public void saveFile() {
        try {
            this.arenaConfigurationFile.save(this.arenaFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
