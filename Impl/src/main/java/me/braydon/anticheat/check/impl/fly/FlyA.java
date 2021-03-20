package me.braydon.anticheat.check.impl.fly;

import lombok.NonNull;
import me.braydon.anticheat.check.Check;
import me.braydon.anticheat.common.MovementData;
import me.braydon.anticheat.player.PlayerData;
import me.braydon.api.check.CheckInfo;
import me.braydon.api.check.CheckType;
import org.bukkit.potion.PotionEffectType;

/**
 * This is just an example fly check.
 *
 * @author Braydon
 */
@CheckInfo(name = "Fly (A)", type = CheckType.FLY, experimental = true)
public class FlyA extends Check {
    public FlyA(@NonNull PlayerData playerData) throws ClassNotFoundException {
        super(playerData);
    }

    @Override
    public void handle(MovementData movementData, long timestamp) {
        if (movementData.isDescending())
            return;
        double deltaY = playerData.movementProcessor.deltaY;
        double maxDeltaY = 0.43;

        int jumpBoostLevel = playerData.getPotionEffectLevel(PotionEffectType.JUMP);
        maxDeltaY+= jumpBoostLevel * 0.12;

        if (deltaY >= maxDeltaY)
            flag(deltaY + ">=" + maxDeltaY);
        debug("dY=" + deltaY, "maxDy=" + maxDeltaY, "jumpLevel=" + jumpBoostLevel);
    }
}