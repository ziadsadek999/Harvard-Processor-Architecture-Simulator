package instructions;

import components.CPU;
import utils.Helper;

public class JR extends RTypeInstruction {

	public JR(int binaryCode) {
		super(binaryCode);
	}

	@Override
	public void execute() {
		int r1 = getR1Content();
		int r2 = getR2Content();
		r1 &= DATA_MASK;
		r2 &= DATA_MASK;
		r1 <<= 8;
		int res = r1 | r2;
		CPU.getInstance().setPC(res);
		CPU.getInstance().println("PC value updated to: " + res + " Binary content: " + Helper.StringExtend(res, 16));
		CPU.getInstance().flush();
	}

}
