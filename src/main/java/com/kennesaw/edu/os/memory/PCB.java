package com.kennesaw.edu.os.memory;
import java.io.*; 
public class PCB
{
   int cpuID;
   String Status;
   int counter;
   int priority;
   int startingAddress;
   char PC;
  

    enum Status
   {
      RUNNING, READY, BLOCKED, NEW
   }
  

   //Setters and getters
   public void setPC(char PC)
   {
      this.PC = PC;
   }
   public int getPC()
   {
     return this.PC;
   }
   
   public void setcpuID(int cpuID)
   {
      this.cpuID = cpuID;
   }
   public int getcpuID()
   {
     return this.cpuID;
   }
 
   
   public void setPriority(int priority)
   {
      this.priority = priority;
   }
   public int gePriority()
   {
     return this.priority;
   }
   
   public void setStartingAddress(int startingAddress)
   {
      this.startingAddress = startingAddress;
   }  
   
   public int getStartingAddress()
   {
     return this.startingAddress;
   }


   public PCB(int cpuID, String Status, int counter, int priority, int startingAddress )
   {
      this.cpuID = cpuID;
      this.Status = Status;
      this.counter = counter;
      this.priority = priority;
      this.startingAddress = startingAddress;

   }

}
