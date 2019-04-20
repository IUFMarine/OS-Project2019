package com.kennesaw.edu.os.memory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Loader
{
    public BufferedReader br;
    public boolean endOfFile = true;
    
    public Loader(String inputFile)  
    {
        try 
        {
        File file = new File(inputFile);
        Scanner scan = new Scanner("Compiletest.txt");
        } catch(Exception e) {
            System.out.print(e);
        }
    }
    
    public void Run() throws IOException
    {
        String lineInFile = "";
        // Job card
        int jobID;
        int num_of_words;
        int priority;
        // Data card
        int input_buffer;
        int output_buffer;
        int temp_buffer_size;
        
        int startingAddress = 0;
        
        int index = 0;
        String[] discard = null;
        //PCB pcb;
        String[] lineArray = new String[5];
        
        while(!endOfFile)
        {
            lineInFile = br.readLine(); // Reads file
            
            if(lineInFile != null)
            {
                // JOB
                if(lineInFile.contains("// JOB")) // Determines whether line contains JOB
                {
                  lineArray = lineInFile.split("\\s+");
                  jobID = Integer.parseInt(lineArray[2],16); // Passes value at index 2 in array to jobID
                  num_of_words = Integer.parseInt(lineArray[3],16); // Passes value at index 3 to now
                  priority = Integer.parseInt(lineArray[4]); // Passes value at index 4 to priorityw
                 
                  
                  PCB pcb = new PCB(jobID, PCB.Status.NEW, num_of_words, priority, startingAddress);
                  startingAddress += num_of_words;
                }//end if
                
                // DATA
                else if(lineInFile.contains("// DATA")) // Determines if line contains DATA
                {
                  lineArray = lineInFile.split("\\s+");
                  input_buffer = Integer.parseInt(lineArray[2], 16);
                  output_buffer = Integer.parseInt(lineArray[3], 16);
                  temp_buffer_size = Integer.parseInt(lineArray[4], 16);
                }//end else if
                
                else if(lineInFile.contains("// END"))
                {
                  endOfFile = true;
                  System.out.println("You have reached the end of the file.");
                }
            }//end if
            
            else 
            {
                Disk.write(index, lineInFile); // Writes to Disk
                index++; 
            }//end else        
      
    }//end while
  }//end Run
}//end loader