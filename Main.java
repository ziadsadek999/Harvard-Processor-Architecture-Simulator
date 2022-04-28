
public class Main {
	Short[] instructions = new Short[1024];
	Byte[] data = new Byte[2048];
	byte[] registers = new byte[64];
	byte statusRegister = 0;
	short pc = 0;
	Short fetching;
	Short decoding;
	Short executing;

	public void run() {
		int c = 0;
		while (true) {
			if (pc >= 1024)
				break;
			executing = decoding;
			decoding = fetching;
			fetching = instructions[pc++];
			if (executing == null && decoding == null && fetching == null)
				break;
			if(executing != null) {
				exec(executing);
			}
			c++;
		}
	}

	public void exec(short instruction) {
		short opcode = (short) (instruction>>12);
		switch(opcode) {
			case 0: add(instruction);break;
			case 1: sub(instruction);break;
			case 2: mul(instruction);break;
			case 3: ldi(instruction);break;
			case 4: beqz(instruction);break;
			case 5: and(instruction);break;
			case 6: or(instruction);break;
			case 7: jr(instruction);break;
			case 8: slc(instruction);break;
			case 9: src(instruction);break;
			case 10: lb(instruction);break;
			case 11: sb(instruction);break;
		}
	}

	private void sb(short instruction) {
		// TODO Auto-generated method stub
		
	}

	private void lb(short instruction) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}

	private void beqz(short instruction) {
		// TODO Auto-generated method stub
		
	}

	private void ldi(short instruction) {
		// TODO Auto-generated method stub
		
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
