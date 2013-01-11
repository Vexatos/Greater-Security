package hangcow.greatersecurity.client.render;

import hangcow.greatersecurity.common.chest.TileEntityLChest;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LChestItemRenderHelper
{
    /** The static instance of ChestItemRenderHelper. */
    public static LChestItemRenderHelper instance = new LChestItemRenderHelper();
    private TileEntityLChest field_78543_b = new TileEntityLChest();

    /**
     * Renders a chest at 0,0,0 - used for item rendering
     */
    public void renderChest(Block par1Block, int par2, float par3)
    {
            TileEntityRenderer.instance.renderTileEntityAt(this.field_78543_b, 0.0D, 0.0D, 0.0D, 0.0F);
    }
}
