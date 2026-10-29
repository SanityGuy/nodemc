package me.serenity.nodeMC;

import me.serenity.nodeMC.listeners.AntiCommandView;
import me.serenity.nodeMC.utils.C;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Objects;

public final class NodeMC extends JavaPlugin {

    @Override
    public void onEnable() {
        registerEvent(new AntiCommandView());

        C.info("Plugin has been enabled!");
    }

    @Override
    public void onDisable() {
        C.info("Plugin has been disabled!");
    }

    public static NodeMC get() {
        return getPlugin(NodeMC.class);
    }

    private static void registerCMD(String cmd, CommandExecutor executor){
        Objects.requireNonNull(get().getCommand(cmd)).setExecutor(executor);
    }

    private static void registerEvent(Listener listener){
        Bukkit.getPluginManager().registerEvents(listener, get());
    }

}
