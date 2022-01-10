package ro.marius.koth;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import ro.marius.koth.arena.ArenaConfigurationFile;
import ro.marius.koth.handlers.ArenaHandler;
import ro.marius.koth.handlers.CommandHandler;
import ro.marius.koth.handlers.KothMatchHandler;
import ro.marius.koth.listeners.*;

public class KothPlugin extends JavaPlugin {

    private KothMatchHandler kothMatchHandler;
    private ArenaHandler arenaHandler;
    private ArenaConfigurationFile arenaConfigurationFile;

    @Override
    public void onEnable() {
        this.kothMatchHandler = new KothMatchHandler();
        this.arenaHandler = new ArenaHandler();
        this.arenaConfigurationFile = new ArenaConfigurationFile();
        CommandHandler.registerCommands(arenaHandler, arenaConfigurationFile);
        this.registerListeners();
        this.loadArenas();
    }

    private void loadArenas(){
        arenaHandler.getArenas().addAll(arenaConfigurationFile.getArenasFromConfiguration());
    }

    private void registerListeners(){
        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new KothAreaSelectorListener(arenaHandler), this);
        pluginManager.registerEvents(new PlayerClickArmor(kothMatchHandler), this);
        pluginManager.registerEvents(new PlayerDamageListener(kothMatchHandler), this);
        pluginManager.registerEvents(new PlayerDeathListener(kothMatchHandler), this);
        pluginManager.registerEvents(new PlayerDropItems(kothMatchHandler), this);
        pluginManager.registerEvents(new PlayerQuitListener(kothMatchHandler), this);
        pluginManager.registerEvents(new PlayerBlockBreak(kothMatchHandler), this);
        pluginManager.registerEvents(new PlayerBlockPlace(kothMatchHandler), this);
    }

    public KothMatchHandler getKothMatchHandler() {
        return kothMatchHandler;
    }

    public ArenaHandler getArenaHandler() {
        return arenaHandler;
    }
}
