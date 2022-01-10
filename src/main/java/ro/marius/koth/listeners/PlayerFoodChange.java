package ro.marius.koth.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class PlayerFoodChange implements Listener {

    @EventHandler
    public void onHunger(FoodLevelChangeEvent e) {

//        if(!(e.getEntity() instanceof Player))
//            return;
//
//        Player p = (Player) e.getEntity();
//        AMatch match = ManagerHandler.getGameManager().getPlayerMatch().get(p);
//
//        if (match == null)
//            return;

        e.setFoodLevel(20);
        e.setCancelled(true);
    }
}
