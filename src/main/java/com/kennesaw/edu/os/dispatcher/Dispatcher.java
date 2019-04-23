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
      System.out.println("Dispatcher Initialize"); 
      
      if (pcb != null)
      {
         System.out.println("Dispatcher PCB: ");
         System.out.println(pcb);
         System.out.println(cpu);
         cpu.setPCB(pcb);
         cpu.run();
         
      }
      else
      {
         System.out.println("Dispatcher passed null PCB object");
         return;
      }
              	
      /*synchronized (cpu) 
      {
         cpu.notify();
      }*/
   } //end method
} //end class
  
