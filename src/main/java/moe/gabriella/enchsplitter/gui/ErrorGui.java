package moe.gabriella.enchsplitter.gui;

import me.gabriella.gabsgui.GUIBase;
import me.gabriella.gabsgui.GUIItem;
import moe.gabriella.enchsplitter.EnchantmentSplitter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

public class ErrorGui extends GUIBase {

    ItemStack toSplit;
    String errorReason;

    public ErrorGui(JavaPlugin plugin, Player player, ItemStack item, String errorReason) {
        super(plugin, player, EnchantmentSplitter.guiName, 27, true, Sound.ENTITY_VILLAGER_NO, 1f, null, 1f);
        toSplit = item;
        this.errorReason = errorReason;
    }

    @Override
    public void setupItems() {
        GUIItem item = new GUIItem(toSplit.getType());
        for (Map.Entry<Enchantment, Integer> e : toSplit.getEnchantments().entrySet()) {
            item.enchantment(e.getKey(), e.getValue());
        }
        item.displayName(toSplit.getItemMeta().getDisplayName());
        item.button(new CloseButton(getPlayer()));

        addItem(13, item);

        GUIItem err = new GUIItem(Material.RED_STAINED_GLASS_PANE);
        err.displayName("" + ChatColor.RED + errorReason);
        err.button(new CloseButton(getPlayer()));
        for (int i = 0; i <= 26; i++) {
            if (i == 13)
                continue;
            addItem(i, err);
        }
    }
}
