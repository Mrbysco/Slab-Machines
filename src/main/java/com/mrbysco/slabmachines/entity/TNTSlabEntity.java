package com.mrbysco.slabmachines.entity;

import com.mrbysco.slabmachines.config.SlabConfig;
import com.mrbysco.slabmachines.init.SlabRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fmllegacy.network.FMLPlayMessages;
import net.minecraftforge.fmllegacy.network.NetworkHooks;

import javax.annotation.Nullable;

public class TNTSlabEntity extends PrimedTnt {
    private static final EntityDataAccessor<Boolean> IS_ETHO = SynchedEntityData.defineId(TNTSlabEntity.class, EntityDataSerializers.BOOLEAN);
    private boolean etho = false;

    public TNTSlabEntity(EntityType<? extends TNTSlabEntity> type, Level level) {
        super(type, level);
    }

    public TNTSlabEntity(Level level, double x, double y, double z, @Nullable LivingEntity igniter, boolean etho) {
        this(SlabRegistry.TNT_SLAB_ENTITY.get(), level);
        this.setPos(x, y, z);
        double d0 = level.random.nextDouble() * (double)((float)Math.PI * 2F);
        this.setDeltaMovement(-Math.sin(d0) * 0.02D, 0.2D, -Math.cos(d0) * 0.02D);
        this.setFuse(80);
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.owner = igniter;
        this.etho = etho;
    }

	public TNTSlabEntity(FMLPlayMessages.SpawnEntity spawnEntity, Level level) {
		this(SlabRegistry.TNT_SLAB_ENTITY.get(), level);
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

    protected void defineSynchedData() {
    	super.defineSynchedData();
        this.entityData.define(IS_ETHO, Boolean.valueOf(false));
    }

    @Override
    public void explode() {
        float f = 2.0F;
        this.level.explode(this, this.getX(), this.getY(0.0625D), this.getZ(), f, Explosion.BlockInteraction.BREAK);

    	if(!this.level.isClientSide) {
            if(this.isEtho()) {
        		double radius = (double)(6F * (0.7F + this.level.random.nextFloat() * 0.6F));
        		
        		AABB hitbox = new AABB(this.getX() - 0.5f, this.getY() - 0.5f, this.getZ() - 0.5f, this.getX() + 0.5f, this.getY() + 0.5f, this.getZ() + 0.5f)
        				.expandTowards(-radius, -radius, -radius).expandTowards(radius, radius, radius);
        		for(LivingEntity entity : this.level.getEntitiesOfClass(LivingEntity.class, hitbox)) {
        			if((entity instanceof Player && !(entity instanceof FakePlayer)) || SlabConfig.COMMON.ethoSlabVillagers.get() && entity instanceof Villager) {
        				for(int i = 40; i >= 0; i--)
            			{
            				if(entity.blockPosition().getY() + i > 256)
            				{
            					return;
            				}
            				if(this.level.getBlockState(entity.blockPosition().offset(0, i, 0)).getBlock() == Blocks.AIR)
            				{
            					this.level.setBlockAndUpdate(entity.blockPosition().offset(0, i, 0), Blocks.CHIPPED_ANVIL.defaultBlockState());
            					break;
            				}
            			}
        			}
        		}
            }
    	}
    }

    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Etho", this.isEtho());
    }

    protected void readAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        this.setEtho(compound.getBoolean("Etho"));
    }


    public void setEtho(boolean isEtho) {
        this.entityData.set(IS_ETHO, isEtho);
        this.etho = isEtho;
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (IS_ETHO.equals(key)) {
            this.etho = this.getEthoDataManager();
        }
    }

    public boolean getEthoDataManager() {
        return this.entityData.get(IS_ETHO);
    }

    public boolean isEtho() {
        return this.etho;
    }
}