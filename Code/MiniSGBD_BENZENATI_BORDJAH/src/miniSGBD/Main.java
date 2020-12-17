package miniSGBD;
import java.io.IOException;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws IOException{		
		
		//Message de présetation
		System.out.println("***********************************************");
		System.out.println("MINI SYSTEME DE GESTION DE BASE DE DONNEES");
		System.out.println("DEVELOPPE PAR : BENZENATI ZINE EDDINE | BORDJAH NADIR");
		System.out.println("***********************************************\n\n");

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
	
		//Début effective de la base de donnée
		while(true)
		{	
			StringBuffer sb = new StringBuffer();
			Scanner s = new Scanner(System.in);

			sb.append(s.nextLine());
			if(sb.toString().equals("")) continue; //cas validation de saisie nul
			
			DBM.ProcessCommand(sb.toString()); 
		}
	}
}
