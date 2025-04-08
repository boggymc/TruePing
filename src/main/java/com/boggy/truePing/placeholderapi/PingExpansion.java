package com.boggy.truePing.placeholderapi;

import com.boggy.truePing.TruePing;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class PingExpansion extends PlaceholderExpansion {

    TruePing plugin;
    public PingExpansion(TruePing plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "trueping";
    }

    @Override
    public @NotNull String getAuthor() {
        return "boggy";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        Player target = Bukkit.getPlayer(params);
        if (target != null) {
            return plugin.getPingManager().getPlayerPing(target.getUniqueId()) + "";
        }

        return null;
    }
}
