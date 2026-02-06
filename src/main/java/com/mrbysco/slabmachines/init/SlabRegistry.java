package com.mrbysco.slabmachines.init;

import com.mrbysco.slabmachines.SlabReference;
import com.mrbysco.slabmachines.blockentity.ChestSlabBlockEntity;
import com.mrbysco.slabmachines.blockentity.furnace.BlastFurnaceSlabBlockEntity;
import com.mrbysco.slabmachines.blockentity.furnace.FurnaceSlabBlockEntity;
import com.mrbysco.slabmachines.blockentity.furnace.SmokerSlabBlockEntity;
import com.mrbysco.slabmachines.blocks.BlastFurnaceSlabBlock;
import com.mrbysco.slabmachines.blocks.CartographyTableSlabBlock;
import com.mrbysco.slabmachines.blocks.ChestSlabBlock;
import com.mrbysco.slabmachines.blocks.CraftingTableSlabBlock;
import com.mrbysco.slabmachines.blocks.FurnaceSlabBlock;
import com.mrbysco.slabmachines.blocks.LoomSlabBlock;
import com.mrbysco.slabmachines.blocks.NoteBlockSlab;
import com.mrbysco.slabmachines.blocks.SmokerSlabBlock;
import com.mrbysco.slabmachines.blocks.TNTSlabBlock;
import com.mrbysco.slabmachines.blocks.TrappedChestSlabBlock;
import com.mrbysco.slabmachines.entity.TNTSlabEntity;
import com.mrbysco.slabmachines.menu.SlabBenchMenu;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.GameData;
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper;
import net.neoforged.neoforge.transfer.item.WorldlyContainerWrapper;

import java.util.List;
import java.util.function.Supplier;

public class SlabRegistry {
	public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(SlabReference.MOD_ID);
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(SlabReference.MOD_ID);
	public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SlabReference.MOD_ID);
	public static final DeferredRegister.Entities ENTITY_TYPES = DeferredRegister.createEntities(SlabReference.MOD_ID);
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, SlabReference.MOD_ID);
	public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU, SlabReference.MOD_ID);

	public static final Supplier<MenuType<SlabBenchMenu>> SLAB_WORKBENCH_CONTAINER = MENU_TYPES.register("slab_workbench", () -> IMenuTypeExtension.create((windowId, inv, data) -> new SlabBenchMenu(windowId, inv)));

	public static final DeferredBlock<CraftingTableSlabBlock> CRAFTING_TABLE_SLAB = BLOCKS.registerBlock("crafting_table_slab", CraftingTableSlabBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.CRAFTING_TABLE));
	public static final DeferredBlock<CartographyTableSlabBlock> CARTOGRAPHY_TABLE_SLAB = BLOCKS.registerBlock("cartography_table_slab", CartographyTableSlabBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.CARTOGRAPHY_TABLE));
	public static final DeferredBlock<LoomSlabBlock> LOOM_SLAB = BLOCKS.registerBlock("loom_slab", LoomSlabBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.LOOM));
	public static final DeferredBlock<FurnaceSlabBlock> FURNACE_SLAB = BLOCKS.registerBlock("furnace_slab", FurnaceSlabBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.FURNACE).lightLevel((state) ->
			state.getValue(BlockStateProperties.LIT) ? 7 : 0));
	public static final DeferredBlock<BlastFurnaceSlabBlock> BLAST_FURNACE_SLAB = BLOCKS.registerBlock("blast_furnace_slab", BlastFurnaceSlabBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.FURNACE).lightLevel((state) ->
			state.getValue(BlockStateProperties.LIT) ? 7 : 0));
	public static final DeferredBlock<SmokerSlabBlock> SMOKER_SLAB = BLOCKS.registerBlock("smoker_slab", SmokerSlabBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.FURNACE).lightLevel((state) ->
			state.getValue(BlockStateProperties.LIT) ? 7 : 0));
	public static final DeferredBlock<ChestSlabBlock> CHEST_SLAB = BLOCKS.registerBlock("chest_slab", ChestSlabBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.CHEST));
	public static final DeferredBlock<TrappedChestSlabBlock> TRAPPED_CHEST_SLAB = BLOCKS.registerBlock("trapped_chest_slab", TrappedChestSlabBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.TRAPPED_CHEST));
	public static final DeferredBlock<NoteBlockSlab> NOTE_SLAB = BLOCKS.registerBlock("note_slab", NoteBlockSlab::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.NOTE_BLOCK));
	public static final DeferredBlock<TNTSlabBlock> TNT_SLAB = BLOCKS.registerBlock("tnt_slab", TNTSlabBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.TNT));

	public static final DeferredItem<BlockItem> CRAFTING_TABLE_SLAB_ITEM = ITEMS.registerSimpleBlockItem(CRAFTING_TABLE_SLAB);
	public static final DeferredItem<BlockItem> CARTOGRAPHY_TABLE_SLAB_ITEM = ITEMS.registerSimpleBlockItem(CARTOGRAPHY_TABLE_SLAB);
	public static final DeferredItem<BlockItem> LOOM_SLAB_ITEM = ITEMS.registerSimpleBlockItem(LOOM_SLAB);
	public static final DeferredItem<BlockItem> FURNACE_SLAB_ITEM = ITEMS.registerSimpleBlockItem(FURNACE_SLAB);
	public static final DeferredItem<BlockItem> BLAST_FURNACE_SLAB_ITEM = ITEMS.registerSimpleBlockItem(BLAST_FURNACE_SLAB);
	public static final DeferredItem<BlockItem> SMOKER_SLAB_ITEM = ITEMS.registerSimpleBlockItem(SMOKER_SLAB);
	public static final DeferredItem<BlockItem> CHEST_SLAB_ITEM = ITEMS.registerSimpleBlockItem(CHEST_SLAB);
	public static final DeferredItem<BlockItem> TRAPPED_CHEST_SLAB_ITEM = ITEMS.registerSimpleBlockItem(TRAPPED_CHEST_SLAB);
	public static final DeferredItem<BlockItem> NOTE_SLAB_ITEM = ITEMS.registerSimpleBlockItem(NOTE_SLAB);
	public static final DeferredItem<BlockItem> TNT_SLAB_ITEM = ITEMS.registerSimpleBlockItem(TNT_SLAB);

	public static final Supplier<CreativeModeTab> SLAB_TAB = CREATIVE_MODE_TABS.register("tab", () -> CreativeModeTab.builder()
			.icon(() -> SlabRegistry.CRAFTING_TABLE_SLAB.asItem().getDefaultInstance())
			.withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
			.title(Component.translatable("itemGroup.slabmachines.tab"))
			.displayItems((displayParameters, output) -> {
				List<ItemStack> stacks = SlabRegistry.ITEMS.getEntries().stream()
						.map(reg -> new ItemStack(reg.get())).toList();
				output.acceptAll(stacks);
			}).build());

	public static final Supplier<BlockEntityType<FurnaceSlabBlockEntity>> FURNACE_SLAB_BE = BLOCK_ENTITY_TYPES.register("furnace_slab", () -> new BlockEntityType<>(FurnaceSlabBlockEntity::new, FURNACE_SLAB.get()));
	public static final Supplier<BlockEntityType<BlastFurnaceSlabBlockEntity>> BLAST_FURNACE_SLAB_BE = BLOCK_ENTITY_TYPES.register("blast_furnace_slab", () -> new BlockEntityType<>(BlastFurnaceSlabBlockEntity::new, BLAST_FURNACE_SLAB.get()));
	public static final Supplier<BlockEntityType<SmokerSlabBlockEntity>> SMOKER_SLAB_BE = BLOCK_ENTITY_TYPES.register("smoker_slab", () -> new BlockEntityType<>(SmokerSlabBlockEntity::new, SMOKER_SLAB.get()));
	public static final Supplier<BlockEntityType<ChestSlabBlockEntity>> CHEST_SLAB_BE = BLOCK_ENTITY_TYPES.register("chest_slab", () -> new BlockEntityType<>(ChestSlabBlockEntity::new, CHEST_SLAB.get(), TRAPPED_CHEST_SLAB.get()));

	public static final DeferredHolder<EntityType<?>, EntityType<TNTSlabEntity>> TNT_SLAB_ENTITY = ENTITY_TYPES.registerEntityType("tnt_slab",
			TNTSlabEntity::new,
			MobCategory.MISC,
			builder -> builder
					.sized(1.0F, 0.5F).clientTrackingRange(10).updateInterval(10)
	);

	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
		var sidedContainers = List.of(
				BLAST_FURNACE_SLAB_BE.get(),
				FURNACE_SLAB_BE.get(),
				SMOKER_SLAB_BE.get()
		);
		for (var type : sidedContainers) {
			event.registerBlockEntity(Capabilities.Item.BLOCK, type, WorldlyContainerWrapper::new);
		}
		event.registerBlockEntity(Capabilities.Item.BLOCK, CHEST_SLAB_BE.get(), (container, side) -> VanillaContainerWrapper.of(container));
	}

	public static void registerPointOfInterests() {
		registerAllStatesToPointOfInterest(PoiTypes.ARMORER, BLAST_FURNACE_SLAB);
		registerAllStatesToPointOfInterest(PoiTypes.BUTCHER, SMOKER_SLAB);
		registerAllStatesToPointOfInterest(PoiTypes.CARTOGRAPHER, CARTOGRAPHY_TABLE_SLAB);
		registerAllStatesToPointOfInterest(PoiTypes.SHEPHERD, SMOKER_SLAB);
	}

	@SuppressWarnings("UnstableApiUsage")
	@SafeVarargs
	private static void registerAllStatesToPointOfInterest(ResourceKey<PoiType> poi, Supplier<? extends Block>... blocks) {
		Holder<PoiType> poiHolder = BuiltInRegistries.POINT_OF_INTEREST_TYPE.getOrThrow(poi);
		for (var block : blocks) {
			block.get().getStateDefinition().getPossibleStates().forEach(
					state -> GameData.getBlockStatePointOfInterestTypeMap().put(state, poiHolder)
			);
		}
	}
}
