package memory;

import components.CPU;
import utils.Helper;

public class DataMemory {

	private Integer[] dataMemory;

	private static final DataMemory instance = new DataMemory();

	private DataMemory() {
		dataMemory = new Integer[2048];
	}

	public int readAddress(int address) {
		return dataMemory[address];
	}

	public void writeAddress(int address, int data) {
		CPU.getInstance().println("WRITING BACK TO DATA MEMORY ADDRESS: " + address);
		CPU.getInstance()
				.println("MEMORY ADDRESS " + address + " Content Updated From binaryContent = "
						+ Helper.StringExtend(dataMemory[address], 8) + " content = "
						+ (dataMemory[address] == null ? 0 : dataMemory[address]) + " To binaryContent = "
						+ Helper.StringExtend(data, 8) + " content = " + data);
		dataMemory[address] = Helper.signExtend(data);
	}

	public static DataMemory getInstance() {
		return instance;
	}

	public void print() {
		CPU.getInstance().println("DATA MEMORY");
		for (int i = 0; i < dataMemory.length; i++) {
			if (dataMemory[i] == null)
				continue;
			CPU.getInstance().println("Data address " + i + ": binaryContent = " + Helper.StringExtend(dataMemory[i], 8)
					+ " content = " + dataMemory[i]);
		}
	}
	public void reset() {
		dataMemory = new Integer[2048];
	}
}
