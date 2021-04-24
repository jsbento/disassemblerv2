package disassembler;

import java.io.IOException;
import java.util.ArrayList;
import util.*;

public class Disassembler
{
	public static void main(String[] args) throws IOException
	{
		ArrayList<String> binInstr = FileManager.readFile(args[0]);
		ArrayList<String> hexInstr = Conversions.getAllHex(binInstr);
		ArrayList<String> instructions = new ArrayList<String>();
		InstructionParser ip = new InstructionParser();
		
		FileManager.writeToFile(hexInstr, "hexInstr.txt");
		FileManager.writeToFile(binInstr, "binInstr.txt");
		
		for(String instr : binInstr)
		{
			if(ip.getMem().containsKey(instr.substring(0,8)))
				instructions.add(ip.condBrnchParse(instr));
			else if(ip.getMem().containsKey(instr.substring(0, 11)))
				instructions.add(ip.regParse(instr));
			else if(ip.getMem().containsKey(instr.substring(0, 10)))
				instructions.add(ip.immParse(instr));
			else if(ip.getMem().containsKey(instr.substring(0, 11)))
				instructions.add(ip.dataParse(instr));
			else if(ip.getMem().containsKey(instr.substring(0, 6)))
				instructions.add(ip.branchParse(instr));
		}
		
		FileManager.writeToFile(instructions, "disassembly.txt");
		FileManager.writeAssembly(instructions, "disassembly.txt", "assembly.txt");
	}
}