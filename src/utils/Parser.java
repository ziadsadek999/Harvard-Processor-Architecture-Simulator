package utils;

import java.io.*;
import java.util.HashMap;

import instructions.*;
import memory.InstructionMemory;

public class Parser {

	public static void parseProgram(String fileName) throws Exception {
		BufferedReader bufferedreader = new BufferedReader(new FileReader(fileName));
		HashMap<String, Integer> opcode = new HashMap<String, Integer>();
		opcode.put("ADD", 0);
		opcode.put("SUB", 1);
		opcode.put("MUL", 2);
		opcode.put("LDI", 3);
		opcode.put("BEQZ", 4);
		opcode.put("AND", 5);
		opcode.put("OR", 6);
		opcode.put("JR", 7);
		opcode.put("SLC", 8);
		opcode.put("SRC", 9);
		opcode.put("LB", 10);
		opcode.put("SB", 11);
		while (bufferedreader.ready()) {
			String inputLine = bufferedreader.readLine();
			String[] line = inputLine.split(" ");
			int binaryCode = opcode.get(line[0]);
			int x = binaryCode;
			binaryCode <<= 12;
			int r1 = Integer.parseInt(line[1].substring(1));
			int r2 = 0;
			if (line[2].charAt(0) == 'R') {
				r2 = Integer.parseInt(line[2].substring(1));
			} else {
				r2 = Integer.parseInt(line[2]);
			}
			r1 <<= 6;
			r2&=(1<<6)-1;
			binaryCode |= r1;
			binaryCode |= r2;
			Instruction instruction;
			switch (x) {
			case 0:
				instruction = new Add(binaryCode);
				break;
			case 1:
				instruction = new Sub(binaryCode);
				break;
			case 2:
				instruction = new Mul(binaryCode);
				break;
			case 3:
				instruction = new LDI(binaryCode);
				break;
			case 4:
				instruction = new BEQZ(binaryCode);
				break;
			case 5:
				instruction = new And(binaryCode);
				break;
			case 6:
				instruction = new Or(binaryCode);
				break;
			case 7:
				instruction = new JR(binaryCode);
				break;
			case 8:
				instruction = new SLC(binaryCode);
				break;
			case 9:
				instruction = new SRC(binaryCode);
				break;
			case 10:
				instruction = new LB(binaryCode);
				break;
			default:
				instruction = new SB(binaryCode);
				break;
			}
			instruction.setInstruction(inputLine);
			InstructionMemory.getInstance().addInstruction(instruction);
		}
	}
}
