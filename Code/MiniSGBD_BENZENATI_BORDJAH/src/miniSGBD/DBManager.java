package miniSGBD;
import java.util.Vector;
import java.io.IOException;
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
		FileManager.getInstance().Init();
	}
	
	//Fin de tache
	public void Finish() {
		BufferManager.getInstance().FlushAll();
	}
	
	//gestion des commandes
	public void ProcessCommand(String command) throws IOException{

		
		StringBuffer nom_relation = new StringBuffer();
		
		StringTokenizer st = new StringTokenizer(command," :(),");
		switch(st.nextToken()) {
		case "CREATEREL" : 
			int nb_colonnes = 0;
			Vector<String> nom_colonnes = new Vector<String>(0);
			Vector<String> type_colonnes = new Vector<String>(0);
			nom_relation.append(st.nextToken());
			while(st.hasMoreTokens()) {
				nom_colonnes.add(st.nextToken());
				type_colonnes.add(st.nextToken());
				nb_colonnes++;
			}
			CreateRelation(nom_relation.toString(),nb_colonnes,nom_colonnes,type_colonnes);
			break;
			
		case "RESET" :
			DBManager.getInstance().reset();
			break;
			
		case "INSERT" :
			StringBuffer sb = null;
			//pour éviter de réécrire :p
			DBInfo DBI = DBInfo.getInstance();
			Record record = null;
			if(st.nextToken().equals("INTO")) {
				sb = new StringBuffer(st.nextToken().toString());
				for(int i = 0;i<DBI.getListe().size();i++) {
					if(DBI.getListe().get(i).getNom_Relation().equals(sb.toString())) {
						record = new Record(DBI.getListe().get(i));
					}
				}
			}
			if(st.nextToken().equals("RECORD")) {
				while(st.hasMoreElements()) {
					record.addValue(st.nextElement().toString());
				}
				FileManager.getInstance().InsertRecordInRelation(record, sb.toString());
			}
			else {
				System.out.print("commande invalide!");
			}
			break;
			
		case "BATCHINSERT" :
			//inserer le bon code :p
			break;
			
		case "SELECTALL" :
			//inserer le bon code :p
			break;
			
		case "SELECTS" :
			//inserer le bon code :p
			break;
			
		default : System.out.print("commande invalide!");
		}
	}
	
	
	public void CreateRelation(String nom_relation, int nb_colonnes, Vector<String> nom_colonnes, Vector<String> type_colonnes) {

		int taille = 0;
		for(int i = 0; i<type_colonnes.size();i++) {
			switch(type_colonnes.get(i)) {
			case "int"  : taille += 4;break;
			case "float" : taille += 4;break;
			default : String s = type_colonnes.get(i).split("string")[1];
			taille += (2 * Integer.parseInt(s));
			}
		}
		//recordSize+1 je crois : p
		int slotc = DBParams.pageSize / (taille + 1);
		RelationInfo ri = new RelationInfo(nom_relation,nb_colonnes,nom_colonnes,type_colonnes,DBInfo.getInstance().getCompteur_relations(),taille,slotc);
		ri.debug();
		FileManager.getInstance().CreateRelationFile(ri);
		DBInfo.getInstance().AddRelation(ri);
	}
	
	public void reset() throws IOException {
		BufferManager.getInstance().reset();
		DBInfo.getInstance().reset();
		FileManager.getInstance().reset();
	}
}
