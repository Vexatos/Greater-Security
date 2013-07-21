package dark.security.common.chest;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public enum HardnessTiers
{
	// TODO Implement and add values
	WOOD("Wood", 100, 100, false, Block.planks, 4),
	STONE("Stone", 0, 0, false, Block.stoneBrick, 4),
	COPPER("Copper", 0, 0, true, "CopperPlates", 4),
	BRONZE("Bronze", 0, 0, true, "BronzePlates", 4),
	IRON("Iron", 0, 0, true),
	STEEL("Steel", 0, 0, true),
	DIAMOND("Crystal", 0, 0, false),
	VOID("Void", 0, 0, false);

	public String name;
	public float resistance;
	public float hardness;

	/** @param name - name for reference and unlocalized translation
	 * @param resistance - resistance to explosions
	 * @param hardness - hardness to mining
	 * @param upgradecost - cost in items to upgrade can use Item, Block, ItemStack, or OreNames */
	private HardnessTiers(String name, float resistance, float hardness, boolean returnOnUpgrade, Object... upgradecost)
	{
		this.name = name;
		this.resistance = resistance;
		this.hardness = hardness;
	}

	public List<ItemStack> getUpgradeItems()
	{
		List<ItemStack> item = new ArrayList<ItemStack>();

		// TODO edit autocrafting manager to have a method to turn an object array into an item list

		return item;
	}

	public static HardnessTiers get(int i)
	{
		if (i < HardnessTiers.values().length)
		{
			return HardnessTiers.values()[i];
		}
		return WOOD;
	}
}
