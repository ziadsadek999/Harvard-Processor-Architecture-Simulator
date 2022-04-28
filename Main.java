
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
		
	}

}
