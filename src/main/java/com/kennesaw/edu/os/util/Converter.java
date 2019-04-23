package com.kennesaw.edu.os.util;
import java.util.*;
import java.io.*;
import java.math.BigInteger;


// Helper Class 
public class Converter
{
    // Hexadecimal TO Decimal
	public static int hexToDecimal (String hexValue)
	{
		return Integer.parseInt(hexValue,16);
	} 
	
	// Hexadecimal TO Binary
	//public static String hexToBinary (String hexValue)
   
   public static String hexToBinary(String hex) {
    return new BigInteger(hex, 16).toString(2);
 }
   
   
   
   
   
	/*{
       System.out.println(hexValue);
	    int decimal = Integer.parseInt(hexValue,16);
       System.out.println(decimal);
	    return Integer.toBinaryString(decimal);
	}*/
	
	// Decimal TO Hexadecimal
	public static String decToHexadecimal (int decValue)
	{
	    return Integer.toHexString(decValue);
	}
	
	// Decimal TO Binary
	public static String decToBinary (int decValue)
	{
	    return Integer.toBinaryString(decValue);
	}
	
	// Binary TO Decimal
	public static int binToDecimal (String binValue)
	{
	    return Integer.parseInt(binValue,2);
	}
	// Binary TO Hexadecimal
	public static String binToHexadecimal (String binValue)
	{
	    int decimal = Integer.parseInt(binValue, 2);
	    return Integer.toHexString(decimal);
	}
    // Function to change the string of binary to integer
    public static int binaryStringToInteger(String binStr) {
        int nInt = Integer.parseInt(binStr, 2); // Change binary string to int value
        return nInt;
    }

}
