package miniSGBD;
import java.io.Serializable;
import java.util.Vector;

public class RelationInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String nom_relation;
	private int nb_colonnes;
	private Vector<String> nom_colonnes ;
	private Vector<String> type_colonnes;
	
	private int fileIdx,recordSize,slotCount;
	
	// TODO nom et type colonnes : carrément pas sur que ça sois correcte :p
	public RelationInfo(String nom_relation,int nb_colonnes,Vector<String> nom_colonnes,Vector<String> type_colonnes,int fileIdx,int recordSize,int slotCount) {
		this.nom_relation = nom_relation;
		this.nb_colonnes = nb_colonnes;
		this.nom_colonnes = nom_colonnes;
		this.type_colonnes = type_colonnes;
		this.setFileIdx(fileIdx);
		this.setRecordSize(recordSize);
		this.setSlotCount(slotCount);
	}
	
	//just for the test parts :p
	public RelationInfo() {
		this.nom_relation = "NOM RELATION ";
		this.nb_colonnes = 0;
		this.nom_colonnes = new Vector<String>(0);
		this.type_colonnes = new Vector<String>(0);
		debug();
	}
	// Appel de la méthode dans le main , sert uniquement au test :p
	public void debug() {
		System.out.println("nom : " + nom_relation);
		System.out.println("nombre de colonnes : " + nb_colonnes);

		for(int i = 0;i<nom_colonnes.size();i++) {
			System.out.println(nom_colonnes.elementAt(i)+"\t"+ type_colonnes.elementAt(i));
		}
	}
	
	public String getNom_Relation() {
		return nom_relation;
	}
	public Vector<String> getNom_Colonnes(){
		return nom_colonnes;
	}
	
	public Vector<String> getType_Colonnes(){
		return type_colonnes;
	}

	public int getSlotCount() {
		return slotCount;
	}

	public void setSlotCount(int slotCount) {
		this.slotCount = slotCount;
	}

	public int getRecordSize() {
		return recordSize;
	}

	public void setRecordSize(int recordSize) {
		this.recordSize = recordSize;
	}

	public int getFileIdx() {
		return fileIdx;
	}

	public void setFileIdx(int fileIdx) {
		this.fileIdx = fileIdx;
	}
}
