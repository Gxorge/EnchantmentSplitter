package moe.gabriella.enchsplitter.gui;

import me.gabriella.gabsgui.GUIButton;
import org.bukkit.entity.Player;

public class CloseButton implements GUIButton {

    Player player;

    public CloseButton(Player player) {
        this.player = player;
    }

    @Override
    public boolean leftClick() {
        player.closeInventory();
        return true;
    }

    @Override
    public boolean leftClickShift() {
        player.closeInventory();
        return true;
    }

    @Override
    public boolean rightClick() {
        player.closeInventory();
        return true;
    }

    @Override
    public boolean rightClickShift() {
        player.closeInventory();
        return true;
    }
}
