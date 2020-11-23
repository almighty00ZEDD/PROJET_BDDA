package miniSGBD;
import java.util.Vector;
import java.io.Serializable;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.FileInputStream;


public final class DBInfo implements Serializable{
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

		//unique instance
		private static DBInfo INSTANCE;
		private static final String path = DBParams.DBPath;
		private static final File  f = new File(path + "/catalog.def");
		private  Vector<RelationInfo> liste_RelationInfo;
		private  int compteur_relations;
		
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
			
			if(f.exists()) {
			FileInputStream file;
			ObjectInputStream object;
			try {
				file = new FileInputStream(f);
				object = new ObjectInputStream(file);
				DBInfo dbinfo = ((DBInfo) object.readObject());
				
				this.liste_RelationInfo = dbinfo.liste_RelationInfo;
				System.out.println(dbinfo.getInstance().getCompteur_relations());
				this.setCompteur_relations(dbinfo.getCompteur_relations());
				object.close();
				file.close();
			} catch(FileNotFoundException e) {
					e.printStackTrace();
			}
			
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
			else {
				liste_RelationInfo = new Vector<RelationInfo>(0);
				setCompteur_relations(0);
			}
		}
		
		//Fin de tache
		public void Finish() {
			try {
				if(!f.exists()) {
					f.createNewFile();
				}
				
				FileOutputStream file;
				ObjectOutputStream object;
				
				file = new FileOutputStream(f);
				object = new ObjectOutputStream(file);
				object.writeObject(DBInfo.getInstance());
				object.close();
				file.close();
			}
			catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch(IOException e) {
				System.out.println(e.getMessage());
			}
			
		}
		
		public void AddRelation(RelationInfo ri) {
			if(liste_RelationInfo.contains(ri)) return;
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
		
		public void debug2() {
			System.out.println(liste_RelationInfo);
		}
}
