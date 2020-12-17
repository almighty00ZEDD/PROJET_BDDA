package miniSGBD;

import java.io.IOException;

public class TP5_TP6_Tests {

	public static void Test() throws IOException {
		//placement dans le main à la place de la boucle while
		
		//séries de commande de tests (automatisation)
		String [] testCommands = {
				"RESET",
				"CREATEREL R C1:int C2:string3 C3:int",
				"INSERT INTO R RECORD (1,aab,2)",
				"INSERT INTO R RECORD (2,abc,2)",
				"INSERT INTO R RECORD (1,agh,1)",
				"SELECTALL FROM R",
				"SELECTS FROM R WHERE C1=1",
				"SELECTS FROM R WHERE C3=1",
				"CREATEREL S C1:string2 C2:int C3:string4 C4:float C5:string5 C6:int C7:int C8:int",
				"BATCHINSERT INTO S FROM FILE S1.csv",
				"SELECTALL FROM S",
				"SELECTS FROM S WHERE C2=19",
				"SELECTS FROM S WHERE C3=Nati"};
	
		
		for(int i  =  0; i < testCommands.length;i++) {
			System.out.println("Simulation n"+ (i+1) +" Commande: "+ testCommands[i]);
			DBManager.getInstance().ProcessCommand(testCommands[i]);
			DBManager.getInstance().Finish();
			DBInfo.getInstance().Finish();
		}
	}
}
