/** -----------------------------------------------------------------------
	SJF.java

	@author William Clift
			Operating Systems
			Ursinus College
			Project 2 - Scheduling Schemes
			14 April 2020

	Compile Instructions:
			
		Compile:	javac SJF.java
		
    ------------------------------------------------------------------- **/
import java.util.*;

public class SJF extends Scheme{

	public String scheme = "SJF";
	public String fileName;
	public CircularLL toSchedule;
	public CircularLL processed;
	public CircularLL incoming;

	/**
	 * Shortest Job First Algorithm
	 *
	 */		
	public SJF(CircularLL incoming, String scheme){
		super(incoming, scheme);
		this.toSchedule = new CircularLL();
		this.processed = new CircularLL();
		this.incoming = incoming;
		checkArrival();
	}

	/**
	 * Runs the Algorithm
	 *
	 */
	public void run(){
		System.out.println("============================================================");

		while(incoming.getSize()>0 && toSchedule.getSize()>0){
			if(incoming.getSize() > 0){
				checkArrival();
			}
			if(toSchedule.getSize()>0){						// If there are processes left
				PCB current = getSmallest();
				PCB result = cpuProcess(current, current.burst_time, toSchedule, incoming);	// Run the next Process in line
				processed.push(result);
			}else{
				cpuTick();
			}
		}
		printEndMetrics(processed);
	}

	/**
	 * @return smallest - the smallest burst time in the list
	 */
	private PCB getSmallest(){
		PCB smallest = toSchedule.work();
		PCB current;
		for(int i = 0; i< toSchedule.getSize(); i++){
			current = toSchedule.work();
			if(smallest.burst_time > current.burst_time){
				smallest = current;
			}
		}
		toSchedule.complete(smallest.pid);

		return smallest;
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