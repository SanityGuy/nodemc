
package me.serenity.nodeMC.commands;

import me.serenity.nodeMC.NodeMC;
import me.serenity.nodeMC.database.DatabaseManager;
import me.serenity.nodeMC.managers.PermissionManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.model.group.Group;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public final class TestingCommands implements CommandExecutor {

    private final PermissionManager pm;
    private final DatabaseManager dm;
    private final MiniMessage mm = MiniMessage.miniMessage();

    public TestingCommands() {
        this.pm = NodeMC.get().getPermissionManager();
        this.dm = NodeMC.get().getDatabaseManager();
    }

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command cmd,
            @NotNull String label,
            @NotNull String[] args
    ) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        switch (cmd.getName().toLowerCase()) {
            case "heywhatsmyrank" -> {
                Group group = pm.getGroup(p);
                String rank = group != null ? group.getDisplayName() : "None";
                String groupName = group != null ? group.getName() : "None";
                String prefix = pm.getPrefix(p);

                p.sendMessage(mm.deserialize(
                        "\n<gray>Hello <white>" + p.getName() + "</white>!</gray>" +
                                "\n<gray>Your current rank right now is: <white>" + rank + "</white></gray>" +
                                "\n<gray>And your group is: <white>" + groupName + "</white></gray>" +
                                "\n<gray>And your prefix is:</gray> <reset>" + prefix + "</reset>\n"
                ));
            }

            case "stats" -> {
                p.sendMessage(mm.deserialize("<gray>Loading your statistics...</gray>"));

                Bukkit.getScheduler().runTaskAsynchronously(NodeMC.get(), () -> {
                    try {
                        DatabaseManager.PlayerStats stats = dm.getPlayerStats(p.getUniqueId());

                        Bukkit.getScheduler().runTask(NodeMC.get(), () -> {
                            if (!p.isOnline()) return;

                            if (stats == null) {
                                p.sendMessage(mm.deserialize("<red>No statistics found. Try rejoining later.</red>"));
                                return;
                            }

                            p.sendMessage(mm.deserialize(
                                    "\n<aqua><bold>NodeMC Player Statistics</bold></aqua>" +
                                            "\n<gray>Username: <white>" + stats.username() + "</white></gray>" +
                                            "\n<gray>UUID: <white>" + stats.uuid() + "</white></gray>" +
                                            "\n<gray>Join Count: <white>" + stats.joinCount() + "</white></gray>" +
                                            "\n<gray>First Join: <white>" + stats.firstJoin() + "</white></gray>" +
                                            "\n<gray>Last Join: <white>" + stats.lastJoin() + "</white></gray>\n"
                            ));
                        });

                    } catch (SQLException ex) {
                        NodeMC.get().getLogger().severe(
                                "Failed to retrieve player statistics: " + ex.getMessage()
                        );

                        Bukkit.getScheduler().runTask(NodeMC.get(), () -> {
                            if (p.isOnline()) {
                                p.sendMessage(mm.deserialize("<red>Failed to retrieve your statistics.</red>"));
                            }
                        });
                    }
                });
            }
        }

        return true;
    }
}
