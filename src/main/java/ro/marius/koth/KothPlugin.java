package ro.marius.koth;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import ro.marius.koth.arena.ArenaConfigurationFile;
import ro.marius.koth.handlers.ArenaHandler;
import ro.marius.koth.handlers.CommandHandler;
import ro.marius.koth.handlers.KothMatchHandler;
import ro.marius.koth.listeners.*;
import ro.marius.koth.match.KothMatch;
import ro.marius.koth.utils.StringUtils;

public class KothPlugin extends JavaPlugin {

    private KothMatchHandler kothMatchHandler;
    private ArenaHandler arenaHandler;
    private ArenaConfigurationFile arenaConfigurationFile;

    @Override
    public void onEnable() {
        this.kothMatchHandler = new KothMatchHandler();
        this.arenaHandler = new ArenaHandler();
        this.arenaConfigurationFile = new ArenaConfigurationFile(this);
        CommandHandler.registerCommands(this);
        this.registerListeners();
        this.loadArenas();
    }

    @Override
    public void onDisable() {
        kothMatchHandler.handlePluginDisable();
        arenaHandler.handlePluginDisable();
    }

    private void loadArenas(){
        arenaHandler.getArenas().addAll(arenaConfigurationFile.getArenasFromConfiguration());
        Bukkit.getConsoleSender().sendMessage(StringUtils.translate("&e&lI've loaded " + arenaHandler.getArenas().size() + " arenas"));
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
        pluginManager.registerEvents(new PlayerClickInventory(kothMatchHandler), this);
        pluginManager.registerEvents(new PlayerInteractItems(kothMatchHandler), this);
        pluginManager.registerEvents(new ArrowHitListener(kothMatchHandler), this);
        pluginManager.registerEvents(new SpectatorDamage(kothMatchHandler), this);
        pluginManager.registerEvents(new PlayerFoodChange(), this);
    }

    public ArenaConfigurationFile getArenaConfiguration() {
        return arenaConfigurationFile;
    }

    public KothMatchHandler getKothMatchHandler() {
        return kothMatchHandler;
    }

    public ArenaHandler getArenaHandler() {
        return arenaHandler;
    }
}
