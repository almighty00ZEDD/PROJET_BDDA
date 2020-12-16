package miniSGBD;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public final class DiskManager {

	private static DiskManager INSTANCE;
	
	private DiskManager() {
		
	}
	
	public static DiskManager getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new DiskManager();
		}
		return INSTANCE;
	}
	
	public void Init() {
		INSTANCE = new DiskManager();
	}
	
	public void Finish() {
		
	}
	
	/**
	 * Création d'un fichier qui contindra les enregistrements d'une relation liée à ce fichier
	 * @param fileIdx numéro du fichier
	 */
	void CreateFile (int fileIdx) {
		File newFileData = new File(DBParams.DBPath+"/Data_"+fileIdx+".rf");
		try {
			if(!newFileData.exists())
			newFileData.createNewFile();

		} catch(IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Ajout d'une page dans u fichier
	 * @param fileIdx numéro du fichier
	 * @return retourne le numéro de la page ajoutée et le numéro du fichier(PageId)
	 */
	public PageId AddPage (int fileIdx) {
		
		int nbPages = 0;
		try {
			//trouvera ou pas le fichier en question
			RandomAccessFile raf  = new RandomAccessFile(DBParams.DBPath+"/Data_"+fileIdx+".rf","rws");
			byte [] pageSize = new byte[DBParams.pageSize];
			//nombre de pages : nb octets deja présents / nb d'octets d'une page c'est aussi le numéro de la page à ajouter (on commence par page 0)
			 nbPages = (int) raf.length() / DBParams.pageSize;
			//début d'écriture en fin du flux présent
			raf.seek(nbPages * DBParams.pageSize);
			raf.write(pageSize);
			raf.close();
			
			
		}
		catch(FileNotFoundException fnf) {
			System.out.println("Erreur : Fichier non trouvé!");
		}
		catch (IOException e){
			System.out.println(e.getMessage());
		}
		
		PageId result = new PageId(nbPages,fileIdx);
		return result;
	}
	
	/**
	 * Lecture d'une page de données
	 * @param page PageId(numeros du fichier et de la page)
	 * @param buff le buffer
	 */
	void ReadPage(PageId page, byte [] buff) {
		try {
			RandomAccessFile raf  = new RandomAccessFile(DBParams.DBPath+"/Data_"+page.getFileIdx()+".rf","r");
			raf.seek(page.getPageIdx() * DBParams.pageSize);
			raf.read(buff);
			raf.close();
		}
		catch(IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Ecriture dans une page de données
	 * @param page PageId(numéros du fichier et de la page)
	 * @param buff le buffer
	 */
	void WritePage(PageId page, byte [] buff) {
		try {
			RandomAccessFile raf  = new RandomAccessFile(DBParams.DBPath+"/Data_"+page.getFileIdx()+".rf","rws");
			raf.seek(page.getPageIdx() * DBParams.pageSize);
			raf.write(buff);
			raf.close();
		}
		catch(IOException e) {
			System.out.println(e.getMessage());
		}
	}
}
