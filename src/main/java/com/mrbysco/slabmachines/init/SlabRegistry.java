package com.mrbysco.slabmachines.init;

import com.mrbysco.slabmachines.SlabReference;
import com.mrbysco.slabmachines.blocks.ChestSlabBlock;
import com.mrbysco.slabmachines.blocks.CraftingTableSlabBlock;
import com.mrbysco.slabmachines.blocks.FurnaceSlabBlock;
import com.mrbysco.slabmachines.blocks.NoteBlockSlab;
import com.mrbysco.slabmachines.blocks.TNTSlabBlock;
import com.mrbysco.slabmachines.blocks.TrappedChestSlabBlock;
import com.mrbysco.slabmachines.container.SlabBenchContainer;
import com.mrbysco.slabmachines.entity.TNTSlabEntity;
import com.mrbysco.slabmachines.tileentity.TileChestSlab;
import com.mrbysco.slabmachines.tileentity.furnace.TileFurnaceSlab;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SlabRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SlabReference.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SlabReference.MOD_ID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, SlabReference.MOD_ID);
    public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, SlabReference.MOD_ID);
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, SlabReference.MOD_ID);

    public static final RegistryObject<ContainerType<SlabBenchContainer>> SLAB_WORKBENCH_CONTAINER = CONTAINERS.register("slab_workbench", () -> IForgeContainerType.create((windowId, inv, data) -> new SlabBenchContainer(windowId, inv)));

    public static final RegistryObject<Block> CRAFTING_TABLE_SLAB = BLOCKS.register("crafting_table_slab", () -> new CraftingTableSlabBlock(AbstractBlock.Properties.of(Material.WOOD)));
    public static final RegistryObject<Block> FURNACE_SLAB = BLOCKS.register("furnace_slab", () -> new FurnaceSlabBlock(AbstractBlock.Properties.of(Material.STONE)));
    public static final RegistryObject<Block> CHEST_SLAB = BLOCKS.register("chest_slab", () -> new ChestSlabBlock(AbstractBlock.Properties.of(Material.WOOD)));
    public static final RegistryObject<Block> TRAPPED_CHEST_SLAB = BLOCKS.register("trapped_chest_slab", () -> new TrappedChestSlabBlock(AbstractBlock.Properties.of(Material.WOOD)));
    public static final RegistryObject<Block> NOTE_SLAB = BLOCKS.register("note_slab", () -> new NoteBlockSlab(AbstractBlock.Properties.of(Material.WOOD)));
    public static final RegistryObject<Block> TNT_SLAB = BLOCKS.register("tnt_slab", () -> new TNTSlabBlock(AbstractBlock.Properties.of(Material.EXPLOSIVE)));

    public static final RegistryObject<Item> CRAFTING_TABLE_SLAB_ITEM = ITEMS.register("crafting_table_slab", () -> new BlockItem(CRAFTING_TABLE_SLAB.get(), new Item.Properties().tab(SlabTab.SLAB_TAB)));
    public static final RegistryObject<Item> FURNACE_SLAB_ITEM = ITEMS.register("furnace_slab", () -> new BlockItem(FURNACE_SLAB.get(), new Item.Properties().tab(SlabTab.SLAB_TAB)));
    public static final RegistryObject<Item> CHEST_SLAB_ITEM = ITEMS.register("chest_slab", () -> new BlockItem(CHEST_SLAB.get(), new Item.Properties().tab(SlabTab.SLAB_TAB)));
    public static final RegistryObject<Item> TRAPPED_CHEST_SLAB_ITEM = ITEMS.register("trapped_chest_slab", () -> new BlockItem(TRAPPED_CHEST_SLAB.get(), new Item.Properties().tab(SlabTab.SLAB_TAB)));
    public static final RegistryObject<Item> NOTE_SLAB_ITEM = ITEMS.register("note_slab", () -> new BlockItem(NOTE_SLAB.get(), new Item.Properties().tab(SlabTab.SLAB_TAB)));
    public static final RegistryObject<Item> TNT_SLAB_ITEM = ITEMS.register("tnt_slab", () -> new BlockItem(TNT_SLAB.get(), new Item.Properties().tab(SlabTab.SLAB_TAB)));

    public static final RegistryObject<TileEntityType<TileFurnaceSlab>> FURNACE_SLAB_TILE = TILES.register("furnace_slab", () -> TileEntityType.Builder.of(TileFurnaceSlab::new, FURNACE_SLAB.get()).build(null));
    public static final RegistryObject<TileEntityType<TileChestSlab>> CHEST_SLAB_TILE = TILES.register("chest_slab", () -> TileEntityType.Builder.of(TileChestSlab::new, CHEST_SLAB.get(), TRAPPED_CHEST_SLAB.get()).build(null));

    public static final RegistryObject<EntityType<TNTSlabEntity>> TNT_SLAB_ENTITY = ENTITIES.register("tnt_slab", () -> register("tnt_slab", EntityType.Builder.<TNTSlabEntity>of(TNTSlabEntity::new, EntityClassification.MISC)
            .sized(1.0F, 0.5F).clientTrackingRange(10).updateInterval(10).setCustomClientFactory(TNTSlabEntity::new)));

    public static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> builder, boolean sendVelocityUpdates) {
        return builder.build(id);
    }

    public static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> builder) {
        return register(id, builder, true);
    }
}
