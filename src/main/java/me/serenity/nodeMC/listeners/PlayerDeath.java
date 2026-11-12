package me.serenity.nodeMC.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeath implements Listener {
    private static final MiniMessage MM = MiniMessage.miniMessage();

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player p = e.getPlayer();
        Component deathMessage = MM.deserialize("<red>☠️ <bold>" + p.name() + "</bold></red> " +
                "<gray>" + e.deathMessage() + "</gray>");

        e.setShowDeathMessages(false);
        p.teleport(p.getWorld().getSpawnLocation());
        Bukkit.broadcast(deathMessage);
    }
}
