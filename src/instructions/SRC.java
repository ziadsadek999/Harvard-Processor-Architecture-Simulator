package instructions;

import components.CPU;
import utils.Helper;

public class SRC extends Instruction {

	public SRC(int binaryCode) {
		super(binaryCode);
	}

	@Override
	public void execute() {
		int r1 = CPU.getInstance().readRegister(getR1());
		int imm = getR2();
		r1 &= DATA_MASK;
		int res = r1>>>imm | r1<<8-imm;
		res = Helper.signExtend(res);
		CPU.getInstance().nFlag(res);
		CPU.getInstance().zFlag(res);
		CPU.getInstance().writeRegister(getR1(), res);
	}

}