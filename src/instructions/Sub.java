package instructions;

import components.CPU;
import utils.Helper;

public class Sub extends RTypeInstruction {

	public Sub(int binaryCode) {
		super(binaryCode);
	}

	@Override
	public void execute() {
		int r1 = getR1Content();
		int r2 = getR2Content();
		int expected = r1 - r2;
		int res = Helper.signExtend(expected & DATA_MASK);
		CPU.getInstance().vFlag(expected, expected & DATA_MASK);
		CPU.getInstance().zFlag(res);
		CPU.getInstance().nFlag(res);
		CPU.getInstance().sFlag();
		CPU.getInstance().writeRegister(getR1(), res);
	}

}
