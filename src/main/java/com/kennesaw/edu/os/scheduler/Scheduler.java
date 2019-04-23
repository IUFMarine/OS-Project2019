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
   public LinkedList<PCB> waitingList = new LinkedList<PCB>();
   public LinkedList<PCB> termList = new LinkedList<PCB>();
   public LinkedList<CPU> cpuStatusList;
   public String Address = " ";
   public String Address2 = " ";
   int holder;
   

   public Scheduler(Memory memory, Disk disk, PCB pcb, Schedulerprocess schedulerprocess, LinkedList<PCB> pcblist, LinkedList<CPU> cpuStatusList, Dispatcher dispatcher) {
   
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
      System.out.println("Scheduler Initialize");
   	// Remove terminated processes from the RAM, may need to change read or other parameters.
      for (PCB pcb : this.pcblist) {
         if (pcb.status.getStatus_NUM() == 4) {
            termList.add(pcb);
            pcblist.remove(pcb);
            for ( int x = 0; x < pcblist.size(); x++ ) {
               Memory.writeMemory(pcb.getStartingAddress(), pcb.getStartingAddress() + Address);
            }			
         }
      }
      for (PCB pcb : this.waitingList)
      {
         if(pcb.status.getStatus_NUM() != 1 || pcb.status.getStatus_NUM() != 2 || pcb.status.getStatus_NUM() != 3)
         {
            pcblist.add(pcb);
            waitingList.remove(pcb);
         }
      
      }
   
   	// Find next process
      if ( pcblist.size() > 0 ) {
         System.out.println("PCB list is populated");
         for(int i = 0; i < pcblist.size(); i++) {
            Jobqueue.add(pcblist.get(i));
            System.out.println("PCB Added to JobQueue" + Jobqueue.get(i));
         }
         if ( this.schedulerprocess == Schedulerprocess.Priority ) {
            System.out.println("Scheduler operating under Priority Scheduling");
         	//Find highest priority process
            PCB pcbp;
            pcbp =  Collections.max(Jobqueue, Comparator.comparing(pcb -> pcb.getPriority()));
            System.out.println("Max Priority PCB: " + pcbp);
            System.out.println(pcbp.instructionLength);       
            if(pcbp.status.getStatus_NUM() == 3 )
            {
               //System.out.println(pcbp);
               writeDiskToMem(pcbp);
               readyqueue.add(pcbp); 
               Jobqueue.remove(pcbp);
               if(Jobqueue.size() == 0)
               {
                  Driver.jobscomplete = true;
               }  
            }
            else 
            {
               waitingList.add(pcbp);
               Jobqueue.remove(pcbp);
            }
                      
         }
      } else if ( this.schedulerprocess == Schedulerprocess.FirstInFirstOut ) {
         System.out.println("Scheduler operating under FIFO Scheduling");
         for(int z = 0; z < pcblist.size(); z++) {
            
            PCB pcbp = pcblist.getFirst();
            if(pcbp.status.getStatus_NUM() == 1 || pcbp.status.getStatus_NUM() == 2 || pcbp.status.getStatus_NUM() == 3)
            {
            
               writeDiskToMem(pcbp);
               readyqueue.add(pcbp); 
               Jobqueue.remove(pcbp);
               if(Jobqueue.size() == 0)
               {
                  Driver.jobscomplete = true;
               } 
            }
            else 
            {
               waitingList.add(pcbp);
               Jobqueue.remove(pcbp);
            }
         
         }
      }
      while(readyqueue.size() != 0){  
         System.out.println(readyqueue.getFirst()); 
         CPU readycpu; 
         CPU cpucheck;
         for(int x = 0; x < cpuStatusList.size(); x++)
         {
         
            cpucheck = cpuStatusList.get(x);
            System.out.println(cpucheck);
         
            if(cpucheck.statusOfCPU.getStatus_NUM() == 1)
            {
               System.out.println("Ready CPU found");
               readycpu = cpucheck;
               dispatcher.setCPU(readycpu);
               continue; 
            }
         }
      
         PCB temppcb;
         if(readyqueue != null)
         {
            temppcb = readyqueue.getFirst();
            if(temppcb != null)
               dispatcher.setPCB(temppcb);
            else
               System.out.println("Null PCB object in Dispatcher");
         
            dispatcher.run();
            pcb.status = pcb.status.RUNNING;
            readyqueue.remove();
         }
      }
   }
}

//may place enum file here.
