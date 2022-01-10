package ro.marius.koth.handlers;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.defaults.BukkitCommand;
import ro.marius.koth.KothPlugin;
import ro.marius.koth.arena.ArenaConfigurationFile;
import ro.marius.koth.commands.AbstractCommand;
import ro.marius.koth.commands.KothCommand;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class CommandHandler {

    public static void registerCommands(KothPlugin kothPlugin) {

        Set<AbstractCommand> abstractCommand = new HashSet<>();
        abstractCommand.add(new KothCommand(kothPlugin));

        try {
            Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");

            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());

            for (BukkitCommand command : abstractCommand) {
                commandMap.register(command.getName(), command);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
