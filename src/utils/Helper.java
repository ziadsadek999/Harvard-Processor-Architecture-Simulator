package utils;

public class Helper {
	
	public static final int NEG_EXTENSION_MASK = -1 - ((1 << 8) - 1);
	public static final int POS_EXTENSION_MASK = ((1 << 8) - 1);
	
	public static int signExtend(int data) {
		if ((1 << 7 & data) != 0)
			return data | NEG_EXTENSION_MASK;
		return data & POS_EXTENSION_MASK;
	}

	public static int setBit(int SREG, int i) {
		return SREG | 1<<i;
	}

	public static int clearBit(int SREG, int i) {
		return SREG & (-1-(1<<i));
	}
}
