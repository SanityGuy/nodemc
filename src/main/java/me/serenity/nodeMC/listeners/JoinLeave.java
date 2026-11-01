package me.serenity.nodeMC.listeners;

import me.serenity.nodeMC.NodeMC;
import me.serenity.nodeMC.managers.PermissionManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinLeave implements Listener {

    private static final MiniMessage mm = MiniMessage.miniMessage();
    private final PermissionManager pm;
    public JoinLeave() { this.pm = NodeMC.get().getPermissionManager(); }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        e.joinMessage(null);

        Component msg;
        if (pm.isHigherThanOrEqual(p, "staff")) {
            msg = mm.deserialize("<aqua>⚡ <bold>" + p.getName() + "</bold> <gray>(Staff) has arrived!</gray>");
        } else if (pm.isHigherThanOrEqual(p, "vip")) {
            msg = mm.deserialize("<dark_aqua>⭐ <bold>" + p.getName() + "</bold> <gray>(VIP) has entered the server!</gray>");
        } else {
            msg = mm.deserialize("<green>" + p.getName() + "</green> <gray>has joined the server.</gray>");
        }

        Bukkit.broadcast(msg);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        e.quitMessage(null);

        Component msg;
        if (pm.isHigherThanOrEqual(p, "staff")) {
            msg = mm.deserialize("<aqua>⚡ <bold>" + p.getName() + "</bold> <gray>(Staff) has departed.</gray>");
        } else if (pm.isHigherThanOrEqual(p, "vip")) {
            msg = mm.deserialize("<dark_aqua>⭐ <bold>" + p.getName() + "</bold> <gray>(VIP) has left.</gray>");
        } else {
            msg = mm.deserialize("<red>" + p.getName() + "</red> <gray>has left the server.</gray>");
        }

        Bukkit.broadcast(msg);
    }
}
