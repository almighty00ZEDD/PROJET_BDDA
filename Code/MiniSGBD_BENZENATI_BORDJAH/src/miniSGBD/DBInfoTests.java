package miniSGBD;

public class DBInfoTests {

	public static void TestsSurTP4_1() {
		//executer en premier pour tester la lecture
		DBInfo DBI = DBInfo.getInstance();
		DBI.Init();
		RelationInfo r1 = new RelationInfo(),r2 = new RelationInfo(),r3 = new RelationInfo(),r4 = new RelationInfo();
		DBI.AddRelation(r1);
		DBI.AddRelation(r2);
		DBI.AddRelation(r3);
		DBI.AddRelation(r4);
		DBI.Finish();
		DBI.debug2();
		System.out.println("\n\n ************************************************** \n\n");
	}
	
	public static void TestsSurTP4_2() {
		//executer après le test1 en haut pour vérifier la lecture
		DBInfo DBI = DBInfo.getInstance();
		DBI.Init();
		DBI.debug2();
	}
}
