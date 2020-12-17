package miniSGBD;

public class Rid {

	private PageId pageId; //num de fichier de et page d'insertion d'un enregistrement
	private int slotIdx; //numéro du slot d'insertion
	
	public Rid(PageId pageIdx,int slotIdx) {
		this.setPageId(pageIdx);
		this.setSlotIdx(slotIdx);
	}

	public int getSlotIdx() {
		return slotIdx;
	}

	public void setSlotIdx(int slotIdx) {
		this.slotIdx = slotIdx;
	}

	public PageId getPageId() {
		return pageId;
	}

	public void setPageId(PageId pageId) {
		this.pageId = pageId;
	}
}
