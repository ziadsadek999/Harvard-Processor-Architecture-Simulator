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
		int PC = CPU.getInstance().getPC();
		r1 &= DATA_MASK;
		if (r1 == 0) {
			CPU.getInstance().setPC(getAddress() + 1 + imm);
			CPU.getInstance().flush();
		}
	}

}
