package ro.marius.koth.commands;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ro.marius.koth.KothPlugin;
import ro.marius.koth.arena.Arena;
import ro.marius.koth.arena.ArenaConfigurationFile;
import ro.marius.koth.handlers.ArenaHandler;
import ro.marius.koth.arena.ArenaSetup;
import ro.marius.koth.utils.ItemBuilder;
import ro.marius.koth.utils.Items;
import ro.marius.koth.utils.StringUtils;

import java.util.Optional;

public class KothCommand extends AbstractCommand {

    private final KothPlugin kothPlugin;

    public KothCommand(KothPlugin kothPlugin) {
        super("koth");
        this.kothPlugin = kothPlugin;
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(StringUtils.translate("&c&lInsufficient arguments!"));
            return;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(StringUtils.translate("&c&lThis command can't be performed from console!"));
            return;
        }

        Player player = (Player) sender;

        if ("join".equalsIgnoreCase(args[0])) {

            if (args.length < 2) {
                player.sendMessage(StringUtils.translate("&c&lInsufficient arguments: /koth join <arenaName>"));
                return;
            }

            Optional<Arena> arenaOptional = kothPlugin.getArenaHandler().findArenaByName(args[1]);

            if (!arenaOptional.isPresent()) {
                player.sendMessage(StringUtils.translate("&cCould not find the arena with the name " + args[1]));
                return;
            }

            Arena arena = arenaOptional.get();
            arena.getKothMatch().addPlayer(player);

            return;
        }

        if ("arenaCreate".equalsIgnoreCase(args[0])) {

            if (args.length < 2) {
                player.sendMessage(StringUtils.translate("&c&lInsufficient arguments: /koth arenaCreate <arenaName>"));
                return;
            }

            ArenaSetup arenaSetup = new ArenaSetup(args[1]);
            arenaSetup.sendStepsMessage(player);
            player.sendMessage(StringUtils.translate("&eSelect the koth area bounds using the axe."));
            player.getInventory().addItem(Items.KOTH_AREA_SELECTOR);
            kothPlugin.getArenaHandler().getPlayerArenaSetup().put(player, arenaSetup);
            return;
        }


        if ("setFirstTeamSpawn".equalsIgnoreCase(args[0])) {
            ArenaSetup arenaSetup = kothPlugin.getArenaHandler().getPlayerArenaSetup().get(player);

            if (arenaSetup == null) {
                player.sendMessage(StringUtils.translate("&c&lFirst you need to use the command /koth arenaCreate arenaName"));
                return;
            }

            arenaSetup.setFirstTeamSpawn(player.getLocation());
            arenaSetup.sendStepsMessage(player);
            return;
        }

        if ("setSecondTeamSpawn".equalsIgnoreCase(args[0])) {
            ArenaSetup arenaSetup = kothPlugin.getArenaHandler().getPlayerArenaSetup().get(player);

            if (arenaSetup == null) {
                player.sendMessage(StringUtils.translate("&c&lFirst you need to use the command /koth arenaCreate arenaName"));
                return;
            }

            arenaSetup.setSecondTeamSpawn(player.getLocation());
            arenaSetup.sendStepsMessage(player);

            return;
        }

        if ("setKothAreaFirstPoint".equalsIgnoreCase(args[0])) {

            ArenaSetup arenaSetup = kothPlugin.getArenaHandler().getPlayerArenaSetup().get(player);

            if (arenaSetup == null) {
                player.sendMessage(StringUtils.translate("&c&lFirst you need to use the command /koth arenaCreate arenaName"));
                return;
            }

            arenaSetup.setKothAreaFirstPoint(player.getLocation());
            arenaSetup.sendStepsMessage(player);

            return;
        }

        if ("setKothAreaSecondPoint".equalsIgnoreCase(args[0])) {

            ArenaSetup arenaSetup = kothPlugin.getArenaHandler().getPlayerArenaSetup().get(player);

            if (arenaSetup == null) {
                player.sendMessage(StringUtils.translate("&c&lFirst you need to use the command /koth arenaCreate arenaName"));
                return;
            }

            arenaSetup.setKothAreaSecondPoint(player.getLocation());
            arenaSetup.sendStepsMessage(player);

            return;
        }

        if ("finish".equalsIgnoreCase(args[0])) {
            ArenaSetup arenaSetup = kothPlugin.getArenaHandler().getPlayerArenaSetup().get(player);

            if (arenaSetup == null) {
                player.sendMessage(StringUtils.translate("&c&lFirst you need to use the command /koth arenaCreate arenaName"));
                return;
            }

            Arena createdArena = arenaSetup.createArena(kothPlugin);
            kothPlugin.getArenaConfiguration().saveArena(createdArena);
            kothPlugin.getArenaHandler().getArenas().add(createdArena);
            player.sendMessage(StringUtils.translate("&a&lThe arena has been successfully created!"));
        }

    }
}
