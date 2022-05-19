package memory;

import components.CPU;
import instructions.Instruction;
import utils.Helper;

public class InstructionMemory {
	private Instruction[] instructionMemory;
	private int lastInstruction;

	private static final InstructionMemory instance = new InstructionMemory();

	private InstructionMemory() {
		this.instructionMemory = new Instruction[1024];
		this.lastInstruction = -1;
	}

	public Instruction fetch() {
		int PC = CPU.getInstance().getPC();
		if (PC < 0 || PC > 1023 || instructionMemory[PC] == null) {
			return null;
		}
		CPU.getInstance().setPC(PC + 1);
		return instructionMemory[PC];
	}

	public void addInstruction(Instruction instruction) {
		instructionMemory[++lastInstruction] = instruction;
	}

	public static InstructionMemory getInstance() {
		return instance;
	}

	public int getLastInstruction() {
		return lastInstruction;
	}

	public void print() {
		CPU.getInstance().println("INSTRUCTIONS MEMORY");
		for (int i = 0; i < instructionMemory.length; i++) {
			if (instructionMemory[i] == null)
				continue;
			CPU.getInstance().println("Instruction " + i + ": binaryContent = "
					+ Helper.StringExtend(instructionMemory[i].getBinaryCode(), 16) + " content = "
					+ instructionMemory[i]);
		}
	}
}
