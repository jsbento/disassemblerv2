package disassembler;

import java.util.HashMap;

import util.Conversions;

public class InstructionParser
{
	public HashMap<String, String> instrMem;
	
	public InstructionParser()
	{
		instrMem = new HashMap<>();
		
		instrMemInit();
	}
	
	public void instrMemInit()
	{
		instrMem.put("10001011000", "ADD");
		instrMem.put("10001010000",	"AND");
		instrMem.put("11010110000",	"BR");
		instrMem.put("11111111110",	"DUMP");
		instrMem.put("11001010000",	"EOR");
		instrMem.put("11111111111",	"HALT");
		instrMem.put("11010011011",	"LSL");
		instrMem.put("11010011010",	"LSR");
		instrMem.put("10101010000",	"ORR");
		instrMem.put("10011011000",	"MUL");
		instrMem.put("11111111100",	"PRNL");
		instrMem.put("11111111101",	"PRNT");
		instrMem.put("11001011000",	"SUB");
		instrMem.put("11101011000",	"SUBS");
		instrMem.put("1001000100", "ADDI");
		instrMem.put("1001001000", "ANDI");
		instrMem.put("1101001000", "EORI");
		instrMem.put("1011001000", "ORRI");
		instrMem.put("1101000100", "SUBI");
		instrMem.put("1111000100", "SUBIS");
		instrMem.put("000101", "B");
		instrMem.put("100101", "BL");
		instrMem.put("10110101", "CBNZ");
		instrMem.put("10110100", "CBZ");
		instrMem.put("01010100", "B.cond");
		instrMem.put("11111000010", "LDUR");
		instrMem.put("11111000000", "STUR");
	}
	
	public HashMap<String, String> getMem()
	{
		return instrMem;
	}
	
	public String checkSpecialRegisters(String str)
	{
		String ret = str;
		ret = ret.replaceAll("X28", "SP");
		ret = ret.replaceAll("X29", "FP");
		ret = ret.replaceAll("X30", "LR");
		ret = ret.replaceAll("X31", "XZR");
		
		return ret;
	}
	
	public String regParse(String instr)
	{
		String result = "";
		result += instrMem.get(instr.substring(0, 11))+" X"+
				  Conversions.binaryToDecimal(instr.substring(27))+", X"+
				  Conversions.binaryToDecimal(instr.substring(22, 27))+", ";
		
		if(result.contains("LSL") || result.contains("LSR"))
			result += "#"+Conversions.binaryToDecimal(instr.substring(16, 22));
		else if(result.contains("BR"))
			result = "BR X" + Conversions.binaryToDecimal(instr.substring(22, 27));
		else
			result += "X"+Conversions.binaryToDecimal(instr.substring(11, 16));
		
		if(result.contains("PRNT"))
			return result.substring(0, 7);
		else if(result.contains("PRNL"))
			return result.substring(0, 4);
		else if(result.contains("DUMP"))
			return result.substring(0, 4);
		else if(result.contains("HALT"))
			return result.substring(0, 4);
		else
			return checkSpecialRegisters(result);
	}
	
	public String immParse(String instr)
	{
		String result = "";
		result += instrMem.get(instr.substring(0, 10))+" X"+
				  Conversions.binaryToDecimal(instr.substring(27))+", X"+
				  Conversions.binaryToDecimal(instr.substring(22, 27))+", #"+
				  Conversions.binaryToDecimal(instr.substring(10, 22));
		
		return checkSpecialRegisters(result);
	}
	
	public String dataParse(String instr)
	{
		String result = "";
		result += instrMem.get(instr.substring(0, 11))+" X"+
				Conversions.binaryToDecimal(instr.substring(27))+", [X"+
				Conversions.binaryToDecimal(instr.substring(22, 27))+", #"+
				Conversions.binaryToDecimal(instr.substring(11, 20))+"]";
			
		return checkSpecialRegisters(result);
	}
	
	public String branchParse(String instr)
	{
		String result = "";
		result += instrMem.get(instr.substring(0, 6))+" "+
				  getBranch(instr.substring(6));
		return result;
	}
	
	public String condBrnchParse(String instr)
	{
		String result = "";
		result += instrMem.get(instr.substring(0, 8));
		if(!result.contains("B.cond"))
			result += " X"+Conversions.binaryToDecimal(instr.substring(27))+", "+
					  getBranch(instr.substring(8, 27));
		else
		{
			result = result.replace("cond", ""+getCondition(instr.substring(27)));
			result += ", "+getBranch(instr.substring(8, 27));
		}
		return result;
	}
	
	public int getBranch(String branchAddr)
	{
		if(branchAddr.charAt(0) == '1')
			return Conversions.twosComplementToDecimal(branchAddr);
		else
			return Conversions.binaryToDecimal(branchAddr);
	}
	
	public String getCondition(String cond)
	{
		int condition = Conversions.binaryToDecimal(cond);
		switch(condition)
		{
			case 0: return "EQ";
			case 1: return "NE";
			case 2: return "HS";
			case 3: return "LO";
			case 4: return "MI";
			case 5: return "PL";
			case 6: return "VS";
			case 7: return "VC";
			case 8: return "HI";
			case 9: return "LS";
			case 10: return "GE";
			case 11: return "LT";
			case 12: return "GT";
			case 13: return "LE";
		}
		return null;
	}
}