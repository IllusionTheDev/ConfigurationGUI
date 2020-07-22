package me.illusion.configgui.gui.menu;

import lombok.Getter;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Menu implements InventoryHolder {

    //-------- DATA METHODS --------

    final Map<Integer, Consumer<InventoryClickEvent>> clickevents = new HashMap<>();

    private final Map<Integer, ItemStack> contents = new HashMap<>();

    @Getter
    private final int size;
    private final String title;

    private Inventory inv;

    @Getter
    private final String identifier;

    //------------------------------

    public Menu(int size, String title, String identifier)
    {
        this.size = size;
        this.title = ChatColor.translateAlternateColorCodes('&', title);
        this.identifier = identifier;
    }

    /**
     * Gets the middle slot of an inventory, should not be used with an even number
     *
     * @param rows The amount of rows
     * @return The middle slot
     */
    public static int getMiddleSlot(int rows)
    {
        return (rows * 9) / 2; //Add 1 if you want user friendly slot, this starts from 0
    }

    //-------- CONSTRUCTOR ---------

    /**
     * Builds a menu, creating an inventory and allowing the menu to be open
     *
     * @return The build menu
     */
    public Menu build()
    {
        inv = Bukkit.createInventory(this, size, title);

        contents.keySet().forEach(i -> inv.setItem(i, contents.get(i)));

        return this;
    }

    //-------- DATA METHODS --------

    public void open(Player player)
    {
        Validate.notNull(inv, "Menu not built.");
        player.openInventory(inv);
    }

    public Menu drawBorder(ItemStack item)
    {
        return drawBorder(item, false);
    }

    public Menu drawBorder(ItemStack item, boolean protect)
    {
        for (int i = 0; i < size; i++)
            if (i < 9 || i > (size - 9) || (i % 9) == 0 || (i % 9) == 8)
                setItem(i, item, protect);
        return this;
    }

    public Menu setItem(int slot, ItemStack item)
    {
        return setItem(slot, item, false);
    }

    public Menu protect(int slot)
    {
        clickevents.put(slot, getActionFromSlot(slot).andThen(e -> e.setCancelled(true)));
        return this;
    }

    public Menu protect()
    {
        for (int i = 0; i < size; i++)
            protect(i);

        return this;
    }

    public Menu setItem(int slot, ItemStack item, boolean protect)
    {
        contents.put(slot, item);
        if (protect)
            protect(slot);

        return this;
    }

    public Menu setItem(int slot, ItemStack item, Consumer<InventoryClickEvent> action)
    {
        contents.put(slot, item);
        clickevents.put(slot, action);
        return this;
    }

    public Menu fill(ItemStack item, boolean protect)
    {
        return fill(item, protect ? (e) -> e.setCancelled(true) : (e) -> {
        });
    }

    public Menu fill(ItemStack item, Consumer<InventoryClickEvent> onclick)
    {
        for (int i = 0; i < size; i++) {
            contents.put(i, item);
            clickevents.put(i, onclick);
        }

        return this;
    }

    //-------- UTIL METHODS --------

    public Menu fill(ItemStack item)
    {
        return fill(item, false);
    }

    private int getPosition(int x, int y)
    {
        return x + ((y - 1) * 9);
    }

    /**
     * Gets a click action
     *
     * @param slot The slot to get the action from
     * @return The action to run
     */
    private Consumer<InventoryClickEvent> getActionFromSlot(int slot)
    {
        return clickevents.containsKey(slot) ? clickevents.get(slot) : (e) -> {
        };
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }

    //------------------------------

}
