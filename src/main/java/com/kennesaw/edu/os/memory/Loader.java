package com.kennesaw.edu.os.memory;

import java.io.*;
import java.util.Scanner;
import com.kennesaw.edu.os.Driver;

public class Loader
{
   //public BufferedReader br;
   public boolean endOfFile = true;
   private File file;
   private BufferedReader br;
    
   public Loader(String inputFile)  
   {
      try 
      {
         System.out.println(inputFile);
         file = new File(inputFile);
         br = new BufferedReader(new FileReader(inputFile));
         endOfFile = false;
      } catch(Exception e) {
         System.out.print("Error in loader" + e);
      }
   }
    
   public void Run() throws IOException
   {
      System.out.println("Loader Initialize");
      //String lineInFile = "";
        // Job card
      int jobID;
      int num_of_words;
      int priority;
        // Data card
      int input_buffer;
      int output_buffer;
      int temp_buffer_size;
      PCB pcb = null;        
      int startingAddress = 0;
      int numPCB = 0;
      int index = 0;
      String[] discard = null;
        //PCB pcb;
      String[] lineArray = new String[5];
        
      while(!endOfFile)
      {
         //lineInFile = br.readLine(); // Reads file
         String lineInFile = br.readLine(); 
         System.out.println(lineInFile);
         
            
         if(lineInFile != null)
         {
            System.out.println("Reading File");
                // JOB
            if(lineInFile.contains("// JOB")) // Determines whether line contains JOB
            {
               lineArray = lineInFile.split("\\s+");
               jobID = Integer.parseInt(lineArray[2],16); // Passes value at index 2 in array to jobID
               num_of_words = Integer.parseInt(lineArray[3],16); // Passes value at index 3 to now
               priority = Integer.parseInt(lineArray[4]); // Passes value at index 4 to priorityw
                 
                  
               pcb = new PCB(jobID, PCB.Status.NEW, num_of_words, priority, startingAddress);
               Driver.insertpcb(pcb);
               System.out.println("PBC " + numPCB + " created.");
               numPCB++;
               startingAddress += num_of_words;
            }//end if
                
                // DATA
            else if(lineInFile.contains("// DATA")) // Determines if line contains DATA
            {
               lineArray = lineInFile.split("\\s+");
               input_buffer = Integer.parseInt(lineArray[2], 16);
               pcb.setInput_Buffer(input_buffer);
               output_buffer = Integer.parseInt(lineArray[3], 16);
               pcb.setOutput_Buffer(output_buffer);
               temp_buffer_size = Integer.parseInt(lineArray[4], 16);
            }//end else if
                
            else if(lineInFile.contains("// END"))
            {
               endOfFile = true;
               System.out.println("You have reached the end of the file.");
            }
            //end if
            
            else 
            {
               Disk.write(index, lineInFile); // Writes to Disk
               index++; 
            }//end else        
         }
      }//end while
   }//end Run
}//end loader