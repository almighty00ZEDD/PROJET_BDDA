package miniSGBD;
import java.io.IOException;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws IOException{		
		//Initialisation de DBParams
		DBParams.DBPath = args[0]; 
		DBParams.pageSize = 4096;
		DBParams.frameCount = 2;
		
		//Pour une écriture plus courte lors des appels
		DBManager DBM = DBManager.getInstance();
		DBInfo DBI = DBInfo.getInstance();
		
		//Initialisation
		DBI.Init();
		DBM.Init();
	
		TP5_TP6_Tests.Test();
		/*
		//Début effective de la base de donnée
		while(true)
		{	
			StringBuffer sb = new StringBuffer();
			Scanner s = new Scanner(System.in);

			sb.append(s.nextLine());
			DBM.ProcessCommand(sb.toString()); //CREATEREL ZEDD C1:int C2:float C3:string4
			//DBM.Finish();
			//sauvegarde
			DBInfo.getInstance().Finish();
		}
		*/
	}
}
