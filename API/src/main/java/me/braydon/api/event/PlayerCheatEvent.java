package me.braydon.api.event;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.braydon.api.check.CheckInfo;
import me.braydon.api.player.Violation;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * This event is fired when a player is detected cheating which contains the player, the info for the
 * check that the player fired, the data for the flag, and the number of violations.
 * <p>
 * The check class is not exposed in the event as it would expose certain data which shouldn't be available in
 * the API - If this framework was to ever be used for a paid product, the end user shouldn't be able to view
 * the check as it may expose information on how the check works.
 *
 * @author Braydon
 * @see Event
 * @see Cancellable
 * @see Player
 * @see CheckInfo
 */
@RequiredArgsConstructor @Setter @Getter
public class PlayerCheatEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();

    private final @NonNull Player player;
    private final @NonNull Violation violation;
    private boolean cancelled;

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}