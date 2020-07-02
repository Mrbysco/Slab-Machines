package com.mrbysco.slabmachines.entity;

import com.mrbysco.slabmachines.config.SlabMachineConfigGen;
import net.minecraft.block.BlockAnvil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

import javax.annotation.Nullable;

public class EntityTNTPrimeSlab extends Entity
{
    private static final DataParameter<Integer> FUSE = EntityDataManager.<Integer>createKey(EntityTNTPrimed.class, DataSerializers.VARINT);
    @Nullable
    private EntityLivingBase tntPlacedBy;
    /** How long the fuse is */
    private int fuse;
    private boolean isEthoSlab;

    public EntityTNTPrimeSlab(World worldIn)
    {
        super(worldIn);
        this.fuse = 80;
        this.preventEntitySpawning = true;
        this.isImmuneToFire = true;
        this.setSize(0.98F, 0.49F);
    }

    public EntityTNTPrimeSlab(World worldIn, double x, double y, double z, EntityLivingBase igniter, boolean etho)
    {
        this(worldIn);
        this.setPosition(x, y, z);
        float f = (float)(Math.random() * (Math.PI * 2D));
        this.motionX = (double)(-((float)Math.sin((double)f)) * 0.02F);
        this.motionY = 0.20000000298023224D;
        this.motionZ = (double)(-((float)Math.cos((double)f)) * 0.02F);
        this.setFuse(80);
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
        this.tntPlacedBy = igniter;
        this.isEthoSlab = etho;
    }

    protected void entityInit()
    {
        this.dataManager.register(FUSE, Integer.valueOf(80));
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking()
    {
        return false;
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
    {
        return !this.isDead;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (!this.hasNoGravity())
        {
            this.motionY -= 0.03999999910593033D;
        }

        this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9800000190734863D;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= 0.9800000190734863D;

        if (this.onGround)
        {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
            this.motionY *= -0.5D;
        }

        --this.fuse;

        if (this.fuse <= 0)
        {
            this.explode();
        }
        else
        {
            this.handleWaterMovement();
            this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
        }
    }

    private void explode()
    {
    	if(!this.world.isRemote)
    	{
    		float f = 4.0F;
            this.world.createExplosion(this, this.posX, this.posY + (double)(this.height / 16.0F), this.posZ, 2.0F, true);
            
            if(this.isEthoSlab)
            {
        		double radius = (double)(6F * (0.7F + this.world.rand.nextFloat() * 0.6F));
        		
        		AxisAlignedBB hitbox = new AxisAlignedBB(this.posX - 0.5f, this.posY - 0.5f, this.posZ - 0.5f, this.posX + 0.5f, this.posY + 0.5f, this.posZ + 0.5f)
        				.expand(-radius, -radius, -radius).expand(radius, radius, radius);
        		for(EntityLivingBase entity : this.world.getEntitiesWithinAABB(EntityLivingBase.class, hitbox))
        		{
        			if((entity instanceof EntityPlayer && !(entity instanceof FakePlayer)) || SlabMachineConfigGen.general.ethoSlabVillagers && entity instanceof EntityVillager)
        			{
        				for(int i = 40; i >= 0; i--)
            			{
            				if(entity.getPosition().getY() + i > 256)
            				{
            					return;
            				}
            				if(this.world.getBlockState(entity.getPosition().add(0, i, 0)).getBlock() == Blocks.AIR)
            				{
            					this.world.setBlockState(entity.getPosition().add(0, i, 0), Blocks.ANVIL.getDefaultState().withProperty(BlockAnvil.DAMAGE, 2));
            					break;
            				}
            			}
        			}
        		}
            }
    	}
        
        this.setDead();

    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void writeEntityToNBT(NBTTagCompound compound)
    {
        compound.setShort("Fuse", (short)this.getFuse());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound compound)
    {
        this.setFuse(compound.getShort("Fuse"));
    }

    /**
     * returns null or the entityliving it was placed or ignited by
     */
    @Nullable
    public EntityLivingBase getTntPlacedBy()
    {
        return this.tntPlacedBy;
    }

    public float getEyeHeight()
    {
        return 0.0F;
    }

    public void setFuse(int fuseIn)
    {
        this.dataManager.set(FUSE, Integer.valueOf(fuseIn));
        this.fuse = fuseIn;
    }

    public void notifyDataManagerChange(DataParameter<?> key)
    {
        if (FUSE.equals(key))
        {
            this.fuse = this.getFuseDataManager();
        }
    }

    /**
     * Gets the fuse from the data manager
     */
    public int getFuseDataManager()
    {
        return ((Integer)this.dataManager.get(FUSE)).intValue();
    }

    public int getFuse()
    {
        return this.fuse;
    }
}