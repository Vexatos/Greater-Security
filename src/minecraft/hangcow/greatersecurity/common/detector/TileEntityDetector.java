package hangcow.greatersecurity.common.detector;

public class TileEntityDetector {
	
	private boolean isPowered = false;
	
	public boolean isDetectorPowered(TileEntityDetector tile){
		
		return this.isPowered;
		
	}
	
	public void setDetectorPowered(TileEntityDetector tile, boolean set){
		
		this.isPowered = set;
		
	}

}
