package com.mrbysco.slabmachines.datagen.data;

import com.mrbysco.slabmachines.SlabReference;
import com.mrbysco.slabmachines.init.SlabRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;

import java.util.concurrent.CompletableFuture;

public class SlabRecipeProvider extends RecipeProvider {

	public SlabRecipeProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(packOutput, lookupProvider);
	}

	@Override
	protected void buildRecipes(RecipeOutput recipeOutput) {
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blocks.CHEST)
				.pattern("S")
				.pattern("S")
				.define('S', SlabRegistry.CHEST_SLAB.get())
				.unlockedBy("has_slab_chest", has(SlabRegistry.CHEST_SLAB.get()))
				.save(recipeOutput, new ResourceLocation(SlabReference.MOD_ID, "chest_from_slab"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SlabRegistry.CHEST_SLAB.get())
				.pattern("SSS")
				.pattern("S S")
				.pattern("SSS")
				.define('S', ItemTags.WOODEN_SLABS)
				.unlockedBy("has_wooden_slab", has(ItemTags.WOODEN_SLABS))
				.save(recipeOutput);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SlabRegistry.CHEST_SLAB.get(), 2)
				.requires(Items.CHEST)
				.unlockedBy("has_chest", has(Items.CHEST))
				.save(recipeOutput, new ResourceLocation(SlabReference.MOD_ID, "slab_from_chest"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blocks.FURNACE)
				.pattern("S")
				.pattern("S")
				.define('S', SlabRegistry.FURNACE_SLAB.get())
				.unlockedBy("has_slab_furnace", has(SlabRegistry.FURNACE_SLAB.get()))
				.save(recipeOutput, new ResourceLocation(SlabReference.MOD_ID, "furnace_from_slab"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SlabRegistry.FURNACE_SLAB.get())
				.pattern("SSS")
				.pattern("S S")
				.pattern("SSS")
				.define('S', SlabReference.COBBLESTONE_SLABS)
				.unlockedBy("has_cobblestone_slab", has(SlabReference.COBBLESTONE_SLABS))
				.save(recipeOutput, new ResourceLocation(SlabReference.MOD_ID, "slab_from_furnace"));

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SlabRegistry.FURNACE_SLAB.get(), 2)
				.requires(Items.FURNACE)
				.unlockedBy("has_furnace", has(Items.FURNACE))
				.save(recipeOutput);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SlabRegistry.NOTE_SLAB.get(), 2)
				.requires(Items.NOTE_BLOCK)
				.unlockedBy("has_note_block", has(Items.NOTE_BLOCK))
				.save(recipeOutput);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blocks.NOTE_BLOCK)
				.pattern("S")
				.pattern("S")
				.define('S', SlabRegistry.NOTE_SLAB.get())
				.unlockedBy("has_note_slab", has(SlabRegistry.NOTE_SLAB.get()))
				.save(recipeOutput, new ResourceLocation(SlabReference.MOD_ID, "note_block_from_slab"));


		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SlabRegistry.TNT_SLAB.get(), 6)
				.pattern("SSS")
				.define('S', Items.TNT)
				.unlockedBy("has_tnt", has(Items.TNT))
				.save(recipeOutput);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blocks.TRAPPED_CHEST)
				.pattern("S")
				.pattern("S")
				.define('S', SlabRegistry.TRAPPED_CHEST_SLAB.get())
				.unlockedBy("has_trapped_slab_chest", has(SlabRegistry.TRAPPED_CHEST_SLAB.get()))
				.save(recipeOutput, new ResourceLocation(SlabReference.MOD_ID, "trapped_chest_from_slab"));

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SlabRegistry.TRAPPED_CHEST_SLAB.get(), 2)
				.requires(Items.TRAPPED_CHEST)
				.unlockedBy("has_trapped_chest", has(Items.TRAPPED_CHEST))
				.save(recipeOutput);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blocks.CRAFTING_TABLE)
				.pattern("S")
				.pattern("S")
				.define('S', SlabRegistry.CRAFTING_TABLE_SLAB.get())
				.unlockedBy("has_slab_crafting_table", has(SlabRegistry.CRAFTING_TABLE_SLAB.get()))
				.save(recipeOutput, new ResourceLocation(SlabReference.MOD_ID, "crafting_table_from_slab"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SlabRegistry.CRAFTING_TABLE_SLAB.get())
				.pattern("SS")
				.pattern("SS")
				.define('S', ItemTags.WOODEN_SLABS)
				.unlockedBy("has_wooden_slab", has(ItemTags.WOODEN_SLABS))
				.save(recipeOutput);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SlabRegistry.CRAFTING_TABLE_SLAB.get(), 2)
				.requires(Items.CRAFTING_TABLE)
				.unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
				.save(recipeOutput, new ResourceLocation(SlabReference.MOD_ID, "slab_from_crafting_table"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SlabRegistry.SMOKER_SLAB.get())
				.pattern(" S ")
				.pattern("SFS")
				.pattern(" S ")
				.define('S', ItemTags.PLANKS)
				.define('F', SlabRegistry.FURNACE_SLAB.get())
				.unlockedBy("has_cobblestone_slab", has(SlabReference.COBBLESTONE_SLABS))
				.save(recipeOutput);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SlabRegistry.SMOKER_SLAB.get(), 2)
				.requires(Items.SMOKER)
				.unlockedBy("has_smoker", has(Items.SMOKER))
				.save(recipeOutput, new ResourceLocation(SlabReference.MOD_ID, "slab_from_smoker"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SlabRegistry.BLAST_FURNACE_SLAB.get())
				.pattern("NNN")
				.pattern("NFN")
				.pattern("SSS")
				.define('S', Blocks.SMOOTH_STONE_SLAB)
				.define('N', Tags.Items.NUGGETS_IRON)
				.define('F', SlabRegistry.FURNACE_SLAB.get())
				.unlockedBy("has_cobblestone_slab", has(SlabReference.COBBLESTONE_SLABS))
				.save(recipeOutput);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SlabRegistry.BLAST_FURNACE_SLAB.get(), 2)
				.requires(Items.BLAST_FURNACE)
				.unlockedBy("has_blast_furnace", has(Items.BLAST_FURNACE))
				.save(recipeOutput, new ResourceLocation(SlabReference.MOD_ID, "slab_from_blast_furnace"));
	}
}
