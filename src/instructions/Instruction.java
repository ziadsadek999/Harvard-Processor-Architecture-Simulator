package instructions;

public abstract class Instruction {

	public static final int R2_MASK = (1 << 6) - 1;
	public static final int R1_MASK = (R2_MASK) << 6;
	public static final int OPCODE_MASK = (1 << 16) - 1 - R1_MASK - R2_MASK;
	public static final int DATA_MASK = (1 << 8) - 1;

	private int binaryCode;
	private int opCode;
	private int R1;
	private int R2;

	public Instruction(int binaryCode) {
		this.binaryCode = binaryCode;
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
}
