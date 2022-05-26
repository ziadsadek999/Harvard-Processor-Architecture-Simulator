package instructions;

import components.CPU;
import utils.Helper;

public class SRC extends ITypeInstruction {

	public SRC(int binaryCode) {
		super(binaryCode);
		setRType(false);
	}

	@Override
	public void execute() {
		int r1 = getR1Content();
		int imm = getR2();
		r1 &= DATA_MASK;
		int res = r1>>>imm | r1<<(8-imm);
		res = Helper.signExtend(res);
		CPU.getInstance().nFlag(res);
		CPU.getInstance().zFlag(res);
		CPU.getInstance().writeRegister(getR1(), res);
	}

}