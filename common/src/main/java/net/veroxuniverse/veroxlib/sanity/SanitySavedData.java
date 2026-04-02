package net.veroxuniverse.veroxlib.sanity;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SanitySavedData extends SavedData {
    private final Map<UUID, SanityData> playerSanity = new HashMap<>();
    private final Map<UUID, Boolean> cultistPlayers = new HashMap<>();

    public static final Factory<SanitySavedData> FACTORY = new Factory<>(
            SanitySavedData::new,
            SanitySavedData::load,
            null
    );

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider provider) {
        ListTag sanityList = new ListTag();
        playerSanity.forEach((uuid, data) -> {
            CompoundTag entry = new CompoundTag();
            entry.putUUID("uuid", uuid);
            entry.putFloat("value", data.value());
            sanityList.add(entry);
        });
        tag.put("player_sanity", sanityList);

        ListTag cultistList = new ListTag();
        cultistPlayers.forEach((uuid, isCultist) -> {
            if (isCultist) {
                CompoundTag entry = new CompoundTag();
                entry.putUUID("uuid", uuid);
                cultistList.add(entry);
            }
        });
        tag.put("cultists", cultistList);
        return tag;
    }

    public static SanitySavedData load(CompoundTag tag, HolderLookup.Provider provider) {
        SanitySavedData data = new SanitySavedData();
        ListTag sanityList = tag.getList("player_sanity", 10);
        for (int i = 0; i < sanityList.size(); i++) {
            CompoundTag entry = sanityList.getCompound(i);
            data.playerSanity.put(entry.getUUID("uuid"), new SanityData(entry.getFloat("value")));
        }
        ListTag cultistList = tag.getList("cultists", 10);
        for (int i = 0; i < cultistList.size(); i++) {
            data.cultistPlayers.put(cultistList.getCompound(i).getUUID("uuid"), true);
        }
        return data;
    }

    public static SanitySavedData get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(FACTORY, "what_lurks_between_sanity");
    }

    public Map<UUID, SanityData> getSanityMap() { return playerSanity; }
    public Map<UUID, Boolean> getCultistMap() { return cultistPlayers; }
}