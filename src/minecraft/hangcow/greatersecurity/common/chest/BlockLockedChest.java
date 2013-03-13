package hangcow.greatersecurity.common.chest;

import net.minecraftforge.common.ForgeDirection;
import hangcow.greatersecurity.common.CommonProxy;
import hangcow.greatersecurity.common.GreaterSecurity;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import universalelectricity.prefab.BlockMachine;

public class BlockLockedChest extends BlockMachine
{

    public BlockLockedChest(int par1)
    {
        super(par1, Material.iron);
        this.setBlockUnbreakable();
        this.setResistance(10000.0f);
        this.blockIndexInTexture = 1;
        this.setCreativeTab(CreativeTabs.tabDecorations);

    }

    @Override
    public int damageDropped(int meta)
    {
        if (meta > 0 && meta < 4)
        {
            return 0;
        }
        else if (meta > 3 && meta < 8)
        {
            return 1;
        }
        else if (meta > 7 && meta < 12)
        {
            return 2;
        }
        else if (meta > 11 && meta < 16) { return 3; }
        return 0;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public int getRenderType()
    {
        return -1;
    }

    @Override
    public void onBlockAdded(World world, int i, int j, int k)
    {
        super.onBlockAdded(world, i, j, k);
        this.unifyAdjacentChests(world, i, j, k);
        for (int s = 0; s < 4; s++)
        {
            int deltaX = 0;
            int deltaZ = 0;
            switch (s)
            {
                case 0:
                    deltaZ = -1;
                    break;
                case 1:
                    deltaZ = 1;
                    break;
                case 2:
                    deltaX = -1;
                    break;
                case 3:
                    deltaX = 1;
                    break;
            }

            int blockID = world.getBlockId(i + deltaX, j, k + deltaZ);
            int tType = getType(world, i, j, k);
            int bType = getType(world, i + deltaX, j, k + deltaZ);
            if (blockID == this.blockID && tType == bType)
            {
                this.unifyAdjacentChests(world, i + deltaX, j, k + deltaZ);
            }
        }
    }

    /**
     * Takes in the metadata at ijk and
     * 
     * @return the meta group type 0-3,, 16 meta 4 per block 4 groups
     */
    public int getType(World world, int i, int j, int k)
    {
        int meta = world.getBlockMetadata(i, j, k);
        int type = 0;
        if (meta < 4 && meta > 0)
        {
            type = 0;
        }
        else if (meta < 8 && meta > 3)
        {
            type = 1;
        }
        else if (meta < 12 && meta > 7)
        {
            type = 2;
        }
        else if (meta < 16 && meta > 11)
        {
            type = 3;
        }

        return type;
    }

    public void unifyAdjacentChests(World world, int i, int j, int k)
    {
        if (!world.isRemote)
        {
            int metaChange = world.getBlockMetadata(i, j, k);
            int type = this.getType(world, i, j, k);
            for (int s = 0; s < 4; s++)
            {
                int deltaX = 0;
                int deltaZ = 0;

                switch (s)
                {
                    case 0:
                        deltaZ--;
                        break;
                    case 1:
                        deltaZ++;
                        break;
                    case 2:
                        deltaX--;
                        break;
                    case 3:
                        deltaX++;
                        break;
                }

                if (world.getBlockId(i + deltaX, j, k + deltaZ) == this.blockID)
                {
                    int bType = getType(world, i + deltaX, j, k + deltaZ);
                    if (type == bType)
                    {
                        metaChange = world.getBlockMetadata(i + deltaX, j, k + deltaZ);
                    }
                }
            }
            world.setBlockMetadataWithNotify(i, j, k, metaChange);
        }
    }

    /**
     * Checks to see if its valid to put this block at the specified
     * coordinates. Args: world, x, y, z
     */
    @Override
    public boolean canPlaceBlockAt(World world, int i, int j, int k)
    {
        int var5 = 0;
        int tType = getType(world, i, j, k);
        for (int s = 0; s < 4; s++)
        {
            int deltaX = 0;
            int deltaZ = 0;

            switch (s)
            {
                case 0:
                    deltaZ = -1;
                    break;
                case 1:
                    deltaZ = 1;
                    break;
                case 2:
                    deltaX = -1;
                    break;
                case 3:
                    deltaX = 1;
                    break;
            }
            int bType = getType(world, i + deltaX, j, k + deltaZ);
            if (world.getBlockId(i + deltaX, j, k + deltaZ) == this.blockID)
            {
                ++var5;
                if (this.isThereANeighborChest(world, i + deltaX, j, k + deltaZ)) { return false; }
            }

        }
        return var5 > 1 ? false : true;
    }

    /**
     * Checks the neighbor blocks to see if there is a chest there. Args: world,
     * x, y, z
     */
    private boolean isThereANeighborChest(World world, int i, int j, int k)
    {

        for (int s = 0; s < 4; s++)
        {
            int deltaX = 0;
            int deltaZ = 0;

            switch (s)
            {
                case 0:
                    deltaZ = -1;
                    break;
                case 1:
                    deltaZ = 1;
                    break;
                case 2:
                    deltaX = -1;
                    break;
                case 3:
                    deltaX = 1;
                    break;
            }
            int bType = getType(world, i + deltaX, j, k + deltaZ);
            int tType = getType(world, i, j, k);
            if (world.getBlockId(i + deltaX, j, k + deltaZ) == this.blockID && tType == bType) { return true; }
        }
        return false;
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which
     * neighbor changed (coordinates passed are their own) Args: x, y, z,
     * neighbor blockID
     */
    @Override
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
        super.onNeighborBlockChange(par1World, par2, par3, par4, par5);
        TileEntityLockedChest var6 = (TileEntityLockedChest) par1World.getBlockTileEntity(par2, par3, par4);

        if (var6 != null)
        {
            var6.updateContainingBlockInfo();
        }
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    @Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        TileEntityLockedChest TileEntity = (TileEntityLockedChest) world.getBlockTileEntity(i, j, k);

        if (TileEntity == null || world.isBlockSolidOnSide(i, j + 1, k, ForgeDirection.DOWN)) { return true; }
        
        for (int s = 0; s < 4; s++)
        {
            int deltaX = 0;
            int deltaZ = 0;

            switch (s)
            {
                case 0:
                    deltaZ = -1;
                    break;
                case 1:
                    deltaZ = 1;
                    break;
                case 2:
                    deltaX = -1;
                    break;
                case 3:
                    deltaX = 1;
                    break;
            }
            int bType = getType(world, i + deltaX, j, k + deltaZ);
            int tType = getType(world, i, j, k);
            if (world.getBlockId(i + deltaX, j, k + deltaZ) == this.blockID && tType == bType && (world.isBlockSolidOnSide(i + deltaX, j, k + deltaZ, ForgeDirection.DOWN))) { return true; }
        }

        if (player.isSneaking()) { return true; }

        if (world.isRemote)
        {
            return true;
        }
        else
        {
            TileEntity blockEntity = (TileEntity) world.getBlockTileEntity(i, j, k);
            if (blockEntity instanceof TileEntityLockedChest)
            {
                TileEntityLockedChest Chest = (TileEntityLockedChest) blockEntity;
                if (Chest.canAccess(player))
                {
                    player.openGui(GreaterSecurity.instance, CommonProxy.CHEST_GUI, world, i, j, k);
                    return true;
                }
                else
                {
                    player.sendChatToPlayer("Chest is Locked");
                    return true;
                }
            }
        }
        return true;
    }

    /**
     * each class overrdies this to return a new <className>
     */
    @Override
    public TileEntity createNewTileEntity(World par1World)
    {
        return new TileEntityLockedChest();
    }
}
