package moe.gabriella.enchsplitter.gui;

import me.gabriella.gabsgui.GUIBase;
import me.gabriella.gabsgui.GUIItem;
import moe.gabriella.enchsplitter.EnchantmentSplitter;
import moe.gabriella.enchsplitter.util.Log;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Map;

public class SplitterGui extends GUIBase {

    ItemStack toSplit;

    public SplitterGui(JavaPlugin plugin, Player player, ItemStack item) {
        super(plugin, player, EnchantmentSplitter.guiName, 54, true, Sound.BLOCK_ANVIL_PLACE, 1f, null, 1f);
        toSplit = item;
    }

    @Override
    public void setupItems() {
        EnchantmentSplitter es = EnchantmentSplitter.getInstance();

        int lvls = 0;
        if (es.perEnch)
            lvls = es.levelsNeeded * toSplit.getEnchantments().size();
        else
            lvls = es.levelsNeeded;

        GUIItem item = new GUIItem(toSplit.getType());
        for (Map.Entry<Enchantment, Integer> e : toSplit.getEnchantments().entrySet()) {
            item.enchantment(e.getKey(), e.getValue());
        }
        item.displayName(toSplit.getItemMeta().getDisplayName());

        // Yellow connector thing
        GUIItem yellow = new GUIItem(Material.YELLOW_STAINED_GLASS_PANE);
        yellow.displayName(" ");

        // Accept prompt
        GUIItem confirm = new GUIItem(Material.LIME_STAINED_GLASS_PANE);
        confirm.displayName("" + ChatColor.GREEN + "" + ChatColor.BOLD + "Confirm Split");
        confirm.button(new SplitterConfirmButton(getPlayer(), toSplit, lvls));

        // Decline prompt
        GUIItem decline = new GUIItem(Material.RED_STAINED_GLASS_PANE);
        decline.displayName("" + ChatColor.RED + "" + ChatColor.BOLD + "Cancel Split");
        decline.button(new CloseButton(getPlayer()));

        // Levels item
        GUIItem levels = new GUIItem(Material.EXPERIENCE_BOTTLE);
        levels.displayName("" + ChatColor.YELLOW + "Levels Needed");
        if (lvls <= 64)
            levels.amount(lvls);

        levels.lore(addLinebreaks("" + ChatColor.AQUA + "You will need " + ChatColor.GREEN + lvls + ChatColor.AQUA + " levels for this split.", 40, "" + ChatColor.GREEN));

        // Books item
        GUIItem books = new GUIItem(Material.BOOK);
        books.displayName("" + ChatColor.YELLOW + "Books Needed");
        int booksNeeded = 0;
        if (es.haveBooks)
            booksNeeded = toSplit.getEnchantments().size();
        if (booksNeeded == 0)
            books.lore(addLinebreaks("" + ChatColor.AQUA + "You won't need any books for this split.", 40, "" + ChatColor.GREEN));
        else
            books.lore(addLinebreaks("" + ChatColor.AQUA + "You will need " + ChatColor.GREEN + toSplit.getEnchantments().size() + ChatColor.AQUA + " books for this split.", 40, "" + ChatColor.GREEN));
        books.amount((booksNeeded == 0 ? 1 : booksNeeded));

        // Assigning yellow pos
        addItem(4, yellow);
        addItem(11, yellow);
        addItem(12, yellow);
        addItem(14, yellow);
        addItem(15, yellow);
        addItem(22, yellow);
        addItem(31, yellow);
        addItem(39, yellow);
        addItem(40, yellow);
        addItem(41, yellow);

        // Assigning confirm
        addItem(28, confirm);
        addItem(29, confirm);
        addItem(37, confirm);
        addItem(38, confirm);
        addItem(46, confirm);
        addItem(47, confirm);


        // Assigning decline
        addItem(33, decline);
        addItem(34, decline);
        addItem(42, decline);
        addItem(43, decline);
        addItem(51, decline);
        addItem(52, decline);

        // Assigning the other items
        addItem(10, levels);
        addItem(13, item);
        addItem(16, books);
    }
}
