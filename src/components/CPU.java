package components;

import utils.Helper;

public class CPU {

	private int[] registers;
	private int PC;
	private int SREG;

	private static final CPU instance = new CPU();

	private CPU() {
		this.registers = new int[64];
		this.PC = 0;
		this.SREG = 0;
	}

	public void run(String fileName) {
		// TODO Auto-generated method stub
	}

	public int readRegister(int idx) {
		return registers[idx];
	}

	public void writeRegister(int idx, int data) {
		registers[idx] = Helper.signExtend(data);
	}

	public void zFlag(int value) {
		if (value == 0) {
			SREG = Helper.setBit(SREG, 0);
		} else {
			SREG = Helper.clearBit(SREG, 0);
		}
	}

	public void sFlag() {
		int n = SREG >> 2;
		int v = SREG >> 3;
		int xor = v ^ n;
		if ((xor & 1) == 1) {
			SREG = Helper.setBit(SREG, 1);
		} else {
			SREG = Helper.clearBit(SREG, 1);
		}
	}

	public void nFlag(int value) {
		if ((value & 1 << 7) != 0) {
			SREG = Helper.setBit(SREG, 2);
		} else {
			SREG = Helper.clearBit(SREG, 2);
		}
	}

	public void vFlag(int v1, int v2, int res) {
		if ((v1 & 1 << 7) == (v2 & 1 << 7) && (v2 & 1 << 7) != (res & 1 << 7)) {
			SREG = Helper.setBit(SREG, 3);
		} else {
			SREG = Helper.clearBit(SREG, 3);
		}
	}

	public void cFlag(int res) {
		if (res>=Byte.MAX_VALUE) {
			SREG = Helper.setBit(SREG, 4);
		} else {
			SREG = Helper.clearBit(SREG, 4);
		}
	}

	public int getPC() {
		return PC;
	}

	public void setPC(int pC) {
		PC = pC;
	}

	public int getSREG() {
		return SREG;
	}

	public void setSREG(int sREG) {
		SREG = sREG;
	}

	public int[] getRegisters() {
		return registers;
	}

	public static CPU getInstance() {
		return instance;
	}

}
