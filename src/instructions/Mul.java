package instructions;

import components.CPU;
import utils.Helper;

public class Mul extends Instruction {

	public Mul(int binaryCode) {
		super(binaryCode);
	}

	@Override
	public void execute() {
		int r1 = CPU.getInstance().readRegister(getR1());
		int r2 = CPU.getInstance().readRegister(getR2());
		r1 &= DATA_MASK;
		r2 &= DATA_MASK;
		int res = r1 * r2;
		res = Helper.signExtend(res);
		CPU.getInstance().zFlag(res);
		CPU.getInstance().nFlag(res);
		CPU.getInstance().writeRegister(getR1(), res);
	}
}
