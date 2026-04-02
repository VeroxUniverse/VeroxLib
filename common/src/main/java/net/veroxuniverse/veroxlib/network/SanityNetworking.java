package net.veroxuniverse.veroxlib.network;

import dev.architectury.networking.NetworkManager;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.veroxuniverse.veroxlib.VeroxLib;
import net.veroxuniverse.veroxlib.api.SanityAPI;

public class SanityNetworking {
    public static final ResourceLocation SANITY_PACKET_ID = ResourceLocation.fromNamespaceAndPath(VeroxLib.MOD_ID, "sanity_sync");
    public static final CustomPacketPayload.Type<SanitySyncPayload> TYPE = new CustomPacketPayload.Type<>(SANITY_PACKET_ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, SanitySyncPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, SanitySyncPayload::value,
            ByteBufCodecs.BOOL, SanitySyncPayload::isCultist,
            SanitySyncPayload::new
    );

    public record SanitySyncPayload(float value, boolean isCultist) implements CustomPacketPayload {
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    public static void register() {
        EnvExecutor.runInEnv(Env.SERVER, () -> () -> {
            NetworkManager.registerS2CPayloadType(TYPE, CODEC);
        });
    }

    public static void syncToClient(Player player, float value) {
        if (player instanceof ServerPlayer serverPlayer) {
            boolean cultist = SanityAPI.isCultist(player);
            NetworkManager.sendToPlayer(serverPlayer, new SanitySyncPayload(value, cultist));
        }
    }
}