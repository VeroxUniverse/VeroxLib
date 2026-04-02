package net.veroxuniverse.veroxlib.neoforge.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.veroxuniverse.veroxlib.VeroxLib;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, VeroxLib.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem("unlit_lantern");

        simpleBlockItem("unlit_torch", "block/unlit_torch");
    }

    private void simpleItem(String name) {
        withExistingParent(name, mcLoc("item/generated"))
                .texture("layer0", modLoc("item/" + name));
    }

    private void simpleBlockItem(String name, String texturePath) {
        withExistingParent(name, mcLoc("item/generated"))
                .texture("layer0", modLoc(texturePath));
    }
}