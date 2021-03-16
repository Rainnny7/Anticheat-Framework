package me.braydon.api.event;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * This event is fired when a player is banned for cheating.
 *
 * @author Braydon
 * @see PlayerCheatEvent
 * @apiNote This event does not support cancelling.
 */
@Setter @Getter
public class PlayerPunishEvent extends PlayerCheatEvent {
    public PlayerPunishEvent(@NonNull PlayerCheatEvent playerCheatEvent) {
        super(playerCheatEvent.getPlayer(), playerCheatEvent.getCheckInfo(), playerCheatEvent.getData(), playerCheatEvent.getViolations());
    }
}