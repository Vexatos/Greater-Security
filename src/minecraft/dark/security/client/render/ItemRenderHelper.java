package dark.security.client.render;

import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

/** special tanks to Mekanism github */
public class ItemRenderHelper implements IItemRenderer
{

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type)
    {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
    {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data)
    {

    }

}
