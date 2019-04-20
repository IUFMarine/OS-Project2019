package com.kennesaw.edu.os.scheduler;

import com.kennesaw.edu.os.Driver;
import com.kennesaw.edu.os.memory.Memory;
import com.kennesaw.edu.os.memory.Disk;
import com.kennesaw.edu.os.memory.PCB;
import com.kennesaw.edu.os.dispatcher.Dispatcher;
import com.kennesaw.edu.os.scheduler.Schedulerprocess;
import com.kennesaw.edu.os.cpu.CPU;
import com.kennesaw.edu.os.cpu.CPU.CPUStatus;


import java.util.*;

public class Scheduler implements Runnable {

   private Memory memory;
   private Disk disk;
   private PCB pcb;
   private CPU cpu;
   private Dispatcher dispatcher;
   private Schedulerprocess schedulerprocess;
   public LinkedList<PCB> readyqueue = new LinkedList<PCB>();
   public LinkedList<PCB> Jobqueue = new LinkedList<PCB>();
   public LinkedList<PCB> pcblist;//temp. a string list might need to change data structure for other variables as well.
   public LinkedList<CPU> cpuStatusList;
   public String Address = " ";
   public String Address2 = " ";
   int holder;
   

   public Scheduler(Memory memory, Disk disk, PCB pcb, CPU cpu, Schedulerprocess schedulerprocess, LinkedList<PCB> pcblist, LinkedList<CPU> cpuStatusList, Dispatcher dispatcher) {
   
      this.cpu = cpu;
      this.pcb = pcb;
      this.disk = disk;
      this.memory = memory;
      this.schedulerprocess = schedulerprocess;
      this.pcblist = pcblist;
      this.dispatcher = dispatcher;
      this.cpuStatusList =cpuStatusList;
      
   }
   public void writeDiskToMem (PCB pcb)
   {
      int Dadd = pcb.getStartingAddress();
      int Madd = pcb.getStartingAddress();
   
      String[] temp = new String[32];
        
      for(int t = 0; t < pcb.getInstructionLength(); t++)
      {
         temp[t] = Disk.read(Dadd);
         Dadd++;
      }
      for(int w = 0; w < pcb.getInstructionLength(); w++)
      {
         Memory.writeMemory(Madd, temp[w]);
         Madd++;
      }
   }


   @Override public void run() {
   	// Remove terminated processes from the RAM, may need to change read or other parameters.
      for (PCB pcb : this.pcblist) {
         if (pcb.status.getStatus_NUM() == 4) {
            for ( int x = 0; x < pcblist.size(); x++ ) {
              Memory.writeMemory(pcb.getStartingAddress(), pcb.getStartingAddress() + Address);
            }			
         }
      }
   
   
   	// Find next process
      if ( pcblist.size() > 0 ) {
         for(int i = 0; i < pcblist.size(); i++) {
            Jobqueue.add(pcb);
         }
         if ( this.schedulerprocess == Schedulerprocess.Priority ) {
         	//Find highest priority process
           PCB pcbp =  Collections.max(Jobqueue, Comparator.comparing(pcb -> pcb.getPriority()));
           writeDiskToMem(pcbp);
           readyqueue.add(pcbp); 
           Jobqueue.remove(pcbp);              
            }
         } else if ( this.schedulerprocess == Schedulerprocess.FirstInFirstOut ) {
            for(int z = 0; z < pcblist.size(); z++) {
               pcblist.get(z);
            }
         }
         
      CPU readycpu; 
     for(int x = 0; x < cpuStatusList.size(); x++)
     {
     CPU cpucheck = new CPU(5);
      cpucheck = cpuStatusList.get(x);
     
      if(cpucheck.CPUStatus.getStatus_NUM() == 0)
      {
      readycpu = cpucheck;
      continue; 
 }
 }
 while(Jobqueue.size() != 0){
 PCB temppcb;
 
 temppcb = Jobqueue.getFirst();
 
      dispatcher.setCPU(cpu);
      dispatcher.setPCB(temppcb);
      dispatcher.run();
      pcb.status = pcb.status.RUNNING;
      Jobqueue.remove();
   }
   }
}

//may place enum file here.
