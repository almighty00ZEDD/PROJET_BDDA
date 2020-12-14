package miniSGBD;
import java.io.IOException;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws IOException{
		//System.out.println(args[0]);
		
		
		//Initialisation de DBParams
		
		DBParams.DBPath = args[0]; 
		DBParams.pageSize = 4096;
	    
			
		DBManager DB = DBManager.getInstance();
		DB.Init();
		
		//from here it's just for tests
		StringBuffer sb = new StringBuffer();
		
		Scanner s = new Scanner(System.in);
		sb.append(s.nextLine());
		DB.ProcessCommand(sb.toString()); //CREATEREL ZEDD C1:int C2:float C3:string4

		s.close();
		DBInfo.getInstance().Finish();
		
		//DBParams.DBPath = "/home/zedd/Bureau/Projet_BDDA_BENZENATI_BORDJAH/DB";
		//DBParams.frameCount  = 2;
		//DiskManagerTests.test_sur_TP2();
		//BufferManagerTests.TestsTp3();
		//DBInfoTests.TestsSurTP4_2();
		//System.out.println(DBInfo.getInstance().getCompteur_relations());
		
	}
}
