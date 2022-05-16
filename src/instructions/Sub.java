package instructions;

import components.CPU;
import utils.Helper;

public class Sub extends Instruction {

	public Sub(int binaryCode) {
		super(binaryCode);
	}

	@Override
	public void execute() {
		int r1 = CPU.getInstance().readRegister(getR1());
		int r2 = CPU.getInstance().readRegister(getR2());
		int expected = r1 - r2;
		r1 &= DATA_MASK;
		r2 &= DATA_MASK;
		int res = r1 - r2;
		CPU.getInstance().cFlag(res);
		res = Helper.signExtend(res);
		CPU.getInstance().vFlag(expected, res);
		CPU.getInstance().zFlag(res);
		CPU.getInstance().nFlag(res);
		CPU.getInstance().sFlag();
		CPU.getInstance().writeRegister(getR1(), res);
	}

}
