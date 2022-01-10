package ro.marius.koth.utils;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_16_R3.IChatBaseComponent;
import net.minecraft.server.v1_16_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_16_R3.PlayerConnection;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PlayerUtils {

    public static void resetPlayer(Player p, boolean clearPotions, boolean clearInventory) {
        p.setHealth(20.0);
        p.setFoodLevel(20);
        p.setFireTicks(0);
        p.setWalkSpeed(0.2F);
        p.setFlySpeed(0.1F);
        p.closeInventory();
        p.updateInventory();
        p.setGameMode(GameMode.SURVIVAL);

        if (clearInventory) {
            p.getInventory().setArmorContents(null);
            p.getInventory().clear();
        }

        if (clearPotions) {
            p.getActivePotionEffects().forEach(effects -> p.removePotionEffect(effects.getType()));
        }

    }

    public static void sendTitle(Player p, Integer fadeIn, Integer stay, Integer fadeOut, String title, String subtitle,
                                 boolean sendAgainTitle) {

        PlayerConnection connection = ((CraftPlayer) p).getHandle().playerConnection;

        if (sendAgainTitle) {
            PacketPlayOutTitle packetPlayOutTimes = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES,
                    null, fadeIn, stay, fadeOut);

            connection.sendPacket(packetPlayOutTimes);
        }

        if (subtitle != null) {

            IChatBaseComponent titleSub = IChatBaseComponent.ChatSerializer
                    .a("{\"text\": \"" + StringUtils.translate(subtitle) + "\"}");
            PacketPlayOutTitle packetPlayOutSubTitle = new PacketPlayOutTitle(
                    PacketPlayOutTitle.EnumTitleAction.SUBTITLE, titleSub);
            connection.sendPacket(packetPlayOutSubTitle);
        }

        if (title != null) {
            IChatBaseComponent titleMain = IChatBaseComponent.ChatSerializer
                    .a("{\"text\": \"" + StringUtils.translate(title) + "\"}");
            PacketPlayOutTitle packetPlayOutTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE,
                    titleMain);
            connection.sendPacket(packetPlayOutTitle);
        }

    }

    public static void sendSuggestCommandMessage(Player p, String command, String text, String displayHoverMessage) {
        TextComponent message = new TextComponent(StringUtils.translate(text));
        message.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command));
        message.setHoverEvent(
                new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(StringUtils.translate(displayHoverMessage)).create()));
        p.spigot().sendMessage(message);

    }

}
