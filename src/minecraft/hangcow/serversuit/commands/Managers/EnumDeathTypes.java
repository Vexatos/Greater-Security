package hangcow.serversuit.commands.Managers;

import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;

public enum EnumDeathTypes
{
        UNKOWN("General", "GPUKDeaths"),
        PVP("PVP", "GPPVPKills"),
        CREEPER("creepers", "GPCrDeaths"), 
        ENVIRONMENTAL("Environmental", "GPErDeaths"), 
        MOBS("mobs", "GPmobDeaths");
        public String name;
        public String saveName;
       

        private EnumDeathTypes(String name, String saveName)
        {
            this.name = name;
            this.saveName = saveName;
        }

        /**
         * gets the death type based off of the damage source
         */
        public static EnumDeathTypes getTypes(DamageSource source)
        {
            if (source.getEntity() instanceof EntityPlayer)
            {
                return PVP;
            }
            else if (source.getEntity() instanceof EntityCreeper)
            {
                return CREEPER;
            }
            else if (source.getEntity() instanceof EntityMob || source.getEntity() instanceof EntitySlime) { return MOBS; }

            return UNKOWN;
        }

        /**
         * @return the DeathType with the same name
         */
        public static EnumDeathTypes getDeathByName(String name)
        {
            for (int i = 0; i < EnumDeathTypes.values().length; i++)
            {
                if (EnumDeathTypes.values()[i].name.equalsIgnoreCase(name)) { return EnumDeathTypes.values()[i]; }
            }

            return UNKOWN;
        }
}
