package me.braydon.api.event;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * This event is fired when a player is banned for cheating.
 *
 * @see PlayerCheatEvent
 * @author Braydon
 */
@Setter @Getter
public class PlayerPunishEvent extends PlayerCheatEvent {
    public PlayerPunishEvent(@NonNull PlayerCheatEvent playerCheatEvent) {
        super(playerCheatEvent.getPlayer(), playerCheatEvent.getCheckInfo(), playerCheatEvent.getData(), playerCheatEvent.getViolations());
    }

    @Override
    public boolean isCancelled() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCancelled(boolean cancelled) {
        throw new UnsupportedOperationException();
    }
}