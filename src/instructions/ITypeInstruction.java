package instructions;

import components.CPU;
import utils.Helper;

public abstract class ITypeInstruction extends Instruction {
	
	private int imm;
	
	public ITypeInstruction(int binaryCode) {
		super(binaryCode);
	}
	
	@Override
	public void decode() {
		super.decode();		
		imm = Helper.signExtendImm(getR2());
		String opCode = Helper.StringExtend(this.getOpCode(), 4);
		String r1 = Helper.StringExtend(this.getR1(), 6);
		String r2 = Helper.StringExtend(this.getR2(), 6);
		int r1Content = this.getR1Content();
		int r2Content = imm;
		if(this instanceof SLC || this instanceof SRC || this instanceof LB|| this instanceof SB) {
			r2Content = this.getR2();
		}
		String r1ContentBinary = Helper.StringExtend(r1Content, 8);
		String r2ContentBinary = Helper.StringExtend(r2Content, 8);
		CPU.getInstance().println("Decoded Instruction For Next Cycle:-");
		CPU.getInstance().println("OPcode = " + opCode + "  R1 = " + r1 + " R2/IMM/OFF = " + r2);
		CPU.getInstance().println("OPcode Value = " + this.getOpCode() + " R1 index = " + this.getR1()
				+ " R2 index/IMM = " + this.getImm());
		CPU.getInstance().println("Inputs For Next Cycle:-");
		CPU.getInstance().println("R1: binaryContent = " + r1ContentBinary + "  content = " + r1Content);
		CPU.getInstance().println("IMM/Address: binaryContent = " + r2ContentBinary + "  content = " + r2Content);
	}


	public int getImm() {
		return imm;
	}


	
}
