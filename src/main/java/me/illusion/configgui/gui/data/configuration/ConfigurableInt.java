package me.illusion.configgui.gui.data.configuration;

import lombok.Getter;
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

public class ConfigurableInt implements Configurable {

    @Getter
    private Menu menu;
    @Getter
    private Menu previousMenu;

    private int val;

    private String path;
    private File file;
    private FileConfiguration config;

    private ItemStack item;

    public ConfigurableInt(int val, String path, File file, FileConfiguration config, int slot, Menu menu, Menu previous)
    {
        System.out.println("new ConfigurableInt");
        this.menu = menu;
        this.previousMenu = previous;
        this.val = val;

        this.path = path;
        this.config = config;
        this.file = file;

        item = new ItemBuilder(Material.SKULL_ITEM)
                .name("&a" + path)
                .skullHash("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTRkNDliYWU5NWM3OTBjM2IxZmY1YjJmMDEwNTJhNzE0ZDYxODU0ODFkNWIxYzg1OTMwYjNmOTlkMjMyMTY3NCJ9fX0=")
                .lore("", "&bLeft Click &7to increase by 1", "&cRight Click &7to decrease by 1", "", "&7Current Value: &6" + val)
                .build();

        menu.setItem(slot, item, (event) -> {
            if (event.isLeftClick())
                this.val = this.val + 1;
            if (event.isRightClick())
                this.val = this.val - 1;

            event.getInventory().setItem(slot, updateItem());
            event.setCancelled(true);
        });
    }

    @Override
    public boolean setValue(Object value) {
        if(value instanceof Integer)
        {
            val = (int) value;
            return true;
        }

        if(value instanceof String)
        {
            String s = (String) value;
            if(s.matches("\\d+"))
            {
                val = Integer.valueOf((String) value);
                return true;
            }
        }
        return false;
    }

    private ItemStack updateItem()
    {
        ItemMeta meta = item.getItemMeta();

        List<String> lore = meta.getLore();

        lore.set(4, ChatColor.translateAlternateColorCodes('&', "&7Current Value: &6" + val));

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
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
