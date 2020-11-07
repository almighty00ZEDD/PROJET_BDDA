package miniSGBD;

public class PageId {

	/**
	 * numero de la page et identifiant du fichier relation
	 */
	//+ potentiellement pin_count
	private int PageIdx,FileIdx;

	
	public PageId(int PageIdx,int FileIdx) {
		setPageIdx(PageIdx);
		setFileIdx(FileIdx);
	}
	public int getPageIdx() {
		return PageIdx;
	}

	public void setPageIdx(int pageIdx) {
		PageIdx = pageIdx;
	}

	public int getFileIdx() {
		return FileIdx;
	}

	public void setFileIdx(int fileIdx) {
		FileIdx = fileIdx;
	}
}
