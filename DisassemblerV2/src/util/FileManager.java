package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.Arrays;

public class FileManager
{
	private static final int BYTE_OFFSET = 4;
	
	public static ArrayList<String> readFile(String filename) throws IOException
	{
		File input = new File("C:/Users/bento/Desktop/FileRepo/"+filename);
		ArrayList<String> instructions = new ArrayList<String>();
		byte[] bytes = Files.readAllBytes(input.toPath());
		
		for(int i = 0; i < bytes.length; i += BYTE_OFFSET)
			instructions.add(Conversions.toBinaryString(Arrays.copyOfRange(bytes, i, i+BYTE_OFFSET)));
		
		return instructions;
	}
	
	public static void writeToFile(ArrayList<String> strList, String fileName) throws IOException
	{
		File output = new File(fileName);
		FileWriter writer = new FileWriter(output);
		
		for(String s : strList)
			writer.write(s+"\n");
		
		writer.close();
	}
	
	public static void writeAssembly(ArrayList<String> assemblyInstr, String disassembled, String output) throws IOException
	{
		File disassembly = new File(disassembled);
		Scanner disassemScan = new Scanner(disassembly);
		HashMap<Integer, String> labels = new HashMap<>();
		int lineNum = 1;
		
		while(disassemScan.hasNextLine())
		{
			String line = disassemScan.nextLine();
			if(line.indexOf("B.") == 0 || line.indexOf("CBZ") == 0 ||
			   line.indexOf("CBNZ") == 0 || line.indexOf("BL") == 0 ||
			   ((line.indexOf("BR") == -1 && line.indexOf("BL") == -1) &&
			   line.indexOf("B") == 0))
			{
				int linesAway = Integer.parseInt(line.substring(line.lastIndexOf(' ')+1));
				labels.put(lineNum+linesAway, "");
			}
			lineNum++;
		}
		
		fillLabelNames(labels);
		disassemScan.close();
		
		File assembly = new File(output);
		FileWriter assemWrite = new FileWriter(assembly);
		disassemScan = new Scanner(disassembly);
		int line = 1;
		
		while(disassemScan.hasNextLine())
		{
			String disassemLine = disassemScan.nextLine();
			if(labels.containsKey(line))
				assemWrite.write("\n"+labels.get(line)+":\n");
			
			if(disassemLine.indexOf("B.") == 0 || disassemLine.indexOf("CBZ") == 0||
			        disassemLine.indexOf("CBNZ") == 0 || disassemLine.indexOf("BL") == 0 ||
			        ((disassemLine.indexOf("BR") == -1 && disassemLine.indexOf("BL") == -1) &&
			        disassemLine.indexOf("B") == 0))
			{
				int labelNum = line+Integer.parseInt(disassemLine.substring(disassemLine.lastIndexOf(' ')+1));
				assemWrite.write(disassemLine.substring(0, disassemLine.lastIndexOf(' ')+1)+labels.get(labelNum)+"\n");
			}
			else
				assemWrite.write(disassemLine+"\n");
			line++;
		}
		disassemScan.close();
		assemWrite.close();
	}
	
	private static void fillLabelNames(HashMap<Integer, String> labels)
	{
		int i = 0;
		for(Integer key : labels.keySet())
			labels.put(key, "label_"+(i++));
	}
}