package miniSGBD;
import java.util.Vector;
import java.util.ArrayList;
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
		
		StringTokenizer st = new StringTokenizer(command," :(),=");
		switch(st.nextToken()) {
		case "CREATEREL" : 
			commande_CREATEREL(st);
			break;
			
		case "RESET" :
			commande_RESET();
			break;
			
		case "INSERT" :
			commande_INSERT(st);
			break;
			
		case "BATCHINSERT" :
			//inserer le bon code :p
			break;
			
		case "SELECTALL" :
			commande_SELECTALL(st);
			break;
			
		case "SELECTS" :
			commande_SELECTS(st);
			break;
			
		default : 
			System.out.println("Commande invalide !");
		}
		BufferManager.getInstance().FlushAll();
	}
	
	public void commande_RESET() throws IOException {
		DBManager.getInstance().reset();
	}
	
	public void commande_CREATEREL(StringTokenizer st) {
		StringBuffer nom_relation = new StringBuffer();
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
	}
	
	public void commande_INSERT(StringTokenizer st) {
		StringBuffer sb = new StringBuffer();
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
		if("RECORD".equals(st.nextElement())) {
			while(st.hasMoreElements()) {
				record.addValue(st.nextElement().toString());
			}
		FileManager.getInstance().InsertRecordInRelation(record, sb.toString());
		}
		else {
			System.out.print("commande invalide!");
		}
	}
	
	public void commande_SELECTALL(StringTokenizer st) {
		StringBuffer s = new StringBuffer();
		if(st.nextElement().equals("FROM")) {
			s.append(st.nextElement().toString());
			System.out.println("Records de la relation "+ s.toString());
			ArrayList<Record> records = new ArrayList<Record>();
			records.addAll(FileManager.getInstance().SlectAllFromRelation(s.toString()));
			for(int i = 0; i< records.size();i++) {
				System.out.print("Record "+ (i+1) +": ");
				for(int j = 0;j < records.get(i).getValues().size();j++) {
					System.out.print(records.get(i).getValues().get(j));
					if(j != records.get(i).getValues().size() -1) {
						System.out.print(" ,");
					}else System.out.print(".");
				}
				System.out.println("");
			}
		}
		else {
			System.out.print("commande invalide!");
		}
	}
	
	public void commande_SELECTS(StringTokenizer st) {
		StringBuffer s = new StringBuffer();
		String parameter = null;
		String colonneF = null;
		int pos = 0;
		if(st.nextElement().equals("FROM")) {
			s.append(st.nextElement().toString());
			System.out.println("Resultats : ");
			ArrayList<Record> records = new ArrayList<Record>();
			
			//les records sans filtrage (SELECTALL style)
			records.addAll(FileManager.getInstance().SlectAllFromRelation(s.toString()));
			
			if(st.nextElement().equals("WHERE")) {
				colonneF = st.nextToken(); //s = nom colonne à filtrer
				parameter = st.nextToken(); //valeur de filtrage
				pos = 0;
				//recherche de la position de la colonne dans la relation
				for(int i = 0; i < DBInfo.getInstance().getListe().size();i++) {
					if(DBInfo.getInstance().getListe().get(i).getNom_Relation().equals(s.toString())) {
						for(int j = 0; j < DBInfo.getInstance().getListe().get(i).getNom_Colonnes().size();j++) {
							if(DBInfo.getInstance().getListe().get(i).getNom_Colonnes().get(j).equals(colonneF)) pos = j;
					
						}
					}
				}
			}
			
			
			for(int i = 0; i< records.size();i++) {
				if(records.get(i).getValues().get(pos).equals(parameter)) {
					System.out.print("Record "+ (i+1) + " : ");
				
				for(int j = 0;j < records.get(i).getValues().size();j++) {
					System.out.print(records.get(i).getValues().get(j));
					if(j != records.get(i).getValues().size() -1) {
						System.out.print(" ,");
					}else System.out.print(".");
				}
				System.out.println("");
				}
			}
		}
		
		else {
			System.out.print("commande invalide!");
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
		RelationInfo ri = new RelationInfo(nom_relation,nb_colonnes,nom_colonnes,type_colonnes,DBInfo.getInstance().getCompteur_relations() + 1,taille,slotc);
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
