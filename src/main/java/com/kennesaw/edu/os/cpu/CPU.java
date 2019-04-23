// Current Package
package com.kennesaw.edu.os.cpu;
import com.kennesaw.edu.os.memory.Memory;
// Imports (Packages)
import com.kennesaw.edu.os.memory.PCB;
import com.kennesaw.edu.os.util.Converter;
 
// CPU Class
public class CPU implements Runnable {
    // Global Variables
   public int reg1, reg2, sReg1, sReg2, dReg, bReg; // Registers
   public int addr; // Current address
   public int numberOfCPUs; // Acquired from main module
   public int jc, pc; // Job Counter & Program Counter
   public int cpuID;
   public int jobID;
   public int processID;
   public Memory ram;
   public String cache[]; // Acquired from Memory Module
   public double cacheUsed; // Value used for metrics (percentage)
   public Register currentRegisters; // called from helper class 'Register'
   public Register accumlator; // Called from helper class 'Register'
   public PCB currentPCB; // Called from pcb class to get information on process
   public String inputBuffer, outputBuffer, tempBuffer; // Buffers
   public int numIOOperations, numOfJobs; // Metrics
   public CPUStatus statusOfCPU; // Used for completion of processes
    
    // Status of current CPU
   public enum CPUStatus
   {
      RUNNING, WAITING, ERROR;
   }
    
    // Default Constructor
    // NEEDS to call cpu.setPCB after object creation (Must have PCB)
   public CPU(int cpuID) {
        // Set all default CPU Operations
      this.currentPCB = null; // Set to null
      this.pc = -1; // initial values
      this.jc = -1; // initial values
      this.cpuID = cpuID;
      this.jobID = 0;
      this.cacheUsed = 0.0; // Initialize cache to 0.0
      this.statusOfCPU = CPUStatus.WAITING; // Initial status to CPU object
   }
    
    // ------------- Main CPU Functions ----------------------
 
   public String fetch(int pc) {
        // Fill cache with instructions
      fillCache();
        // Get current instruction from cache using program counter
      String instruct = cache[pc];
        // Assuming CPU has access to correct PCB Object
      processID = currentPCB.getProcessID();
      cacheUsed = cacheUsed(cache);
      return instruct;
   }
    
    // Called to decode fetch()
   public int decode(String instruct) {
        // Local Variables - Used to extract directions for given instructions
      String binInstr = Converter.hexToBinary(instruct.substring(2));
      String tmpInstr = binInstr; // Used as a placeholder
      int instType = Integer.parseInt(tmpInstr.substring(0,2));
      int opCode = Converter.binaryStringToInteger(tmpInstr.substring(2,8));
        
      switch(instType) {
        // Arithmetic or logical operation
         case 00:
            sReg1 = Converter.binaryStringToInteger(tmpInstr.substring(8,12));
            sReg2 = Converter.binaryStringToInteger(tmpInstr.substring(12,16));
            dReg = Converter.binaryStringToInteger(tmpInstr.substring(16,20));
            break;
        // Conditional Jump
         case 01:
            bReg = Converter.binaryStringToInteger(tmpInstr.substring(8,12));
            dReg = Converter.binaryStringToInteger(tmpInstr.substring(12,16));
            addr = Converter.binaryStringToInteger(tmpInstr.substring(16));
            break;
        // Unconditional Jump
         case 10:
            addr = Converter.binaryStringToInteger(tmpInstr.substring(8));
            break;
        // IO Operation
         case 11:
            reg1 = Converter.binaryStringToInteger(tmpInstr.substring(8,12));
            reg2 = Converter.binaryStringToInteger(tmpInstr.substring(12,16));
            addr = Converter.binaryStringToInteger(tmpInstr.substring(16));
            numIOOperations++; // Increment total I/O Operations
            break;
        // No valid instruction type given or never assigned
         default:
            this.statusOfCPU = CPUStatus.ERROR; // Error instType
            System.out.println("EXCEPTION: Invalid Instruction Type");
      }
        // Returns opcode to use for execution
      return opCode;
   }
    
    // Called by run() to execute given opCode
   public void execute(int opCode) {
        
      switch (opCode) {
        // OpCode {00} - Read Content of I/P buffer into a accumlator
         case 0:
            System.out.println("Executing Read OpCode");
            accumlator.readBuffer(inputBuffer, addr);
         // OpCode {01} - Writes the content of accumulator into O/P buffer
         case 1:
            System.out.println("**Executing Write OpCode");
            outputBuffer = accumlator.writeBuffer(outputBuffer, addr);
         // OpCode {02} - Stores content of a reg. into an address
         case 2:
            addr = currentRegisters.getReg(dReg);
            // Memory.writeMemory(addr, currentRegisters.getReg((int)addr)); // Right context of memory. Write to memory or disk
            // Write to memory? (Phase 2)
            break;
        // OpCode {03} - Loads the content of an address into a reg
         case 3:
            currentRegisters.setReg(currentRegisters.getReg((int)addr), dReg);
            // Memory.writeMemory(addr, currentRegisters.getReg((int)addr)); // Right context of memory. Write to memory or disk
            // Write to memory? Phase(2)
            break;
        // OpCode {04} - Transfers the content of one register into another
         case 4:
            System.out.println("Executing Move OpCode");
            currentRegisters.setReg(currentRegisters.getReg(reg1), reg2);
            break;
        // OpCode {05} - Adds content of two S-regs into D-reg
         case 5:
            System.out.println("Executing ADD OpCode");
            currentRegisters.setReg(currentRegisters.getReg(sReg1) + currentRegisters.getReg(sReg2), dReg); // Add values of sreg1 + sreg2 -> dReg
            break;
        // OpCode {06} - Subtracts content of two S-regs into D-reg
         case 6:
            System.out.println("Executing SUB OpCode");
            currentRegisters.setReg(currentRegisters.getReg(sReg1) - currentRegisters.getReg(sReg2), dReg); // Sub values of sreg1 - sreg2 -> dReg
            break;
        // OpCode {07} - Multiplies content of two S-regs into D-reg
         case 7:
            System.out.println("Executing MUL OpCode");
            currentRegisters.setReg(currentRegisters.getReg(sReg1) * currentRegisters.getReg(sReg2), dReg);
            break;
        // OpCode {08} - Divides content of two S-regs into D-regs
         case 8:
            System.out.println("Executing DIV OpCode");
            // If second sreg(divisor) is equal to 0, output error string
            if (currentRegisters.getReg(sReg2) == 0) {
               System.out.println("Error division by 0");
            } else {
               currentRegisters.setReg(currentRegisters.getReg(sReg1) / currentRegisters.getReg(sReg2), dReg);
            }
            break;
        // OpCode {09} - Logical AND of two S-regs into D-reg
         case 9:
            System.out.println("Executing AND OpCode");
            currentRegisters.setReg(currentRegisters.getReg(reg1) & currentRegisters.getReg(reg2), dReg);
            break;
        // OpCode {0A} - Logical OR of two S-regs into D-reg
         case 10:
            System.out.println("Executing OR OpCode");
            currentRegisters.setReg(currentRegisters.getReg(reg1) | currentRegisters.getReg(reg2), dReg);
            break;
        // OpCode {0B} - Transfers address/data direction into a regisetr
         case 11:
            System.out.println("Executing MOVI OpCode");
            currentRegisters.setReg((int)addr, dReg); // Cast string addr as int and move to dReg
            break;
        // OpCode {0C} - Adds a data value direction to the content of a register
         case 12:
            System.out.println("Executing ADDI OpCode");
            currentRegisters.setReg((int)addr + currentRegisters.getReg(dReg), dReg); // Cast string addr as int and add with dReg, replacing dReg with value
            break;
        // OpCode {0D} - Multiplies a data value directly with the content of a register
         case 13:
            System.out.println("Execuing MULI OpCode");
            currentRegisters.setReg((int)addr * currentRegisters.getReg(dReg), dReg);
            break;
        // OpCode {0E} - Divides a data directly to the content of a register
         case 14:
            System.out.println("Execuing DIVI OpCode");
            if (currentRegisters.getReg(dReg) == 0) {
               System.out.println("Error division by 0");
            } else {
               currentRegisters.setReg((int)addr / currentRegisters.getReg(dReg), dReg);
            }
            break;
        // OpCode {0F} - Loads a data/address directly to the content of a register
         case 15:
            System.out.println("Executing LDI OpCode");
            currentRegisters.setReg((int)addr, dReg); // Cast value of addr(string) to int and replace dReg
            break;
        // OpCode {10} - Sets the D-reg to 1 if first S-reg is less than bReg; 0 otherwise
         case 16:
            if (currentRegisters.getReg(sReg1) < currentRegisters.getReg(bReg))
               currentRegisters.setReg(1, dReg);
            else {
               currentRegisters.setReg(0, dReg);
            }
            break;
        // OpCode {11} - Sets the D-reg to 1 if first S-reg is less than a data; 0 otherwise
         case 17:
            System.out.println("Executing SLTI OpCode");
            if (currentRegisters.getReg(sReg1) < (int)addr)
               currentRegisters.setReg(1, dReg);
            else
               currentRegisters.setReg(0, dReg);
            break;
        // OpCode {12} - Logical End of program
         case 18:
            System.out.println("Executing HLT OpCode");
            pc = 0; // Logical End to Program
            // METRICS CLASS - OUTPUT LOGS of runtime, etc.
            break;
        // OpCode {13} - Does nothing and moves to next instruction
         case 19:
            System.out.println("Executing NOP OpCode");
            // Maybe move to next
            break;
        // OpCode {14} - Jumps to a specified location
         case 20:
            System.out.println("Executing JMP OpCode");
            pc = (int)addr / 4; // Jump to another location by alternating value of the pc
            break;
        // OpCode {15} - Branches to an address when content of B-reg = D-reg
         case 21:
            System.out.println("Executing BEQ OpCode");
            if (currentRegisters.getReg(dReg) == currentRegisters.getReg(bReg))
               pc = (int)addr / 4; // Jump
            break;
        // OpCode {16} - Branches to an address when content of B-reg <> D-reg
         case 22:
            System.out.println("Executing BNE OpCode");
            if (currentRegisters.getReg(dReg) != currentRegisters.getReg(bReg));
            pc = (int)addr / 4; // Jump
            break;
        // OpCode {17} - Branches to an address when content of B-reg = 0
         case 23:
            System.out.println("Executing BEZ OpCode");
            if (currentRegisters.getReg(bReg) == 0)
               pc = (int)addr / 4; // Jump
            break;
        // OpCode {18} - Branches to an address when content of B-reg <> 0
         case 24:
            System.out.println("Executing BNZ OpCode");
            if (currentRegisters.getReg(bReg) != 0)
               pc = (int)addr / 4; // Jump
            break;
        // OpCode {19} - Branches to an address when content of B-reg > 0
         case 25:
            System.out.println("Executing BGZ OpCode");
            if (currentRegisters.getReg(bReg) > 0) {
               pc = (int)addr / 4;
               break;
            }
         // OpCode {1A} - Branches to an address when content of B-reg < 0
         case 26:
            System.out.println("Executing BLZ OpCode");
            if (currentRegisters.getReg(bReg) < 0)
               pc = (int)addr / 4;
            break;
        // Default Case - (OpCode > 26 or invalid operation passed)
         default:
            System.out.println("Invalid Operation");
      }
   }
    
    // Runs all commands while pc < jc
    @Override
   	public void run() {
      // Sets current PCB
      if (this.currentPCB == null) 
      {
         System.out.println("Error: CPU passed null PCB object");
         this.statusOfCPU = CPUStatus.ERROR;
         return;
      }
      // Get the Job Count from the PCB
      jc = this.currentPCB.getInstructionLength();
      numOfJobs = jc; // For metrics
      
      // Loops until all jobs are finished
      while (jc > 0) { 
         try {
        	// Set pc value each iteration of while loop
            pc = this.currentPCB.getPC();
            // Executes while jobs are available
            execute(decode(fetch(pc)));
            // Once completed set to status and wait for another process
            this.statusOfCPU = CPUStatus.WAITING;
            jc--; // Decrement total jobs
         } catch (Exception e) {
            // There was an error with try/catch. Update status
            this.statusOfCPU = CPUStatus.ERROR;
            // Output Exception error
            System.out.println("Exception Error: " + e);
         }
      }
      // Once jobs are completed then set currentPCB to null
      this.currentPCB = null;
   }
    
    // Assigns value to cache given PC
   public void fillCache() {
	  // Read value of memory at the given address
      cache[pc] = Memory.readMemory(addr);
   }
    
    // ------------- Getters & Setters ------------------------
    
    // Returns current cache array
   public String[] getCache() {
      return cache;
   }
    
    // Assigns the parameter cache to CPUs cache
   public void setCache(String [] cache_) {
      cache = cache_;
   }
 
   // Get instance of RAM
   public void setRAM(Memory ram) {
      this.ram = ram;
   }
    
    // Returns the ID of the current job
   public int getJobID() {
      return jobID;
   }
    
    // Sets the job id to CPU
   public void setJobID(int jobID) {
      this.jobID = jobID;
   }
    
    // Returns the ID of the current CPU(Multi-CPUs)
   public int getProcessID() {
      return processID;
   }
    
    // Sets a CPU ID to CPU
   public void setProcessID(int processID) {
      this.processID = processID;
   }
    
    // Returns current PCB
   public PCB getPCB() {
      return this.currentPCB;
   }
    
    // Sets a new PCB
   public void setPCB(PCB pcb) {
      this.currentPCB = pcb;
   }
    
    // Returns current cpu status
   public CPUStatus getCPUStatus() {
      return this.statusOfCPU;
   }
    
    // --------------- Metrics --------------------------------
    
    // Returns number of jobs/tasks
   public int numOfJobs() {
      return numOfJobs;
   }
    
    // Returns total number of I/O Operations
   public int numberOfIOOperations() {
      return numIOOperations;
   }
    
    // Returns percentage of cache used
   public double cacheUsed(String [] cache) {
      double percentOfCache = 0.0; // Initialize
      percentOfCache = cache.length / 1024; // Divide by initial value
      return percentOfCache * 100; // Return percentage
   }
    
    // Format and Print Metrics
   public void printMetrics() {
      System.out.println("--------------------------\n");
      System.out.println("JobID: " + jobID);
      System.out.println("ProcessID: " + processID);
      System.out.println("Cache Used: " + cacheUsed + "%");
      System.out.println("Number of I/O Operations: " + numIOOperations);
      System.out.println("Number of total jobs: " + numOfJobs);
      System.out.println("--------------------------\n");
   }
}
