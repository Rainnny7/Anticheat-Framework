package me.braydon.anticheat.player;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import me.braydon.anticheat.check.Check;
import me.braydon.anticheat.check.CheckManager;
import me.braydon.anticheat.processor.impl.PacketProcessor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @author Braydon
 */
@Getter
public class PlayerData {
    private static final Map<UUID, PlayerData> dataMap = new HashMap<>();

    private final UUID uuid;

    // Processors and checks
    @Getter(AccessLevel.NONE) public PacketProcessor packetProcessor;
    private final List<Check> checks = new ArrayList<>();

    // Other data
    @Setter private boolean banned;

    /**
     * Construct a new player data object for the provided player and cache it in the data map so
     * it can be used later
     *
     * @param player the player to construct the data for
     * @see Player
     */
    public PlayerData(Player player) {
        uuid = player.getUniqueId();
        packetProcessor = new PacketProcessor(this);
        for (Class<? extends Check> checkClass : CheckManager.CHECK_CLASSES) {
            try {
                checks.add(checkClass.getConstructor(PlayerData.class).newInstance(this));
            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ex) {
                ex.printStackTrace();
            }
        }
        dataMap.put(uuid, this);
    }

    /**
     * Get the Bukkit player for this data object
     *
     * @return the Bukkit player
     * @see Player
     */
    public Player getBukkitPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    /**
     * Get the player data for the provided player
     *
     * @param player the player to get the player data for
     * @return the player data if found, otherwise null
     * @see Player
     */
    public static PlayerData get(Player player) {
        return dataMap.get(player.getUniqueId());
    }

    /**
     * Cleanup the player data for the provided player.
     * <p>
     * If the provided player does not have any player data, a {@link NullPointerException} will be thrown
     *
     * @param player the player to cleanup the player data for
     * @see Player
     */
    public static void cleanup(Player player) {
        PlayerData playerData = dataMap.remove(player.getUniqueId());
        if (playerData == null)
            throw new NullPointerException("Player does not have player data to cleanup: " + player.getName());
        playerData.packetProcessor = null;
        playerData.getChecks().clear();
    }
}