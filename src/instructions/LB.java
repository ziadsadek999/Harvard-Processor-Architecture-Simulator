package instructions;

import components.CPU;
import memory.DataMemory;
import utils.Helper;

public class LB extends Instruction {

	public LB(int binaryCode) {
		super(binaryCode);
	}

	@Override
	public void execute() {
		int address = getR2();
		int data = DataMemory.getInstance().readAddress(address);
		data = Helper.signExtend(data);
		CPU.getInstance().writeRegister(getR1(), data);
	}

}
