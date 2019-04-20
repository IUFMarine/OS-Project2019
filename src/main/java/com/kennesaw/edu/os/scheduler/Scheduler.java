package com.kennesaw.edu.os.scheduler;

import com.kennesaw.edu.os.Driver;
import com.kennesaw.edu.os.memory.Memory;
import com.kennesaw.edu.os.memory.Disk;
import com.kennesaw.edu.os.memory.PCB;
import com.kennesaw.edu.os.dispatcher.Dispatcher;
import com.kennesaw.edu.os.scheduler.Schedulerprocess;

import java.util.*;

public class Scheduler implements Runnable {

	private Memory memory;
   private Disk disk;
	private PCB pcb;
   private Dispatcher dispatcher;
	private Schedulerprocess schedulerprocess;
   public LinkedList<PCB> readyqueue = new LinkedList<PCB>();
   public LinkedList<PCB> Jobqueue = new LinkedList<PCB>();
   public LinkedList<PCB> pcblist;//temp. a string list might need to change data structure for other variables as well.
   public String Address = " ";
   public String Address2 = " ";
   int holder;
   

	public Scheduler( Memory memory, Disk disk, PCB pcb, Schedulerprocess schedulerprocess, LinkedList<PCB> pcblist, Dispatcher dispatcher) {

		this.pcb = pcb;
      this.disk = disk;
		this.memory = memory;
		this.schedulerprocess = schedulerprocess;
      this.pcblist = pcblist;
      this.dispatcher = dispatcher;
	}

	@Override public void run() {
		// Remove terminated processes from the RAM, may need to change read or other parameters.
		for (PCB pcb : this.pcblist) {
			if (pcb.status.getStatus_NUM() == 4) {
					for ( int x = 0; x < pcblist.size(); x++ ) {
						this.disk.write(pcb.getStartingAddress(), pcb.getStartingAddress() + Address);
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
				for ( PCB pcb : pcblist ) {
					if ( pcb.getPriority() < pcblist.size()) { //need a look ahead for priority check.
						 //return pcb item in list. Will check will bug squashing.
					}
				}
			} else if ( this.schedulerprocess == Schedulerprocess.FirstInFirstOut ) {
			   for(int z = 0; z < pcblist.size(); z++) {
               pcblist.get(z);
            }
			}
         int size = Jobqueue.size();
         for(int q = 0; q < size; q++) {
            this.memory.writeMemory(q, pcb.getStartingAddress() + Address2);//change string parameter here.
         }
         
         
            readyqueue.add(pcb);//pseudo coded.
            
            dispatcher.setPCB(pcb);
            dispatcher.run();
				pcb.status = pcb.status.READY;
			}
		}
}
//may place enum file here.
