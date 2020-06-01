/** -----------------------------------------------------------------------
	RR.java

	@author William Clift
			Operating Systems
			Ursinus College
			Project 2 - Scheduling Schemes
			14 April 2020

	Compile Instructions:
			
		Compile:	javac RR.java
		
    ------------------------------------------------------------------- **/
import java.util.*;

public class RR extends Scheme{

	public String scheme = "RR";
	public String fileName;
	public CircularLL toSchedule;
	public CircularLL incoming;
	public int time_quantum;
	public CircularLL processed;

	/**
	 * Round Robin Algorithm
	 * @param toSchedule the Circular Linked List of PCBs
	 * @param time_quantum the amount of time alloted per process.
	 */
	public RR(CircularLL incoming, String scheme, int time_quantum){
		super(incoming, scheme);
		this.toSchedule = new CircularLL();
		this.incoming = incoming;
		this.time_quantum = time_quantum;
		this.processed = new CircularLL();
	}

	/**
	 * Runs the Round-Robin Algorithm
	 *
	 */
	public void run(){
		System.out.println("============================================================");
		boolean done = false;								// Sentinal Value

		while(!done){
			if(incoming.getSize() > 0){
				checkArrival();
			}
			PCB current = toSchedule.head;
			if(toSchedule.getSize()>0){						// If there are processes left
				if(current.time_remaining > this.time_quantum){
					PCB next = toSchedule.work();
					cpuProcess(next, this.time_quantum, toSchedule, incoming);	// Run the next Process in line
				}else{
					PCB next = toSchedule.pop();
					PCB result = cpuProcess(next, next.time_remaining, toSchedule, incoming);
					processed.push(result);
				}
			} else{
				cpuTick();
			}

			if(incoming.getSize() < 1 && toSchedule.getSize() < 1){
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