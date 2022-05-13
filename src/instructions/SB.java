package instructions;

import components.CPU;
import memory.DataMemory;

public class SB extends Instruction {

	public SB(int binaryCode) {
		super(binaryCode);
	}

	@Override
	public void execute() {
		int r1= CPU.getInstance().readRegister(getR1());
		int address = getR2();
		DataMemory.getInstance().writeAddress(address, r1);
	}

}
