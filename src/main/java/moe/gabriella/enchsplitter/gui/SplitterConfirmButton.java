package moe.gabriella.enchsplitter.gui;

import me.gabriella.gabsgui.GUIButton;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Map;

public class SplitterConfirmButton implements GUIButton {

    Player player;
    ItemStack item;
    int levelsToRemove;

    public SplitterConfirmButton(Player player, ItemStack item, int levelsToRemove) {
        this.player = player;
        this.item = item;
        this.levelsToRemove = levelsToRemove;
    }

    @Override
    public boolean leftClick() {
        player.closeInventory();
        ArrayList<ItemStack> toRemove = new ArrayList<>();
        int booksRemoved = item.getEnchantments().size();

        player.getInventory().removeItem(item);
        player.getInventory().removeItem(new ItemStack(Material.BOOK, booksRemoved));

        ItemStack newI = new ItemStack(item.getType());
        ItemMeta newIm = newI.getItemMeta();
        if (newIm instanceof Damageable) {
            Damageable typeM = (Damageable) item.getItemMeta();
            ((Damageable) newIm).setDamage(typeM.getDamage());
        }
        newIm.setDisplayName(item.getItemMeta().getDisplayName());
        newI.setItemMeta(newIm);

        player.getInventory().addItem(newI);
        for (Map.Entry<Enchantment, Integer> e : item.getEnchantments().entrySet()) {
            if (player.getInventory().firstEmpty() == -1) {
                player.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "Inventory is full, cancelling split...");
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                for (ItemStack i : toRemove) {
                    player.getInventory().removeItem(i);
                }
                player.getInventory().removeItem(newI);
                player.getInventory().addItem(item);
                player.getInventory().addItem(new ItemStack(Material.BOOK, booksRemoved));
                return true;
            }

            ItemStack book = new ItemStack(Material.ENCHANTED_BOOK, 1);
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) book.getItemMeta();
            meta.addStoredEnchant(e.getKey(), e.getValue(), true);
            book.setItemMeta(meta);
            player.getInventory().addItem(book);
            toRemove.add(book);
        }
        player.sendMessage("" + ChatColor.GREEN + "Item has been successfully split!");
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 0f);
        if (player.getGameMode() != GameMode.CREATIVE)
            player.setLevel(player.getLevel() - levelsToRemove);

        return true;
    }

    @Override public boolean leftClickShift() { return false; }
    @Override public boolean rightClick() { return false; }
    @Override public boolean rightClickShift() { return false; }
}
