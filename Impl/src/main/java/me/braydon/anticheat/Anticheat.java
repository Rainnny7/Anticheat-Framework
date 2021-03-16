package me.braydon.anticheat;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.settings.PacketEventsSettings;
import lombok.Getter;
import me.braydon.anticheat.player.PlayerData;
import me.braydon.anticheat.player.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Braydon
 */
@Getter
public class Anticheat extends JavaPlugin {
    public static Anticheat INSTANCE;

    private PacketEvents packetEvents;

    @Override
    public void onLoad() {
        INSTANCE = this;
        packetEvents = PacketEvents.create(this);
        packetEvents.load();
    }

    @Override
    public void onEnable() {
        packetEvents.init(new PacketEventsSettings().checkForUpdates(false));
        new PlayerDataManager(this);
    }

    @Override
    public void onDisable() {
        packetEvents.terminate();
        for (Player player : Bukkit.getOnlinePlayers())
            PlayerData.cleanup(player);
    }
}