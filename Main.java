
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
		int res = registers[r1]*registers[r2];
		

	}

	private void sub() {
		int r2 = decodedInstruction[2];
		int r1 = decodedInstruction[1];
		registers[r1] -= registers[r2];

	}

	private void add() {
		int r2 = decodedInstruction[2];
		int r1 = decodedInstruction[1];
		registers[r1] += registers[r2];
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
	private void cflag(int result) {
		if (result <= Byte.MAX_VALUE) {
			if ((sreg & (1<<4)) != 0) {
				sreg ^= (1<<4);
			}
		} else {
			sreg |= (1<<4);
		}
	}
}