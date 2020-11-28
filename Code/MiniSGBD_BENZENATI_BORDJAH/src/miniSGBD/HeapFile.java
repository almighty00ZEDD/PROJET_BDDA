package miniSGBD;

import java.nio.ByteBuffer;

public class HeapFile {

	private RelationInfo relInfo;

	public HeapFile(RelationInfo relIfo) {
		setRelInfo(relInfo);
	}

	//TODO
	public void createNewOnDisk() {
		DiskManager.getInstance().CreateFile(relInfo.getFileIdx());
		DiskManager.getInstance().WritePage(getHeaderPage(relInfo), new byte[DBParams.pageSize]);
		byte[] contenu = BufferManager.getInstance().GetPage(getHeaderPage(relInfo));
		for (int i = 0; i < DBParams.pageSize; i++) {
			contenu[i] = (byte) 0;
		}
		
		BufferManager.getInstance().FreePage(getHeaderPage(relInfo), true);
	}
	//TODO
	public PageId addDataPage() {
		PageId result = DiskManager.getInstance().AddPage(relInfo.getFileIdx());
		byte [] buff = BufferManager.getInstance().GetPage(result);
		HeaderPage header = new HeaderPage();
		header.fill(buff);
		buff = header.newPage(buff, relInfo);
		DiskManager.getInstance().WritePage(result, buff);
		BufferManager.getInstance().FreePage(result, true);
		return result;
	}
	
	public PageId getFreeDataPageId() {
		byte [] buff = new byte[DBParams.pageSize];
		buff = BufferManager.getInstance().GetPage(getHeaderPage(relInfo));
		HeaderPage header = new HeaderPage();
		header.fill(buff);
		int pagenumber = header.getAvailablePage(relInfo);
		BufferManager.getInstance().FreePage(getHeaderPage(relInfo), true);
		if(pagenumber == 0) {
			return null;
		}
		else {
			return new PageId(pagenumber,relInfo.getFileIdx());
		}
		
	}
	
	//TODO position a modifier et slot sur le rid (place dispo)
	public Rid writeRecordToDataPage(Record record, PageId pageId) {
		Rid rid = new Rid(pageId,0);
		byte [] buff= BufferManager.getInstance().GetPage(pageId);
		record.WriteToBuffer(ByteBuffer.wrap(buff), 0);
		DiskManager.getInstance().WritePage(pageId, buff);
		BufferManager.getInstance().FreePage(pageId, true);	
		return rid;
	}

	private PageId getHeaderPage(RelationInfo relInfo) {
		return new PageId(0, relInfo.getFileIdx());
	}

	public RelationInfo getRelInfo() {
		return relInfo;
	}

	public void setRelInfo(RelationInfo relInfo) {
		this.relInfo = relInfo;
	}

}
