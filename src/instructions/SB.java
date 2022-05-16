package instructions;

import components.CPU;
import memory.DataMemory;
import utils.Helper;

public class SB extends Instruction {

	public SB(int binaryCode) {
		super(binaryCode);
		setRType(false);
	}

	@Override
	public void execute() {
		int r1= CPU.getInstance().readRegister(getR1());
		int address = Helper.signExtendImm(getR2());
		DataMemory.getInstance().writeAddress(address, r1);
	}

}
