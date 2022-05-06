import java.io.*;
import java.util.*;

public class Main {
	Integer[] instructions = new Integer[1024];
	Integer[] data = new Integer[2048];
	int[] registers = new int[64];
	int sreg = 0;
	int pc = 0;
	Integer fetching;
	Integer decoding;
	Integer executing;
	int[] decodedInstruction = new int[3];
	HashMap<Integer, String> instructionsMap = new HashMap<Integer, String>();

	public static void main(String[] args) throws Exception {
		Main m = new Main("test1.txt");
		m.run();
	}

	public Main(String fileName) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		int i = 0;
		while (br.ready()) {
			String ss = br.readLine();
			String[] s = ss.split(" ");
			instructions[i] = 0;
			switch (s[0]) {
			case "ADD":
				instructions[i] = 0;
				break;
			case "SUB":
				instructions[i] = 1;
				break;
			case "MUL":
				instructions[i] = 2;
				break;
			case "LDI":
				instructions[i] = 3;
				break;
			case "BEQZ":
				instructions[i] = 4;
				break;
			case "AND":
				instructions[i] = 5;
				break;
			case "OR":
				instructions[i] = 6;
				break;
			case "JR":
				instructions[i] = 7;
				break;
			case "SLC":
				instructions[i] = 8;
				break;
			case "SRC":
				instructions[i] = 9;
				break;
			case "LB":
				instructions[i] = 10;
				break;
			case "SB":
				instructions[i] = 11;
				break;
			}
			instructions[i] <<= 6;
			int r1 = Integer.parseInt(s[1].substring(1));
			instructions[i] |= r1;
			instructions[i] <<= 6;
			int r2 = 0;
			switch (s[0]) {
			case "ADD":
			case "SUB":
			case "MUL":
			case "AND":
			case "OR":
			case "JR":
				r2 = Integer.parseInt(s[2].substring(1));
				instructions[i] |= r2;
				break;
			case "LDI":
			case "BEQZ":
			case "SLC":
			case "SRC":
			case "LB":
			case "SB":
				r2 = Integer.parseInt(s[2]);
				instructions[i] |= (r2 & ((1 << 6) - 1));
				break;
			}
			instructionsMap.put(instructions[i], ss);
			i++;
		}
		br.close();
	}

	public void run() {
		int c = 1;
		while (true) {

			executing = decoding;
			decoding = fetching;
			int prevPC = pc;
			if (pc < 1024)
				fetching = instructions[pc++];
			else
				fetching = null;
			if (executing == null && decoding == null && fetching == null)
				break;
			System.out.println("Start of Cycle " + c);
			System.out.println("Program Counter: binaryContent = " + extend(prevPC, 16) + " content = " + prevPC);
			if (executing != null) {
				System.out.println("EXECUTING " + instructionsMap.get(executing) + " " + extend(executing, 16));
				exec();

			}
			if (decoding != null) {

				System.out.println("DECODING " + extend(decoding, 16));
				System.out.println("Decoded Instruction For Next Cycle:-");
				decode(decoding);
				int op = decodedInstruction[0];
				int r1 = decodedInstruction[1];
				int r2 = decodedInstruction[2];
				System.out.println("OPcode=" + extend(op, 4) + " R1=" + extend(r1, 6) + " R2/IMM/OFF=" + extend(r2, 6));
				System.out.println("OPcodeValue=" + op + " R1Index=" + r1 + " R2Index/Immediate=" + r2);
				System.out.println("Inputs For Next Cycle:-");
				System.out.println("R1: binaryContent = " + extend(registers[r1], 8) + " content = " + registers[r1]);
				if (op == 0 || op == 1 || op == 2 || op == 5 || op == 6 || op == 7) {
					System.out
							.println("R2: binaryContent = " + extend(registers[r2], 8) + " content = " + registers[r2]);
				} else if (op >= 10) {
					System.out.println("ADDRESS: binaryContent = " + extend(r2, 6) + " content = " + r2);
				} else {
					System.out.println("IMM: binaryContent = " + extend(r2, 6) + " content = " + r2);
				}
			}
			if (fetching != null) {
				System.out.println("FETCHING " + extend(fetching, 16) + " From Instruction Memory");
				System.out.println("Program Counter Updated To binaryContent = " + extend(pc, 16) + " content = " + pc);
			}

			c++;
			System.out.println();

		}
		System.out.println();
		System.out.println("EXECUTION FINISHED!");
		System.out.println();
		int cc = (sreg & (1 << 4)) == 0 ? 0 : 1;
		int v = (sreg & (1 << 3)) == 0 ? 0 : 1;
		int n = (sreg & (1 << 2)) == 0 ? 0 : 1;
		int s = (sreg & (1 << 1)) == 0 ? 0 : 1;
		int z = (sreg & (1)) == 0 ? 0 : 1;
		System.out.println("REGISTERS");
		System.out.println(
				"Status Register: " + extend(sreg, 8) + " C=" + cc + " V=" + v + " N=" + n + " S=" + s + " Z=" + z);
		System.out.println("Program Counter: binaryContent = " + extend(pc, 16) + " content = " + pc);
		for (int i = 0; i < registers.length; i++) {
			System.out.println(
					"Register " + i + ": binaryContent = " + extend(registers[i], 8) + " content = " + registers[i]);
		}
		System.out.println();
		System.out.println("INSTRUCTIONS MEMORY");
		for (int i = 0; i < instructions.length; i++) {
			if (instructions[i] == null)
				continue;
			System.out.println("Instruction " + i + ": binaryContent = " + extend(instructions[i], 16) + " content = "
					+ instructionsMap.get(instructions[i]));
		}
		System.out.println();
		System.out.println("DATA MEMORY");
		for (int i = 0; i < data.length; i++) {
			if (data[i] == null)
				continue;
			System.out
					.println("Data address " + i + ": binaryContent = " + extend(data[i], 8) + " content = " + data[i]);
		}
	}

	public String extend(int x, int l) {
		String res = Integer.toBinaryString(x);
		if (res.length() > l) {
			return res.substring(res.length() - l, res.length());
		}
		while (res.length() < l) {
			res = "0" + res;
		}
		return res;
	}

	public void decode(int instruction) {
		// opcode at index 0
		decodedInstruction[0] = (instruction >> 12);
		// r1 at index 1
		int r1 = (instruction >> 6);
		r1 = (r1 % (1 << 6));
		decodedInstruction[1] = r1;
		// r2 or immediate at index 2
		decodedInstruction[2] = (instruction % (1 << 6));
		int imm = (instruction % (1 << 6));
		int op = decodedInstruction[0];
		if (op == 3 || op == 4 || op == 8 || op == 9) {
			if ((imm & (1 << 5)) != 0) {
				int x = ~0;
				x = x << 6;
				decodedInstruction[2] = imm | x;
			}
		}
	}

	public void exec() {
		int opcode = decodedInstruction[0];
		int tmp = registers[decodedInstruction[1]];
		switch (opcode) {
		case 0:
			add();
			break;
		case 1:
			sub();
			break;
		case 2:
			mul();
			break;
		case 3:
			ldi();
			break;
		case 4:
			beqz();
			break;
		case 5:
			and();
			break;
		case 6:
			or();
			break;
		case 7:
			jr();
			break;
		case 8:
			slc();
			break;
		case 9:
			src();
			break;
		case 10:
			lb();
			break;
		case 11:
			sb();
			break;
		}
		int c = (sreg & (1 << 4)) == 0 ? 0 : 1;
		int v = (sreg & (1 << 3)) == 0 ? 0 : 1;
		int n = (sreg & (1 << 2)) == 0 ? 0 : 1;
		int s = (sreg & (1 << 1)) == 0 ? 0 : 1;
		int z = (sreg & (1)) == 0 ? 0 : 1;
		System.out.println(
				"Status Register: " + extend(sreg, 8) + " C=" + c + " V=" + v + " N=" + n + " S=" + s + " Z=" + z);
		if (opcode != 11 && opcode != 4 && opcode != 7) {
			printChange(tmp);
		}

	}

	private void printChange(int prev) {
		int r1 = decodedInstruction[1];
		System.out.println("WRITING BACK To Register " + r1);
		System.out.println("Register " + r1 + " Content Updated From binaryContent = " + extend(prev, 8) + " content = "
				+ prev + " To binaryContent = " + extend(registers[r1], 8) + " content = " + registers[r1]);
	}

	private void sb() {
		int r2 = decodedInstruction[2];
		int r1 = decodedInstruction[1];
		data[r2] = registers[r1];
	}

	private void lb() {
		int r2 = decodedInstruction[2];
		int r1 = decodedInstruction[1];
		registers[r1] = data[r2];

	}

	private void src() {
		int r1 = decodedInstruction[1];
		int imm = decodedInstruction[2];
		byte x = (byte) registers[r1];
		int y = Byte.toUnsignedInt(x);
		int tmp = y << (8 - imm);
		y = (y >>> imm) | tmp;

		if ((y & (1 << 7)) != 0) {
			int z = ~0;
			z = z << 8;
			registers[r1] = y | z;
		} else {
			registers[r1] = y;
		}
		nflag(registers[r1]);
		zflag(registers[r1]);
	}

	private void slc() {
		int r1 = decodedInstruction[1];
		int imm = decodedInstruction[2];
		byte x = (byte) registers[r1];
		int y = Byte.toUnsignedInt(x);
		int tmp = y >>> (8 - imm);
		y = (y << imm) | tmp;

		if ((y & (1 << 7)) != 0) {
			int z = ~0;
			z = z << 8;
			registers[r1] = y | z;
		} else {
			registers[r1] = y;
		}
		nflag(registers[r1]);
		zflag(registers[r1]);
	}

	private void jr() {
		int r1 = decodedInstruction[1];
		int r2 = decodedInstruction[2];
		pc = Integer.parseInt(extend(registers[r1], 8) + extend(registers[r2], 8), 2);
		fetching = null;
		decoding = null;
		System.out.println("INSTRUCTIONS IN THE DECODING AND FETCHING STATES ARE FLUSHED");
		System.out.println("Program Counter Updated To binaryContent = " + extend(pc, 16) + " content = " + pc);
	}

	private void or() {
		int r2 = decodedInstruction[2];
		int r1 = decodedInstruction[1];
		registers[r1] |= registers[r2];
		nflag(registers[r1]);
		zflag(registers[r1]);
	}

	private void and() {
		int r2 = decodedInstruction[2];
		int r1 = decodedInstruction[1];
		registers[r1] &= registers[r2];
		nflag(registers[r1]);
		zflag(registers[r1]);
	}

	private void beqz() {
		int imm = decodedInstruction[2];
		int r = decodedInstruction[1];
		if (registers[r] == 0) {
			pc = (pc + 1 + imm);
			fetching = null;
			decoding = null;
			System.out.println("INSTRUCTIONS IN THE DECODING AND FETCHING STATES ARE FLUSHED");
			System.out.println("Program Counter Updated To binaryContent = " + extend(pc, 16) + " content = " + pc);
		}
	}

	private void ldi() {
		int imm = decodedInstruction[2];
		int r = decodedInstruction[1];
		registers[r] = imm;
	}

	private void mul() {
		int r2 = decodedInstruction[2];
		int r1 = decodedInstruction[1];
		int res = registers[r1] * registers[r2];
		int y = Integer.parseInt(extend(res, 8));
		if ((y & (1 << 7)) != 0) {
			int z = ~0;
			z = z << 8;
			registers[r1] = y | z;
		} else {
			registers[r1] = y;
		}
		zflag(registers[r1]);
		nflag(registers[r1]);
	}

	private void sub() {
		int r2 = decodedInstruction[2];
		int r1 = decodedInstruction[1];
		sflag(registers[r1], registers[r2], 1);
		int res = registers[r1] - registers[r2];
		int y = Integer.parseInt(extend(res, 8));
		if ((y & (1 << 7)) != 0) {
			int z = ~0;
			z = z << 8;
			registers[r1] = y | z;
		} else {
			registers[r1] = y;
		}
		zflag(registers[r1]);
		nflag(registers[r1]);
		vflag(res, registers[r1]);
	}

	private void add() {
		int r2 = decodedInstruction[2];
		int r1 = decodedInstruction[1];
		sflag(registers[r1], registers[r2], 0);
		cflag(registers[r2],registers[r1]);
		int res = registers[r1] + registers[r2];
		int y = Integer.parseInt(extend(res, 8));
		if ((y & (1 << 7)) != 0) {
			int z = ~0;
			z = z << 8;
			registers[r1] = y | z;
		} else {
			registers[r1] = y;
		}
		zflag(registers[r1]);
		nflag(registers[r1]);
		vflag(res, registers[r1]);
		
	}
	private void cflag(int x,int y) {
		int carry = 0;
		int mask = 1;
		while(mask<(1<<8)) {
			int a = (mask&x)==0?0:1;
			int b = (mask&y)==0?0:1;
			int res = a+b+carry;
			if(res>1)
				carry = 1;
			mask<<=1;
		}
		if (carry == 0) {
			if ((sreg & (1 << 4)) != 0) {
				sreg ^= (1 << 4);
			}
			System.out.println("Carry Flag Updated To 0");
		} else {
			sreg |= (1 << 4);
			System.out.println("Carry Flag Updated To 1");
		}
	}
	private void nflag(int result) {
		if (result >= 0) {
			if ((sreg & (1 << 2)) != 0) {
				sreg ^= (1 << 2);
			}
			System.out.println("Negative Flag Updated To 0");
		} else {
			sreg |= (1 << 2);
			System.out.println("Negative Flag Updated To 1");
		}

	}

	private void zflag(int result) {
		if (result != 0) {
			if ((sreg & (1)) != 0) {
				sreg ^= (1);
			}
			System.out.println("Zero Flag Updated To 0");
		} else {
			sreg |= (1);
			System.out.println("Zero Flag Updated To 1");
		}
	}

	private void sflag(int x, int y, int op) {
		if (op == 0) {
			if (x + y >= 0) {
				if ((sreg & (2)) != 0) {
					sreg ^= (2);
				}
				System.out.println("Sign Flag Updated To 0");
			} else {
				sreg |= (2);
				System.out.println("Sign Flag Updated To 1");
			}
		} else {
			if (x - y >= 0) {
				if ((sreg & (2)) != 0) {
					sreg ^= (2);
				}
				System.out.println("Sign Flag Updated To 0");
			} else {
				sreg |= (2);
				System.out.println("Sign Flag Updated To 1");
			}
		}
	}

	private void vflag(int expecRes, int actualRes) {
		if ((expecRes != actualRes)) {
			sreg |= (1 << 3);
			System.out.println("Overflow Flag Updated To 1");
			return;
		}
		if ((sreg & (1 << 3)) != 0) {
			sreg ^= (1 << 3);
		}
		System.out.println("Overflow Flag Updated To 0");
	}
}