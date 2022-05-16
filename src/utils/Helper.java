package utils;

public class Helper {

	public static final int NEG_EXTENSION_MASK = (-1) << 8;
	public static final int POS_EXTENSION_MASK = ((1 << 8) - 1);

	public static int signExtend(int data) {
		if ((1 << 7 & data) != 0)
			return data | NEG_EXTENSION_MASK;
		return data & POS_EXTENSION_MASK;
	}

	public static int setBit(int SREG, int i) {
		return SREG | 1 << i;
	}

	public static int clearBit(int SREG, int i) {
		return SREG & (-1 - (1 << i));
	}

	public static int signExtendImm(int data) {
		int negMask = (-1) << 6;
		if (((1 << 5) & data) != 0) {
			data |= negMask;
		}
		return data;
	}

	public static String StringExtend(Integer num, int size) {
		if (num == null)
			num = 0;
		String res = Integer.toBinaryString(num);
		if (res.length() > size) {
			return res.substring(res.length() - size);
		}
		while (res.length() < size) {
			res = "0" + res;
		}
		return res;
	}

	public static int getBit(int num, int idx) {
		return (((1 << idx) & num) == 0) ? 0 : 1;
	}
}
