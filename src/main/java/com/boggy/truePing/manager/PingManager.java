package com.boggy.truePing.manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PingManager {

    HashMap<UUID, Integer> playerPings = new HashMap<>();

    public int getPlayerPing(UUID uuid) {
        if (!playerPings.containsKey(uuid)) {
            Player player = Bukkit.getPlayer(uuid);
            return player.getPing();
        }

        return playerPings.get(uuid);
    }

    public void setPlayerPing(UUID uuid, int ping) {
        playerPings.put(uuid, ping);
    }

}
