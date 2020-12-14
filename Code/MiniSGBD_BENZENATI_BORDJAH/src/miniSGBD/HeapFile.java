package miniSGBD;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class HeapFile {

	private RelationInfo relInfo;

	public HeapFile(RelationInfo relInfo) {
			setRelInfo(relInfo);
	}

	// TODO
	public void createNewOnDisk() {
		
		DiskManager.getInstance().CreateFile(getRelInfo().getFileIdx());
		PageId pi = DiskManager.getInstance().AddPage(relInfo.getFileIdx());
		byte[] buffer = BufferManager.getInstance().GetPage(pi);

		for (int i = 0; i < buffer.length; i++) {
			buffer[i] = 0;
		}

		DiskManager.getInstance().WritePage(pi, buffer);
		BufferManager.getInstance().FreePage(pi, true);
	}

	// TODO
	public PageId addDataPage() {
		PageId pageId = DiskManager.getInstance().AddPage(relInfo.getFileIdx());
		// get buffer of header page
		byte[] buffHeaderPage = BufferManager.getInstance().GetPage(new PageId(0, relInfo.getFileIdx()));
		HeaderPage header = new HeaderPage(relInfo.getFileIdx());

		header.fill(buffHeaderPage);
		header.incrementerNBDataPages();
		header.addSlotLibre(relInfo.getSlotCount());

		DiskManager.getInstance().WritePage(new PageId(0, relInfo.getFileIdx()),
				header.writeHeaderPage(buffHeaderPage));
		BufferManager.getInstance().FreePage(header, true);

		return pageId;
	}

	public PageId getFreeDataPageId() {
		PageId pageId = null;
		byte[] buffHeaderPage = BufferManager.getInstance().GetPage(getHeaderPage(relInfo));

		HeaderPage header = new HeaderPage(relInfo.getFileIdx());

		header.fill(buffHeaderPage);

		for (int i = 0; i < header.getNbDataPages(); i++) {
			if (header.getSlotsLibres().get(i) > 0) {

				pageId = new PageId(i + 1, relInfo.getFileIdx());
			}
		}

		BufferManager.getInstance().FreePage(getHeaderPage(relInfo), false);

		return pageId;

	}

	// TODO position a modifier et slot sur le rid (place dispo)
	public Rid writeRecordToDataPage(Record record, PageId pageId) {
		Rid rid = new Rid(pageId, 0);
		HeaderPage header = new HeaderPage(relInfo.getFileIdx());
		byte[] buff = BufferManager.getInstance().GetPage(pageId);
		byte[] buffHeaderPage = BufferManager.getInstance().GetPage(getHeaderPage(relInfo));
		header.fill(buffHeaderPage);

		// Ã©criture Ã  fur et a mesure des records : slot d'Ã©criture = nb max de slots
		// - nb slots libres puis fois record size
		int position = relInfo.getSlotCount() - (header.getSlotsLibres().get(pageId.getPageIdx()-1)) + 1;
		rid.setSlotIdx(position);
		position *= (relInfo.getRecordSize() + 1);
		record.WriteToBuffer(ByteBuffer.wrap(buff), position);

		// décrémenter le nombre de slots libre de la page en question
		Integer nbrSlotLibre = header.getSlotsLibres().get(pageId.getPageIdx()-1);
		nbrSlotLibre -= 1;
		header.getSlotsLibres().set(pageId.getPageIdx() -1 , nbrSlotLibre);

		DiskManager.getInstance().WritePage(new PageId(0, relInfo.getFileIdx()),
				header.writeHeaderPage(buffHeaderPage));
		BufferManager.getInstance().FreePage(getHeaderPage(relInfo), true);
		BufferManager.getInstance().FreePage(pageId, true);

		return rid;
	}//INSERT INTO ZEDD RECORD (15,8,zedd)


	// TODO 80% sure!
	public ArrayList<Record> getRecordsInDataPage(PageId pageId) {
		// read data page with pageId
		byte[] buff = BufferManager.getInstance().GetPage(pageId);

		// read header page
		HeaderPage header = new HeaderPage(relInfo.getFileIdx());
		byte[] buffHeaderPage = BufferManager.getInstance().GetPage(getHeaderPage(relInfo));
		header.fill(buffHeaderPage);
		// calculer le nombre de slots occupés
		int nbSlotsOccupes = relInfo.getSlotCount() - (header.getSlotsLibres().get(pageId.getPageIdx()));

		ArrayList<Record> liste_records = new ArrayList<Record>();
		int position = relInfo.getRecordSize() + 1;
		for (int i = 0; i < nbSlotsOccupes; i++) {
			Record r = new Record(relInfo);
			r.readFromBuffer(ByteBuffer.wrap(buff), position * i);
			liste_records.add(r);
		}

		// free data page
		BufferManager.getInstance().FreePage(pageId, false);
		BufferManager.getInstance().FreePage(header, false);

		return liste_records;
	}

	// TODO reste Ã  savoir si la file existe!
	public Rid InserRecord(Record record) {
		PageId pageId = this.getFreeDataPageId();
		if (pageId == null) {
			pageId = this.addDataPage();
		}
		Rid rid = this.writeRecordToDataPage(record, pageId);

		return rid;
	}

	// listeDeRecordsGetAllRecords (), avec listeDeRecords une liste ou tableau de
	// Record.
	public ArrayList<Record> GetAllRecords() {
		ArrayList<Record> liste_tout_records = new ArrayList<Record>();

		// read header page
		HeaderPage header = new HeaderPage(relInfo.getFileIdx());
		byte[] buffHeaderPage = BufferManager.getInstance().GetPage(getHeaderPage(relInfo));
		header.fill(buffHeaderPage);
		int nbDataPages = header.getNbDataPages();
		
		// free hader page
		BufferManager.getInstance().FreePage(header, false);
		
		for(int i = 0; i < nbDataPages; i++) {
			ArrayList<Record> records  = getRecordsInDataPage(new PageId(i+1, relInfo.getFileIdx()));
			
			liste_tout_records.addAll(records);
		}

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