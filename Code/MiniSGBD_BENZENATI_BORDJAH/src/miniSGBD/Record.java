package miniSGBD;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class Record {

	public RelationInfo relInfo;
	public ArrayList<String> values;
	
	public Record(RelationInfo relInfo) {
		this.relInfo = relInfo;
	}
	
	public void writeToBuffer(ByteBuffer buff, int position) {
		int offset = position;
		for(int i = 0 ; i < values.size(); i++) {
			buff.put(values.get(i).getBytes(),offset,values.get(i).length());
			offset += values.get(i).length();
		}
		
	}
	
	public void readFromBuffer(ByteBuffer buff, int position) {
		int offset = position;
		ByteBuffer recup;
		for(int i = 0 ; i < values.size(); i++) {
		}
	}
}
