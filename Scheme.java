/** -----------------------------------------------------------------------
	Scheme.java

	@author William Clift
			Operating Systems
			Ursinus College
			Project 2 - Scheduling Schemes
			14 April 2020

			The purpose of the assignment is to simulate different CPU Scheduling
			Algorithms.

	Compile and Run Instructions:
			
		Compile:	javac Scheme.java PCB.java CircularLL.java FCFS.java RR.java SJF.java
		
		Run:		java Scheme [input_file] [FCFS|RR|SJF] [time_quantum]

    ------------------------------------------------------------------- **/
import java.util.*;
import java.io.*;


public class Scheme{

	protected String scheme;
	public int cpuTime = 0;
	public int idle = 0;
	public CircularLL incoming;
	public CircularLL toSchedule;
	public CircularLL processed;

	/**
	 * Constructor for the Scheme Super Class to all of 
	 * the scheduling algorithms
	 */
	public Scheme(CircularLL incoming, String scheme){
		this.incoming = incoming;
		this.scheme = scheme;
	}

	public static void main(String[] args) throws FileNotFoundException{
		String fileName = args[0];
		String scheme = args[1];
		if(scheme.equals("RR") && args.length < 3){
			System.out.println("Round Robin requires a Time Quantum input.");
		}else{

			CircularLL incoming = new CircularLL();

			try{
			Scanner f = new Scanner(new File(fileName));
			int i =0;

			while(f.hasNextLine()){
				String line = f.nextLine();
				Scanner l = new Scanner(line);
				PCB process = new PCB();
				if(l.hasNextInt()){
					process.pid = l.nextInt();
				}
				if(l.hasNextInt()){
					process.arrival_time = l.nextInt();
				}
				if(l.hasNextInt()){
					process.burst_time = l.nextInt();
					process.time_remaining = process.burst_time;
				}
				incoming.push(process);
				i++;
			}
			f.close();

			// Creates a new object of the scheme type
			if(scheme.compareTo("FCFS") == 0){
				Scheme s = new FCFS(incoming, scheme);
				schemeHandle(i, s, fileName);
			}else if(scheme.compareTo("RR") == 0){
				if(args[2] != null){
					Scheme s = new RR(incoming, scheme, Integer.parseInt(args[2]));
					schemeHandle(i, s, fileName);
				}else{
					System.out.println("RR needs a time quantum input.");
				}
			}else if(scheme.compareTo("SJF") == 0){
				Scheme s = new SJF(incoming, scheme); 
				schemeHandle(i, s, fileName);
			}else{
				System.out.println("Invalid Scheme");
			}

			}catch(IOException e){
				System.out.println("File Not Found.");
			}
		}
	}

	/**
	 * 	Pass to this method to handle the scheme running, only if valid
	 * @param s the Scheme
	 * @param fileName the name of the input file
	 */
	public static void schemeHandle(int tasks, Scheme s, String fileName){
		startup(tasks, fileName, s.scheme);		// Prints out initial setup lines 
		Scanner read = new Scanner(System.in);
		String c = "c";
		if(read.hasNextLine()){
			c = read.nextLine();
		}
		if (c.equals("")){	// If the user presses 'enter'
   			s.run();		// Run the Scheduling Algorithm
		}
	}

	/**
	 * Prints the initial setup lines
	 *
	 */
	public static void startup(int tasks, String fileName, String scheme){
		System.out.println("Scheduling Algorithm: " + scheme);
		System.out.print("Total " + tasks + " tasks are read from \"" + fileName + "\". Press \'enter\' to start...");
	}

	/**
	 * Prints out the Metrics **At the End of Running **
	 */
	public void printEndMetrics(CircularLL processed){
		float[] metrics = getMetrics(processed);
		//Printouts
		System.out.println("============================================================");
		System.out.println("Average CPU Usage:		" + metrics[0] + "%");
		System.out.println("Average Wait Time:		" + metrics[1]);
		System.out.println("Average Response Time:		" + metrics[2]);
		System.out.println("Average Turnaround Time:	" + metrics[3]);
		System.out.println("============================================================");
	}

	/**
	 * 	CPU Usage, Avg Wait time, Avg Response Time, Avg Turnaround Time
	 *  @param processed the CircularLL of processes that were processed.
	 *	@return metrics array of the metrics
	 */
	protected float[] getMetrics(CircularLL processed){
		int wait_total = 0;
		int response_total = 0;
		int turnaround_total = 0;
		float avgWait = 0;
		float avgResp = 0;
		float avgTurn = 0;
		float cpuUsage = 0;

		if(processed.getSize()>0){
			PCB current = processed.head;

			for(int i = 0; i<processed.getSize(); i++){
				wait_total += current.getWaitTime();
				response_total += current.getResponseTime();
				turnaround_total += current.getTurnaroundTime();

				current = current.getNext();
			}

			avgWait = wait_total / processed.getSize();
			avgResp = response_total / processed.getSize();
			avgTurn = turnaround_total / processed.getSize();
			cpuUsage = 100 * (cpuTime - idle) / cpuTime; 
		}else{
			System.out.println("Not yet processed.");
		}

		float[] metrics = new float[4];
		metrics[0] = cpuUsage;
		metrics[1] = avgWait;
		metrics[2] = avgResp;
		metrics[3] = avgTurn;

		return metrics;
	}


	/**
	 * Runs the Algorithm
	 *
	 */
	public void run(){

	}

	/**
	 * Simulates a process in the CPU
	 * Prints out the necessary writeouts
	 * @param process
	 * @param quantum
	 * @param toSchedule
	 */
	protected PCB cpuProcess(PCB process, int quantum, CircularLL toSchedule, CircularLL incoming){
		while(process.time_remaining > 0 && quantum > 0){
			System.out.println("<System Time " + this.cpuTime + "> Process " + process.pid + " is Running.");
			process.time_remaining--;
			quantum--;
			cpuTick(process, toSchedule);
		}
		if(process.time_remaining < 1){
			System.out.println("<System Time " + this.cpuTime + "> Process " + process.pid + " has Finished.....");
		}
		if(toSchedule.getSize()==0){
			System.out.println("<System Time " + this.cpuTime + "> All Processes Finished.....");
		}
		return process;
	}

	/**
	 * Simulates a clock tick of the CPU
	 *
	 */
	protected void cpuTick(PCB process, CircularLL toSchedule){
		this.cpuTime++;
		PCB current = process;

		// Increment the Waittime on all other processes
		for(int i = 0; i < toSchedule.getSize(); i++){
			current = current.getNext();
			current.waitTick();
		}
		checkArrival(incoming, toSchedule);
	}

	/**
	 * Simulates a clock tick of the CPU when idle (No processes)
	 *
	 */
	protected void cpuTick(){
		this.cpuTime++;
		this.idle++;
		checkArrival(incoming, toSchedule);
	}

	/**
	 * Check if any processes have arrived.
	 *
	 */
	private void checkArrival(CircularLL incoming, CircularLL toSchedule){
		PCB current = incoming.head;
		for(int i = 0; i < incoming.getSize(); i++){
			current = incoming.head;
			if(current.arrival_time == cpuTime){
				PCB in = incoming.pop();
					
				toSchedule.push(in);

				System.out.println(in.pid);		//
			}else{
				incoming.work();
			}
		}
	}
	
}