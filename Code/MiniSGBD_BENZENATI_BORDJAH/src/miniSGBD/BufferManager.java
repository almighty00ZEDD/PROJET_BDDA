package miniSGBD;

public final class BufferManager {

	private static BufferManager INSTANCE;
	private static Frame [] frames; 
	
	private BufferManager() {
		frames  = new  Frame [DBParams.frameCount];
		
		for(int i = 0; i< DBParams.frameCount ; i++) {
			frames[i] = new Frame();
		}
	}
	
	public static BufferManager getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new BufferManager();
		}
		return INSTANCE;
	}
	

	public byte [] GetPage(PageId pi) {
		//page déja chargée donc présente dans le buffer pool
		for(int i  = 0;i< frames.length;i++) {
			if(frames[i].getPi() != null && frames[i].getPi().equals(pi)) {
				frames[i].increment_pin_count();
				return frames[i].getBuffer();
			}
		}
		
		//page non chargée
		//lecture disque imposée
		//case vide dispo : 
		for(int i = 0;i<frames.length;i++) {
			if(frames[i].getPin_count() == 0) {
				byte [] lecture = new byte[DBParams.pageSize];
				DiskManager.getInstance().ReadPage(pi, lecture);
				frames[i].setBuffer(lecture);
				frames[i].setPi(pi);
				frames[i].increment_pin_count();
				return frames[i].getBuffer();
			}
		}
		
		//Remplacement : CLOCK (focused on the ref_bit , works fine :p)
		boolean pageReplaced = false;
		while(!pageReplaced) {
			for(int i = 1; i< frames.length;i++) {
				 if(frames[i].getRef_bit() == 1) {
					 frames[i].setRef_bit(0);
				 }
				 else if(frames[i].getRef_bit() == 0) {
					 pageReplaced = true;
					 if(frames[i].isDirty()) {
						 DiskManager.getInstance().WritePage(frames[i].getPi(), frames[i].getBuffer());
					 }
					 byte [] lecture = new byte[DBParams.pageSize];
						DiskManager.getInstance().ReadPage(pi, lecture);
						frames[i].setBuffer(lecture);
						frames[i].setPi(pi);
						frames[i].increment_pin_count();
						return frames[i].getBuffer();
				 }
			}
		}
		return null;
	}
	
	void FreePage(PageId page,boolean valdirty) {
		for(int i = 0;i <frames.length;i++) {
			if(frames[i].getPi().equals(page)) {
				if(valdirty) frames[i].setDirty(true);
				if(frames[i].getPin_count() >= 0) frames[i].decrement_pin_count();
				if(frames[i].getPin_count() == 0) frames[i].setRef_bit(1);
			}
		}
	}
	
	void FlushAll() {
		for(int i = 0;i<frames.length;i++) {
			if(frames[i].isDirty()) {
				DiskManager.getInstance().WritePage(frames[i].getPi(), frames[i].getBuffer());
			}
			frames[i].reset();
		}
	}
	
	public void Init() {

	}
	
	public void Finish() {
		FlushAll();
	}

	
}
