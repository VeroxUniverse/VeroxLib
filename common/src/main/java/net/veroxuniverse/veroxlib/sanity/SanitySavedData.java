package net.veroxuniverse.veroxlib.sanity;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SanitySavedData extends SavedData {

    private static final String KEY_PLAYER_SANITY = "player_sanity";
    private static final String KEY_CULTISTS = "cultists";
    private static final String KEY_UUID = "uuid";
    private static final String KEY_VALUE = "value";
    private static final int NBT_COMPOUND_TYPE = 10;

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
            entry.putUUID(KEY_UUID, uuid);
            entry.putFloat(KEY_VALUE, data.value());
            sanityList.add(entry);
        });
        tag.put(KEY_PLAYER_SANITY, sanityList);

        ListTag cultistList = new ListTag();
        cultistPlayers.forEach((uuid, isCultist) -> {
            if (isCultist) {
                CompoundTag entry = new CompoundTag();
                entry.putUUID(KEY_UUID, uuid);
                cultistList.add(entry);
            }
        });
        tag.put(KEY_CULTISTS, cultistList);
        return tag;
    }

    public static SanitySavedData load(CompoundTag tag, HolderLookup.Provider provider) {
        SanitySavedData data = new SanitySavedData();
        ListTag sanityList = tag.getList(KEY_PLAYER_SANITY, NBT_COMPOUND_TYPE);
        for (int i = 0; i < sanityList.size(); i++) {
            CompoundTag entry = sanityList.getCompound(i);
            data.playerSanity.put(entry.getUUID(KEY_UUID), new SanityData(entry.getFloat(KEY_VALUE)));
        }
        ListTag cultistList = tag.getList(KEY_CULTISTS, NBT_COMPOUND_TYPE);
        for (int i = 0; i < cultistList.size(); i++) {
            data.cultistPlayers.put(cultistList.getCompound(i).getUUID(KEY_UUID), true);
        }
        return data;
    }

    public static SanitySavedData get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(FACTORY, "what_lurks_between_sanity");
    }

    public float getSanity(UUID uuid) {
        return playerSanity.getOrDefault(uuid, new SanityData(100f)).value();
    }

    public void setSanity(UUID uuid, float value) {
        playerSanity.put(uuid, new SanityData(Math.max(0, Math.min(100, value))));
        setDirty();
    }

    public void modifySanity(UUID uuid, float amount) {
        SanityData current = playerSanity.getOrDefault(uuid, new SanityData(100f));
        playerSanity.put(uuid, current.add(amount));
        setDirty();
    }

    public boolean isCultist(UUID uuid) {
        return cultistPlayers.getOrDefault(uuid, false);
    }

    public void setCultist(UUID uuid, boolean value) {
        cultistPlayers.put(uuid, value);
        setDirty();
    }

    public Map<UUID, SanityData> getSanityMapReadOnly() {
        return Collections.unmodifiableMap(playerSanity);
    }
}