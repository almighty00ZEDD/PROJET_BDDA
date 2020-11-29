package miniSGBD;

import java.nio.ByteBuffer;
import java.util.ArrayList;

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
		HeaderPage header = new HeaderPage();
		byte [] buff= BufferManager.getInstance().GetPage(pageId);
		byte [] buff2 = BufferManager.getInstance().GetPage(getHeaderPage(relInfo));
		header.fill(buff2);
		//écriture à fur et a mesure des records : slot d'écriture = nb max de slots - nb slots libres puis fois record size
		int position = relInfo.getSlotCount() - (header.getSlotsLibres().get(pageId.getPageIdx()));
		rid.setSlotIdx(position);
		position *= (relInfo.getRecordSize() + 1);
		position++;
		record.WriteToBuffer(ByteBuffer.wrap(buff), position);
		BufferManager.getInstance().FreePage(getHeaderPage(relInfo), true);
		BufferManager.getInstance().FreePage(pageId, true);	
		return rid;
	}
	
	//TODO 80% sure!
	public ArrayList<Record> getRecordsInDataPage(PageId pageId){
		byte [] buff = BufferManager.getInstance().GetPage(pageId);
		ArrayList<Record> liste_records = new ArrayList<Record>();
		int position = (relInfo.getSlotCount() * relInfo.getRecordSize()) + 1;
		for(int i = 0; i < relInfo.getSlotCount() ; i++) {
				Record r = new Record(relInfo);
				r.readFromBuffer(ByteBuffer.wrap(buff), position * i);
				liste_records.add(r);
		}
		return liste_records;
	}
	
	//TODO reste à savoir si la file existe!
	public Rid InserRecord(Record record) {
		PageId pageId = this.getFreeDataPageId();
		if(pageId == null) {
			pageId = this.addDataPage();
		}
		Rid rid = this.writeRecordToDataPage(record, pageId);
		return rid;
	}
	
	//listeDeRecordsGetAllRecords (), avec listeDeRecords une liste ou tableau de Record.
	public ArrayList<Record> GetAllRecords(){
		ArrayList<Record> liste_tout_records = new ArrayList<Record>(0);
		/*
		*Charger la header page avec le buffer manager
		*
		*pour toute les pages : si il y'a des slots non libres extraire les records dessu!
		*/
		return liste_tout_records;
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
