package me.braydon.anticheat.processor;

import io.github.retrooper.packetevents.event.PacketListenerDynamic;
import io.github.retrooper.packetevents.event.priority.PacketEventPriority;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import me.braydon.anticheat.Anticheat;
import me.braydon.anticheat.player.PlayerData;
import me.braydon.anticheat.processor.impl.PacketProcessor;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;

/**
 * A processor is used to process certain data such as from packets, events, etc.
 * <p>
 * All processors can handle packets and bukkit events
 *
 * @see PacketProcessor for an example
 * @see NMSPacket
 * @see Event
 * @author Braydon
 */
public class Processor extends PacketListenerDynamic implements Listener {
    protected final PlayerData playerData;

    public Processor(PlayerData playerData) {
        super(PacketEventPriority.LOWEST);
        this.playerData = playerData;
        Anticheat.INSTANCE.getPacketEvents().getEventManager().registerListener(this);
        Bukkit.getPluginManager().registerEvents(this, Anticheat.INSTANCE);
    }
}