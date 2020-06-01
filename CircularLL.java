/** -----------------------------------------------------------------------
	CircularLL.java

	@author William Clift
			Operating Systems
			Ursinus College
			Project 2 - Scheduling Schemes
			14 April 2020

	Compile Instructions:
			
		Compile:	javac CircularLL.java
		
    ------------------------------------------------------------------- **/
/**
*	A linked list datastructure of PCB's
*/
public class CircularLL{
	public PCB head;
	public PCB tail;
	private int size = 0;

	/**
	 * Circular Linked List Implementation Constructor
	 *	
	 */
	public CircularLL(){
		this.head = null;
		this.tail = null;
	}

	/**
	 * Adds to the tail
	 * @param n
	 */
	public void push(PCB n){
		if(size == 0){
			setHead(n);
			setTail(n);
		}else{
			tail.setNext(n);
			setTail(n);
		}
		this.size++;
	}

	/**
	 * Removes the head and returns it
	 * @return pop
	 */
	public PCB pop(){
		PCB pop = head;
		tail.setNext(head.getNext());
		setHead(tail.getNext());
		size--;
		return pop;
	}

	/**
	 * Returns the head, moves it to the tail, and shifts up
	 * @return work
	 */
	public PCB work(){
		PCB work = head;
		setHead(head.getNext());
		setTail(tail.getNext());
		return work;
	}


//May not need this method
	/**
	 * Removes a PCB with a particular PID
	 * @param pid - the PID number
	 */
	public PCB complete(int pid){
		boolean found = false;
		PCB previous = tail;
		PCB current = head;

		int i = getSize();
		while(!found){
			if(current.pid == pid){
				if(current.pid == head.pid){
					head = head.getNext();
				}
				if(current.pid == tail.pid){
					tail = previous;
				}
				previous.setNext(current.getNext());
				found = true;
				size--;
			}else{
				current = current.getNext();
				previous = previous.getNext();
			}
			i--;
			if(i<0){
				System.out.println("Process not found.");
				current = null;
				found = true;
			}
		}
		return current;
	}

	/**
	 * Returns the pid from the node at a certain index
	 *	@param index
	 *  @return n.pid
	 */
	public int getPID(int index){
        if(index>size){
            return -1;
        }
        PCB n = head;
        while(index-1!=0){		// When it gets to the index, stop
            n=n.getNext();
            index--;
        }
        return n.getPid();			// Return data from that index
    }

    /**
	 * Returns the size of the circular linked list
	 * @return size
	 */
	public int getSize(){
		return this.size;
	}


	/**
	 * Set the Head of the Circular Linked List
	 * @param n
	 */
	public void setHead(PCB n){
		this.head = n;
	}

	/**
	 * Set the Tail and set tail.next to the head
	 *	@param n 
	 */
	public void setTail(PCB n){
		this.tail = n;
		n.setNext(head);
	}

	/**
	 * Converts the circular to a String
	 */
	public String toString(){
		String whole = "";
		PCB process = head;
		for(int i = 0; i < this.size; i++){
			whole += process.burst_time + "\n";
			process = process.getNext();
		}
		return whole;
	}
}