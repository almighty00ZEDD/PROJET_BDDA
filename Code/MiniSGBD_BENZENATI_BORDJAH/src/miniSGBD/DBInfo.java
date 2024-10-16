package miniSGBD;
import java.util.Vector;
import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public final class DBInfo implements Serializable{

		
	private static final long serialVersionUID = 1L;
		//unique instance
		private static DBInfo INSTANCE;
		private static final String path = DBParams.DBPath;
		private static  File  f = new File(path + "/catalog.def");
		private transient Vector<RelationInfo> liste_RelationInfo;
		private int compteur_relations;
		
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
			try {
				if(f.exists()) {
					FileInputStream fis = new FileInputStream(f);
					ObjectInputStream ois = new ObjectInputStream(fis);
					@SuppressWarnings("unchecked")
					Vector<RelationInfo> recup = (Vector<RelationInfo>) (ois.readObject());
					setListe(recup);
					setCompteur_relations(recup.size());
					fis.close();
				}
				else {
					setCompteur_relations(0);
					liste_RelationInfo = new Vector<RelationInfo>(0);
				}
				
			}catch(IOException e) {
				System.out.println(e);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//Fin de tache
		public void Finish() {
			try {
				FileOutputStream fos = new FileOutputStream(f);
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(liste_RelationInfo);
				oos.close();
				fos.close();
			}
			catch(IOException e) {
				System.out.println(e);
			}
		}
		
		public void AddRelation(RelationInfo ri) {
			liste_RelationInfo.add(ri);
			setCompteur_relations(getCompteur_relations() + 1);
		}
		
		//juste pour les tests
		public void debug() {
			RelationInfo ri = liste_RelationInfo.elementAt(0);
			ri.debug();
		}

		public  int getCompteur_relations() {
			return compteur_relations;
		}
		

		public  void setCompteur_relations(int compteur_relations) {
			this.compteur_relations = compteur_relations;
		}
		
		public void setListe(Vector<RelationInfo> l) {
			liste_RelationInfo = l;
		}
		
		public Vector<RelationInfo> getListe() {
			return liste_RelationInfo;
		}
		
		public void debug2() {
			System.out.println(liste_RelationInfo);
		}
		
		/**
		 * Remise à neuf de la base de donnée, suppression de toute les relations et le fichier catalog
		 * @throws IOException
		 */
		public void reset() throws IOException{
			liste_RelationInfo.clear();
			setCompteur_relations(0);
			File catalog = new File(path + "/catalog.def");
			catalog.delete();
		}
}
