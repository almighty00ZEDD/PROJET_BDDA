package miniSGBD;

public class Frame {

	private  PageId pi;
	private int pin_count;
	private boolean dirty;
	private byte [] buffer;
	private int ref_bit;
	private int pin_count_zero;
	
	public Frame() {
		this.setPi(null);
		this.setDirty(false);
		this.setPin_count(0);
		this.setRef_bit(0);
		setPin_count_zero(0);
		buffer = new byte [DBParams.pageSize];
	}
	

	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	public int getPin_count() {
		return pin_count;
	}

	public void setPin_count(int pin_count) {
		this.pin_count = pin_count;
	}

	public PageId getPi() {
		return pi;
	}

	public void setPi(PageId pi) {
		this.pi = pi;
	}
	
	public void increment_pin_count() {
		pin_count++;
	}
	
	public void decrement_pin_count() {
		pin_count--;
	}

	public byte [] getBuffer() {
		return buffer;
	}

	public void setBuffer(byte [] buffer) {
		this.buffer = buffer;
	}


	public int getRef_bit() {
		return ref_bit;
	}


	public void setRef_bit(int ref_bit) {
		this.ref_bit = ref_bit;
	}
	
	public void reset() {
		this.setPi(null);
		this.setDirty(false);
		this.setPin_count(0);
		this.setRef_bit(0);	
		buffer = new byte [DBParams.pageSize];
	}


	public int getPin_count_zero() {
		return pin_count_zero;
	}


	public void setPin_count_zero(int pin_count_zero) {
		this.pin_count_zero = pin_count_zero;
	}

}
