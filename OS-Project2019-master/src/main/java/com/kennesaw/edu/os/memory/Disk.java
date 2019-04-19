package com.kennesaw.edu.os.memory;

public class Disk
{
	private static String[] Disk = new String[2048]; // String array for Disk
	

	// Read Disk
	public static String read(int address)
	{
	    return Disk[address];
	}
	
	// Write to Disk
	public static void write(int address, String data)
	{
	   Disk[address] = data;
	}
}
