package ro.marius.koth.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import ro.marius.koth.handlers.ArenaHandler;
import ro.marius.koth.arena.ArenaSetup;
import ro.marius.koth.utils.Items;
import ro.marius.koth.utils.StringUtils;

public class KothAreaSelectorListener implements Listener {

    private final ArenaHandler arenaHandler;

    public KothAreaSelectorListener(ArenaHandler arenaHandler) {
        this.arenaHandler = arenaHandler;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {

        Player p = e.getPlayer();
        ArenaSetup arenaSetup = arenaHandler.getPlayerArenaSetup().get(p);

        if (arenaSetup == null) return;

        ItemStack handItem = e.getItem();

        if(handItem == null) return;

        if (e.getClickedBlock() == null) return;

        if (!handItem.isSimilar(Items.KOTH_AREA_SELECTOR)) {
            return;
        }

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Location loc = e.getClickedBlock().getLocation();
            int x = loc.getBlockX();
            int y = loc.getBlockY();
            int z = loc.getBlockZ();
            arenaSetup.setKothAreaSecondPoint(loc);
            p.sendMessage(StringUtils.translate("&eYou have set the &d#2 &ecorner at &a" + x + " , " + y + " , " + z));
            e.setCancelled(true);
        }

        if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
            Location loc = e.getClickedBlock().getLocation();
            int x = loc.getBlockX();
            int y = loc.getBlockY();
            int z = loc.getBlockZ();
            arenaSetup.setKothAreaFirstPoint(loc);
            p.sendMessage(StringUtils.translate("&eYou have set the &d#1 &ecorner at &a" + x + " , " + y + " , " + z));
            e.setCancelled(true);
        }

    }

}
