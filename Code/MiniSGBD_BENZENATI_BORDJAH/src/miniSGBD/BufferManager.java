package miniSGBD;

public final class BufferManager {

	private static BufferManager INSTANCE;
	private static Frame[] frames;
	private int temps;

	private BufferManager() {
		this.temps = 0;
		frames = new Frame[DBParams.frameCount];

		for (int i = 0; i < DBParams.frameCount; i++) {
			frames[i] = new Frame();
		}
	}

	public static BufferManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new BufferManager();
		}
		return INSTANCE;
	}

	public byte[] GetPage(PageId pi) {
		// incrémenter temps
		temps++;

		// page dÃ©ja chargÃ©e donc prÃ©sente dans le buffer pool
		for (int i = 0; i < frames.length; i++) {
			if (frames[i].getPi() != null && frames[i].getPi().equals(pi)) {
				frames[i].increment_pin_count();
				return frames[i].getBuffer();
			}
		}

		// page non chargÃ©e
		// lecture disque imposÃ©e
		// case vide dispo :
		for (int i = 0; i < frames.length; i++) {
			if (frames[i].getPin_count() == 0) {
				DiskManager.getInstance().ReadPage(pi, frames[i].getBuffer());
				frames[i].setPi(pi);
				frames[i].increment_pin_count();
				return frames[i].getBuffer();
			}
		}

		// page non chargé et bufferpool complet
		// politique LRU
		// remplacement quand y a pas de place
		int indice = 0;
		int tempsMin = 0;
		for (int i = 0; i < frames.length; i++) {

			if (frames[i].getPin_count() == 0) {
				if (frames[i].getPin_count_zero() < tempsMin) {
					tempsMin = frames[i].getPin_count_zero();
					indice = i;
				}
			}
		}

		if (frames[indice].isDirty()) {
			DiskManager.getInstance().WritePage(frames[indice].getPi(), frames[indice].getBuffer());
		}

		DiskManager.getInstance().ReadPage(pi, frames[indice].getBuffer());
		frames[indice].setPi(pi);
		frames[indice].setRef_bit(0);
		frames[indice].setDirty(false);
		frames[indice].setPin_count_zero(0);
		frames[indice].setPin_count(1);
		return frames[indice].getBuffer();

	}

	void FreePage(PageId page, boolean valdirty) {
		temps++;
		for (int i = 0; i < frames.length; i++) {
			if (frames[i].getPi() != null && frames[i].getPi().equals(page)) {
				frames[i].setDirty(valdirty);
				frames[i].decrement_pin_count();
				if (frames[i].getPin_count() == 0) {
					frames[i].setRef_bit(1);
					frames[i].setPin_count_zero(temps);
				}
			}
		}
	}

	void FlushAll() {
		for (int i = 0; i < frames.length; i++) {
			if (frames[i].isDirty()) {
				DiskManager.getInstance().WritePage(frames[i].getPi(), frames[i].getBuffer());
			}
			frames[i].reset();
		}
	}

	public void Init() {

	}

	public void Finish() {

	}
	public void reset(){
		for(int i = 0; i< frames.length;i++) {
			frames[i].reset();
		}
	}
}