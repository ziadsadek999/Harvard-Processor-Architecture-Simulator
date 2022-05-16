package instructions;

import components.CPU;
import utils.Helper;

public class BEQZ extends Instruction {
	
	public BEQZ(int binaryCode) {
		super(binaryCode);
		setRType(false);
	}

	@Override
	public void execute() {
		int r1 = CPU.getInstance().readRegister(getR1());
		int imm  = Helper.signExtendImm(getR2());
		int PC = CPU.getInstance().getPC();
		r1 &= DATA_MASK;
		if (r1 == 0) {
			CPU.getInstance().setPC(PC + imm);
		}
	}

}
