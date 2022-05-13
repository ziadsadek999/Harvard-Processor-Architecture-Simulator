package utils;

public class Helper {
	
	public static final int NEG_EXTENSION_MASK = -1 - ((1 << 8) - 1);
	public static final int POS_EXTENSION_MASK = ((1 << 8) - 1);
	
	public static int signExtend(int data) {
		if ((1 << 8 & data) != 0)
			return data | NEG_EXTENSION_MASK;
		return data & POS_EXTENSION_MASK;
	}

	public static int setBit(int sREG, int i) {
		// TODO Auto-generated method stub
		return 0;
	}

	public static int clearBit(int sREG, int i) {
		// TODO Auto-generated method stub
		return 0;
	}
}
