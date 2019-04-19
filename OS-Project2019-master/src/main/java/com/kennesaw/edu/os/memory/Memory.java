package com.kennesaw.edu.os.memory;
import java.util.*;
import java.io.*;

public class Memory
{
   public static String memory[] = new String[1024];
   boolean busy = true;
   int x;
   int lock;
   int next = 0;
    
    // Reads from memory
    public static String readMemory(int address)
    {
        if (address > memory.length)
            System.out.println("Address is invalid.");
        return memory[address];
    }
    // Writes to memory
    public static void writeMemory(int address, String data)
    {
        memory[address] = data;
    }
    // Determines if memory is full
    public static boolean isFull()
    {
        return Arrays.asList(memory).indexOf(null) == -1;
    }
   
   // Acquire for the semaphore (lock)
   public static void acquire(boolean busy, int lock, int next)
   {
      while(lock == 1) // if key is there
      {
         lock--; // process takes key
         busy = true; // crit section becomes locked
         
         // do stuff: gotta figure out what "stuff" actually is
        
         next++;
      }
   }
   // Release for the semaphore (lock)
   public static void release(boolean busy, int lock)
   {
      lock++; // if previous process is complete, replace the key
      busy = false; // crit section is open
   }
}

