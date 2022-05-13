package memory;

import components.CPU;
import instructions.Instruction;

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
		CPU.getInstance().setPC(PC+1);
		return instructionMemory[PC];
	}
	
	public void addInstruction(Instruction instruction) {
		instructionMemory[++lastInstruction] = instruction;
	}
	
	public InstructionMemory getInstance() {
		return instance;
	}
	
	public int getLastInstruction() {
		return lastInstruction;
	}
}
