
public class Main {
	Short[] instructions = new Short[1024];
	Byte[] data = new Byte[2048];
	byte[] registers = new byte[64];
	byte statusRegister = 0;
	short pc = 0;
	Short fetching;
	Short decoding;
	Short executing;
	short[] decodedInstruction;

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
				exec(executing);
			
			c++;
		}
	}

	public void decode(short instruction) {
		// opcode at index 0
		decodedInstruction[0] = (short) (instruction >> 12);
		// r1 at index 1
		short r1 = (short) (instruction >> 6);
		r1 = (short) (r1 % (1 << 6));
		decodedInstruction[1] = r1;
		// r2 or immediate at index 2
		decodedInstruction[2] = (short) (instruction % (1 << 6));
	}

	public void exec(short instruction) {
		short opcode = decodedInstruction[0];
		switch (opcode) {
		case 0:
			add(instruction);
			break;
		case 1:
			sub(instruction);
			break;
		case 2:
			mul(instruction);
			break;
		case 3:
			ldi(instruction);
			break;
		case 4:
			beqz(instruction);
			break;
		case 5:
			and(instruction);
			break;
		case 6:
			or(instruction);
			break;
		case 7:
			jr(instruction);
			break;
		case 8:
			slc(instruction);
			break;
		case 9:
			src(instruction);
			break;
		case 10:
			lb(instruction);
			break;
		case 11:
			sb(instruction);
			break;
		}
	}

	private void sb(short instruction) {
		short r2 = decodedInstruction[2];
		short r1 = decodedInstruction[1]
		data[r2]=registers[r1];

	}

	private void lb(short instruction) {
		short r2 = decodedInstruction[2];
		short r1 = decodedInstruction[1]
		registers[r1]=data[r2];

	}

	private void src(short instruction) {
		// TODO Auto-generated method stub

	}

	private void slc(short instruction) {
		// TODO Auto-generated method stub

	}

	private void jr(short instruction) {
		// TODO Auto-generated method stub

	}

	private void or(short instruction) {
		// TODO Auto-generated method stub

	}

	private void and(short instruction) {
		short r2 = decodedInstruction[2];
		short r1 = decodedInstruction[1];
		registers[r1] &= registers[r2];

	}

	private void beqz(short instruction) {
		short imm = decodedInstruction[2];
		short r = decodedInstruction[1];
		if (r == 0) {
			pc = (short) (pc + 1 + imm);
		}

	}

	private void ldi(short instruction) {
		short imm = decodedInstruction[2];
		short r = decodedInstruction[1];
		registers[r] = (byte) imm;
	}

	private void mul(short instruction) {
		// TODO Auto-generated method stub

	}

	private void sub(short instruction) {
		// TODO Auto-generated method stub

	}

	private void add(short instruction) {
		// TODO Auto-generated method stub

	}

}
