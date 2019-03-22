package com.mrbysco.slabmachines.gui.compat.tcon;

import net.minecraft.entity.player.InventoryPlayer;
import slimeknights.tconstruct.tools.common.inventory.ContainerCraftingStation;
import slimeknights.tconstruct.tools.common.tileentity.TileCraftingStation;

public class ContainerCraftingStationSlab extends ContainerCraftingStation{

	public ContainerCraftingStationSlab(InventoryPlayer playerInventory, TileCraftingStation tile) {
		super(playerInventory, tile);
	}
	
	/*
	@Override
	public boolean detectedTinkerStationParts(World world, BlockPos start) {
		Set<Integer> found = Sets.newHashSet();
	    Set<BlockPos> visited = Sets.newHashSet();
	    Set<IBlockState> ret = Sets.newHashSet();
	    boolean hasMaster = false;

	    // BFS for related blocks
	    Queue<BlockPos> queue = Queues.newPriorityQueue();
	    queue.add(start);

	    while(!queue.isEmpty()) {
	      BlockPos pos = queue.poll();
	      // already visited between adding and call
	      if(visited.contains(pos)) {
	        continue;
	      }

	      IBlockState state = world.getBlockState(pos);
	      storeMultipartState(state, pos, queue, found, visited, ret);
	      if(!(state.getBlock() instanceof ITinkerStationBlock)) {
	        continue;
	      }

	      // found a part, add surrounding blocks that haven't been visited yet
	      if(!visited.contains(pos.north())) {
	        queue.add(pos.north());
	      }
	      if(!visited.contains(pos.east())) {
	        queue.add(pos.east());
	      }
	      if(!visited.contains(pos.south())) {
	        queue.add(pos.south());
	      }
	      if(!visited.contains(pos.west())) {
	        queue.add(pos.west());
	      }
	      // add to visited
	      visited.add(pos);

	      // save the thing 
	      ITinkerStationBlock tinker = (ITinkerStationBlock) state.getBlock();
	      Integer number = tinker.getGuiNumber(state);
	      if(!found.contains(number)) {
	        found.add(number);
	        tinkerStationBlocks.add(Pair.of(pos, state));
	        ret.add(state);
	        if(state.getProperties().containsKey(BlockToolTable.TABLES)) {
	          BlockToolTable.TableTypes type = state.getValue(BlockToolTable.TABLES);
	          if(type != null && type == BlockToolTable.TableTypes.CraftingStation) {
	            hasMaster = true;
	          }
	        }
	      }
	    }
	    
	    TinkerCompBlock comp = new TinkerCompBlock();
	    tinkerStationBlocks.sort(comp);
	    
		return hasMaster;
	}
	
	@Optional.Method(modid = "mcmultipart")
	public void storeMultipartState(IBlockState state, BlockPos pos, Queue<BlockPos> queue, Set<Integer> found, Set<BlockPos> visited, Set<IBlockState> ret)
	{
		if(state.getBlock() instanceof mcmultipart.block.BlockMultipartContainer)
		{
			java.util.Optional<IBlockState> down = mcmultipart.api.multipart.MultipartHelper.getPartState(world, pos, mcmultipart.api.slot.EnumFaceSlot.DOWN);
			if (down.isPresent() && down.get().getBlock() instanceof ITinkerStationBlock) 
			{
			    ITinkerStationBlock tinker = (ITinkerStationBlock) down.get().getBlock();

				Integer number = tinker.getGuiNumber(down.get());
			    if(!found.contains(number)) {
			    	found.add(number);
			        tinkerStationBlocks.add(Pair.of(pos, down.get()));
			        ret.add(down.get());
			    }
			}

			java.util.Optional<IBlockState> up = mcmultipart.api.multipart.MultipartHelper.getPartState(world, pos, mcmultipart.api.slot.EnumFaceSlot.UP);
			if (up.isPresent() && up.get().getBlock() instanceof ITinkerStationBlock) {
				ITinkerStationBlock tinker = (ITinkerStationBlock) up.get().getBlock();
				Integer number = tinker.getGuiNumber(up.get());
			    if(!found.contains(number)) {
			    	found.add(number);
			        tinkerStationBlocks.add(Pair.of(pos, up.get()));
			        ret.add(up.get()); 
			    }
			}
			
			if(!visited.contains(pos.north())) {
		        queue.add(pos.north());
		      }
		      if(!visited.contains(pos.east())) {
		        queue.add(pos.east());
		      }
		      if(!visited.contains(pos.south())) {
		        queue.add(pos.south());
		      }
		      if(!visited.contains(pos.west())) {
		        queue.add(pos.west());
		      }
		      // add to visited
		      visited.add(pos);
		}
	}
	
	public class TinkerCompBlock implements Comparator<Pair<BlockPos, IBlockState>> {

	    @Override
	    public int compare(Pair<BlockPos, IBlockState> o1, Pair<BlockPos, IBlockState> o2) {
	      IBlockState s1 = o1.getRight();
	      IBlockState s2 = o2.getRight();
	
	      return ((ITinkerStationBlock) s2.getBlock()).getGuiNumber(s2) - ((ITinkerStationBlock) s1.getBlock()).getGuiNumber(s1);
	    }
	}
	*/
}
