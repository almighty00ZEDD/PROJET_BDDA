package miniSGBD;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class HeapFile {

	private RelationInfo relInfo;

	public HeapFile(RelationInfo relInfo) {
			setRelInfo(relInfo);
	}

	/**
	 * Création d'un nouveau fichier pour une relation dans la disque
	 */
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

	/**
	 * Ajout d'une page de données pour un fichier d'une relation
	 * @return
	 */
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

	/**
	 * Recherche d'une page de données libre
	 * @return le PageId de la page de donnée (numero du fichier et numéro de la page dans le fichier)
	 */
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

	/**
	 * Ecriture d'un enregistrement dans un fichier lié à une relation
	 * @param record l'enregistrement
	 * @param pageId les information de la page (numero du fichier et de la page dans le fichier)
	 * @return
	 */
	public Rid writeRecordToDataPage(Record record, PageId pageId) {
		Rid rid = new Rid(pageId, 0);
		HeaderPage header = new HeaderPage(relInfo.getFileIdx());
		ByteBuffer buff = ByteBuffer.wrap(BufferManager.getInstance().GetPage(pageId));
		byte[] buffHeaderPage = BufferManager.getInstance().GetPage(getHeaderPage(relInfo));
		header.fill(buffHeaderPage);

		// écriture à  fur et a mesure des records : slot d'écriture = nb max de slots
		// - nb slots libres puis fois record size
		int position = relInfo.getSlotCount() - (header.getSlotsLibres().get(pageId.getPageIdx()-1));
		
		rid.setSlotIdx(position);
		position *= (relInfo.getRecordSize() + 1);
		record.WriteToBuffer(buff, position);

		// décrémenter le nombre de slots libre de la page en question
		Integer nbrSlotLibre = header.getSlotsLibres().get(pageId.getPageIdx()-1);
		nbrSlotLibre -= 1;
		header.getSlotsLibres().set(pageId.getPageIdx() -1 , nbrSlotLibre);
		
		DiskManager.getInstance().WritePage(new PageId(0, relInfo.getFileIdx()), header.writeHeaderPage(buffHeaderPage));
		DiskManager.getInstance().WritePage(pageId, buff.array());
		BufferManager.getInstance().FreePage(getHeaderPage(relInfo), true);
		BufferManager.getInstance().FreePage(pageId, true);

		return rid;
	}

	/**
	 * Ecris un enregistrement directement dans un slot donné
	 * 
	 * @param record l'enregistrment
	 * @param pageId num du fichier et de la page
	 * @param slotnumber numéro du slot ou écrire l'enregistrement
	 * @return identifiant de l'enregistrement
	 */
	public Rid writeRecordToSlot(Record record, PageId pageId , int slotnumber) {
		Rid rid = new Rid(pageId, 0);
		HeaderPage header = new HeaderPage(relInfo.getFileIdx());
		ByteBuffer buff = ByteBuffer.wrap(BufferManager.getInstance().GetPage(pageId));
		byte[] buffHeaderPage = BufferManager.getInstance().GetPage(getHeaderPage(relInfo));
		header.fill(buffHeaderPage);

		// écriture à  fur et a mesure des records : slot d'écriture = nb max de slots - nb slots libres puis fois record size

		int position = 0;
		
		//formule calculé par tests fait sur une feuille ... difficile à expliquer
		if( (slotnumber % relInfo.getSlotCount()) == 0 ) position = relInfo.getSlotCount();
		else position = (slotnumber % relInfo.getSlotCount());
		
		rid.setSlotIdx(position);
		position *= (relInfo.getRecordSize() + 1);
		record.WriteToBuffer(buff, position);

		// décrémenter le nombre de slots libre de la page en question
		Integer nbrSlotLibre = header.getSlotsLibres().get(pageId.getPageIdx()-1);
		nbrSlotLibre -= 1;
		header.getSlotsLibres().set(pageId.getPageIdx() -1 , nbrSlotLibre);
		
		DiskManager.getInstance().WritePage(new PageId(0, relInfo.getFileIdx()), header.writeHeaderPage(buffHeaderPage));
		DiskManager.getInstance().WritePage(pageId, buff.array());
		BufferManager.getInstance().FreePage(getHeaderPage(relInfo), true);
		BufferManager.getInstance().FreePage(pageId, true);

		return rid;
	}
	
	
	/**
	 * Retourner tout les enregistrement dans une page de données
	 * @param pageId les information de la page (numero du fichier et de la page dans le fichier)
	 * @return liste des enregistrements présents dans cette page
	 */
	public ArrayList<Record> getRecordsInDataPage(PageId pageId) {
		// read data page with pageId
		ByteBuffer buff = ByteBuffer.wrap(BufferManager.getInstance().GetPage(pageId));
		// read header page
		HeaderPage header = new HeaderPage(relInfo.getFileIdx());
		byte[] buffHeaderPage = BufferManager.getInstance().GetPage(getHeaderPage(relInfo));
		header.fill(buffHeaderPage);
		// calculer le nombre de slots occupés
		int nbSlotsOccupes = relInfo.getSlotCount() - (header.getSlotsLibres().get(pageId.getPageIdx()-1));

		ArrayList<Record> liste_records = new ArrayList<Record>();
		int position = relInfo.getRecordSize() + 1;
		for (int i = 0; i < nbSlotsOccupes; i++) {
			Record r = new Record(relInfo);
			r.readFromBuffer(buff, position * i);
			liste_records.add(r);
		}

		// free data page
		BufferManager.getInstance().FreePage(pageId, false);
		BufferManager.getInstance().FreePage(header, false);

		return liste_records;
	}

	/**
	 * Insertion d'un enregistrement page de donnée libre
	 * @param record l'enregistrement
	 * @return identifiant pour l'enregistrement
	 */
	public Rid InserRecord(Record record) {
		PageId pageId = this.getFreeDataPageId();
		if (pageId == null) {
			pageId = this.addDataPage();
		}
		Rid rid = this.writeRecordToDataPage(record, pageId);

		return rid;
	}

	/**
	 * Retourne tout les enregistrements depuis un fichier lié à une relation
	 * @return liste des enregistrements
	 */
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

	/**
	 * PageId pour une header page (il n'a que le numero du fichier qui varie)
	 * @param relInfo la relation en question
	 * @return
	 */
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