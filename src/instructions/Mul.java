package instructions;

import components.CPU;
import utils.Helper;

public class Mul extends RTypeInstruction {

	public Mul(int binaryCode) {
		super(binaryCode);
	}

	@Override
	public void execute() {
		int r1 = getR1Content();
		int r2 = getR2Content();
		int res = r1 * r2;
		res = Helper.signExtend(res);
		CPU.getInstance().zFlag(res);
		CPU.getInstance().nFlag(res);
		CPU.getInstance().writeRegister(getR1(), res);
	}
}
