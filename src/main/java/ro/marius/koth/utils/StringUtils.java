package ro.marius.koth.utils;

import org.bukkit.ChatColor;

public class StringUtils {

    public static String translate(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static String formatIntoHHMMSS(int secs) {
        int remainder = secs % 3600;
        int minutes = remainder / 60;
        int seconds = remainder % 60;

        return (minutes < 10 ? "0" : "") + minutes + ":" + (seconds < 10 ? "0" : "") + seconds;
    }

}
