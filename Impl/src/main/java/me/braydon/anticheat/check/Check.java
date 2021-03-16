package me.braydon.anticheat.check;

import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import me.braydon.anticheat.Anticheat;
import me.braydon.anticheat.meta.MetadataManager;
import me.braydon.anticheat.player.PlayerData;
import me.braydon.api.check.CheckInfo;
import me.braydon.api.event.PlayerCheatEvent;
import me.braydon.api.event.PlayerPunishEvent;
import me.braydon.api.player.Violation;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * The purpose of a check is to detect specific cheats. Each check is capable of handling packets, movement, and attacking.
 * Each check object contains player data, the player, and information for the check.
 *
 * @author Braydon
 * @see PlayerData
 * @see Player
 * @see CheckInfo
 */
@Getter
public class Check {
    protected final PlayerData playerData;
    protected final Player player;
    private final CheckInfo checkInfo;

    @Getter(AccessLevel.NONE) private int violations;

    public Check(@NonNull PlayerData playerData) throws ClassNotFoundException {
        this.playerData = playerData;
        if (!getClass().isAnnotationPresent(CheckInfo.class))
            throw new ClassNotFoundException("Check is missing @CheckInfo annotation");
        player = playerData.getBukkitPlayer();
        checkInfo = getClass().getAnnotation(CheckInfo.class);
    }

    /**
     * This method is fired when the player sends or receives a packet.
     *
     * @param packetId the id of the packet
     * @param nmsPacket the nms packet
     * @param packet the raw nms packet
     * @see PacketType
     * @see NMSPacket
     */
    public void handle(byte packetId, @NonNull NMSPacket nmsPacket, @NonNull Object packet) {}

    /**
     * This method is used to flag the player with the given data.
     * <p>
     * When a player is flagged, all online staff members are alerted with the check they flagged and the data
     *
     * @param data
     */
    protected final void flag(String... data) {
        violations++;

        Violation violation = new Violation(
                checkInfo,
                data,
                violations,
                player.getLocation(),
                playerData.packetProcessor.ping,
                Anticheat.INSTANCE.getRecentTps()[0],
                System.currentTimeMillis()
        );
        playerData.addViolation(violation);

        PlayerCheatEvent playerCheatEvent = new PlayerCheatEvent(player, violation);
        Bukkit.getPluginManager().callEvent(playerCheatEvent);
        if (playerCheatEvent.isCancelled())
            return;

        String message = ChatColor.stripColor(String.join(", ", data)).trim();
        String checkName = (checkInfo.experimental() ? "§7§o*" : "§f") + checkInfo.name();
        for (Player staff : Bukkit.getOnlinePlayers()) {
            if (!staff.hasPermission("anticheat.alert"))
                continue;
            staff.sendMessage("§8[§6§lAC§8] §f" + player.getName() + " §7flagged " + checkName + " §c(x" + violations + ")" +
                    (message.isEmpty() ? "" : " §7[" + message + "]"));
        }
        if (violations >= checkInfo.maxVl() && checkInfo.ban() && !checkInfo.experimental() && !playerData.isBanned()) {
            playerData.setBanned(true);

            String metadataJson = MetadataManager.getMetadataJson(playerData);
            Anticheat.INSTANCE.getLogger().info(player.getName() + " was banned for cheating (" + checkInfo.name() + "): " + metadataJson);

            Bukkit.getPluginManager().callEvent(new PlayerPunishEvent(playerCheatEvent));
            Bukkit.getScheduler().runTask(Anticheat.INSTANCE, () -> player.kickPlayer("[AC] Unfair Advantage"));
        }
    }
}