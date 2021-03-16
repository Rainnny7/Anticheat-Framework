package me.braydon.anticheat.player;

import lombok.NonNull;
import me.braydon.anticheat.Anticheat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author Braydon
 */
public class PlayerDataManager implements Listener {
    public PlayerDataManager(@NonNull Anticheat plugin) {
        // When this class is initialized, we loop over all online players
        // and construct a new player data object for them. The purpose of
        // this is to support server reloads.
        for (Player player : Bukkit.getOnlinePlayers())
            new PlayerData(player);
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
        new PlayerData(event.getPlayer());
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