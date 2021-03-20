package me.braydon.anticheat.processor.impl;

import lombok.NonNull;
import me.braydon.anticheat.common.MovementData;
import me.braydon.anticheat.player.PlayerData;
import me.braydon.anticheat.processor.Processor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * This processor processes data from player movements.
 *
 * @author Braydon
 */
public class MovementProcessor extends Processor {
    public double deltaY;

    public MovementProcessor(@NonNull PlayerData playerData) {
        super(playerData);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onMove(PlayerMoveEvent event) {
        if (!event.getPlayer().getUniqueId().equals(playerData.getBukkitPlayer().getUniqueId()))
            return;
        Location from = event.getFrom();
        Location to = event.getTo();

        // Checking the types of movement the player makes
        List<MovementData.MovementType> movementTypes = new ArrayList<>();
        if (from.getX() != to.getX())
            movementTypes.add(MovementData.MovementType.X);
        if (from.getY() != to.getY())
            movementTypes.add(MovementData.MovementType.Y);
        if (from.getZ() != to.getZ())
            movementTypes.add(MovementData.MovementType.Z);
        if (from.getYaw() != to.getYaw())
            movementTypes.add(MovementData.MovementType.YAW);
        if (from.getPitch() != to.getPitch())
            movementTypes.add(MovementData.MovementType.PITCH);
        MovementData movementData = new MovementData(from, to, movementTypes);

        // Processing movement data
        deltaY = Math.abs(from.getY() - to.getY());

        long timestamp = System.currentTimeMillis();
        playerData.getChecks().parallelStream().forEach(check -> check.handle(movementData, timestamp));
    }
}