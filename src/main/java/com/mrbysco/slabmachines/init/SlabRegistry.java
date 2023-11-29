package com.mrbysco.slabmachines.init;

import com.mrbysco.slabmachines.SlabReference;
import com.mrbysco.slabmachines.blockentity.ChestSlabBlockEntity;
import com.mrbysco.slabmachines.blockentity.furnace.BlastFurnaceSlabBlockEntity;
import com.mrbysco.slabmachines.blockentity.furnace.FurnaceSlabBlockEntity;
import com.mrbysco.slabmachines.blockentity.furnace.SmokerSlabBlockEntity;
import com.mrbysco.slabmachines.blocks.BlastFurnaceSlabBlock;
import com.mrbysco.slabmachines.blocks.ChestSlabBlock;
import com.mrbysco.slabmachines.blocks.CraftingTableSlabBlock;
import com.mrbysco.slabmachines.blocks.FurnaceSlabBlock;
import com.mrbysco.slabmachines.blocks.NoteBlockSlab;
import com.mrbysco.slabmachines.blocks.SmokerSlabBlock;
import com.mrbysco.slabmachines.blocks.TNTSlabBlock;
import com.mrbysco.slabmachines.blocks.TrappedChestSlabBlock;
import com.mrbysco.slabmachines.menu.SlabBenchMenu;
import com.mrbysco.slabmachines.entity.TNTSlabEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.Supplier;

public class SlabRegistry {
	public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(SlabReference.MOD_ID);
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(SlabReference.MOD_ID);
	public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SlabReference.MOD_ID);
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, SlabReference.MOD_ID);
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, SlabReference.MOD_ID);
	public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU, SlabReference.MOD_ID);

	public static final Supplier<MenuType<SlabBenchMenu>> SLAB_WORKBENCH_CONTAINER = MENU_TYPES.register("slab_workbench", () -> IMenuTypeExtension.create((windowId, inv, data) -> new SlabBenchMenu(windowId, inv)));

	public static final DeferredBlock<CraftingTableSlabBlock> CRAFTING_TABLE_SLAB = BLOCKS.register("crafting_table_slab", () -> new CraftingTableSlabBlock(BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE)));
	public static final DeferredBlock<FurnaceSlabBlock> FURNACE_SLAB = BLOCKS.register("furnace_slab", () -> new FurnaceSlabBlock(BlockBehaviour.Properties.copy(Blocks.FURNACE).lightLevel((state) ->
			state.getValue(BlockStateProperties.LIT) ? 7 : 0)));
	public static final DeferredBlock<BlastFurnaceSlabBlock> BLAST_FURNACE_SLAB = BLOCKS.register("blast_furnace_slab", () -> new BlastFurnaceSlabBlock(BlockBehaviour.Properties.copy(Blocks.FURNACE).lightLevel((state) ->
			state.getValue(BlockStateProperties.LIT) ? 7 : 0)));
	public static final DeferredBlock<SmokerSlabBlock> SMOKER_SLAB = BLOCKS.register("smoker_slab", () -> new SmokerSlabBlock(BlockBehaviour.Properties.copy(Blocks.FURNACE).lightLevel((state) ->
			state.getValue(BlockStateProperties.LIT) ? 7 : 0)));
	public static final DeferredBlock<ChestSlabBlock> CHEST_SLAB = BLOCKS.register("chest_slab", () -> new ChestSlabBlock(BlockBehaviour.Properties.copy(Blocks.CHEST)));
	public static final DeferredBlock<TrappedChestSlabBlock> TRAPPED_CHEST_SLAB = BLOCKS.register("trapped_chest_slab", () -> new TrappedChestSlabBlock(BlockBehaviour.Properties.copy(Blocks.TRAPPED_CHEST)));
	public static final DeferredBlock<NoteBlockSlab> NOTE_SLAB = BLOCKS.register("note_slab", () -> new NoteBlockSlab(BlockBehaviour.Properties.copy(Blocks.NOTE_BLOCK)));
	public static final DeferredBlock<TNTSlabBlock> TNT_SLAB = BLOCKS.register("tnt_slab", () -> new TNTSlabBlock(BlockBehaviour.Properties.copy(Blocks.TNT)));

	public static final DeferredItem<BlockItem> CRAFTING_TABLE_SLAB_ITEM = ITEMS.registerSimpleBlockItem(CRAFTING_TABLE_SLAB);
	public static final DeferredItem<BlockItem> FURNACE_SLAB_ITEM = ITEMS.registerSimpleBlockItem(FURNACE_SLAB);
	public static final DeferredItem<BlockItem> BLAST_FURNACE_SLAB_ITEM = ITEMS.registerSimpleBlockItem(BLAST_FURNACE_SLAB);
	public static final DeferredItem<BlockItem> SMOKER_SLAB_ITEM = ITEMS.registerSimpleBlockItem(SMOKER_SLAB);
	public static final DeferredItem<BlockItem> CHEST_SLAB_ITEM = ITEMS.registerSimpleBlockItem(CHEST_SLAB);
	public static final DeferredItem<BlockItem> TRAPPED_CHEST_SLAB_ITEM = ITEMS.registerSimpleBlockItem(TRAPPED_CHEST_SLAB);
	public static final DeferredItem<BlockItem> NOTE_SLAB_ITEM = ITEMS.registerSimpleBlockItem(NOTE_SLAB);
	public static final DeferredItem<BlockItem> TNT_SLAB_ITEM = ITEMS.registerSimpleBlockItem(TNT_SLAB);

	public static final Supplier<CreativeModeTab> SLAB_TAB = CREATIVE_MODE_TABS.register("tab", () -> CreativeModeTab.builder()
			.icon(() -> SlabRegistry.CRAFTING_TABLE_SLAB_ITEM.get().getDefaultInstance())
			.withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
			.title(Component.translatable("itemGroup.slabmachines.tab"))
			.displayItems((displayParameters, output) -> {
				List<ItemStack> stacks = SlabRegistry.ITEMS.getEntries().stream()
						.map(reg -> new ItemStack(reg.get())).toList();
				output.acceptAll(stacks);
			}).build());

	public static final Supplier<BlockEntityType<FurnaceSlabBlockEntity>> FURNACE_SLAB_BE = BLOCK_ENTITY_TYPES.register("furnace_slab", () -> BlockEntityType.Builder.of(FurnaceSlabBlockEntity::new, FURNACE_SLAB.get()).build(null));
	public static final Supplier<BlockEntityType<BlastFurnaceSlabBlockEntity>> BLAST_FURNACE_SLAB_BE = BLOCK_ENTITY_TYPES.register("blast_furnace_slab", () -> BlockEntityType.Builder.of(BlastFurnaceSlabBlockEntity::new, BLAST_FURNACE_SLAB.get()).build(null));
	public static final Supplier<BlockEntityType<SmokerSlabBlockEntity>> SMOKER_SLAB_BE = BLOCK_ENTITY_TYPES.register("smoker_slab", () -> BlockEntityType.Builder.of(SmokerSlabBlockEntity::new, SMOKER_SLAB.get()).build(null));
	public static final Supplier<BlockEntityType<ChestSlabBlockEntity>> CHEST_SLAB_BE = BLOCK_ENTITY_TYPES.register("chest_slab", () -> BlockEntityType.Builder.of(ChestSlabBlockEntity::new, CHEST_SLAB.get(), TRAPPED_CHEST_SLAB.get()).build(null));

	public static final Supplier<EntityType<TNTSlabEntity>> TNT_SLAB_ENTITY = ENTITY_TYPES.register("tnt_slab", () -> EntityType.Builder.<TNTSlabEntity>of(TNTSlabEntity::new, MobCategory.MISC)
			.sized(1.0F, 0.5F).clientTrackingRange(10).updateInterval(10).setCustomClientFactory(TNTSlabEntity::new).build("tnt_slab"));

}
