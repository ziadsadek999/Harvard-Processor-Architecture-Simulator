package instructions;

import components.CPU;
import utils.Helper;

public class LDI extends Instruction{

	public LDI(int binaryCode) {
		super(binaryCode);
		setRType(false);
	}
	
	@Override
	public void execute() {
		int	imm = Helper.signExtendImm(getR2());
		CPU.getInstance().writeRegister(getR1(), imm);
	}

}
