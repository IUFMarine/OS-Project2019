package com.kennesaw.edu.os;

import com.kennesaw.edu.os.cpu.CPU;
import com.kennesaw.edu.os.cpu.Register;
import com.kennesaw.edu.os.dispatcher.Dispatcher;
import com.kennesaw.edu.os.memory.Disk;
import com.kennesaw.edu.os.memory.Loader;
import com.kennesaw.edu.os.memory.Memory;
import com.kennesaw.edu.os.memory.PCB;
import com.kennesaw.edu.os.memory.PCB.Status;
import com.kennesaw.edu.os.scheduler.Scheduler;
import com.kennesaw.edu.os.scheduler.Schedulerprocess;

import java.io.*;
import java.util.*;
import java.nio.*;
 

public class Driver {
   
   //instance variables
   private CPU[] cpus;
   private Thread[] threads; //simulated threads
   private Scheduler scheduler;
   private Dispatcher dispatcher;
   private Disk disk;
   private Memory memory;
   private static Loader loader;
   private Register registers;
   private PCB pcb;
   public static LinkedList<PCB> pcblist;
   public static LinkedList<CPU> cpuStatusList;
   public int cpuID;
   public Status status;
   public int counter;
   public int priority;
   public int startingAddress;
   public String inputFile;
   
   ///private String Status;
   
   //final static int NUM_CPUS = 1;
   public static int cpuset = 1;
   // public static int[] cpuset = { 1, 4 };

   
   //final static int D_SIZE = 2048;
   //final static int RAM_SIZE = 1024;
   //final static int RE_SIZE = 16;
   //final static int C_SIZE = 100;
   
   private static int RAMsize = 1024;
   private static int cacheSize = 100;
   private static int registerSize = 16;
   private static int disksize = 2048;
   private static int numcpus = 1;
   private static Schedulerprocess schedulerprocess = Schedulerprocess.FirstInFirstOut;
   
   
   public Driver( int disksize, int RAMsize, int registerSize, int cacheSize, int numcpus, 
   Schedulerprocess schedulerprocess) {
           
      //this.disk = disk;
      this.pcblist = new LinkedList <PCB>();
      this.disksize = disksize;
      this.RAMsize = RAMsize;
      this.registerSize = registerSize; 
      this.cacheSize = cacheSize;
      this.numcpus = numcpus;
      this.schedulerprocess = schedulerprocess;
      
      
      
      this.registers = new Register();
      
      this.loader = new Loader("C://Users//Marc//Desktop//OS-Project2019-master//OS-Project2019-master//src//main//java//com//kennesaw//edu//os//Instructions.txt");
      this.pcb = new PCB(cpuID, status, counter, priority, startingAddress);
      
      //loadingfile( new file getLoader().getResource( "Program File.txt"))
      //start run file here.
      //code for an output file here.
      
      this.cpus = new CPU[numcpus];
     //System.out.println(this.cpus);
      this.threads = new Thread[this.cpus.length];
   
      for (int x = 0; x < this.cpus.length; x++ ) {
			CPU cpu = new CPU(x);  
		   this.cpus[x] = cpu;
         cpuStatusList.add(cpu);
			this.threads[x] = new Thread( this.cpus[x] );
         //cpu.printDump();
      }
      
      for(int y = 0; y < this.pcb.getPC(); y++) {
         insertpcb(pcb); 
      }
      this.dispatcher = new Dispatcher();
      this.scheduler = new Scheduler(memory, disk, pcb, cpu, schedulerprocess, pcblist, cpuStatusList, dispatcher);
   }
   
   public void loadingfile(String inputfile) {
      loader = new Loader(inputFile);
   } 
   
   public void run() throws InterruptedException  {//for thread array.
      for(int e = 0; e < cpus.length; e++) {
         this.threads[e].start();
      }
         boolean jobscomplete = false;
         while(!jobscomplete) {
            this.scheduler.run();
            //this.dispatcher.run();
         
            boolean jobcompleted = true;
            for(PCB pcb: this.pcblist) {
               if(pcb.status.getStatus_NUM() != 4) {
                  jobcompleted = false;
               }
            }
            
         boolean notalive = true;
         for (Thread thread : this.threads) {
            if(thread != null) {
               notalive = false;
            }
         }
         
         for (CPU cpu : this.cpus) {
			//cpu.signalShutdown(); -- might need for later.
			synchronized (cpu) {
         cpu.notify(); 
			}
		}

		// Wait for the threads
		boolean[] joined = new boolean[this.threads.length];
		for ( int x = 0; x < joined.length; x++ ) {
			joined[x] = false;
		}

		boolean ATJoined = true;

		
      do {
			for ( int f = 0; f < this.cpus.length; f++ ) {
				synchronized (this.cpus[f]) {
				}
				this.threads[f].join();
				if ( !this.threads[f].isAlive() ) { 
					joined[f] = true;
				}
			}

			for ( boolean aJoined : joined ) {
				if ( !aJoined ) {
					ATJoined = false;
					break;
				}
			}
		} while (!ATJoined);        
   }
   
      //if(loader == completed) {
      // scheduler.run();
      //} code used here to check for rewriting back to memory.
   
      }
  
  
   public static void reset() {
      loader = null;
   }
 
   public static void main(String []args) {
      Driver driver = new Driver(disksize, RAMsize, registerSize, cacheSize, numcpus, schedulerprocess);
      // Try / Catch for loader execution
      try {
         loader.Run();
      } catch (IOException e) {
         System.out.println("Error loading file. Exception: " + e);
      }
      // Try / Catch for driver execution
      try {
         driver.run();
      } catch (InterruptedException e) {
         System.out.println("Driver did not run successfully. Exception : " + e);
      }
   }// end main method
   
   public void dump() {
      System.out.println("Disk size: " + disk +  "RAM usage: " + RAMsize + "Number of registers: " + registers );
      for (CPU cpu : this.cpus) {
        System.out.println( "ProcessID: " + pcb.getProcessID());
		   //cpu.printDump();
		   System.out.println();
      }  
   }//This method is here for the printdump, which may be placed else where, also should there be a pcb dump as well?
   
   public static void insertpcb(PCB pcb) {
      pcblist.add(pcb);
   } 
}//end driver class