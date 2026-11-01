package me.serenity.nodeMC.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.serenity.nodeMC.NodeMC;
import me.serenity.nodeMC.managers.PermissionManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatFormat implements Listener {

    private final PermissionManager pm;
    private static final MiniMessage mm = MiniMessage.miniMessage();

    public ChatFormat() {
        this.pm = NodeMC.get().getPermissionManager();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncChatEvent e) {
        String prefix = pm.getPrefix(e.getPlayer());
        String safePrefix = prefix != null ? prefix : "";

        Component format = mm.deserialize(
                safePrefix + " <white>" + e.getPlayer().getName() + "</white><gray> » </gray>"
        );

        e.renderer((source, sourceDisplayName, message, viewer) ->
                format.append(message)
        );
    }
}
