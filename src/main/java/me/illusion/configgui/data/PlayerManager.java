package me.illusion.configgui.data;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManager {

    private Map<UUID, ConfiguratingPlayer> players = new HashMap<>();

    public ConfiguratingPlayer register(UUID uuid)
    {
        players.put(uuid, new ConfiguratingPlayer(uuid));
        return get(uuid);
    }

    public ConfiguratingPlayer get(Player player)
    {
        return get(player.getUniqueId());
    }

    public ConfiguratingPlayer get(UUID uuid)
    {
        return players.getOrDefault(uuid, null);
    }
}
