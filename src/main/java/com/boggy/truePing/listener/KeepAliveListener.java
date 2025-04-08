package com.boggy.truePing.listener;

import com.boggy.truePing.TruePing;
import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientKeepAlive;

public class KeepAliveListener implements PacketListener {

    TruePing plugin;
    public KeepAliveListener(TruePing plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent e) {
        if (e.getPacketType() != PacketType.Play.Client.KEEP_ALIVE) return;

        WrapperPlayClientKeepAlive packet = new WrapperPlayClientKeepAlive(e);
        long id = packet.getId();
        boolean isManual = (id >>> 32) == 0xABCDEF00L;

        if (!isManual) {
            return;
        }

        id = id & 0xFFFFFFFFL;
        long ping = System.currentTimeMillis() - id;

        plugin.getPingManager().setPlayerPing(e.getUser().getUUID(), (int) ping);

        e.setCancelled(true);
    }

}
