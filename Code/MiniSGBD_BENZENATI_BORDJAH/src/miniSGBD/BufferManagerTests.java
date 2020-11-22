package miniSGBD;

public class BufferManagerTests {

	public static void TestsTp3() {
		
		BufferManager B = BufferManager.getInstance();
		DiskManager D = DiskManager.getInstance();
		PageId page1,page2,page3;
		D.Init();
		D.CreateFile(13);
		page1 = new PageId(0,13);
		page2 = new PageId(1,13);
		page3 = new PageId(0,14);
	
		byte [] binText = new String("MESSAGE A LIRE P1").getBytes();
		D.WritePage(page1, binText);
		
		binText = new String("MESSAGE A LIRE P2").getBytes();
		D.WritePage(page2, binText);
		
		binText = new String("MESSAGE A LIRE P3").getBytes();
		D.WritePage(page3, binText);
		
		
		System.out.println(new String(B.GetPage(page1)));
		System.out.println(new String(B.GetPage(page2)));
		B.FreePage(page1, false);
		B.Init();
		System.out.println(new String(B.GetPage(page3)));
		
		return;
	}
}
