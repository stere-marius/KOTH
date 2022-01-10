package ro.marius.koth.utils;

import org.bukkit.ChatColor;

public class StringUtils {

    public static String translate(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

}
