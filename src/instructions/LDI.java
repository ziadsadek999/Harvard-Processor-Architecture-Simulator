package instructions;

import components.CPU;

public class LDI extends Instruction{

	public LDI(int binaryCode) {
		super(binaryCode);
	}
	
	@Override
	public void execute() {
		int	imm = getR2();
		CPU.getInstance().writeRegister(getR1(), imm);
	}

}
