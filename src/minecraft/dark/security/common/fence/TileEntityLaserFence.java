package dark.security.common.fence;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockFluid;
import net.minecraft.block.BlockMushroom;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.IFluidBlock;
import universalelectricity.core.vector.Vector3;
import dark.api.ISpecialAccess;
import dark.core.terminal.TileEntityTerminal;
import dark.security.common.GreaterSecurity;

public class TileEntityLaserFence extends TileEntityTerminal implements ISpecialAccess
{

    public static final int MAX_LASER_RANGE = 10;
    public static final int UPDATE_RATE = 3;

    private Color beamColor = Color.red;

    public static List<Block> laserPassBlocks = new ArrayList<Block>();

    static
    {
        registerLaserPassBlock(Block.fire);
        registerLaserPassBlock(Block.tallGrass);
        registerLaserPassBlock(Block.crops);
        registerLaserPassBlock(Block.deadBush);
        registerLaserPassBlock(Block.mushroomRed);
        registerLaserPassBlock(Block.mushroomBrown);
        registerLaserPassBlock(Block.netherStalk);
        registerLaserPassBlock(Block.sapling);
        registerLaserPassBlock(Block.melonStem);
        registerLaserPassBlock(Block.pumpkinStem);
        registerLaserPassBlock(Block.torchWood);
    }

    public TileEntityLaserFence()
    {
        this.WATTS_PER_TICK = 5;
        this.MAX_WATTS = this.WATTS_PER_TICK * 20;
    }

    public static void registerLaserPassBlock(Block block)
    {
        if (block != null && !laserPassBlocks.contains(block))
        {
            laserPassBlocks.add(block);
        }
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();

        if (this.ticks % UPDATE_RATE == 0)
        {
            int gridSize = this.getGridSize();
            if (this.canRun() && this.canDeployGrid(gridSize))
            {
                this.deployGrid(gridSize);
            }
        }
    }

    @Override
    public boolean canRun()
    {
        return super.canRun() && worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord);
    }

    public boolean canDeployGrid(int gridSize)
    {
        if (gridSize > 1)
        {
            for (int blockDistance = 1; blockDistance < gridSize; blockDistance++)
            {
                if (!this.canBeamPassThrew(new Vector3(this).clone().modifyPositionFromSide(this.getFacingDirection(), blockDistance)))
                {
                    // System.out.println("Can't create lasers :: "+loc.toString());
                    return false;

                }
            }
        }
        // System.out.println("Can create lasers");
        return true;
    }

    /** Can the laser pass threw the block without being sloped
     *
     * @param vec - location of the block
     * @return true if it can */
    public boolean canBeamPassThrew(Vector3 vec)
    {
        int blockID = vec.getBlockID(this.worldObj);
        Block block = Block.blocksList[blockID];

        if (block != null)
        {
            if (block instanceof BlockFluid || block instanceof IFluidBlock)
            {
                return true;
            }
            if (laserPassBlocks.contains(block))
            {
                return true;
            }
            if (blockID == 0 || block.isAirBlock(worldObj, vec.intX(), vec.intY(), vec.intZ()))
            {
                return true;
            }
        }
        return false;
    }

    /** Gets the max size of the laser grid
     *
     * @return neg one if grid can't be created too a size. */
    public int getGridSize()
    {
        for (int tileDistance = TileEntityLaserFence.MAX_LASER_RANGE; tileDistance > 0; tileDistance--)
        {
            Vector3 tileLoc = new Vector3(this).clone().modifyPositionFromSide(this.getFacingDirection(), tileDistance);

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

    /** Creates or renews the laser grid by size
     *
     * @param gridLength - size of laser grid from emitter */
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

    /** Gets the direction this tile faces for rendering */
    public ForgeDirection getFacingDirection()
    {
        int meta = 0;
        if (worldObj != null)
        {
            meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord) % 6;
        }
        return ForgeDirection.getOrientation(meta);
    }

    /** is this block rotated on its facing side
     *
     * @return true if its been rotated 90 degrees more than normal */
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

    @Override
    public AxisAlignedBB getRenderBoundingBox()
    {
        return INFINITE_EXTENT_AABB;
    }

}
