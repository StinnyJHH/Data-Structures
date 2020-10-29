package pqueue.priorityqueues; // ******* <---  DO NOT ERASE THIS LINE!!!! *******


/* *****************************************************************************************
 * THE FOLLOWING IMPORTS WILL BE NEEDED BY YOUR CODE, BECAUSE WE REQUIRE THAT YOU USE
 * ANY ONE OF YOUR EXISTING MINHEAP IMPLEMENTATIONS TO IMPLEMENT THIS CLASS. TO ACCESS
 * YOUR MINHEAP'S METHODS YOU NEED THEIR SIGNATURES, WHICH ARE DECLARED IN THE MINHEAP
 * INTERFACE. ALSO, SINCE THE PRIORITYQUEUE INTERFACE THAT YOU EXTEND IS ITERABLE, THE IMPORT OF ITERATOR
 * IS NEEDED IN ORDER TO MAKE YOUR CODE COMPILABLE. THE IMPLEMENTATIONS OF CHECKED EXCEPTIONS
 * ARE ALSO MADE VISIBLE BY VIRTUE OF THESE IMPORTS.
 ** ********************************************************************************* */

import pqueue.exceptions.*;
import pqueue.heaps.ArrayMinHeap;
import pqueue.heaps.EmptyHeapException;
import pqueue.heaps.MinHeap;

import java.util.Iterator;
/**
 * <p>{@link MinHeapPriorityQueue} is a {@link PriorityQueue} implemented using a {@link MinHeap}.</p>
 *
 * <p>You  <b>must</b> implement the methods of this class! To receive <b>any credit</b> for the unit tests
 * related to this class, your implementation <b>must</b> use <b>whichever</b> {@link MinHeap} implementation
 * among the two that you should have implemented you choose!</p>
 *
* @author  ---- Austin Han ----
 *
 * @param <T> The Type held by the container.
 *
 * @see LinearPriorityQueue
 * @see MinHeap
 * @see PriorityQueue
 */
public class MinHeapPriorityQueue<T> implements PriorityQueue<T>{

	/* ***********************************************************************************
	 * Write any private data elements or private methods for MinHeapPriorityQueue here...*
	 * ***********************************************************************************/

	private class Node implements Comparable<Node>{
		private int priority;
		private T element;
		@Override
		public int compareTo(MinHeapPriorityQueue<T>.Node o) {
			if(priority < o.priority)
				return -1;
			else if(priority > o.priority)
				return 1;
			else {
				return 0;
			}
		}
		
	}

	private ArrayMinHeap<Node> data;

	/* *********************************************************************************************************
	 * Implement the following public methods. You should erase the throwings of UnimplementedMethodExceptions.*
	 ***********************************************************************************************************/
		/**
	 * Simple default constructor.
	 */
	public MinHeapPriorityQueue(){
		data = new ArrayMinHeap<Node>();
	}

	@Override
	public void enqueue(T element, int priority) throws InvalidPriorityException {	// DO *NOT* ERASE THE "THROWS" DECLARATION!
		if(priority < 1)
			throw new InvalidPriorityException("bad priority number");
		Node temp = new Node();
		temp.priority = priority;
		temp.element = element;
		data.insert(temp);
	}

	@Override
	public T dequeue() throws EmptyPriorityQueueException {		// DO *NOT* ERASE THE "THROWS" DECLARATION!
		try {
			return data.deleteMin().element;
		} catch (EmptyHeapException e) {
			throw new EmptyPriorityQueueException(null);
		}
	}

	@Override
	public T getFirst() throws EmptyPriorityQueueException {	// DO *NOT* ERASE THE "THROWS" DECLARATION!
		try {
			return data.getMin().element;
		} catch(EmptyHeapException e) {
			throw new EmptyPriorityQueueException(null);
		}
	}

	@Override
	public int size() {
		return data.size();
	}

	@Override
	public boolean isEmpty() {
		return data.isEmpty();
	}


	@Override
	public Iterator<T> iterator() {
		throw new UnimplementedMethodException();
	}

}
