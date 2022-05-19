package instructions;

import components.CPU;

public abstract class Instruction {
	
	public static final int R2_MASK = (1 << 6) - 1;
	public static final int R1_MASK = (R2_MASK) << 6;
	public static final int OPCODE_MASK = ((1 << 4) - 1) << 12;
	public static final int DATA_MASK = (1 << 8) - 1;

	private int binaryCode;
	private int opCode;
	private int R1;
	private int R2;
	private int R1Content;
	private String instruction;
	private boolean RType;

	public Instruction(int binaryCode) {
		this.binaryCode = binaryCode;
		setRType(true);
	}

	public void decode() {
		this.opCode = binaryCode;
		opCode &= OPCODE_MASK;
		opCode >>= 12;

		this.R1 = binaryCode;
		R1 &= R1_MASK;
		R1 >>= 6;

		this.R2 = binaryCode;
		this.R2 &= R2_MASK;
		
		R1Content = CPU.getInstance().readRegister(getR1());
	}

	public abstract void execute();

	public int getOpCode() {
		return opCode;
	}

	public int getR1() {
		return R1;
	}

	public int getBinaryCode() {
		return binaryCode;
	}

	public int getR2() {
		return R2;
	}
	
	public int getR1Content() {
		return R1Content;
	}


	public String toString() {
		return instruction;
	}

	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}

	public boolean hasImmediate() {
		return !RType;
	}

	public void setRType(boolean rType) {
		RType = rType;
	}
}
