
package me.serenity.nodeMC.commands;

import me.serenity.nodeMC.NodeMC;
import me.serenity.nodeMC.managers.PermissionManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.model.group.Group;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class TestingCommands implements CommandExecutor {

    private final PermissionManager pm;
    private final MiniMessage mm = MiniMessage.miniMessage();
    public TestingCommands() { this.pm = NodeMC.get().getPermissionManager(); }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        switch (cmd.getName().toLowerCase()) {
            case "heywhatsmyrank" -> {
                Group group = pm.getGroup(p);
                String rank = group != null? group.getDisplayName() : "None";
                String groupName = group != null ? group.getName() : "None";
                String prefix = pm.getPrefix(p);

                p.sendMessage(mm.deserialize(
                        "\n<gray>Hello <white>" + p.getName() + "</white>!</gray>" +
                                "\n<gray>Your current rank right now is: <white>" + rank + "</white></gray>" +
                                "\n<gray>And your group is: <white>" + groupName + "</white></gray>" +
                                "\n<gray>And your prefix is:</gray> <reset>" + prefix + "</reset>\n"
                ));
            }
        }

        return true;
    }
}
