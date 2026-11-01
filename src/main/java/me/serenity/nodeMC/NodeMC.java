package me.serenity.nodeMC;

import me.serenity.nodeMC.commands.TestingCommands;
import me.serenity.nodeMC.listeners.AntiCommandView;
import me.serenity.nodeMC.listeners.ChatFormat;
import me.serenity.nodeMC.listeners.EntitySpawn;
import me.serenity.nodeMC.listeners.JoinLeave;
import me.serenity.nodeMC.managers.PermissionManager;
import me.serenity.nodeMC.utils.C;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class NodeMC extends JavaPlugin {

    private LuckPerms luckPerms;
    private PermissionManager permissionManager;

    @Override
    public void onEnable() {
        RegisteredServiceProvider<LuckPerms> provider =
                Bukkit.getServicesManager().getRegistration(LuckPerms.class);

        if (provider == null) {
            getLogger().severe("LuckPerms was not found! Disabling NodeMC.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        luckPerms = provider.getProvider();
        permissionManager = new PermissionManager(luckPerms);

        registerCMD("heywhatsmyrank", new TestingCommands());

        registerEvent(new AntiCommandView());
        registerEvent(new EntitySpawn());
        registerEvent(new ChatFormat());
        registerEvent(new JoinLeave());

        C.info("Plugin has been enabled!");
    }

    @Override
    public void onDisable() {
        C.info("Plugin has been disabled!");
    }

    public static NodeMC get() {
        return getPlugin(NodeMC.class);
    }

    public LuckPerms getLuckPerms() {
        return luckPerms;
    }

    public PermissionManager getPermissionManager() {
        return permissionManager;
    }

    public void registerCMD(String command, CommandExecutor executor) {
        Objects.requireNonNull(getCommand(command), "Command not defined: " + command)
                .setExecutor(executor);
    }

    public void registerEvent(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }
}