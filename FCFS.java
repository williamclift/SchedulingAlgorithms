/** -----------------------------------------------------------------------
	FCFS.java

	@author William Clift
			Operating Systems
			Ursinus College
			Project 2 - Scheduling Schemes
			14 April 2020

	Compile Instructions:
			
		Compile:	javac FCFS.java
		
    ------------------------------------------------------------------- **/
import java.util.*;

public class FCFS extends Scheme{

	public String scheme = "FCFS";
	public String fileName;
	public CircularLL incoming;
	public CircularLL toSchedule;
	public CircularLL processed;

	/**
	 * First Come, First Served Algorithm
	 *
	 */
	public FCFS(CircularLL incoming, String scheme){
		super(incoming, scheme);
		this.incoming = incoming;
		this.toSchedule = new CircularLL();
		this.processed = new CircularLL();
		checkArrival();
	}

	/**
	 * Runs the Algorithm
	 *
	 */
	public void run(){
		System.out.println("============================================================");
		boolean done = false;						// Sentinal Value

		while(!done){
			if(incoming.getSize() > 0){
				checkArrival();
			}

			if(toSchedule.getSize()>0){				// If there are processes left
				PCB current = toSchedule.pop();
				PCB result = cpuProcess(current, current.burst_time, toSchedule, incoming);	// Run the next Process in line
				processed.push(result);
			}else{
				cpuTick();
			}

			if(incoming.getSize() < 1 && toSchedule.getSize()<1){
				done = true;
			}
		}
		printEndMetrics(processed);
	}

	/**
	 * Check if any processes have arrived.
	 *
	 */
	private void checkArrival(){
		PCB current = incoming.head;
		for(int i = 0; i < incoming.getSize(); i++){
			current = incoming.head;
			if(current.arrival_time == cpuTime){
				PCB in = incoming.pop();
				toSchedule.push(in);
			}else{
				incoming.work();
			}
		}
	}
}