package instructions;

import components.CPU;
import utils.Helper;

public class LDI extends ITypeInstruction{

	public LDI(int binaryCode) {
		super(binaryCode);
		setRType(false);
	}
	
	@Override
	public void execute() {
		int	imm = getImm();
		CPU.getInstance().writeRegister(getR1(), imm);
	}

}
