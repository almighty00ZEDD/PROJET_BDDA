package miniSGBD;

public final class BufferManager {

	private static BufferManager INSTANCE;
	
	private BufferManager() {
		
	}
	
	public BufferManager getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new BufferManager();
		}
		return INSTANCE;
	}
	
	// TODO !!! vraiment à revoir !!! 
	public byte [] GetPage(PageId pi) {
		byte [] recuperation = new byte [DBParams.pageSize]; //sera libéré à la fin de la fonction donc pas de duplication!
		DiskManager.getInstance().ReadPage(pi, recuperation);
		return recuperation;
	}
	
	void FreePage(PageId page,boolean valdirty) {
		
	}
	
	void FlushAll() {
		
	}
	public void Init() {
		
	}
	
	public void Finish() {
		
	}
}
