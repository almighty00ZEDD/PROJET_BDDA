package miniSGBD;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class HeaderPage extends PageId {

	private int NbDataPages;
	private ArrayList<Integer> slotsLibres;

	
	public HeaderPage(int fileIdx) {
		super(0, fileIdx);
		NbDataPages = 0;
		this.slotsLibres = new ArrayList<>(0);
	}

	public void fill(byte[] buff) {
		ByteBuffer b = ByteBuffer.wrap(buff);
		setNbDataPages(b.getInt());
		for(int i = 0; i < NbDataPages ;i++) {
			slotsLibres.add(b.getInt());
		}
	}
	
	public byte[] writeHeaderPage(byte [] buff) {
		ByteBuffer b = ByteBuffer.wrap(buff);
		b.clear();
		b = ByteBuffer.allocate(DBParams.pageSize);
		b.putInt(getNbDataPages());
		for(int i = 0; i< getNbDataPages();i++) {
			b.putInt(slotsLibres.get(i));
		}
		return b.array();
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
	
	public int getAvailablePage() {
		for(int i = 0;i < NbDataPages;i++) {
			if(slotsLibres.get(i) > 0) {
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
	
	public void incrementerNBDataPages() {
		this.NbDataPages++;
	}
	
	public void addSlotLibre(Integer slotCount) {
		this.slotsLibres.add(slotCount);
	}
}