package instructions;

import components.CPU;
import utils.Helper;

public class BEQZ extends ITypeInstruction {

	public BEQZ(int binaryCode) {
		super(binaryCode);
		setRType(false);
	}

	@Override
	public void execute() {
		int r1 = getR1Content();
		int imm = getImm();
		r1 &= DATA_MASK;
		if (r1 == 0) {
			int pc = getAddress() + 1 + imm;
			CPU.getInstance().setPC(pc);
			CPU.getInstance().println("PC value updated to: " + pc + " Binary content: " + Helper.StringExtend(pc, 16));
			CPU.getInstance().flush();
		}
	}

}
