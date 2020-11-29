package miniSGBD;
import java.util.ArrayList;

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
	
	//void CreateRelationFile (relInfo), 
	void CreateRelationFile(RelationInfo relInfo) {
		HeapFile hf = new HeapFile(relInfo);
		heapFiles.add(hf);
		hf.createNewOnDisk();
	}
	
	public ArrayList<HeapFile> getHeapFiles() {
		return heapFiles;
	}
	public void setHeapFiles(ArrayList<HeapFile> heapFiles) {
		this.heapFiles = heapFiles;
	}
}
