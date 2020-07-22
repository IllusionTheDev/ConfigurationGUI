package me.illusion.configgui.gui.data.configuration;

import me.illusion.configgui.gui.menu.Menu;

public interface Configurable {

    Menu getMenu();
    Menu getPreviousMenu();

    boolean setValue(Object value);

    void save();
}
