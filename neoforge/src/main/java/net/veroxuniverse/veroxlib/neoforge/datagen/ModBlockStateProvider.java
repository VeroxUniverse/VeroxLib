package net.veroxuniverse.veroxlib.neoforge.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.WallTorchBlock;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.veroxuniverse.veroxlib.VeroxLib;
import net.veroxuniverse.veroxlib.registry.ModBlocks;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, VeroxLib.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        ModelFile torch = models().getBuilder("unlit_torch")
                .parent(new ModelFile.UncheckedModelFile(mcLoc("block/torch")))
                .texture("torch", modLoc("block/unlit_torch"));
        simpleBlock(ModBlocks.UNLIT_TORCH.get(), torch);

        ModelFile wallTorch = models().getBuilder("unlit_wall_torch")
                .parent(new ModelFile.UncheckedModelFile(mcLoc("block/wall_torch")))
                .texture("torch", modLoc("block/unlit_torch"));

        getVariantBuilder(ModBlocks.UNLIT_WALL_TORCH.get()).forAllStates(state ->
                ConfiguredModel.builder()
                        .modelFile(wallTorch)
                        .rotationY(((int) state.getValue(WallTorchBlock.FACING).toYRot() + 90) % 360)
                        .build()
        );

        ModelFile lantern = models().getBuilder("unlit_lantern")
                .parent(new ModelFile.UncheckedModelFile(mcLoc("block/template_lantern")))
                .texture("lantern", modLoc("block/unlit_lantern"))
                .texture("particle", modLoc("block/unlit_lantern"));

        ModelFile hangingLantern = models().getBuilder("unlit_hanging_lantern")
                .parent(new ModelFile.UncheckedModelFile(mcLoc("block/template_hanging_lantern")))
                .texture("lantern", modLoc("block/unlit_lantern"))
                .texture("particle", modLoc("block/unlit_lantern"));

        getVariantBuilder(ModBlocks.UNLIT_LANTERN.get()).forAllStates(state -> {
            boolean hanging = state.getValue(LanternBlock.HANGING);
            return ConfiguredModel.builder()
                    .modelFile(hanging ? hangingLantern : lantern)
                    .build();
        });
    }
}
