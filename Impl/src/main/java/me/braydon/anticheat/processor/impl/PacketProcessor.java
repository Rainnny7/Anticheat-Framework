package me.braydon.anticheat.processor.impl;

import io.github.retrooper.packetevents.event.eventtypes.CancellableNMSPacketEvent;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PacketPlaySendEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import lombok.NonNull;
import me.braydon.anticheat.Anticheat;
import me.braydon.anticheat.check.Check;
import me.braydon.anticheat.player.PlayerData;
import me.braydon.anticheat.processor.Processor;
import org.bukkit.entity.Player;

/**
 * This processor processes data from packets.
 *
 * @author Braydon
 */
public class PacketProcessor extends Processor {
    public int ping;
    public long lastFlying;

    public PacketProcessor(@NonNull PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {
        Player player = event.getPlayer();
        if (!player.equals(playerData.getBukkitPlayer()))
            return;
        // Processing data from the received packet
        switch (event.getPacketId()) {
            case PacketType.Play.Client.KEEP_ALIVE: {
                ping = Anticheat.INSTANCE.getPacketEvents().getPlayerUtils().getPing(player);
                break;
            }
            case PacketType.Play.Client.FLYING: {
                lastFlying = System.currentTimeMillis();
                break;
            }
        }
        // Handling the received packet for all of the checks
        handleChecks(event);
    }

    @Override
    public void onPacketPlaySend(PacketPlaySendEvent event) {
        if (!event.getPlayer().equals(playerData.getBukkitPlayer()))
            return;
        // Handling the sent packet for all of the checks
        handleChecks(event);
    }

    /**
     * Handle the checks for the given packet event
     *
     * @param nmsPacketEvent the packet event
     * @see Check
     * @see CancellableNMSPacketEvent
     */
    private void handleChecks(@NonNull CancellableNMSPacketEvent nmsPacketEvent) {
        NMSPacket nmsPacket = nmsPacketEvent.getNMSPacket();
        playerData.getChecks().parallelStream().forEach(check ->
                check.handle(nmsPacketEvent.getPacketId(), nmsPacket, nmsPacket.getRawNMSPacket(), System.currentTimeMillis()));
    }
}