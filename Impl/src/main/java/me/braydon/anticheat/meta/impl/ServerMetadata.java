package me.braydon.anticheat.meta.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.NonNull;
import me.braydon.anticheat.Anticheat;
import me.braydon.anticheat.meta.IMetadata;
import me.braydon.anticheat.player.PlayerData;
import org.bukkit.Bukkit;

/**
 * @author Braydon
 */
public class ServerMetadata implements IMetadata {
    /**
     * Get the name of the metadata element
     *
     * @return the name
     */
    @Override
    public String getName() {
        return "server";
    }

    /**
     * Get the json element for the provided player data.
     *
     * @param playerData the player data to get the json element for.
     * @return the json element
     * @see JsonElement
     * @see PlayerData
     */
    @Override
    public JsonElement getJsonElement(@NonNull PlayerData playerData) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("players", Bukkit.getOnlinePlayers().size());
        jsonObject.addProperty("tps", Anticheat.INSTANCE.getRecentTps()[0]);
        return jsonObject;
    }
}