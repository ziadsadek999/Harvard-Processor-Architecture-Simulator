package instructions;

import components.CPU;
import memory.DataMemory;
import utils.Helper;

public class SB extends ITypeInstruction {

	public SB(int binaryCode) {
		super(binaryCode);
		setRType(false);
	}

	@Override
	public void execute() {
		int r1= getR1Content();
		int address = getImm();
		DataMemory.getInstance().writeAddress(address, r1);
	}

}
