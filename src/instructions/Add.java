package instructions;

import components.CPU;
import utils.Helper;

public class Add extends RTypeInstruction {

	public Add(int binaryCode) {
		super(binaryCode);
	}

	@Override
	public void execute() {
		int r1 = getR1Content();
		int r2 = getR2Content();
		int expected = r1 + r2;
		r1 &= DATA_MASK;
		r2 &= DATA_MASK;
		int res = r1 + r2;
		CPU.getInstance().cFlag(res);
		res = Helper.signExtend(res);
		CPU.getInstance().vFlag(expected, res);
		CPU.getInstance().zFlag(res);
		CPU.getInstance().nFlag(res);
		CPU.getInstance().sFlag();
		CPU.getInstance().writeRegister(getR1(), res);
	}
}
