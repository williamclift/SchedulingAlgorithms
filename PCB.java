/** -----------------------------------------------------------------------
	PCB.java

	@author William Clift
			Operating Systems
			Ursinus College
			Project 2 - Scheduling Schemes
			14 April 2020

	Compile Instructions:
			
		Compile:	javac PCB.java
		
    ------------------------------------------------------------------- **/

public class PCB{

	public int pid, arrival_time, burst_time, time_remaining;
	private PCB next;
	private int wait_time;
	private int response_time;
	private int turnaround_time;

	/**
     * Process Control Block Constructor
     * No parameterrs
     */
	public PCB(){

	}

	/**
     * Process Control Block Constructor with parameters
     * @param pid
     * @param arrival_time
     * @param burst_time
     */
	public PCB(int pid, int arrival_time, int burst_time){
		this.pid = pid;
		this.arrival_time = arrival_time;
		this.burst_time = burst_time;
		this.time_remaining = burst_time;
		this.next = null;
		this.wait_time = 0;
	}

	/**
	 * @return the PCB it points to
	 */
	public PCB getNext(){
		return this.next;
	}

	/**
	 * @return the pid
	 */
	public int getPid(){
		return this.pid;
	}

		/**
	 * Set the next Node of the Circular Linked List
	 * @param n
	 */
	public void setNext(PCB n){
		this.next = n;
	}

	/**
	 *	Adds to the wait time for a process
	 */
	public void waitTick(){
		this.wait_time++;
		if(time_remaining == burst_time){
			this.response_time++;
		}
	}

	/**
	 *	@return this.wait_time
	 */
	public int getWaitTime(){
		return this.wait_time;
	}

	/**
	 *	@return this.response_time
	 */
	public int getResponseTime(){
		return this.response_time;
	}

	/**
	 *	@param cpuTime
	 */
	public void setResponseTime(int cpuTime){
		this.response_time = cpuTime - this.arrival_time;
	}

	/**
	 *	@return this.turnaround_time
	 */
	public int getTurnaroundTime(){
		return this.wait_time + this.burst_time;
	}

	/**
	 *	@param cpuTime
	 */
	public void setTurnaroundTime(int cpuTime){
		this.turnaround_time = cpuTime - arrival_time;
	}

}