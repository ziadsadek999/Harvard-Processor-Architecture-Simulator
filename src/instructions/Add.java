package instructions;

import components.CPU;
import memory.DataMemory;

public class Add extends Instruction {

	public Add(int binaryCode) {
		super(binaryCode);
	}

	@Override
	public void execute() {
		int r1 = CPU.getInstance().readRegister(getR1());
		int r2 = CPU.getInstance().readRegister(getR2());
		r1 &= DATA_MASK;
		r2 &= DATA_MASK;
		int res = r1+r2;
		CPU.getInstance().cFlag(r1, r2, res);
		CPU.getInstance().vFlag(r1, r2, res);
		CPU.getInstance().zFlag(res);
		CPU.getInstance().nFlag(res);
		CPU.getInstance().sFlag();
		CPU.getInstance().writeRegister(getR1(), res);
	}

}
