package moe.gabriella.enchsplitter;

import moe.gabriella.enchsplitter.commands.SplitCommand;
import moe.gabriella.enchsplitter.gui.ErrorGui;
import moe.gabriella.enchsplitter.gui.SplitterGui;
import moe.gabriella.enchsplitter.util.Log;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.UUID;

public class EnchantmentSplitter extends JavaPlugin implements Listener {

    public FileConfiguration config;
    private static EnchantmentSplitter instance;

    public ArrayList<UUID> rateLimit;

    public int levelsNeeded;
    public boolean perEnch;
    public boolean haveBooks;
    public boolean command;
    public boolean usePermission;
    public boolean commandPermission;

    public static String guiName = "" + ChatColor.DARK_PURPLE + "" + ChatColor.MAGIC + "11" + ChatColor.RESET + " " + ChatColor.LIGHT_PURPLE + "Enchantment Splitter " + ChatColor.DARK_PURPLE + "" + ChatColor.MAGIC + "11";

    @Override
    public void onEnable() {
        Log.info("Starting Enchantment Splitter...");

        instance = this;
        this.saveDefaultConfig();
        config = this.getConfig();

        levelsNeeded = config.getInt("levelsNeeded");
        perEnch = config.getBoolean("perEnch");
        haveBooks = config.getBoolean("haveBooks");
        command = config.getBoolean("command");
        usePermission = config.getBoolean("usePermission");
        commandPermission = config.getBoolean("commandPermission");

        this.getServer().getPluginManager().registerEvents(this, this);
        rateLimit = new ArrayList<>();

        getCommand("split").setExecutor(new SplitCommand());

        Log.info("Enchantment Splitter is ready!");
    }

    @Override
    public void onDisable() {
        Log.info("Thank you for using Enchantment Splitter, bye bye!");
    }

    public static EnchantmentSplitter getInstance() {
        return instance;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && player.isSneaking() && event.getClickedBlock() != null && (event.getClickedBlock().getType() == Material.ANVIL || event.getClickedBlock().getType() == Material.CHIPPED_ANVIL || event.getClickedBlock().getType() == Material.DAMAGED_ANVIL)) {
            ItemStack item = player.getInventory().getItemInMainHand();
            event.setCancelled(true);

            handleRequest(player, item);
        }
    }

    public void handleRequest(Player player, ItemStack item) {
        if (isRatelimited(player))
            return;

        ratelimit(player);

        if (!player.hasPermission("enchsplitter.use") && usePermission) {
            player.sendMessage("" + ChatColor.RED + "You don't have permission to use this!");
            return;
        }

        if (item == null || item.getType() == Material.AIR) {
            player.sendMessage("" + ChatColor.RED + "You must be holding an item to do this!");
            return;
        }

        if (item.getEnchantments().size() == 0) {
            new ErrorGui(this, player, item, "This item has no enchantments!").open(false);
            return;
        }

        int lvls = 0;
        if (perEnch)
            lvls = levelsNeeded * item.getEnchantments().size();
        else
            lvls = levelsNeeded;

        if (player.getLevel() < lvls && player.getGameMode() != GameMode.CREATIVE) {
            new ErrorGui(this, player, item, "You need " + lvls + " levels to do this!").open(false);
            return;
        }

        if (haveBooks && amountOfBooks(player) < item.getEnchantments().size()) {
            new ErrorGui(this, player, item, "You need " + item.getEnchantments().size() + " books to do this!").open(false);
            return;
        }


        if (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.CREATIVE)
            new SplitterGui(this, player, item).open(false);
    }

    public void ratelimit(Player player) {
        UUID uuid = player.getUniqueId();
        rateLimit.add(player.getUniqueId());
        new BukkitRunnable() {

            @Override
            public void run() {
                rateLimit.remove(uuid);
            }
        }.runTaskLater(this, 30);
    }

    public boolean isRatelimited(Player player) {
        return rateLimit.contains(player.getUniqueId());
    }

    public int amountOfBooks(Player player) {
        int amount = 0;
        for (ItemStack i : player.getInventory().getContents()) {
            if (i != null && i.getType() == Material.BOOK)
                amount += i.getAmount();
        }
        return amount;
    }
}
