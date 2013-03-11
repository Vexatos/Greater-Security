package hangcow.serversuit.commands.Managers;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import universalelectricity.core.vector.Vector3;

public class ActionInstance
{
    public enum action
    {
        OPENED("Opened"), LCLICKED("LClicked"), RCLICKED("RClicked"), DESTROYED("Destroyed"), PLACED("Placed");
        public String name;

        private action(String name)
        {
            this.name = name;
        }
    }

    public Vector3 loc;
    public Block block;
    public action act;
    public String time;
    public EntityPlayer player;

    public ActionInstance(Vector3 loc, Block block, action act, String time, EntityPlayer player)
    {
        this.loc = loc;
        this.block = block;
        this.act = act;
        this.time = time;
        this.player = player;
    }

}
