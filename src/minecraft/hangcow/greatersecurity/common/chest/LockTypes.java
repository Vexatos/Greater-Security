package hangcow.greatersecurity.common.chest;

/**
 * Should only be used with the GS gui manager as the ID are linked to it
 * 
 * @author DarkGuardsman
 * 
 */
public enum LockTypes
{
	CLASSIC("Key", -1), NAME("User", -1), PIN("Code", -1);

	String name;
	int GUI_ID;

	/**
	 * @param name - reference and unlocalized gui name
	 * @param GUI_ID - id reference for the gui manager
	 */
	private LockTypes(String name, int GUI_ID)
	{
		this.name = name;
		this.GUI_ID = GUI_ID;
	}
}
