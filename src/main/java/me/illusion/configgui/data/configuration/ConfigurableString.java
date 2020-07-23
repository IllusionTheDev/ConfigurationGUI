package me.illusion.configgui.data.configuration;

import lombok.Getter;
import me.illusion.configgui.ConfigurationPlugin;
import me.illusion.configgui.data.ConfiguratingPlayer;
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

public class ConfigurableString implements Configurable {

    @Getter
    private Menu menu;
    @Getter
    private Menu previousMenu;

    private String val;

    private String path;
    private File file;
    private FileConfiguration config;

    private int slot;

    private ItemStack item;

    public ConfigurableString(String val, ConfigurationPlugin main, String path, File file, FileConfiguration config, int slot, Menu menu, Menu previous)
    {
        this.menu = menu;
        this.previousMenu = previous;
        this.val = val;

        this.path = path;
        this.config = config;
        this.file = file;

        this.slot = slot;

        item = new ItemBuilder(Material.SKULL_ITEM)
                .name("&a" + path)
                .skullHash("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTRkNDliYWU5NWM3OTBjM2IxZmY1YjJmMDEwNTJhNzE0ZDYxODU0ODFkNWIxYzg1OTMwYjNmOTlkMjMyMTY3NCJ9fX0=")
                .lore("", "&bClick &7to edit this line.", "", "&7Current Value: ", " &f" + val)
                .build();

        menu.setItem(slot, item, (event) -> {
            event.setCancelled(true);
            ConfiguratingPlayer player = main.getPlayerManager().get(event.getWhoClicked().getUniqueId());

            if(player == null)
                return;

            player.setCurrentlyConfigurating(this);
            event.getWhoClicked().closeInventory();
            event.getWhoClicked().sendMessage(ChatColor.GREEN + "Type your current value into chat. Type \"cancel\" to stop");
        });
    }

    @Override
    public boolean setValue(Object value) {
        this.val = value.toString();
        menu.getInventory().setItem(slot, updateItem());
        return true;
    }

    private ItemStack updateItem()
    {
        ItemMeta meta = item.getItemMeta();

        List<String> lore = meta.getLore();

        lore.set(4, ChatColor.translateAlternateColorCodes('&', " &f" + val));

        meta.setLore(lore);
        item.setItemMeta(meta);

        menu.getInventory().setItem(slot, item);

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
