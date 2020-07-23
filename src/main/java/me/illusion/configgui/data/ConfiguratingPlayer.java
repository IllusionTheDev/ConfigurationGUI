package me.illusion.configgui.data;

import lombok.Getter;
import lombok.Setter;
import me.illusion.configgui.data.configuration.Configurable;

import java.util.UUID;

@Getter
public class ConfiguratingPlayer {

    private UUID uuid;

    @Setter
    private Configurable currentlyConfigurating = null;

    public ConfiguratingPlayer(UUID uuid) {
        this.uuid = uuid;
    }
}
