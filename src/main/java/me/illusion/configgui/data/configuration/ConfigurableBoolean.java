package me.illusion.configgui.data.configuration;

import com.google.common.primitives.Doubles;
import lombok.Getter;
import me.illusion.configgui.ConfigurationPlugin;
import me.illusion.configgui.gui.menu.Menu;
import me.illusion.configgui.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ConfigurableBoolean implements Configurable {

    @Getter
    private Menu menu;
    @Getter
    private Menu previousMenu;

    private boolean val;

    private String path;
    private File file;
    private FileConfiguration config;

    private ItemStack item;

    private int slot;

    public ConfigurableBoolean(boolean val, ConfigurationPlugin main, String path, File file, FileConfiguration config, int slot, Menu menu, Menu previous)
    {
        this.menu = menu;
        this.previousMenu = previous;
        this.val = val;

        this.path = path;
        this.config = config;
        this.file = file;

        this.slot = slot;

        String color = val ? "&a" : "&c";

        item = new ItemBuilder(Material.SKULL_ITEM)
                .name("&a" + path)
                .skullHash("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTRkNDliYWU5NWM3OTBjM2IxZmY1YjJmMDEwNTJhNzE0ZDYxODU0ODFkNWIxYzg1OTMwYjNmOTlkMjMyMTY3NCJ9fX0=")
                .lore("",
                        "&bClick &7to toggle value",
                        "",
                        "&7Current Value: " + color + val)
                .build();

        menu.setItem(slot, item, (event) -> {
            this.val = !this.val;
            event.getInventory().setItem(slot, updateItem());
            event.setCancelled(true);
        });
    }

    private ItemStack updateItem()
    {
        ItemMeta meta = item.getItemMeta();

        List<String> lore = meta.getLore();

        String color = val ? "&a" : "&c";
        lore.set(3, ChatColor.translateAlternateColorCodes('&', "&7Current Value: " + color + val));

        meta.setLore(lore);
        item.setItemMeta(meta);

        menu.getInventory().setItem(slot, item);

        return item;
    }

    @Override
    public boolean setValue(Object value) {
        if(value instanceof Boolean)
        {
            this.val = (boolean) value;
            updateItem();
            return true;
        }

        return false;
    }

    @Override
    public void save() {
        config.set(path, val);
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
