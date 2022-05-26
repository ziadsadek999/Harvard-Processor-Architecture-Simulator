package instructions;

import components.CPU;
import utils.Helper;

public abstract class RTypeInstruction extends Instruction {

	private int R2Content;

	public RTypeInstruction(int binaryCode) {
		super(binaryCode);
	}

	@Override
	public void decode() {
		super.decode();
		R2Content = CPU.getInstance().readRegister(getR2());
		String opCode = Helper.StringExtend(this.getOpCode(), 4);
		String r1 = Helper.StringExtend(this.getR1(), 6);
		String r2 = Helper.StringExtend(this.getR2(), 6);
		int r1Content = this.getR1Content();
		int r2Content = R2Content;
		String r1ContentBinary = Helper.StringExtend(r1Content, 8);
		String r2ContentBinary = Helper.StringExtend(r2Content, 8);
		CPU.getInstance().println("Decoded Instruction For Next Cycle:-");
		CPU.getInstance().println("OPcode = " + opCode + "  R1 = " + r1 + " R2/IMM/OFF = " + r2);
		CPU.getInstance().println("OPcode Value = " + this.getOpCode() + "R1 index = " + this.getR1()
				+ " R2 index/IMM = " + this.getR2());
		CPU.getInstance().println("Inputs For Next Cycle:-");
		CPU.getInstance().println("R1: binaryContent = " + r1ContentBinary + "  content = " + r1Content);
		CPU.getInstance().println("R2: binaryContent = " + r2ContentBinary + "  content = " + r2Content);
	}

	public int getR2Content() {
		return R2Content;
	}

}
