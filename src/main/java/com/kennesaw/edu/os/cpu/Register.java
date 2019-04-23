package com.kennesaw.edu.os.cpu;
// Register class 
public class Register {
	
	// Global Variables
	int [] CPU_Register = null; // Initialize To Empty
	int [] accumlator;
	String data;

	// Constructor
	public Register() {
		accumlator = null; // Only used when accumlator is called
	}
	
	// Set Register 
	public void setReg(int value, int position) {
		// No space
		if (position == 1) return;
		// If space is available, then assign value to array
		else if (position < CPU_Register.length) {
			CPU_Register[position] = value; 
		} 
		else {
			System.out.println("Something went wrong when tryint to set register");
		}
	}
	
	// Retrieve Register
	public int getReg(int position) {
		return CPU_Register[position];
	}
	
	public void readBuffer(int buffer, int addr) {
		accumlator[addr] = buffer; // Set's data to parameter
	}
	
	public int writeBuffer(int buffer, int addr) {
		buffer = accumlator[addr];
		return buffer;
	}
}
