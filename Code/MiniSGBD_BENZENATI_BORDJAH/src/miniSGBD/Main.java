package miniSGBD;
//import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		/*
		//Initialisation de DBParams
		DBParams.DBPath = args[0]; 
		DBParams.pageSize = 4096;
		
		DBManager DB = DBManager.getInstance();
		DB.Init();
		
		//from here it's just for tests
		StringBuffer sb = new StringBuffer();
		
		Scanner s = new Scanner(System.in);
		sb.append(s.nextLine());
		DB.ProcessCommand(sb.toString());
		s.close();
		*/
		DBParams.DBPath = "/home/zedd/Bureau/Projet_BDDA_BENZENATI_BORDJAH/DB";
		DBParams.frameCount  = 2;
		DiskManagerTests.test_sur_TP2();
	}
}
