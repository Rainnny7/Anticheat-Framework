package me.braydon.anticheat.player;

import lombok.NonNull;
import me.braydon.anticheat.Anticheat;
import me.braydon.api.event.PlayerPunishEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Braydon
 */
public class PlayerDataManager implements Listener {
    /**
     * The maximum amount of time to have an ip address flagged as an extreme prejudice
     */
    private static final long EXTREME_PREJUDICE_MAX_TIME = TimeUnit.MINUTES.toMillis(10L);

    /**
     * The map of flagged ip addresses. The key being the ip address and the value being
     * the last time in millis when the ip address was flagged.
     */
    private final Map<String, Long> extremePrejudice = new HashMap<>();

    public PlayerDataManager(@NonNull Anticheat plugin) {
        // When this class is initialized, we loop over all online players
        // and construct a new player data object for them. The purpose of
        // this is to support server reloads.
        for (Player player : Bukkit.getOnlinePlayers())
            new PlayerData(player);

        // Start a sync repeating task that runs every second that removes all flagged ip addresses that
        // have been flagged for the maximum amount of time.
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () ->
                extremePrejudice.entrySet().removeIf(entry -> (System.currentTimeMillis() - entry.getValue()) >= EXTREME_PREJUDICE_MAX_TIME), 20L, 20L);

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    /**
     * When a player joins the server we construct new player data for them to be used later.
     *
     * @see Player
     * @see PlayerData
     */
    @EventHandler(priority = EventPriority.LOWEST)
    private void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = new PlayerData(player);
        InetSocketAddress address = player.getAddress();
        if (address != null && (extremePrejudice.containsKey(address.getHostName())))
            playerData.setExtremePrejudice(true);
    }

    /**
     * When a player is punished for cheating, we flag their ip address as an extreme prejudice.
     */
    @EventHandler
    private void onPunish(PlayerPunishEvent event) {
        InetSocketAddress address = event.getPlayer().getAddress();
        if (address == null)
            return;
        extremePrejudice.put(address.getHostName(), System.currentTimeMillis());
    }

    /**
     * When a player disconnects from the server we cleanup their player data.
     *
     * @see Player
     * @see PlayerData
     */
    @EventHandler(priority = EventPriority.LOWEST)
    private void onQuit(PlayerQuitEvent event) {
        PlayerData.cleanup(event.getPlayer());
    }
}