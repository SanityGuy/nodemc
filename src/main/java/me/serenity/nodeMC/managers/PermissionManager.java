package me.serenity.nodeMC.managers;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class PermissionManager {

    private final LuckPerms luckPerms;

    public PermissionManager(LuckPerms luckPerms) {
        this.luckPerms = luckPerms;
    }

    public boolean isInGroup(Player player, String group) {
        return isInGroup(player.getUniqueId(), group);
    }

    public boolean isInGroup(UUID player, String group) {
        User user = luckPerms.getUserManager().getUser(player);

        if (user == null) return false;

        Collection<Group> inheritedGroups = user.getInheritedGroups(user.getQueryOptions());
        return inheritedGroups.stream().anyMatch(g -> g.getName().equalsIgnoreCase(group));
    }

    public String getRank(Player player) {
        User user = luckPerms.getUserManager().getUser(player.getUniqueId());
        return user != null ? user.getPrimaryGroup() : "none";
    }

    public Group getGroup(Player player) {
        return luckPerms.getGroupManager().getGroup(getRank(player));
    }

    public CompletableFuture<Void> setRank(UUID player, String group) {
        return luckPerms.getUserManager().modifyUser(player, user -> {
            user.setPrimaryGroup(group);
        });
    }

    public boolean isAdmin(UUID player) {
        return isInGroup(player, "admin");
    }
    public void addPermission(User user, String permission) {
        user.data().add(Node.builder(permission).build());
        luckPerms.getUserManager().saveUser(user);
    }

    public CompletableFuture<Void> addPermission(UUID player, String permission) {
        return luckPerms.getUserManager().modifyUser(player, user ->
                user.data().add(Node.builder(permission).build())
        );
    }

    public void removePermission(User user, String permission) {
        user.data().remove(Node.builder(permission).build());
        luckPerms.getUserManager().saveUser(user);
    }

    public CompletableFuture<Void> removePermission(UUID player, String permission) {
        return luckPerms.getUserManager().modifyUser(player, user ->
                user.data().remove(Node.builder(permission).build())
        );
    }

    public boolean hasPermission(User user, String permission) {
        return user.getCachedData().getPermissionData().checkPermission(permission).asBoolean();
    }

    public boolean hasPermission(Player player, String permission) {
        return player.hasPermission(permission);
    }

    public boolean isHigherThanOrEqual(Player player, String group) {
        Group rank = luckPerms.getGroupManager().getGroup(group);
        if (rank == null) return false;
        int targetWeight = rank.getWeight().orElse(0);

        User user = luckPerms.getPlayerAdapter(Player.class).getUser(player);
        int playerWeight = user.getNodes(NodeType.INHERITANCE).stream()
                .map(InheritanceNode::getGroupName)
                .map(g -> luckPerms.getGroupManager().getGroup(g))
                .filter(Objects::nonNull)
                .mapToInt(g -> g.getWeight().orElse(0))
                .max()
                .orElse(0);

        return playerWeight >= targetWeight;
    }

    public CompletableFuture<User> loadUser(UUID player) {
        return luckPerms.getUserManager().loadUser(player);
    }

    public String getPrefix(Player player) {
        User user = luckPerms.getUserManager().getUser(player.getUniqueId());
        if (user == null) return "";
        String prefix = user.getCachedData().getMetaData().getPrefix();
        return prefix != null? prefix : "";
    }
}