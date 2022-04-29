
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
		short r2 = decodedInstruction[2];
		short r1 = decodedInstruction[1];
		data[r2]=registers[r1];

	}

	private void lb() {
		short r2 = decodedInstruction[2];
		short r1 = decodedInstruction[1];
		registers[r1]=data[r2];

	}

	private void src() {
		// TODO Auto-generated method stub

	}

	private void slc() {
		// TODO Auto-generated method stub

	}

	
	private void jr() {
		short r2 = decodedInstruction[2];
		short r1 = decodedInstruction[1];
		pc = Short.parseShort(Byte.toString(registers[r1]) + Byte.toString(registers[r2]));

	}

	private void or() {
		short r2 = decodedInstruction[2];
		short r1 = decodedInstruction[1];
		registers[r1] |= registers[r2] ;

	}

	private void and() {
		short r2 = decodedInstruction[2];
		short r1 = decodedInstruction[1];
		registers[r1] &= registers[r2];

	}

	private void beqz() {
		short imm = decodedInstruction[2];
		short r = decodedInstruction[1];
		if (r == 0) {
			pc = (short) (pc + 1 + imm);
		}

	}

	private void ldi() {
		short imm = decodedInstruction[2];
		short r = decodedInstruction[1];
		registers[r] = (byte) imm;
	}

	private void mul() {
		short r2 = decodedInstruction[2];
		short r1 = decodedInstruction[1];
		registers[r1] *= registers[r2] ;

	}

	private void sub() {
		short r2 = decodedInstruction[2];
		short r1 = decodedInstruction[1];
		registers[r1] -= registers[r2] ;

	}

	private void add() {
		short r2 = decodedInstruction[2];
		short r1 = decodedInstruction[1];
		registers[r1] += registers[r2] ;
	}

}
