package me.illusion.configgui.gui.data;

import me.illusion.configgui.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MenuItems {

    public static ItemStack SAVE = new ItemBuilder(Material.SKULL_ITEM)
            .name("&a&lSave")
            .skullHash("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWUyOGJlYTlkMzkzNzNkMzZlZThmYTQwZWM4M2Y5YzNmY2RkOTMxNzUyMjc3NDNmOWRkMWY3ZTc4ODZiN2VlNSJ9fX0=")
            .build();

    public static ItemStack BACK_1 = new ItemBuilder(Material.SKULL_ITEM)
            .name("&c&lGo Back")
            .skullHash("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzg2MTg1YjFkNTE5YWRlNTg1ZjE4NGMzNGYzZjNlMjBiYjY0MWRlYjg3OWU4MTM3OGU0ZWFmMjA5Mjg3In19fQ==")
            .build();
}
