package ambos.simpletech;

import net.minecraft.core.block.Block;
import net.minecraft.core.data.DataLoader;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.data.registry.recipe.RecipeNamespace;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.item.ItemStack;
import turniplabs.halplibe.util.RecipeEntrypoint;

public class SimpleTechRecipes implements RecipeEntrypoint {
    public static final RecipeNamespace SIMPLE_TECH = new RecipeNamespace();
    public static final RecipeGroup<RecipeEntryCrafting<?, ?>> WORKBENCH = new RecipeGroup<>(
            new RecipeSymbol(new ItemStack(Block.workbench)));

    @Override
    public void onRecipesReady() {
        SIMPLE_TECH.register("workbench", WORKBENCH);
        Registries.RECIPES.register("simpletech", SIMPLE_TECH);
        DataLoader.loadRecipes("/assets/simpletech/recipes/workbench.json");
    }
}
