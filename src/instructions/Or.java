package instructions;

import components.CPU;
import utils.Helper;

public class Or extends RTypeInstruction {

	public Or(int binaryCode) {
		super(binaryCode);
	}

	@Override
	public void execute() {
		int r1 = getR1Content();
		int r2 = getR2Content();
		r1 &= DATA_MASK;
		r2 &= DATA_MASK;
		int res = r1 | r2;
		res = Helper.signExtend(res);
		CPU.getInstance().nFlag(res);
		CPU.getInstance().zFlag(res);
		CPU.getInstance().writeRegister(getR1(), res);
	}

}
