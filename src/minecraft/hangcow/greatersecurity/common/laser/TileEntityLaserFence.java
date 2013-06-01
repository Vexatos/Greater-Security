package hangcow.greatersecurity.common.laser;

import hangcow.greatersecurity.common.GreaterSecurity;

import java.awt.Color;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockFluid;
import net.minecraft.block.BlockMushroom;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.electricity.IElectricityNetwork;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.CustomDamageSource;
import dark.library.access.interfaces.ISpecialAccess;
import dark.library.terminal.TileEntityTerminal;

public class TileEntityLaserFence extends TileEntityTerminal implements ISpecialAccess
{
	public static final int MAX_LASER_RANGE = 10;
	public static final int UPDATE_RATE = 3;
	public static final double WattTick = 10;
	
	/** The required and deducted watts for each shock */
	public static final double WATT_PER_SHOCK = 10;

	private Color beamColor = Color.red;

	Vector3 fenceLocation = null;
	
	/** Whether or not the tile entity can shock */
	public boolean canShock;

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (fenceLocation == null)
		{
			fenceLocation = new Vector3(this);
		}

		if (this.ticks % UPDATE_RATE == 0) // TODO add power check
		{
			int gridSize = this.getGridSize();
			if (worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord) && this.canDeployGrid(gridSize) && this.wattsReceived >= TileEntityLaserFence.WattTick)
			{
				this.wattsReceived -= TileEntityLaserFence.WattTick;
				// System.out.println("Creating Lasers");
				this.deployGrid(gridSize);
			}
		}
		// TODO Added power check for canShock
		if (this.wattsReceived >= this.WATT_PER_SHOCK){
			
			this.canShock = true;
			
		}else{
			
			this.canShock = false;
			
		}
	}

	public boolean canDeployGrid(int gridSize)
	{
		if (gridSize > 1)
		{
			for (int blockDistance = 1; blockDistance < gridSize; blockDistance++)
			{
				if (!this.canRenderThrew(fenceLocation.clone().modifyPositionFromSide(this.getFacingDirection(), blockDistance)))
				{
					// System.out.println("Can't create lasers :: "+loc.toString());
					return false;

				}
			}
		}
		// System.out.println("Can create lasers");
		return true;
	}

	public boolean canRenderThrew(Vector3 vec)
	{
		int blockID = vec.getBlockID(this.worldObj);
		Block block = Block.blocksList[blockID];

		if (blockID != 0)
			return true;
		if (blockID != Block.fire.blockID)
			return true;
		if (blockID != Block.tallGrass.blockID)
			return true;
		if (blockID != Block.blockSnow.blockID)
			return true;
		if (block != null)
		{
			if (block instanceof BlockFlower)
				return true;
			if (block instanceof BlockMushroom)
				return true;
			if (block instanceof BlockFluid)
				return true;
		}
		return false;
	}

	/**
	 * Gets the max size of the laser grid
	 * 
	 * @return neg one if grid can't be created too a size.
	 */
	public int getGridSize()
	{
		for (int tileDistance = TileEntityLaserFence.MAX_LASER_RANGE; tileDistance > 0; tileDistance--)
		{
			Vector3 tileLoc = fenceLocation.clone().modifyPositionFromSide(this.getFacingDirection(), tileDistance);

			if (tileLoc.getTileEntity(worldObj) instanceof TileEntityLaserFence && tileLoc.getTileEntity(worldObj) != this)
			{
				TileEntityLaserFence fence = (TileEntityLaserFence) tileLoc.getTileEntity(worldObj);
				if (fence.getFacingDirection() == this.getFacingDirection().getOpposite() && fence.isRotated() == this.isRotated())
				{
					return tileDistance;
				}
			}

		}
		return -1;
	}

	/**
	 * Creates or renews the laser grid by size
	 * 
	 * @param gridLength - size of laser grid from emitter
	 */
	public void deployGrid(int gridLength)
	{
		gridLength = Math.abs(gridLength);
		ForgeDirection direction = this.getFacingDirection();

		Vector3 start = new Vector3(this.xCoord + 0.5, this.yCoord + 0.25, this.zCoord + 0.25);
		Vector3 end = start.clone().modifyPositionFromSide(this.getFacingDirection(), gridLength + .75);
		Vector3 change = new Vector3(0, 0, 0.28125);

		if (direction == ForgeDirection.DOWN)
		{
			start = new Vector3(this.xCoord + 0.5, this.yCoord + 0.75, this.zCoord + 0.25);
			end = start.clone().modifyPositionFromSide(this.getFacingDirection(), gridLength + .75);
			change = new Vector3(0, 0, 0.28125);

			if (this.isRotated())
			{
				start = new Vector3(this.xCoord + 0.25, this.yCoord + 0.75, this.zCoord + 0.5);
				end = start.clone().modifyPositionFromSide(this.getFacingDirection(), gridLength + .75);
				change = new Vector3(0.28125, 0, 0);
			}
		}
		else if (direction == ForgeDirection.UP)
		{
			start = new Vector3(this.xCoord + 0.5, this.yCoord + 0.25, this.zCoord + 0.25);
			end = start.clone().modifyPositionFromSide(this.getFacingDirection(), gridLength + .75);
			change = new Vector3(0, 0, 0.28125);

			if (this.isRotated())
			{
				start = new Vector3(this.xCoord + 0.25, this.yCoord + 0.25, this.zCoord + 0.5);
				end = start.clone().modifyPositionFromSide(this.getFacingDirection(), gridLength + .75);
				change = new Vector3(0.28125, 0, 0);
			}
		}
		else if (direction == ForgeDirection.EAST)
		{
			start = new Vector3(this.xCoord + 0.25, this.yCoord + 0.25, this.zCoord + 0.5);
			end = start.clone().modifyPositionFromSide(this.getFacingDirection(), gridLength + .75);
			change = new Vector3(0, 0.28125, 0);

			if (this.isRotated())
			{
				start = new Vector3(this.xCoord + 0.25, this.yCoord + 0.5, this.zCoord + 0.25);
				end = start.clone().modifyPositionFromSide(this.getFacingDirection(), gridLength + .75);
				change = new Vector3(0, 0, 0.28125);
			}
		}
		else if (direction == ForgeDirection.WEST)
		{
			start = new Vector3(this.xCoord + 0.75, this.yCoord + 0.25, this.zCoord + 0.5);
			end = start.clone().modifyPositionFromSide(this.getFacingDirection(), gridLength + .75);
			change = new Vector3(0, 0.28125, 0);

			if (this.isRotated())
			{
				start = new Vector3(this.xCoord + 0.75, this.yCoord + 0.5, this.zCoord + 0.25);
				end = start.clone().modifyPositionFromSide(this.getFacingDirection(), gridLength + .75);
				change = new Vector3(0, 0, 0.28125);
			}
		}
		else if (direction == ForgeDirection.NORTH)
		{
			start = new Vector3(this.xCoord + 0.5, this.yCoord + 0.25, this.zCoord + 0.75);
			end = start.clone().modifyPositionFromSide(this.getFacingDirection(), gridLength + .75);
			change = new Vector3(0, 0.28125, 0);

			if (this.isRotated())
			{
				start = new Vector3(this.xCoord + 0.25, this.yCoord + 0.5, this.zCoord + 0.75);
				end = start.clone().modifyPositionFromSide(this.getFacingDirection(), gridLength + .75);
				change = new Vector3(0.28125, 0, 0);
			}
		}
		else if (direction == ForgeDirection.SOUTH)
		{
			start = new Vector3(this.xCoord + 0.5, this.yCoord + 0.25, this.zCoord + 0.25);
			end = start.clone().modifyPositionFromSide(this.getFacingDirection(), gridLength + .75);
			change = new Vector3(0, 0.28125, 0);

			if (this.isRotated())
			{
				start = new Vector3(this.xCoord + 0.25, this.yCoord + 0.5, this.zCoord + 0.25);
				end = start.clone().modifyPositionFromSide(this.getFacingDirection(), gridLength + .75);
				change = new Vector3(0.28125, 0, 0);
			}
		}

		if (!worldObj.isRemote)
		{
			end = end.clone().add(change).add(change);
			List<EntityLiving> entities;
			entities = worldObj.getEntitiesWithinAABB(EntityLiving.class, AxisAlignedBB.getBoundingBox(start.x < end.x ? start.x : end.x, start.y < end.y ? start.y : end.y, start.z < end.z ? start.z : end.z,

			start.x > end.x ? start.x : end.x, start.y > end.y ? start.y : end.y, start.z > end.z ? start.z : end.z));

			for (EntityLiving entity : entities)
			{
				if (entity != null && !entity.isDead)
				{
					entity.addVelocity(-entity.motionX, -entity.motionY, -entity.motionZ);
					entity.attackEntityFrom(DamageSource.onFire, 40);
					entity.setFire(5);
				}
			}
		}
		else
		{

			GreaterSecurity.proxy.renderBeam(worldObj, start, end, beamColor, UPDATE_RATE);
			// DarkMain.renderBeam(worldObj, start, end.clone().add(change).add(change), Color.BLUE,
			// UPDATE_RATE);
			GreaterSecurity.proxy.renderBeam(worldObj, start.clone().add(change), end.clone().add(change), beamColor, UPDATE_RATE);
			GreaterSecurity.proxy.renderBeam(worldObj, start.clone().add(change).add(change), end.clone().add(change).add(change), beamColor, UPDATE_RATE);
		}
	}

	/**
	 * Gets the direction this tile faces for rendering
	 */
	public ForgeDirection getFacingDirection()
	{
		int meta = 0;
		if (worldObj != null)
		{
			meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord) % 6;
		}
		return ForgeDirection.getOrientation(meta);
	}

	/**
	 * is this block rotated on its facing side
	 * 
	 * @return true if its been rotated 90 degrees more than normal
	 */
	public boolean isRotated()
	{
		if (worldObj != null)
		{
			return worldObj.getBlockMetadata(xCoord, yCoord, zCoord) > 5;
		}
		return false;
	}

	@Override
	public boolean canConnect(ForgeDirection direction)
	{
		return direction == getFacingDirection().getOpposite();
	}

	@Override
	public String getChannel()
	{
		return GreaterSecurity.CHANNEL;
	}

	public ElectricityPack getRequest()
	{
		return new ElectricityPack(120, TileEntityLaserFence.WattTick / 120);
	}

	// TODO Added shockEntity method for use in BlockLaserFence
	/**
	 * Checks if an entity can be shocked / electocuted and does it if possible. 
	 * @param entity
	 */
	public void shockEntity(Entity par5Entity) {
		
		if (this.canShock){
			
			double voltage = this.getVoltage();
			
			int damage;
			
			if (voltage == 120.0){
				
				damage = 2;
			
			}else if (voltage == 240.0){
				
				damage = 4;
				
			}else{
				
				damage = 0;
				
			}
			
			par5Entity.attackEntityFrom(CustomDamageSource.electrocution, damage);
			
			this.wattsReceived -= this.WATT_PER_SHOCK;
			
		}
		
	}

}
