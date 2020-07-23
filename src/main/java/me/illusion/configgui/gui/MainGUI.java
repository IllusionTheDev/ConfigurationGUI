package me.illusion.configgui.gui;

import com.google.common.base.Strings;
import lombok.Getter;
import me.illusion.configgui.ConfigurationPlugin;
import me.illusion.configgui.data.MenuItems;
import me.illusion.configgui.data.configuration.*;
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
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class MainGUI {

    private final Map<Class<?>, Class<?>> classes = new HashMap<>();
    @Getter
    private Menu mainMenu;
    private File directory;

    private ConfigurationPlugin main;

    public MainGUI(ConfigurationPlugin plugin)
    {
        main = plugin;
        directory = plugin.getDataFolder().getParentFile();

        classes.put(int.class, ConfigurableInt.class);
        classes.put(double.class, ConfigurableDouble.class);
        classes.put(String.class, ConfigurableString.class);
        classes.put(boolean.class, ConfigurableBoolean.class);

        setup();
    }

    private void setup()
    {
        if (directory == null)
            return;

        File[] files = directory.listFiles();

        if (files == null)
            return;

        Map<String, Menu> menus = new HashMap<>();
        int slot = 0;

        for (File dir : files) {
            String name = dir.getName();
            Plugin plugin = Bukkit.getPluginManager().getPlugin(name);

            if (plugin == null)
                continue;

            File[] contents = dir.listFiles();

            if (contents == null || contents.length == 0)
                continue;

            menus.put(dir.getName(), createMenu(contents));
        }

        List<Map.Entry<String, Menu>> sorted = menus.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toList());

        mainMenu = new Menu(((menus.size() / 9) + 1) * 9, centerTitle("Select your plugin"), "main-gui");
        slot = 0;

        for(Map.Entry<String, Menu> entry : sorted) {
            String name = entry.getKey();
            Menu subMenu = entry.getValue();

            mainMenu.setItem(slot++, makeItem(name), (event) -> {
                event.setCancelled(true);
                event.getWhoClicked().closeInventory();
                event.getWhoClicked().openInventory(subMenu.getInventory());
            });
        }

        mainMenu.build();
    }

    private Menu createMenu(File[] contents)
    {
        StringComparator comparator = new StringComparator();

        contents = Arrays.stream(contents)
                .filter(file -> file.getName().endsWith(".yml"))
                .sorted((f1, f2) -> comparator.compare(f1.getName(), f2.getName()))
                .collect(Collectors.toList()).toArray(new File[]{});

        Menu menu = new Menu(((contents.length / 9) + 1) * 9, centerTitle("Select your file"), "sub-1");

        Consumer<InventoryClickEvent> action = (event) -> {
            event.setCancelled(true);
            event.getWhoClicked().closeInventory();
        };

        for(int i = 0; i < contents.length; i++)
        {
            File file = contents[i];

            FileConfiguration cfg = FileUtil.configurationFromFile(file);

            if (cfg == null)
                continue;

            Map<String, Object> objects = new HashMap<>();
            createItems(cfg, objects);
            int slot = 0;

            if (objects.isEmpty())
                continue;

            List<Map.Entry<String, Object>> list = objects.entrySet().stream()
                    .sorted((e1, e2) -> comparator.compare(e1.getKey(), e2.getKey()))
                    .collect(Collectors.toList());

            Menu subMenu = new Menu(((list.size() / 9) + 1) * 9, centerTitle(file.getName()), "sub-2");

            List<Configurable> configurables = new ArrayList<>();

            for(Map.Entry<String, Object> entry : list) {
                String path = entry.getKey().startsWith(".") ? entry.getKey().replaceFirst("\\.", "") : entry.getKey();
                Object object = entry.getValue();

                Class<?> clazz = PrimitiveUnboxer.unbox(object.getClass());

                if (classes.containsKey(clazz)) {
                    Class<?> configurableClass = classes.get(clazz);
                    try {
                        configurables.add((Configurable) configurableClass.getConstructors()[0].newInstance(object, main, path, file, cfg, slot++, subMenu, menu));
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }

            subMenu.setItem(subMenu.getSize() - 2, MenuItems.SAVE, (event) -> {
                event.setCancelled(true);
                configurables.forEach(Configurable::save);
            });

            subMenu.setItem(subMenu.getSize() - 1, MenuItems.BACK_1,
                    action.andThen((event) -> event.getWhoClicked().openInventory(menu.getInventory())));

            menu.setItem(i, makeItem(file.getName()),
                    action.andThen((event) -> event.getWhoClicked().openInventory(subMenu.getInventory())));

            subMenu.build();
        }

        menu.setItem(menu.getSize() - 1, MenuItems.BACK_1,
                action.andThen((event) -> event.getWhoClicked().openInventory(mainMenu.getInventory())));

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
