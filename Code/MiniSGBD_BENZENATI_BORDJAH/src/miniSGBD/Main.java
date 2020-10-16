package miniSGBD;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		DBManager DB = DBManager.getInstance();
		DB.Init();
		//j'avais oublié bordel :p
		StringBuffer sb = new StringBuffer();
		Scanner s = new Scanner(System.in);
		sb.append(s.nextLine());
		DB.ProcessCommand(sb.toString());
		
	}

}
