package miniSGBD;
import java.util.Vector;
import java.util.StringTokenizer;

public final class DBManager {

	//unique instance
	private static DBManager INSTANCE;
	
	//Constructeur
	private  DBManager(){
		
	}
	
	//accès à l'instance unique du DBManager
	public static DBManager getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new DBManager();
		}
		
		return INSTANCE;
	}
	
	//Initialisation
	public void Init() {
		DBInfo.getInstance().Init();
		DBInfo.getInstance().Finish();
	}
	
	//Fin de tache
	public void Finish() {
		
	}
	
	//gestion des commandes
	public void ProcessCommand(String command) {
		//TODO juste un test :p
		int nb_colonnes = 0;
		Vector<String> nom_colonnes = new Vector<String>(0);
		Vector<String> type_colonnes = new Vector<String>(0);
		StringBuffer nom_relation = new StringBuffer();
		
		StringTokenizer st = new StringTokenizer(command," :");
		if(st.nextToken().equals("CREATEREL")) {
			nom_relation.append(st.nextToken());
			while(st.hasMoreTokens()) {
				nom_colonnes.add(st.nextToken());
				type_colonnes.add(st.nextToken());
				nb_colonnes++;
			}
			CreateRelation(nom_relation.toString(),nb_colonnes,nom_colonnes,type_colonnes);
		
			//not necessary just for debug
			DBInfo.getInstance().debug();
		}
		else System.out.print("commande invalide!");
		
	}
	
	public void CreateRelation(String nom_relation, int nb_colonnes, Vector<String> nom_colonnes, Vector<String> type_colonnes) {
		RelationInfo ri = new RelationInfo(nom_relation,nb_colonnes,nom_colonnes,type_colonnes);
		DBInfo.getInstance().AddRelation(ri);
	}
}
