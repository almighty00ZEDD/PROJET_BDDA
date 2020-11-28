package miniSGBD;

public class Rid {

	private PageId pageId;
	private int slotIdx;
	
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
