package miniSGBD;

public final class DiskManagerTests {

	//Appel directement dans le main
	public static void test_sur_TP2() {
		DiskManager D = DiskManager.getInstance();
		PageId page;
		D.Init();
		D.CreateFile(13);
		page = new PageId(0,13);
		System.out.println(page.getFileIdx()+"\t\t"+ page.getPageIdx());
		byte [] binText = new String("MESSAGE A LIRE").getBytes();
		D.WritePage(page, binText);
		byte [] binTextReaded = new byte [DBParams.pageSize];
		D.ReadPage(page, binTextReaded);
		System.out.println(new String(binTextReaded));
	}
}
