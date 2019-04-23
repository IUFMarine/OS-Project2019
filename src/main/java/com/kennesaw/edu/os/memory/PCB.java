package com.kennesaw.edu.os.memory;

import java.io.*; 
public class PCB //implements Comparable
{
   public int ProcessID;
   //String Status;
   public int instructionLength;
   int priority;
   int startingAddress;
   char PC;
   int Input_Buffer;
   int Output_Buffer;
   int temp_buffer_size;
   
   public enum Status
   {
      RUNNING(0), READY(1), BLOCKED(2), NEW(3), TERMINATED(4);
      
      public int Status_TYPE;
      
      Status(int Status_NUM) {
         Status_TYPE = Status_NUM;
      }
      
      public int getStatus_NUM() {
         System.out.println(this.Status_TYPE);
         return this.Status_TYPE;
      }
            
   }
   

   //Total wating time
   long startWaitingTime = 0;
   long endWaitingTime = 0;
   long totalWaitingTime = 0;   
   //Total running time
   long startRunningTime = 0;
   long endRunningTime = 0;
   long totalRunningTime = 0;
   
   public Status status;
   
   
   public void Times() 
   {
        //Waiting time measurement---- NEW, BLOCKED, and READY
      if(status.getStatus_NUM() == 1 || status.getStatus_NUM() == 2 || status.getStatus_NUM() == 3)
      {       //Waiting Time
         startWaitingTime = System.nanoTime();
         if(status.getStatus_NUM() != 1 || status.getStatus_NUM() != 2 || status.getStatus_NUM() != 3)
         {
            endWaitingTime = System.nanoTime();
         }
                
         totalWaitingTime += endWaitingTime - startWaitingTime;
      }
          //When it is running status 
      else if(status.getStatus_NUM() == 0)
      {
                //Running Time
         startRunningTime = System.nanoTime();
                //Add a process
         if(status.getStatus_NUM() != 0)
         {
            endRunningTime = System.nanoTime();
         }
                
         totalRunningTime += endRunningTime - startRunningTime;
      }
          //When it is terminated
      else
      {
         System.out.println("Process " + ProcessID + "Terminated");
         System.out.println("Total Running Time : " + totalRunningTime);
         System.out.println("Total Waiting Time : " + totalWaitingTime);
      }
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
   
   public void setProcessID(int ProcessID)
   {
      this.ProcessID = ProcessID;
   }
   public int getProcessID()
   {
      return this.ProcessID;
   }
 
   
   public void setPriority(int priority)
   {
      this.priority = priority;
   }
   public int getPriority()
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
   
   public int getInstructionLength()
   {
      return this.instructionLength;
   }
   
   public long getWaitingTime()
   {
      return this.totalWaitingTime;
   }
   
   public long getRunningTime()
   {
      return this.totalRunningTime;
   }
   
   public void setInput_Buffer(int b)
   {
      this.Input_Buffer = b;
   }
   
   public int getInput_Buffer()
   {
      return this.Input_Buffer;
   }
   
   public void setOutput_Buffer(int b)
   {
      this.Output_Buffer = b;
   }

   public int getOutput_Buffer()
   {
      return this.Output_Buffer;
   }


   //@override public compareTo()


   public PCB(int ProcessID, Status status, int instructionLength, int priority, int startingAddress )
   {
      this.ProcessID = ProcessID;
      this.status = status;
      this.priority = priority;
      this.instructionLength = instructionLength;
      this.startingAddress = startingAddress;
   
   }

}



