package miniSGBD;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class Record {

	private RelationInfo relInfo;
	private ArrayList<String> values;
	
	public Record(RelationInfo relInfo) {
		this.relInfo = relInfo;
		values = new ArrayList<String>(0);
	}
	
	/**
	 * Ecriture d'un enregistrement dans un buffer (string to bytes)
	 * @param buff le buffer
	 * @param position la position de début d'écriture dans le buffer
	 */
	public void WriteToBuffer(ByteBuffer buff, int position) {
		buff.position(position);
		for (int i = 0; i < values.size(); i++) {
			if (relInfo.getType_Colonnes().get(i).equals("int")) {
				buff.putInt(Integer.parseInt(values.get(i)));
			} else if (relInfo.getType_Colonnes().get(i).equals("float")) {
				buff.putFloat(Float.parseFloat(values.get(i)));
			} else if (relInfo.getType_Colonnes().get(i).substring(0,6).equals("string")) {
				for (int j = 0; j < values.get(i).length(); j++) {
					buff.putChar(values.get(i).charAt(j));
				}
			}
		}
	}
	
	/**
	 * Lecture d'un enregistrement depuis un buffer (bytes to string)
	 * @param buff le buffer
	 * @param position position de début de lecture
	 */
	public void readFromBuffer(ByteBuffer buff,int position) {
		buff.position(position);
		for (int i = 0; i < relInfo.getType_Colonnes().size(); i++) {
			if (relInfo.getType_Colonnes().get(i).equals("int")) {
				values.add(String.valueOf(buff.getInt()));
			} else if (relInfo.getType_Colonnes().get(i).equals("float")) {
				values.add(String.valueOf(buff.getFloat()));
			} else if (relInfo.getType_Colonnes().get(i).substring(0,6).equals("string")) {
				
				int valeur = Integer.parseInt(relInfo.getType_Colonnes().get(i).split("string")[1]);
				StringBuilder sb = new StringBuilder();
				for (int j = 0; j <valeur; j++) {
					
					sb.append(String.valueOf(buff.getChar()));
				}
				values.add(sb.toString());
				}
			}
		}
	
	public RelationInfo getRelationInfo() {
		return relInfo;
	}
	
	public void addValue(String value) {
		values.add(value);
	}
	
	public ArrayList<String> getValues(){
		return values;
	}

}

