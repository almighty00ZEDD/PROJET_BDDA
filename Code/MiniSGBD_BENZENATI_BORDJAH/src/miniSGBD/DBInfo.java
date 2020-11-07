package miniSGBD;
import java.util.Vector;

public final class DBInfo {
		//unique instance
		private static DBInfo INSTANCE;
		
		private static Vector<RelationInfo> liste_RelationInfo;
		private static int compteur_relations;
		
		//Constructeur
		private  DBInfo(){
			
		}
		
		//accès à l'instance unique du DBManager
		public static DBInfo getInstance() {
			if(INSTANCE == null) {
				INSTANCE = new DBInfo();
			}
			
			return INSTANCE;
		}
		
		//Initialisation
		public void Init() {
			
			liste_RelationInfo = new Vector<RelationInfo>(10);
			setCompteur_relations(0);
		}
		
		//Fin de tache
		public void Finish() {
		
		}
		
		public void AddRelation(RelationInfo ri) {
			if(liste_RelationInfo.contains(ri)) return;
				liste_RelationInfo.add(ri);
				setCompteur_relations(getCompteur_relations() + 1);
			
		}
		
		//pas canon :p
		public void debug() {
			RelationInfo ri = liste_RelationInfo.elementAt(0);
			ri.debug();
		}

		public static int getCompteur_relations() {
			return compteur_relations;
		}

		public static void setCompteur_relations(int compteur_relations) {
			DBInfo.compteur_relations = compteur_relations;
		}
}
