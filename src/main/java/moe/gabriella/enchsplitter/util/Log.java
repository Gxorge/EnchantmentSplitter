package moe.gabriella.enchsplitter.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Log {

    public static void info(String message) { Bukkit.getServer().getConsoleSender().sendMessage("" + ChatColor.WHITE + "[EnchantmentSplitter] " + message); }
    public static void error(String message) { Bukkit.getServer().getConsoleSender().sendMessage("" + ChatColor.RED + "[EnchantmentSplitter: ERROR] " + message); }

}
