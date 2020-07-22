package me.illusion.configgui.gui;

import com.google.common.base.Strings;
import lombok.Getter;
import me.illusion.configgui.gui.data.MenuItems;
import me.illusion.configgui.gui.data.configuration.ConfigurableInt;
import me.illusion.configgui.gui.menu.Menu;
import me.illusion.configgui.util.FileUtil;
import me.illusion.configgui.util.ItemBuilder;
import me.illusion.configgui.util.PrimitiveUnboxer;
import me.illusion.configgui.util.StringComparator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class MainGUI {

    @Getter
    private Menu mainMenu;
    private File directory;

    private int slot;

    private final Class<?>[] classes = {
            ConfigurableInt.class,
    };

    public MainGUI(JavaPlugin plugin)
    {
        directory = plugin.getDataFolder().getParentFile();
        setup();
    }

    private void setup()
    {
        if(directory == null)
            return;

        File[] files = directory.listFiles();

        if(files == null)
            return;

        Map<String, Menu> menus = new HashMap<>();

        for(File dir : files)
        {
            String name = dir.getName();
            Plugin plugin = Bukkit.getPluginManager().getPlugin(name);

            if(plugin == null)
                continue;

            File[] contents = dir.listFiles();

            if(contents == null || contents.length == 0)
                continue;

            menus.put(dir.getName(), createMenu(contents));
        }

        List<Map.Entry<String, Menu>> sorted = menus.entrySet()
                .stream()
                .sorted(Comparator.comparing(Map.Entry::getKey))
                .collect(Collectors.toList());

        mainMenu = new Menu(((menus.size() / 9) + 1) * 9, centerTitle("Select your plugin"), "main-gui");

        sorted.forEach(entry -> {
            String name = entry.getKey();
            Menu subMenu = entry.getValue();

            mainMenu.setItem(slot++, makeItem(name), (event) -> {
                event.setCancelled(true);
                event.getWhoClicked().closeInventory();
                event.getWhoClicked().openInventory(subMenu.getInventory());
            });
        });

        mainMenu.build();
    }

    private Menu createMenu(File[] contents)
    {
        contents = (File[]) Arrays.stream(contents).filter(file -> file.getName().endsWith(".yml")).toArray();

        Menu menu = new Menu(((contents.length / 9) + 1) * 9, centerTitle("Select your plugin"), "sub-1");
        StringComparator comparator = new StringComparator();

        for(File file : contents)
        {
            FileConfiguration cfg = FileUtil.configurationFromFile(file);

            if(cfg == null)
                continue;

            Map<String, Object> objects = new HashMap<>();
            createItems(cfg, objects);

            List<Map.Entry<String, Object>> list = objects.entrySet().stream()
                    .sorted((e1, e2) -> comparator.compare(e1.getKey(), e2.getKey()))
                    .collect(Collectors.toList());

            Menu subMenu = new Menu(((contents.length / 9) + 1) * 9, centerTitle(file.getName()), "sub-2");

            list.forEach((entry) -> {
                String path = entry.getKey().startsWith(".") ? entry.getKey().replaceFirst("\\.", "") : entry.getKey();
                Object object = entry.getValue();

                Class<?> clazz = PrimitiveUnboxer.unbox(object.getClass());
                System.out.println(clazz);
                for (Class<?> configurableClass : classes)
                    if (configurableClass.getConstructors()[0].getParameterTypes()[0].equals(clazz)) {
                        try {
                            configurableClass.getConstructors()[0].newInstance(object, path, file, cfg, slot++, subMenu, menu);
                        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }

            });

            subMenu.setItem(subMenu.getSize() - 1, MenuItems.BACK_1, (event) -> {
                event.setCancelled(true);
                event.getWhoClicked().closeInventory();
                event.getWhoClicked().openInventory(menu.getInventory());
            });
        }

        return menu.build();

    }

    private void createItems(ConfigurationSection section, Map<String, Object> map)
    {
        for (String key : section.getKeys(false)) {
            if (section.isConfigurationSection(key))
                createItems(section.getConfigurationSection(key), map);
            else {
                map.put(section.getCurrentPath() + "." + key, section.get(key));
            }
        }
    }

    private ItemStack makeItem(String name)
    {
        return new ItemBuilder(Material.PAPER)
                .name("&b" + name)
                .build();
    }

    public String centerTitle(String title) {
        return Strings.repeat(" ", 27 - ChatColor.stripColor(title).length()) + title;
    }
}
