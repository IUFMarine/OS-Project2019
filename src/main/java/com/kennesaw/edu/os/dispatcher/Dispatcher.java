package com.kennesaw.edu.os.dispatcher;

import com.kennesaw.edu.os.cpu.CPU;
import com.kennesaw.edu.os.memory.PCB;
import com.kennesaw.edu.os.memory.Memory;
import com.kennesaw.edu.os.Driver;

import java.util.*;

public class Dispatcher implements Runnable {
   public CPU[] cpus; 
   public Memory memory;
   public PCB pcb;
   
   public Dispatcher (CPU [] cpus, Memory memory) {
      this.cpus = cpus;
      this.memory = memory;
     
   }
   public void setPCB(PCB pcb)
   {
   this.pcb = pcb;
   }
   
   public void run() {//dispatcher sets the cpu process and then loads in the pcb through context switching. 
    System.out.println("here" + this.cpus);
    for (CPU cpu : this.cpus) {
    
		if (cpu.getPCB() == null)  {
				cpu.getCPUStatus();
            cpu.setPCB(pcb);
            cpu.run();
				//Write back something into memory here.
            synchronized (cpu) {
            cpu.notify();
            }
       }
    }
   }//end method
}//end class
  
