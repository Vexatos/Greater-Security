package dark.security.api;

public interface IPortcullis
{

    public int getMaxSize();

    /** tells the portcullis to extend up wards, can also be used to check if it even can extend
     * 
     * @param doExtend - should it actual extend
     * @return */
    public boolean extend(boolean doExtend);

    /** tells the portcullis to retract into itself, can also be used to check if it even can
     * 
     * @param doRetract - should it actual retract
     * @return */
    public boolean retract(boolean doRetract);

    public boolean isExtended();

}
