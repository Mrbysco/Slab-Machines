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
    private static final DataParameter<Boolean> IS_ETHO = EntityDataManager.createKey(TNTSlabEntity.class, DataSerializers.BOOLEAN);
    private boolean etho = false;

    public TNTSlabEntity(EntityType<? extends TNTSlabEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public TNTSlabEntity(World worldIn, double x, double y, double z, @Nullable LivingEntity igniter, boolean etho) {
        this(SlabRegistry.TNT_SLAB_ENTITY.get(), worldIn);
        this.setPosition(x, y, z);
        double d0 = worldIn.rand.nextDouble() * (double)((float)Math.PI * 2F);
        this.setMotion(-Math.sin(d0) * 0.02D, 0.2D, -Math.cos(d0) * 0.02D);
        this.setFuse(80);
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
        this.tntPlacedBy = igniter;
        this.etho = etho;
    }

	public TNTSlabEntity(FMLPlayMessages.SpawnEntity spawnEntity, World worldIn) {
		this(SlabRegistry.TNT_SLAB_ENTITY.get(), worldIn);
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

    protected void registerData() {
    	super.registerData();
        this.dataManager.register(IS_ETHO, Boolean.valueOf(false));
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick() {
        if (!this.hasNoGravity()) {
            this.setMotion(this.getMotion().add(0.0D, -0.04D, 0.0D));
        }

        this.move(MoverType.SELF, this.getMotion());
        this.setMotion(this.getMotion().scale(0.98D));
        if (this.onGround) {
            this.setMotion(this.getMotion().mul(0.7D, -0.5D, 0.7D));
        }

        --this.fuse;
        if (this.fuse <= 0) {
            this.remove();
            if (!this.world.isRemote) {
                this.explode();
            }
        } else {
            this.func_233566_aG_();
            if (this.world.isRemote) {
                this.world.addParticle(ParticleTypes.SMOKE, this.getPosX(), this.getPosY() + 0.5D, this.getPosZ(), 0.0D, 0.0D, 0.0D);
            }
        }
    }

    protected void explode() {
        float f = 2.0F;
        this.world.createExplosion(this, this.getPosX(), this.getPosYHeight(0.0625D), this.getPosZ(), f, Explosion.Mode.BREAK);

    	if(!this.world.isRemote) {
            if(this.isEtho()) {
        		double radius = (double)(6F * (0.7F + this.world.rand.nextFloat() * 0.6F));
        		
        		AxisAlignedBB hitbox = new AxisAlignedBB(this.getPosX() - 0.5f, this.getPosY() - 0.5f, this.getPosZ() - 0.5f, this.getPosX() + 0.5f, this.getPosY() + 0.5f, this.getPosZ() + 0.5f)
        				.expand(-radius, -radius, -radius).expand(radius, radius, radius);
        		for(LivingEntity entity : this.world.getEntitiesWithinAABB(LivingEntity.class, hitbox)) {
        			if((entity instanceof PlayerEntity && !(entity instanceof FakePlayer)) || SlabConfig.COMMON.ethoSlabVillagers.get() && entity instanceof VillagerEntity) {
        				for(int i = 40; i >= 0; i--)
            			{
            				if(entity.getPosition().getY() + i > 256)
            				{
            					return;
            				}
            				if(this.world.getBlockState(entity.getPosition().add(0, i, 0)).getBlock() == Blocks.AIR)
            				{
            					this.world.setBlockState(entity.getPosition().add(0, i, 0), Blocks.CHIPPED_ANVIL.getDefaultState());
            					break;
            				}
            			}
        			}
        		}
            }
    	}
    }

    protected void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putBoolean("Etho", this.isEtho());
    }

    protected void readAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        this.setEtho(compound.getBoolean("Etho"));
    }


    public void setEtho(boolean isEtho) {
        this.dataManager.set(IS_ETHO, isEtho);
        this.etho = isEtho;
    }

    public void notifyDataManagerChange(DataParameter<?> key) {
        super.notifyDataManagerChange(key);
        if (IS_ETHO.equals(key)) {
            this.etho = this.getEthoDataManager();
        }
    }

    public boolean getEthoDataManager() {
        return this.dataManager.get(IS_ETHO);
    }

    public boolean isEtho() {
        return this.etho;
    }
}