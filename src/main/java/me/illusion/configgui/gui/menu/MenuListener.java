package me.illusion.configgui.gui.menu;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MenuListener implements Listener {

    @EventHandler
    private void onClick(InventoryClickEvent e)
    {
        if (!(e.getView().getTopInventory().getHolder() instanceof Menu))
            return;

        Menu menu = (Menu) e.getView().getTopInventory().getHolder();

        if (menu.clickevents.containsKey(e.getRawSlot()))
            menu.clickevents.get(e.getRawSlot()).accept(e);
    }

}
