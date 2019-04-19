package com.kennesaw.edu.os.util;
import java.util.*;
import java.io.*;


// Helper Class 
class Converter
{
    // Hexadecimal TO Decimal
	public static int hexToDecimal (String hexValue)
	{
		return Integer.parseInt(hexValue,16);
	} 
	
	// Hexadecimal TO Binary
	public static String hexToBinary (String hexValue)
	{
	    int decimal = Integer.parseInt(hexValue,16);
	    return Integer.toBinaryString(decimal);
	}
	
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
}
