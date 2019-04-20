package com.kennesaw.edu.os.dispatcher;

import com.kennesaw.edu.os.cpu.CPU;
import com.kennesaw.edu.os.memory.PCB;
import com.kennesaw.edu.os.Driver;

import java.util.*;

public class Dispatcher implements Runnable {
   public CPU cpu; 
   public PCB pcb;
   
   public Dispatcher ()
   {
      
   }
   public void setPCB(PCB pcb)
   {
      this.pcb = pcb;
   }
   public void setCPU(CPU cpu)
   {
      this.cpu = cpu;
   }
   
   public void run() {
   //Dispatcher will be set a PCB and a CPU through the Scheduler. Dispatcher loads the PCB into the CPU instance and runs the CPU.  
      
      cpu.setPCB(pcb);
      if (pcb != null)
      {
         cpu.run();
      }
      else
      {
         System.out.println("Dispatcher passed null PCB to CPU");
         return;
      }
              	
      /*synchronized (cpu) 
      {
         cpu.notify();
      }*/
   } //end method
} //end class
  
