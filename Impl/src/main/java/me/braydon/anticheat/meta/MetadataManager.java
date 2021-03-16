package me.braydon.anticheat.meta;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import lombok.NonNull;
import me.braydon.anticheat.meta.impl.PlayerMetadata;
import me.braydon.anticheat.meta.impl.ServerMetadata;
import me.braydon.anticheat.meta.impl.ViolationMetadata;
import me.braydon.anticheat.player.PlayerData;

/**
 * @author Braydon
 */
public class MetadataManager {
    /**
     * The array of metadata elements to use when creating metadata for player data
     *
     * @see IMetadata
     * @see PlayerData
     */
    private static final IMetadata[] METADATA_ELEMENTS = new IMetadata[] {
            new PlayerMetadata(),
            new ViolationMetadata(),
            new ServerMetadata()
    };
    private static final Gson GSON = new GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .create();

    /**
     * Create the metadata json for the provided player data.
     *
     * @param playerData the player data to create the metadata json for
     * @return the metadata json
     * @see IMetadata
     * @see PlayerData
     */
    public static String getMetadataJson(@NonNull PlayerData playerData) {
        JsonObject jsonElement = new JsonObject();
        for (IMetadata metadataElement : METADATA_ELEMENTS)
            jsonElement.add(metadataElement.getName(), metadataElement.getJsonElement(playerData));
        return GSON.toJson(jsonElement);
    }
}