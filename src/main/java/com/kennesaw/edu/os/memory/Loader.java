package com.kennesaw.edu.os.memory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Loader
{
    public BufferedReader br;
    public boolean endOfFile = false;
    
    
    // Job Cards
    final int JOB_ID_POS = 2;
    final int JOB_NUM_OF_WORDS = 3;
    final int JOB_PRIORITY = 4;
    
    // Data Cards
    final int DATA_INPUT_BUFFER = 2;
    final int DATA_OUTPUT_BUFFER = 3;
    final int DATA_TEMP_BUFFER = 4;
    
    public Loader(String inputFile)  
    {
        try 
        {
        File file = new File(inputFile);
        Scanner scan = new Scanner("Instructions.txt");
        } catch(Exception e) {
            System.out.print(e);
        }
    }
    
    public void Run() throws IOException
    {
        String lineInFile = "";
        int currentJob = 0;
        int index = 0;
        String[] discard = null;
        
        while(!endOfFile)
        {
            lineInFile = br.readLine(); // Reads file
            
            if(lineInFile != null)
            {
                // JOB
                if(lineInFile.contains("JOB")) // Determines whether line contains JOB
                {
                    lineInFile.split("//+s"); // If line contains JOB, discards "//"
                    currentJob = Integer.parseInt(discard[JOB_ID_POS],16); // in Hex
                }
            }
            
            else 
            {
                Disk.write(index, lineInFile); // Writes to Disk
                index++; 
            }
            
        }
        
        
    }
}
