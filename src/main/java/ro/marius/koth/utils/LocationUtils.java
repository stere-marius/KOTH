package ro.marius.koth.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Objects;

public class LocationUtils {

    public static Location getConvertedStringToLocation(String string) {
        String[] sp = string.split(",");

        if (sp.length < 5) {
            return null;
        }

        World world = Bukkit.getWorld(sp[0]);
        double x = Double.parseDouble(sp[1]);
        double y = Double.parseDouble(sp[2]);
        double z = Double.parseDouble(sp[3]);
        float yaw = Float.parseFloat(sp[4]);
        float pitch = Float.parseFloat(sp[5]);
        return new Location(world, x, y, z, yaw, pitch);
    }

    public static String getConvertedStringToLocation(Location location) {
        return Objects.requireNonNull(location.getWorld()).getName() + "," + location.getX() + "," + location.getY() + "," + location.getZ()
                + "," + location.getYaw() + "," + location.getPitch();
    }


}
