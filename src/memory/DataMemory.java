package memory;

import utils.Helper;

public class DataMemory {

	private int[] dataMemory;

	private static final DataMemory instance = new DataMemory();

	private DataMemory() {
		dataMemory = new int[2048];
	}

	public int readAddress(int address) {
		return dataMemory[address];
	}

	public void writeAddress(int address, int data) {
		dataMemory[address] = Helper.signExtend(data);
	}

	

	public static DataMemory getInstance() {
		return instance;
	}

}
