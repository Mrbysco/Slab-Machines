package com.mrbysco.slabmachines.entity;

import com.mrbysco.slabmachines.config.SlabConfig;
import com.mrbysco.slabmachines.init.SlabRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class TNTSlabEntity extends TNTEntity {
    private static final DataParameter<Boolean> IS_ETHO = EntityDataManager.defineId(TNTSlabEntity.class, DataSerializers.BOOLEAN);
    private boolean etho = false;

    public TNTSlabEntity(EntityType<? extends TNTSlabEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public TNTSlabEntity(World worldIn, double x, double y, double z, @Nullable LivingEntity igniter, boolean etho) {
        this(SlabRegistry.TNT_SLAB_ENTITY.get(), worldIn);
        this.setPos(x, y, z);
        double d0 = worldIn.random.nextDouble() * (double)((float)Math.PI * 2F);
        this.setDeltaMovement(-Math.sin(d0) * 0.02D, 0.2D, -Math.cos(d0) * 0.02D);
        this.setFuse(80);
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.owner = igniter;
        this.etho = etho;
    }

	public TNTSlabEntity(FMLPlayMessages.SpawnEntity spawnEntity, World worldIn) {
		this(SlabRegistry.TNT_SLAB_ENTITY.get(), worldIn);
	}

	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

    protected void defineSynchedData() {
    	super.defineSynchedData();
        this.entityData.define(IS_ETHO, Boolean.valueOf(false));
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick() {
        if (!this.isNoGravity()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.04D, 0.0D));
        }

        this.move(MoverType.SELF, this.getDeltaMovement());
        this.setDeltaMovement(this.getDeltaMovement().scale(0.98D));
        if (this.onGround) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.7D, -0.5D, 0.7D));
        }

        --this.life;
        if (this.life <= 0) {
            this.remove();
            if (!this.level.isClientSide) {
                this.explode();
            }
        } else {
            this.updateInWaterStateAndDoFluidPushing();
            if (this.level.isClientSide) {
                this.level.addParticle(ParticleTypes.SMOKE, this.getX(), this.getY() + 0.5D, this.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }
    }

    protected void explode() {
        float f = 2.0F;
        this.level.explode(this, this.getX(), this.getY(0.0625D), this.getZ(), f, Explosion.Mode.BREAK);

    	if(!this.level.isClientSide) {
            if(this.isEtho()) {
        		double radius = (double)(6F * (0.7F + this.level.random.nextFloat() * 0.6F));
        		
        		AxisAlignedBB hitbox = new AxisAlignedBB(this.getX() - 0.5f, this.getY() - 0.5f, this.getZ() - 0.5f, this.getX() + 0.5f, this.getY() + 0.5f, this.getZ() + 0.5f)
        				.expandTowards(-radius, -radius, -radius).expandTowards(radius, radius, radius);
        		for(LivingEntity entity : this.level.getEntitiesOfClass(LivingEntity.class, hitbox)) {
        			if((entity instanceof PlayerEntity && !(entity instanceof FakePlayer)) || SlabConfig.COMMON.ethoSlabVillagers.get() && entity instanceof VillagerEntity) {
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

    protected void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Etho", this.isEtho());
    }

    protected void readAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        this.setEtho(compound.getBoolean("Etho"));
    }


    public void setEtho(boolean isEtho) {
        this.entityData.set(IS_ETHO, isEtho);
        this.etho = isEtho;
    }

    public void onSyncedDataUpdated(DataParameter<?> key) {
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