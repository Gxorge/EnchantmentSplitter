package moe.gabriella.enchsplitter.commands;

import moe.gabriella.enchsplitter.EnchantmentSplitter;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SplitCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("" + ChatColor.RED + "Only players can run this!");
            return true;
        }

        Player player = (Player) sender;

        if (!EnchantmentSplitter.getInstance().command) {
            player.sendMessage("" + ChatColor.RED + "This command is disabled!");
            return true;
        }

        if (!player.hasPermission("enchsplitter.command.split") && EnchantmentSplitter.getInstance().commandPermission) {
            player.sendMessage("" + ChatColor.RED + "You don't have permission to do this!");
            return true;
        }

        EnchantmentSplitter.getInstance().handleRequest(player, player.getInventory().getItemInMainHand());

        return true;
    }
}
