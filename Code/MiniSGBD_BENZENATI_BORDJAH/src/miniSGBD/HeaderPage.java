package miniSGBD;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class HeaderPage {

	private int NbDataPages;
	private ArrayList<Integer> slotsLibres;
	
	public HeaderPage() {
		this.setNbDataPages(0);
		this.setSlotsLibres(new ArrayList<Integer>(0));
	}

	public void fill(byte[] buff) {
		ByteBuffer b = ByteBuffer.wrap(buff);
		setNbDataPages(b.getInt());
		for(int i = 0; i < NbDataPages ;i++) {
			slotsLibres.add(b.getInt());
		}
	}
	
	public byte [] newPage(byte [] buff,RelationInfo relInfo) {
		setNbDataPages(getNbDataPages() + 1);
		ByteBuffer b = ByteBuffer.wrap(buff);
		b.putInt(NbDataPages);
		for(int i = 0;i<NbDataPages;i++) {
			b.putInt(relInfo.getSlotCount());
		}
		return b.array();
	}
	
	public int getAvailablePage(RelationInfo relInfo) {
		for(int i = 0;i < NbDataPages;i++) {
			if(slotsLibres.get(i) < relInfo.getSlotCount()) {
				return (i+1);
			}
		}
		return 0;
	}
	public int getNbDataPages() {
		return NbDataPages;
	}

	public void setNbDataPages(int nbDataPages) {
		NbDataPages = nbDataPages;
	}

	public ArrayList<Integer> getSlotsLibres() {
		return slotsLibres;
	}

	public void setSlotsLibres(ArrayList<Integer> slotsLibres) {
		this.slotsLibres = slotsLibres;
	}
}
