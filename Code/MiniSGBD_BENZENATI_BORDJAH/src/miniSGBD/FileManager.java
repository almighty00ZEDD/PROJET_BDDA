package miniSGBD;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;

public final class FileManager {

	private static FileManager INSTANCE;
	private ArrayList<HeapFile> heapFiles;
	
	private FileManager() {
		setHeapFiles(new ArrayList<HeapFile>(0));
	}
	public static FileManager getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new FileManager();
		}
		return INSTANCE;
	}
	public void Init() {
		DBInfo dbi = DBInfo.getInstance();
		int nb = dbi.getCompteur_relations();
		for(int i = 0;i<nb;i++) {
			HeapFile hf = new HeapFile(dbi.getListe().get(i));
			heapFiles.add(hf);
		}
	}
	
	public void Finish() {
		
	}
	
	/**
	 * Création d'un fichier qui contiendra les records de la relation (en binaire)
	 * @param relInfo la relation liée au fichié
	 */
	void CreateRelationFile(RelationInfo relInfo) {
		HeapFile hf = new HeapFile(relInfo);
		hf.createNewOnDisk();
		heapFiles.add(hf);
	}
	
	/**
	 * Insertion d'un enregistrement dans une relation et sa sauvegarde dans le bon fichier 
	 * @param record l'enregistrement (valeurs pour les colonnes)
	 * @param relName nom de la relation concernée
	 * @return identifiant de cet enregistrement
	 */
	Rid InsertRecordInRelation(Record record, String relName) {
		Rid rid = null;
		for(int i = 0;i < heapFiles.size();i++) {
			if(heapFiles.get(i).getRelInfo().getNom_Relation().equals(relName)) {
				rid = heapFiles.get(i).InserRecord(record);
			}
			
		}
		return rid;
	}
	
	/**
	 * lecture du fichier d'une relation pour y retourner tout ce qu'ils contiens comme enregistrements
	 * @param relName nom de la relation
	 * @return liste des enregistrements
	 */
	public ArrayList<Record> SlectAllFromRelation(String relName){
		ArrayList<Record> liste = new ArrayList<Record>(0);
		for(int i = 0;i < heapFiles.size();i++) {
			if(heapFiles.get(i).getRelInfo().getNom_Relation().equals(relName)) {
				liste = heapFiles.get(i).GetAllRecords();
			}
		}
		
		return liste;
	}
	
	public ArrayList<HeapFile> getHeapFiles() {
		return heapFiles;
	}
	public void setHeapFiles(ArrayList<HeapFile> heapFiles) {
		this.heapFiles = heapFiles;
	}
	
	/**
	 * Remise à neuf et supression de tout les fichiers et les enregistrements de la base de donnée
	 * @throws IOException
	 */
	public void reset() throws IOException{
		for(int i  = 0 ; i < heapFiles.size() ;i++) {
			int fileIdx = heapFiles.get(i).getRelInfo().getFileIdx();
			File f = new File(DBParams.DBPath +"/Data_"+fileIdx+".rf");
			if(f.exists()) f.delete();
		}
		heapFiles.clear();
	}
}
