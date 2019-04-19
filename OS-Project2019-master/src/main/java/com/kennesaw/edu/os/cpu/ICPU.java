package com.kennesaw.edu.os.cpu;

public interface ICPU {

	/**
	 * 
	 * @param pc
	 * @return
	 */
	public String fetch(int pc);
	
	public int decode(String instruct);
	
	public void execute(int opCode);
	
	public void run();
	
}
