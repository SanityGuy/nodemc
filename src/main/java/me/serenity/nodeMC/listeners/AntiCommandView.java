package me.serenity.nodeMC.listeners;


import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Locale;
import java.util.Set;

public class AntiCommandView implements Listener {

    private static final MiniMessage MM = MiniMessage.miniMessage();
    private static final Set<String> BLOCKED = Set.of(
            "op",
            "pl",
            "plugins",
            "ver",
            "version",
            "kick",
            "ban",
            "mute",
            "spigot",
            "bukkit",
            "bungeecord"
    );

    @EventHandler
    public void onPlayerCommandProcess(PlayerCommandPreprocessEvent e){
        Player p = e.getPlayer();
        if (p.hasPermission("nodemc.staff")) return;

        String message = e.getMessage().trim();
        if (message.length() < 2 || message.charAt(0) != '/') return;
        String[] parts = message.split("\\s+", 2);
        String command = parts[0].substring(1).toLowerCase(Locale.ROOT);

        int namespaceSeperator = command.lastIndexOf(':');
        if (namespaceSeperator >= 0) command = command.substring(namespaceSeperator + 1);

        if (!BLOCKED.contains(command)) return;

        e.setCancelled(true);
        p.sendMessage(MM.deserialize("<red>Command not found. View <bold>/help</bold> for help!</red>"));

        Component log = MM.deserialize(
                        "<cyan><bold>LOG</bold></cyan> <reset>» <cyan>User <reset>"
                ).append(Component.text(p.getName()))
                .append(MM.deserialize(
                        "<cyan> has tried to execute the command: <reset>"
                )).append(Component.text(message));

        for (Player staff : Bukkit.getOnlinePlayers())
            if (staff.hasPermission("nodemc.staff")) staff.sendMessage(log);

    }

}
