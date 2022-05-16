package instructions;

import components.CPU;
import memory.DataMemory;
import utils.Helper;

public class LB extends Instruction {

	public LB(int binaryCode) {
		super(binaryCode);
		setRType(false);
	}

	@Override
	public void execute() {
		int address = Helper.signExtendImm(getR2());
		int data = DataMemory.getInstance().readAddress(address);
		CPU.getInstance().writeRegister(getR1(), data);
	}

}
