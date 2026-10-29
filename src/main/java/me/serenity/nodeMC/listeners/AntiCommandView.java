package me.serenity.nodeMC.listeners;


import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Set;

public class AntiCommandView implements Listener {

    private static final MiniMessage MM = MiniMessage.miniMessage();
    private static final Set<String> BLOCKED = Set.of("op", "pl", "plugin", "ver", "version", "kick", "ban", "mute", "spigot", "bukkit", "bungeecord");

    @EventHandler
    public void onPlayerCommandProcess(PlayerCommandPreprocessEvent e){
        Player p = e.getPlayer();
        if (p.hasPermission("nodemc.operator")) return;

        String message = e.getMessage();
        String[] parts = message.split(" ");
        String command = parts[0].substring(1);

        if (BLOCKED.contains(command.toLowerCase())) {
            e.setCancelled(true);

            for (Player staff : Bukkit.getOnlinePlayers()){
                if (!staff.hasPermission("nodemc.staff")) return;

                staff.sendMessage(MM.deserialize(
                        "<cyan><bold>LOG</bold></cyan> <reset>»</reset> " +
                                "<cyan>User <reset>"+p.displayName()+"</reset> has executed the command<reset>: "+message));
            }

            p.sendMessage(MM.deserialize("<red>Command not found. View <bold>/help</bold> for help!</red>"));
        }
    }

}
