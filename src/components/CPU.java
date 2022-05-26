package components;

import java.awt.Label;
import java.awt.TextArea;

import javax.swing.JLabel;

import instructions.ITypeInstruction;
import instructions.Instruction;
import instructions.RTypeInstruction;
import instructions.SLC;
import instructions.SRC;
import memory.DataMemory;
import memory.InstructionMemory;
import utils.Helper;
import utils.Parser;
import views.MainFrame;

public class CPU {
	private int cycle;
	private int[] registers;
	private int PC;
	private int SREG;
	private Instruction fetching;
	private Instruction decoding;
	private Instruction executing;
	private MainFrame window;
	private TextArea log;
	private static final CPU instance = new CPU();

	public static void main(String[] args) throws Exception {
		CPU.getInstance().window = new MainFrame();
		CPU.getInstance().log = CPU.getInstance().window.log;
	}

	private CPU() {
		this.registers = new int[64];
		this.PC = 0;
		this.SREG = 0;
		cycle = 1;
	}

	public boolean runNextInstruction() {
		executing = decoding;
		decoding = fetching;
		fetching = InstructionMemory.getInstance().fetch();

		if (fetching == null && decoding == null && executing == null) {
			CPU.getInstance().println("EXECUTION FINISHED!");
			CPU.getInstance().println("");
			CPU.getInstance().println("REGISTERS");
			CPU.getInstance()
					.println("Status Register: " + Helper.StringExtend(SREG, 8) + " C=" + Helper.getBit(SREG, 4) + " V="
							+ Helper.getBit(SREG, 3) + " N=" + Helper.getBit(SREG, 2) + " S=" + Helper.getBit(SREG, 1)
							+ " Z=" + Helper.getBit(SREG, 0));
			CPU.getInstance()
					.println("Program Counter: binaryContent = " + Helper.StringExtend(PC, 16) + " content = " + PC);
			for (int i = 0; i < registers.length; i++) {
				CPU.getInstance().println("Register " + i + ": binaryContent = " + Helper.StringExtend(registers[i], 8)
						+ " content = " + registers[i]);
			}
			CPU.getInstance().println("");
			InstructionMemory.getInstance().print();
			CPU.getInstance().println("");
			DataMemory.getInstance().print();
			cycle = 1;
			return true;
		}
		CPU.getInstance().println("Start of Cycle " + cycle);
		CPU.getInstance().println(
				"Program Counter: binaryContent = " + Helper.StringExtend(PC - 1, 16) + " content = " + (PC - 1));

		if (executing != null) {
			CPU.getInstance().println(
					"EXECUTING " + executing + " MACHINE CODE: " + Helper.StringExtend(executing.getBinaryCode(), 16));
			executing.execute();
			CPU.getInstance()
					.println("Status Register: " + Helper.StringExtend(SREG, 8) + " C=" + Helper.getBit(SREG, 4) + " V="
							+ Helper.getBit(SREG, 3) + " N=" + Helper.getBit(SREG, 2) + " S=" + Helper.getBit(SREG, 1)
							+ " Z=" + Helper.getBit(SREG, 0));
		}
		if (decoding != null) {
			decoding.decode();

		}
		if (fetching != null) {
			CPU.getInstance().println(
					"FETCHING " + Helper.StringExtend(fetching.getBinaryCode(), 16) + " From Instruction Memory");
			CPU.getInstance()
					.println("Program Counter: binaryContent = " + Helper.StringExtend(PC, 16) + " content = " + (PC));
		}
		cycle++;
		CPU.getInstance().println("");
		return false;
	}

	public void runAll() throws Exception {

		while (true) {
			executing = decoding;
			decoding = fetching;
			fetching = InstructionMemory.getInstance().fetch();

			if (fetching == null && decoding == null && executing == null) {
				break;
			}
			CPU.getInstance().println("Start of Cycle " + cycle);
			CPU.getInstance().println(
					"Program Counter: binaryContent = " + Helper.StringExtend(PC - 1, 16) + " content = " + (PC - 1));

			if (executing != null) {
				CPU.getInstance().println("");
				CPU.getInstance().println("");
				CPU.getInstance().println("EXECUTING " + executing + " MACHINE CODE: "
						+ Helper.StringExtend(executing.getBinaryCode(), 16));
				executing.execute();
				CPU.getInstance()
						.println("Status Register: " + Helper.StringExtend(SREG, 8) + " C=" + Helper.getBit(SREG, 4)
								+ " V=" + Helper.getBit(SREG, 3) + " N=" + Helper.getBit(SREG, 2) + " S="
								+ Helper.getBit(SREG, 1) + " Z=" + Helper.getBit(SREG, 0));
			}
			if (decoding != null) {
				decoding.decode();

			}
			if (fetching != null) {
				CPU.getInstance().println("");
				CPU.getInstance().println("");
				CPU.getInstance().println(
						"FETCHING " + Helper.StringExtend(fetching.getBinaryCode(), 16) + " From Instruction Memory");
				CPU.getInstance().println(
						"Program Counter: binaryContent = " + Helper.StringExtend(PC, 16) + " content = " + (PC));
			}
			cycle++;
			CPU.getInstance().println("");
		}
		CPU.getInstance().println("EXECUTION FINISHED!");
		CPU.getInstance().println("REGISTERS");
		CPU.getInstance()
				.println("Status Register: " + Helper.StringExtend(SREG, 8) + " C=" + Helper.getBit(SREG, 4) + " V="
						+ Helper.getBit(SREG, 3) + " N=" + Helper.getBit(SREG, 2) + " S=" + Helper.getBit(SREG, 1)
						+ " Z=" + Helper.getBit(SREG, 0));
		CPU.getInstance()
				.println("Program Counter: binaryContent = " + Helper.StringExtend(PC, 16) + " content = " + PC);
		for (int i = 0; i < registers.length; i++) {
			CPU.getInstance().println("Register " + i + ": binaryContent = " + Helper.StringExtend(registers[i], 8)
					+ " content = " + registers[i]);
		}
		CPU.getInstance().println("");
		InstructionMemory.getInstance().print();
		CPU.getInstance().println("");
		DataMemory.getInstance().print();
		cycle = 1;
	}

	public void flush() {
		CPU.getInstance().println("INSTRUCTIONS IN THE DECODING AND FETCHING STATES ARE FLUSHED");
		fetching = null;
		decoding = null;
	}

	public int readRegister(int idx) {
		return registers[idx];
	}

	public void writeRegister(int idx, int data) {
		CPU.getInstance().println("WRITING BACK To Register " + idx);
		CPU.getInstance()
				.println("Register " + idx + " Content Updated From binaryContent = "
						+ Helper.StringExtend(registers[idx], 8) + " content = " + registers[idx]
						+ " To binaryContent = " + Helper.StringExtend(data, 8) + " content = " + data);
		registers[idx] = Helper.signExtend(data);
		window.registers[idx].setText("R" + idx + ": 0b" + Helper.StringExtend(data, 8) + "/" + data);
	}

	public void zFlag(int value) {
		if (value == 0) {
			SREG = Helper.setBit(SREG, 0);
			CPU.getInstance().println("Zero Flag Updated To 1");
		} else {
			SREG = Helper.clearBit(SREG, 0);
			CPU.getInstance().println("Zero Flag Updated To 0");
		}
	}

	public void sFlag() {
		int n = SREG >> 2;
		int v = SREG >> 3;
		int xor = v ^ n;
		if ((xor & 1) == 1) {
			SREG = Helper.setBit(SREG, 1);
			CPU.getInstance().println("Sign Flag Updated To 1");

		} else {
			SREG = Helper.clearBit(SREG, 1);
			CPU.getInstance().println("Sign Flag Updated To 0");
		}
	}

	public void nFlag(int value) {
		if (value < 0) {
			SREG = Helper.setBit(SREG, 2);
			CPU.getInstance().println("Negative Flag Updated To 1");

		} else {
			SREG = Helper.clearBit(SREG, 2);
			CPU.getInstance().println("Negative Flag Updated To 0");

		}
	}

	public void vFlag(int expected, int res) {
		if ((expected & (1 << 7)) != (res & (1 << 7))) {
			SREG = Helper.setBit(SREG, 3);
			CPU.getInstance().println("Overflow Flag Updated To 1");

		} else {
			SREG = Helper.clearBit(SREG, 3);
			CPU.getInstance().println("Overflow Flag Updated To 0");

		}
	}

	public void cFlag(int res) {
		if ((res & (1 << 8)) != 0) {
			SREG = Helper.setBit(SREG, 4);
			CPU.getInstance().println("Carry Flag Updated To 1");

		} else {
			SREG = Helper.clearBit(SREG, 4);
			CPU.getInstance().println("Carry Flag Updated To 0");

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

	public void println(String s) {
		log.setText(log.getText() + s + "\n");
	}

}
