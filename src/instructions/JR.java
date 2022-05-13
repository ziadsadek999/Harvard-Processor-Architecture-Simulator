package instructions;

import components.CPU;

public class JR extends Instruction {

	public JR(int binaryCode) {
		super(binaryCode);
	}

	@Override
	public void execute() {
		int r1 = CPU.getInstance().readRegister(getR1());
		int r2 = CPU.getInstance().readRegister(getR2());
		r1 &= DATA_MASK;
		r2 &= DATA_MASK;
		r1 <<= 8;
		int res = r1 | r2;
		CPU.getInstance().setPC(res);
	}

}
