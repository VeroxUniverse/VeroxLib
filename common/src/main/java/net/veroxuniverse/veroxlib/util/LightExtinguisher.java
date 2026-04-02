package net.veroxuniverse.veroxlib.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.veroxuniverse.veroxlib.registry.ModBlocks;
import net.veroxuniverse.veroxlib.registry.ModTags;

public class LightExtinguisher {

    public static void extinguishAroundPlayer (Player player, int radius) {
        if (!(player.level() instanceof ServerLevel level)) return;

        BlockPos center = player.blockPosition();

        BlockPos.betweenClosedStream(center.offset(-radius, -2, -radius), center.offset(radius, 2, radius))
                .forEach(pos -> {
                    BlockState state = level.getBlockState(pos);

                    if (state.is(ModTags.LIGHT_SOURCES)) {
                        processBlock(level, pos, state);
                    }
                });

    }

    private static void processBlock(ServerLevel level, BlockPos pos, BlockState state) {
        if (state.is(Blocks.TORCH)) {
            level.setBlock(pos, ModBlocks.UNLIT_TORCH.get().defaultBlockState(), 3);
            playEffects(level, pos);
            return;
        }

        if (state.is(Blocks.WALL_TORCH)) {
            Direction facing = state.getValue(WallTorchBlock.FACING);
            level.setBlock(pos, ModBlocks.UNLIT_WALL_TORCH.get().defaultBlockState().setValue(WallTorchBlock.FACING, facing), 3);
            playEffects(level, pos);
            return;
        }

        if (state.getBlock() instanceof LanternBlock || state.is(Blocks.LANTERN)) {
            boolean isHanging = state.getValue(LanternBlock.HANGING);
            boolean isWaterlogged = state.getValue(LanternBlock.WATERLOGGED);

            BlockState unlitState = ModBlocks.UNLIT_LANTERN.get().defaultBlockState()
                    .setValue(LanternBlock.HANGING, isHanging)
                    .setValue(LanternBlock.WATERLOGGED, isWaterlogged);

            level.setBlock(pos, unlitState, 3);
            playEffects(level, pos);
            return;
        }

        if (state.hasProperty(BlockStateProperties.LIT)) {
            if (state.getValue(BlockStateProperties.LIT)) {
                level.setBlock(pos, state.setValue(BlockStateProperties.LIT, false), 3);
                playEffects(level, pos);
            }
            return;
        }

        if (state.is(ModTags.LIGHT_SOURCES)) {
            level.destroyBlock(pos, true);
            playEffects(level, pos);
        }
    }

    private static void playEffects(Level level, BlockPos pos) {
        level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5f, 1.2f);
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.SMOKE,
                    pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    5, 0.02, 0.05, 0.02, 0.02);
        }
    }
}
