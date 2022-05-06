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
	int mask = (1 << 8) - 1;
	String[] instructionsString = new String[1024];

	public Main(String fileName) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		int i = 0;
		while (br.ready()) {
			String ss = br.readLine();
			instructionsString[i] = ss;
			String[] s = ss.split(" ");
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
			instructions[i]<<=6;
			int r1 = Integer.parseInt(s[1].substring(1));
			instructions[i]|=r1;
			instructions[i]<<=6;
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
				instructions[i] |= r2;
				break;
			}
			i++;
		}
		br.close();
	}

	public void run() {
		int c = 1;
		while (true) {
			if (pc >= 1024)
				break;
			
			
			
			executing = decoding;
			decoding = fetching;
			fetching = instructions[pc++];
			if (executing == null && decoding == null && fetching == null)
				break;
			if (decoding != null)
				decode(decoding);
			if (executing != null)
				exec();

			c++;
		}
	}

	public void decode(int instruction) {
		// opcode at index 0
		decodedInstruction[0] = (int) (instruction >> 12);
		// r1 at index 1
		int r1 = (int) (instruction >> 6);
		r1 = (int) (r1 % (1 << 6));
		decodedInstruction[1] = r1;
		// r2 or immediate at index 2
		decodedInstruction[2] = (int) (instruction % (1 << 6));
	}

	public void exec() {
		int opcode = decodedInstruction[0];
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
		int temp = r1 << (8 - imm);
		registers[r1] = (r1 >> imm | temp) & mask;
		nflag(registers[r1]);
		zflag(registers[r1]);
	}

	private void slc() {
		int r1 = decodedInstruction[1];
		int imm = decodedInstruction[2];
		int temp = r1 >> (8 - imm);
		registers[r1] = (r1 << imm | temp) & mask;
		nflag(registers[r1]);
		zflag(registers[r1]);
	}

	private void jr() {
		int r1 = decodedInstruction[1];
		int r2 = decodedInstruction[2];
		String s1 = Integer.toBinaryString(registers[r1]);
		String s2 = Integer.toBinaryString(registers[r2]);
		while (s2.length() < 8) {
			s2 = '0' + s2;
		}
		pc = Integer.parseInt(s1 + s2, 2);
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
		if (r == 0) {
			pc = (pc + 1 + imm);
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
		int x = registers[r1] & mask;
		int y = registers[r2] & mask;
		if (x * y > Byte.MAX_VALUE) {
			sreg |= (1 << 4);
		} else {
			if ((sreg & (1 << 4)) != 0) {
				sreg ^= (1 << 4);
			}
		}
		int sign = res & (1 << 7);
		if (sign == 0) {
			if (res >= 0) {
				if ((sreg & (1 << 3)) != 0) {
					sreg ^= (1 << 3);
				}
			} else {
				sreg |= (1 << 3);
			}
		} else {
			if (res >= 0) {
				sreg |= (1 << 3);
			} else {
				if ((sreg & (1 << 3)) != 0) {
					sreg ^= (1 << 3);
				}
			}
		}
		nflag(res);
		zflag(res);
		registers[r1] = res & mask;
	}

	private void sub() {
		int r2 = decodedInstruction[2];
		int r1 = decodedInstruction[1];
		int res = registers[r1] - registers[r2];
		int x = registers[r1] & mask;
		int y = registers[r2] & mask;
		if (x - y > Byte.MAX_VALUE) {
			sreg |= (1 << 4);
		} else {
			if ((sreg & (1 << 4)) != 0) {
				sreg ^= (1 << 4);
			}
		}
		int sign = res & (1 << 7);
		if (sign == 0) {
			if (res >= 0) {
				if ((sreg & (1 << 3)) != 0) {
					sreg ^= (1 << 3);
				}
			} else {
				sreg |= (1 << 3);
			}
		} else {
			if (res >= 0) {
				sreg |= (1 << 3);
			} else {
				if ((sreg & (1 << 3)) != 0) {
					sreg ^= (1 << 3);
				}
			}
		}
		nflag(res);
		zflag(res);
		registers[r1] = res & mask;

	}

	private void add() {
		int r2 = decodedInstruction[2];
		int r1 = decodedInstruction[1];
		int res = registers[r1] + registers[r2];
		int x = registers[r1] & mask;
		int y = registers[r2] & mask;
		if (x + y > Byte.MAX_VALUE) {
			sreg |= (1 << 4);
		} else {
			if ((sreg & (1 << 4)) != 0) {
				sreg ^= (1 << 4);
			}
		}
		int sign = res & (1 << 7);
		if (sign == 0) {
			if (res >= 0) {
				if ((sreg & (1 << 3)) != 0) {
					sreg ^= (1 << 3);
				}
			} else {
				sreg |= (1 << 3);
			}
		} else {
			if (res >= 0) {
				sreg |= (1 << 3);
			} else {
				if ((sreg & (1 << 3)) != 0) {
					sreg ^= (1 << 3);
				}
			}
		}
		nflag(res);
		zflag(res);
		registers[r1] = res & mask;
	}

	private void nflag(int result) {
		if ((result & (1 << 7)) == 0) {
			if ((sreg & (1 << 2)) != 0) {
				sreg ^= (1 << 2);
			}
		} else {
			sreg |= (1 << 2);
		}
	}

	private void zflag(int result) {
		if (result != 0) {
			if ((sreg & (1)) != 0) {
				sreg ^= (1);
			}
		} else {
			sreg |= (1);
		}
	}
}