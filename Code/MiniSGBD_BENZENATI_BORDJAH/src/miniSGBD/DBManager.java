package miniSGBD;
import java.util.Vector;
import java.util.ArrayList;
import java.io.IOException;
import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

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
		DBInfo.getInstance().Finish();
	}
	
	/**
	 * Prise en compte de la commande et appel des fonctions qui les générent en dessous si commande correctemet saisie
	 * @param command ligne de commande saisie au clavier
	 * @throws IOException
	 */
	public void ProcessCommand(String command) throws IOException{
		
		StringTokenizer st = new StringTokenizer(command," :(),");
		switch(st.nextToken()) {
		case "CREATEREL" : 
			commande_CREATEREL(st);
			break;
		
		case "EXIT":
			System.exit(0);
			break;
			
		case "RESET" :
			commande_RESET();
			break;
			
		case "INSERT" :
			commande_INSERT(st);
			break;
			
		case "BATCHINSERT" :
			commande_BATCHINSERT(st);
			break;
			
		case "SELECTALL" :
			commande_SELECTALL(st);
			break;
		
		case "SELECTC" :
			commande_SELECTC(st);
			//System.out.println("Ne marche pas.......");
			break;
		
		case "UPDATE" :
			commande_UPDATE(st);
			break;
			
		case "SELECTS" :
			commande_SELECTS(st);
			break;
			
		default : 
			System.out.println("Commande invalide !");
		}
		BufferManager.getInstance().FlushAll();
	}
	
	/**
	 * Commande de remise à neuf de la base de données
	 * @throws IOException
	 */
	public void commande_RESET() throws IOException {
		DBManager.getInstance().reset();
	}
	
	public void commande_UPDATE(StringTokenizer st) {
		int totalUpdated = 0;
		StringBuffer relName = new StringBuffer(st.nextToken());
		String parameter = null, parameter2 = null;
		String colonneF1 = null , colonneF2 = null;
		int pos1 = 0,pos2 = 0;
		if(st.nextToken().equals("SET")) {
			ArrayList<Record> records = new ArrayList<Record>(0);
			ArrayList<Integer> slot_number = new ArrayList(0);// pour garder une trace du numero de slot pour chaque record 
			
			
				//les records sans filtrage (SELECTALL style)
			records.addAll(FileManager.getInstance().SlectAllFromRelation(relName.toString()));
			
			for(int i = 1; i <= records.size();i++) {
				slot_number.add(i);
			}
			
			StringBuffer critere1 = new StringBuffer(st.nextToken()); //C2=v2 (nouvelle modif)
			if(st.nextToken().equals("WHERE")) {
				StringBuffer critere2 = new StringBuffer(st.nextToken());
				
				parameter2 = critere2.toString().split("=")[1]; parameter = critere1.toString().split("=")[1];
				colonneF2 = critere2.toString().split("=")[0];colonneF1 = critere1.toString().split("=")[0];

				//recherche de la position de la colonne dans la relation
				for(int i = 0; i < DBInfo.getInstance().getListe().size();i++) {
					if(DBInfo.getInstance().getListe().get(i).getNom_Relation().equals(relName.toString())) {
						for(int j = 0; j < DBInfo.getInstance().getListe().get(i).getNom_Colonnes().size();j++) {
							if(DBInfo.getInstance().getListe().get(i).getNom_Colonnes().get(j).equals(colonneF1)) pos1 = j;
						
							if(DBInfo.getInstance().getListe().get(i).getNom_Colonnes().get(j).equals(colonneF2)) pos2 = j;
						}
					}
				}

				//filtrage des records (filtrage WHERE de la commande Update)
				for(int i  = 0; i<records.size();i++) {
					if(!records.get(i).getValues().get(pos2).equals(parameter2)) {
						records.remove(i);
						slot_number.remove(i);//garde la trace du slot je rappel ;)
						i--; //ajustement sinon on skip le prochain :p
					}
				}
				
				
				//jusqu'ici records est trié ET nous conaissant les numéros des slots ou écrire
				//reste a connaitre fileidx, page idx, la réponse est dans la DBInfo :)
				int fileIdx = 0,slotCount = 0, numRelation = 0;
				for(int i = 0; i < DBInfo.getInstance().getListe().size() ;i++) {
					if(DBInfo.getInstance().getListe().get(i).getNom_Relation().equals(relName.toString())) {
						numRelation = i;
						fileIdx = DBInfo.getInstance().getListe().get(i).getFileIdx();
						slotCount = DBInfo.getInstance().getListe().get(i).getSlotCount();
					}
				}
				
				//nous avont fileIdx et nous savons grace à slotCount sur quelle page est un records en regardant sa position! (sa position est dans slot_number ;) )

				for(int i  = 0; i<records.size();i++) {
					
					records.get(i).getValues().set(pos1, parameter); //ajustement du slot
					
					int pageIdx = slot_number.get(i)/slotCount;
						
					if( (slot_number.get(i) % slotCount) != 0) pageIdx++;
						
						//écriture dans le slot
						FileManager.getInstance().getHeapFiles().get(numRelation).writeRecordToSlot(records.get(i) ,new PageId(fileIdx,pageIdx), slot_number.get(i));
						totalUpdated++;
				}
				
				System.out.println("Total updated records : " + totalUpdated);
			}
		}
	}
	/**
	 * Commande de création d'une relation
	 * @param st ligne délimitée par les séparateurs
	 */
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
		DBManager.getInstance().Finish();
	}
	
	/**
	 * Commande d'insertion simple (une ligne)
	 * @param st ligne délimitée par les séparateurs
	 */
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
		DBManager.getInstance().Finish();
	}
	
	/**
	 * Commande d'insertion multiple depuis un fichier texte
	 * @param st ligne délimitée par les séparateurs
	 * @throws IOException
	 */
	public void commande_BATCHINSERT(StringTokenizer st) throws IOException {
		String nom_relation = null, path = null;
		if(st.nextToken().equals("INTO")) {
			nom_relation = st.nextToken();
			if(st.nextToken().equals("FROM") && st.nextToken().equals("FILE")) {
				path = "/home/zedd/Bureau/Projet_BDDA_BENZENATI_BORDJAH/" + st.nextToken();
				File f = new File(path);
				FileReader fr = new FileReader(f);
				BufferedReader br = new BufferedReader(fr);
				String line;
				while( (line = br.readLine() ) != null)
				 {
					//découper ici avec un StringTokenizer engendre des bugs (tokens vides sans raisons) d'ou je contourne le problème
					StringBuffer s = new StringBuffer("INSERT INTO "+ nom_relation + " RECORD("+line+")");
					ProcessCommand(s.toString());
				}
			
				fr.close();
				br.close();
				DBManager.getInstance().Finish();
			}
		}
	}
	
	/**
	 * Commande de séléction totale
	 * @param st ligne de commande délimitée
	 */
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
	
	/**
	 * Commande de selection simple
	 * @param st ligne de commande délimitée par les séparateurs
	 */
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
				StringTokenizer st2 = new StringTokenizer(st.nextToken(),"=");
				colonneF = st2.nextToken(); //s = nom colonne à filtrer
				parameter = st2.nextToken(); //valeur de filtrage
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
	
	public void commande_SELECTC(StringTokenizer st) {
		StringBuffer s = new StringBuffer();
		if(st.nextElement().equals("FROM")) {
			s.append(st.nextElement().toString());
			System.out.println("Resultats : ");
			ArrayList<Record> records = new ArrayList<Record>();
			
			//les records sans filtrage (SELECTALL style)
			records.addAll(FileManager.getInstance().SlectAllFromRelation(s.toString()));
			
			ArrayList<String> whereAnd = new ArrayList<String>(); whereAnd.add("WHERE"); whereAnd.add("AND");
			while(st.hasMoreTokens() && whereAnd.contains(st.nextToken().toString())) {
				ArrayList<Record> records_filtres = new ArrayList<Record>(0);
				records_filtres.addAll(filtrage(st,s.toString(),records)); //récupération après filtrage des record
				records.clear(); //vider la liste des records avant le nouveau filtre
				records.addAll(records_filtres); //ajouter les records filtrés pour un nouveau filtrage
			}
			
			//affichage
			for(int i = 0; i < records.size();i++) {
				System.out.print("Record "+ (i+1)+" : ");
				for(int j = 0; j < records.get(i).getValues().size();j++) {
					System.out.print(records.get(i).getValues().get(j)+", ");
				}
				System.out.println("");
			}
		}				
}
	
	private ArrayList<Record> filtrage(StringTokenizer st, String s,ArrayList<Record> recordss){
		String parameter = null;
		String colonneF = null;
		ArrayList<Record> records= new ArrayList<Record>();
		records.addAll(recordss);
		String filter = st.nextToken();
		String op = "";
		if(filter.contains("=")) op ="=";
		if(filter.contains(">")) op =">";
		if(filter.contains("<")) op ="<";
		if(filter.contains("<=")) op ="<=";
		if(filter.contains(">=")) op =">=";

		StringTokenizer st2 = new StringTokenizer(filter,op);
		colonneF = st2.nextToken(); //s = nom colonne à filtrer
		parameter = st2.nextToken(); //valeur de filtrage
		int pos = 0;
		//recherche de la position de la colonne dans la relation
		for(int i = 0; i < DBInfo.getInstance().getListe().size();i++) {
			if(DBInfo.getInstance().getListe().get(i).getNom_Relation().equals(s.toString())) {
				for(int j = 0; j < DBInfo.getInstance().getListe().get(i).getNom_Colonnes().size();j++) {
					if(DBInfo.getInstance().getListe().get(i).getNom_Colonnes().get(j).equals(colonneF)) {
						pos = j;
					}
				}
			}
		}
		
		//SELECTC FROM S WHERE C2=100 AND C3=Nati
		//filtrage dans records avec param
		for(int i = 0; i< records.size();i++) {
			
			switch(op) {
			 case "=" :
				 if(!records.get(i).getValues().get(pos).equals(parameter)) {					
					 records.remove(records.get(i));//enlever de la liste s'il ne reponds pas au criteres
					 i--; //le filtrage ne marchais pas ....voila pourquoi
				 }
				 break;
				
			 case "<" :
				 if(!(Double.parseDouble(records.get(i).getValues().get(pos)) < Double.parseDouble(parameter)) ) {					
					 records.remove(i);//enlever de la liste s'il ne reponds pas au criteres
					 i--;
				 }
				 break;
			
			 case ">" :
				 if(!(Double.parseDouble(records.get(i).getValues().get(pos)) > Double.parseDouble(parameter)) ) {					
					 records.remove(i);//enlever de la liste s'il ne reponds pas au criteres
					 i--;
				 }
				 break;
			 
			 case "<=" :
				 if(!(Double.parseDouble(records.get(i).getValues().get(pos)) <= Double.parseDouble(parameter)) ) {					
					 records.remove(i);//enlever de la liste s'il ne reponds pas au criteres
					 i--;
				 }
				 break;
			 
			 case ">=" :
				 if(!(Double.parseDouble(records.get(i).getValues().get(pos)) >= Double.parseDouble(parameter)) ) {					
					 records.remove(i);//enlever de la liste s'il ne reponds pas au criteres
					 i--;
				 }
				 break;
				 
			 default : System.out.println("erreur !");break;
			}
		}
		return records;
	}	
	//affichage du resultat


		
		
		
	
	/**
	 * Création d'une relation et calcul des paramètres ngendrés dans la base de données
	 * @param nom_relation nom de la relation
	 * @param nb_colonnes nombre de colonnes
	 * @param nom_colonnes nom de chaque colonnes
	 * @param type_colonnes type de chaque colonne
	 */
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
		
		int slotc = DBParams.pageSize / (taille + 1);
		RelationInfo ri = new RelationInfo(nom_relation,nb_colonnes,nom_colonnes,type_colonnes,DBInfo.getInstance().getCompteur_relations() + 1,taille,slotc);
		ri.debug();
		FileManager.getInstance().CreateRelationFile(ri);
		DBInfo.getInstance().AddRelation(ri);
	}
	
	/**
	 * Remise à zero de toute les sauvegardes (mise à neuf de la base de données)
	 * @throws IOException
	 */
	public void reset() throws IOException {
		BufferManager.getInstance().reset();
		DBInfo.getInstance().reset();
		FileManager.getInstance().reset();
	}
}
