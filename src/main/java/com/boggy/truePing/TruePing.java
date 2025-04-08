package com.boggy.truePing;

import com.boggy.truePing.listener.KeepAliveListener;
import com.boggy.truePing.manager.PingManager;
import com.boggy.truePing.placeholderapi.PingExpansion;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerKeepAlive;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class TruePing extends JavaPlugin {
    PingManager pingManager = new PingManager();

    @Override
    public void onLoad() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().load();

        PacketEvents.getAPI().getEventManager().registerListener(new KeepAliveListener(this), PacketListenerPriority.LOWEST);
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        PacketEvents.getAPI().load();

        pingManager = new PingManager();
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            sendPingPacket();
        }, getConfig().getInt("refresh-rate"), getConfig().getInt("refresh-rate"));

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PingExpansion(this).register();
        }
    }

    private void sendPingPacket() {
        long timestamp = System.currentTimeMillis();
        long marker = 0xABCDEF00L; // to filter our manually sent packet on receive
        long customKeepAliveId = (marker << 32) | (timestamp & 0xFFFFFFFFL);

        WrapperPlayServerKeepAlive packet = new WrapperPlayServerKeepAlive(customKeepAliveId);
        for (Player player : Bukkit.getOnlinePlayers()) {
            User user = PacketEvents.getAPI().getPlayerManager().getUser(player);
            if (user.getEncoderState() != ConnectionState.PLAY) continue;
            user.writePacket(packet);
        }

    }

    @Override
    public void onDisable() {
        PacketEvents.getAPI().terminate();
    }

    public PingManager getPingManager() {
        return pingManager;
    }
}
