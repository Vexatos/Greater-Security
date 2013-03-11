package hangcow.serversuit.commands.blockLogger;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import universalelectricity.core.vector.Vector3;

public class ActionInstance
{
    public enum ClickAction
    {
        OPENED("Opened"), LEFT("LClicked"), RIGHT("RClicked"), DESTROYED("Destroyed"), PLACED("Placed");
        public String name;

        private ClickAction(String name)
        {
            this.name = name;
        }
    }

    public Vector3 location;
    public Block block;
    public ClickAction action;
    public String time;
    public EntityPlayer player;

    public ActionInstance(Vector3 loc, Block block, ClickAction act, String time, EntityPlayer player)
    {
        this.location = loc;
        this.block = block;
        this.action = act;
        this.time = time;
        this.player = player;
    }

}
