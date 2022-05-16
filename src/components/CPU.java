package components;

import instructions.Instruction;
import memory.DataMemory;
import memory.InstructionMemory;
import utils.Helper;
import utils.Parser;

public class CPU {

	private int[] registers;
	private int PC;
	private int SREG;
	private Instruction fetching;
	private Instruction decoding;
	private Instruction executing;

	private static final CPU instance = new CPU();

	public static void main(String[] args) throws Exception {
		CPU.getInstance().run("src/test2.txt");
	}
	
	private CPU() {
		this.registers = new int[64];
		this.PC = 0;
		this.SREG = 0;
	}

	public void run(String fileName) throws Exception {
		Parser.parseProgram(fileName);
		
		int cycle = 1;
		while (true) {
			executing = decoding;
			decoding = fetching;
			fetching = InstructionMemory.getInstance().fetch();

			if (fetching == null && decoding == null && executing == null) {
				break;
			}
			System.out.println("Start of Cycle " + cycle);
			System.out.println(
					"Program Counter: binaryContent = " + Helper.StringExtend(PC - 1, 16) + " content = " + (PC - 1));

			if (executing != null) {
				System.out.println("EXECUTING " + executing + " MACHINE CODE: " + Helper.StringExtend(executing.getBinaryCode(), 16));
				executing.execute();
				System.out.println("Status Register: " + Helper.StringExtend(SREG, 8) + " C=" + Helper.getBit(SREG, 4)
						+ " V=" + Helper.getBit(SREG, 3) + " N=" + Helper.getBit(SREG, 2) + " S="
						+ Helper.getBit(SREG, 1) + " Z=" + Helper.getBit(SREG, 0));
			}
			if (decoding != null) {
				decoding.decode();
				String opCode = Helper.StringExtend(decoding.getOpCode(), 4);
				String r1 = Helper.StringExtend(decoding.getR1(), 6);
				String r2 = Helper.StringExtend(decoding.getR2(), 6);
				int r1Content = registers[decoding.getR1()];
				int r2Content = decoding.hasImmediate() ? decoding.getR2() : registers[decoding.getR2()];
				String r1ContentBinary = Helper.StringExtend(r1Content, 8);
				String r2ContentBinary = Helper.StringExtend(r2Content, 8);
				System.out.println("Decoded Instruction For Next Cycle:-");
				System.out.println("OPcode = " + opCode + "  R1 = " + r1 + " R2/IMM/OFF = " + r2);
				System.out.println("OPcode Value = " + decoding.getOpCode() + "R1 index = " + decoding.getR1()
						+ " R2 index/IMM = " + decoding.getR2());
				System.out.println("Inputs For Next Cycle:-");
				System.out.println("R1: binaryContent = " + r1ContentBinary + "  content = " + r1Content);
				System.out.println("R2: binaryContent = " + r2ContentBinary + "  content = " + r2Content);
			}
			if (fetching != null) {
				System.out.println(
						"FETCHING " + Helper.StringExtend(fetching.getBinaryCode(), 16) + " From Instruction Memory");
				System.out.println(
						"Program Counter: binaryContent = " + Helper.StringExtend(PC, 16) + " content = " + (PC));
			}
			cycle++;
			System.out.println();
		}
		System.out.println("REGISTERS");
		System.out.println("Status Register: " + Helper.StringExtend(SREG, 8) + " C=" + Helper.getBit(SREG, 4) + " V="
				+ Helper.getBit(SREG, 3) + " N=" + Helper.getBit(SREG, 2) + " S=" + Helper.getBit(SREG, 1) + " Z="
				+ Helper.getBit(SREG, 0));
		System.out.println("Program Counter: binaryContent = " + Helper.StringExtend(PC, 16) + " content = " + PC);
		for (int i = 0; i < registers.length; i++) {
			System.out.println("Register " + i + ": binaryContent = " + Helper.StringExtend(registers[i], 8)
					+ " content = " + registers[i]);
		}
		System.out.println();
		InstructionMemory.getInstance().print();
		System.out.println();
		DataMemory.getInstance().print();
	}

	public void flush() {
		System.out.println("INSTRUCTIONS IN THE DECODING AND FETCHING STATES ARE FLUSHED");
		fetching = null;
		decoding = null;
	}

	public int readRegister(int idx) {
		return registers[idx];
	}

	public void writeRegister(int idx, int data) {
		System.out.println("WRITING BACK To Register " + idx);
		System.out.println("Register " + idx + " Content Updated From binaryContent = "
				+ Helper.StringExtend(registers[idx], 8) + " content = " + registers[idx] + " To binaryContent = "
				+ Helper.StringExtend(data, 8) + " content = " + data);
		registers[idx] = Helper.signExtend(data);
	}

	public void zFlag(int value) {
		if (value == 0) {
			SREG = Helper.setBit(SREG, 0);
			System.out.println("Zero Flag Updated To 1");
		} else {
			SREG = Helper.clearBit(SREG, 0);
			System.out.println("Zero Flag Updated To 0");
		}
	}

	public void sFlag() {
		int n = SREG >> 2;
		int v = SREG >> 3;
		int xor = v ^ n;
		if ((xor & 1) == 1) {
			SREG = Helper.setBit(SREG, 1);
			System.out.println("Sign Flag Updated To 1");

		} else {
			SREG = Helper.clearBit(SREG, 1);
			System.out.println("Sign Flag Updated To 0");
		}
	}

	public void nFlag(int value) {
		if (value < 0) {
			SREG = Helper.setBit(SREG, 2);
			System.out.println("Negative Flag Updated To 1");

		} else {
			SREG = Helper.clearBit(SREG, 2);
			System.out.println("Negative Flag Updated To 0");

		}
	}

	public void vFlag(int expected, int res) {
		if ((expected & (1 << 7)) != (res & (1 << 7))) {
			SREG = Helper.setBit(SREG, 3);
			System.out.println("Overflow Flag Updated To 1");

		} else {
			SREG = Helper.clearBit(SREG, 3);
			System.out.println("Overflow Flag Updated To 0");

		}
	}

	public void cFlag(int res) {
		if (res >= Byte.MAX_VALUE) {
			SREG = Helper.setBit(SREG, 4);
			System.out.println("Carry Flag Updated To 1");

		} else {
			SREG = Helper.clearBit(SREG, 4);
			System.out.println("Carry Flag Updated To 0");

		}
	}

	public int getPC() {
		return PC;
	}

	public void setPC(int pC) {
		PC = pC;
	}

	public int getSREG() {
		return SREG;
	}

	public void setSREG(int sREG) {
		SREG = sREG;
	}

	public int[] getRegisters() {
		return registers;
	}

	public static CPU getInstance() {
		return instance;
	}

}
