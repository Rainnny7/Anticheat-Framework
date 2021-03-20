package me.braydon.anticheat;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.settings.PacketEventsSettings;
import lombok.Getter;
import me.braydon.anticheat.command.AnticheatCommand;
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
    private double[] recentTps;

    @Override
    public void onLoad() {
        INSTANCE = this;
        packetEvents = PacketEvents.create(this);
        packetEvents.load();
    }

    @Override
    public void onEnable() {
        packetEvents.init(new PacketEventsSettings().checkForUpdates(false));
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            recentTps = packetEvents.getServerUtils().getRecentTPS();
        }, 0L, 20L);
        new PlayerDataManager(this);
        getCommand("anticheat").setExecutor(new AnticheatCommand());
    }

    @Override
    public void onDisable() {
        packetEvents.terminate();
        for (Player player : Bukkit.getOnlinePlayers())
            PlayerData.cleanup(player);
    }
}