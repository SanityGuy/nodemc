package me.serenity.nodeMC.utils;

import org.bukkit.Bukkit;

public class C {
    private static final String PREFIX = "[NodeMC] ";
    public static void info(String s) { Bukkit.getLogger().info(PREFIX+s); }
    public static void warn(String s) { Bukkit.getLogger().warning(PREFIX+s); }
}
