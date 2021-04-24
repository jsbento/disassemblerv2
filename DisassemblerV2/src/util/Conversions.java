package util;

import java.math.BigInteger;
import java.util.ArrayList;

public class Conversions
{
	private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
	private static final int BINARY_RADIX = 2;
	
	public static String bytesToHex(byte[] bytes)
    {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++)
        {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
	
	public static String toBinaryString(byte[] arr)
    {
    	String result = "";
        for(int i = 0; i < arr.length; i++)
        {
        	StringBuilder sb = new StringBuilder("00000000");
            for(int bit = 0; bit < 8; bit++)
            	if(((arr[i] >> bit) & 1) > 0)
            		sb.setCharAt(7 - bit, '1');
            result += sb.toString();
        }
        return result;
    }
	
	public static String toHexString(String binaryString)
	{
		return new BigInteger(binaryString, 2).toString(16);
	}
	
	public static ArrayList<String> getAllHex(ArrayList<String> binaryInstr)
	{
		ArrayList<String> output = new ArrayList<String>();
		
		for(String s : binaryInstr)
			output.add(toHexString(s));
		
		return output;
	}
	
	public static int binaryToDecimal(String binaryString)
	{
		return Integer.parseInt(binaryString, BINARY_RADIX);
	}
	
	public static int twosComplementToDecimal(String binaryString)
	{
		int val = binaryToDecimal(binaryString)-1;
		String binString = Integer.toBinaryString(val);
		String ret = "";
		for(char c : binString.toCharArray())
			if(c == '0')
				ret += "1";
			else
				ret += "0";
		return -Integer.parseInt(ret, BINARY_RADIX);
	}
}