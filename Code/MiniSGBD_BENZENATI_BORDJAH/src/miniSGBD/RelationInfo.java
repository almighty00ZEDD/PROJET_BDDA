package miniSGBD;
import java.util.Vector;

public class RelationInfo {

	private String nom_relation;
	private int nb_colonnes;
	private Vector<String> nom_colonnes ;
	private Vector<String> type_colonnes;
	
	// TODO nom et type colonnes : carrément pas sur que ça sois correcte :p
	public RelationInfo(String nom_relation,int nb_colonnes,Vector<String> nom_colonnes,Vector<String> type_colonnes) {
		this.nom_relation = nom_relation;
		this.nb_colonnes = nb_colonnes;
		this.nom_colonnes = nom_colonnes;
		this.type_colonnes = type_colonnes;
	}
	
	//debuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuug pas canon :p
	public void debug() {
		System.out.println("nom : " + nom_relation);
		System.out.println("nombre de colonnes : " + nb_colonnes);

		for(int i = 0;i<nom_colonnes.size();i++) {
			System.out.println(nom_colonnes.elementAt(i)+"\t"+ type_colonnes.elementAt(i));
		}
	}
}
