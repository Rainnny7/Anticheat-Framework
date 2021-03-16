package me.braydon.anticheat.meta.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.NonNull;
import me.braydon.anticheat.meta.IMetadata;
import me.braydon.anticheat.player.PlayerData;
import me.braydon.api.player.Violation;

/**
 * @author Braydon
 */
public class ViolationMetadata implements IMetadata {
    /**
     * Get the name of the metadata element
     *
     * @return the name
     */
    @Override
    public String getName() {
        return "violations";
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
        JsonArray jsonArray = new JsonArray();
        for (Violation violation : playerData.getViolations()) {
            JsonObject jsonObject = new JsonObject();

            // Adding the check data to the json object.
            jsonObject.addProperty("check", violation.getCheckInfo().name());
            jsonObject.addProperty("data", String.join(", ", violation.getData()));
            jsonObject.addProperty("violations", violation.getViolations());

            // Creating a new json object for the player's location and adding it to the violation json object.
            JsonObject playerLocationJsonObject = new JsonObject();
            playerLocationJsonObject.addProperty("world", violation.getLocation().getWorld().getName());
            playerLocationJsonObject.addProperty("x", violation.getLocation().getX());
            playerLocationJsonObject.addProperty("y", violation.getLocation().getY());
            playerLocationJsonObject.addProperty("z", violation.getLocation().getZ());
            playerLocationJsonObject.addProperty("yaw", violation.getLocation().getYaw());
            playerLocationJsonObject.addProperty("pitch", violation.getLocation().getPitch());

            // Adding the above location json object and player's ping to the violation json object.
            jsonObject.add("location", playerLocationJsonObject);
            jsonObject.addProperty("ping", violation.getPing());

            // Adding the server tps to the violation json object.
            jsonObject.addProperty("tps", violation.getTps());

            // Adding the timestamp the violation occurred to the violation json object.
            jsonObject.addProperty("timestamp", violation.getTimestamp());
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }
}