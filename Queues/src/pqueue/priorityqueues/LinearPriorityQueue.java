package pqueue.priorityqueues; // ******* <---  DO NOT ERASE THIS LINE!!!! *******

/* *****************************************************************************************
 * THE FOLLOWING IMPORTS ARE HERE ONLY TO MAKE THE JAVADOC AND iterator() METHOD SIGNATURE
 * "SEE" THE RELEVANT CLASSES. SOME OF THOSE IMPORTS MIGHT *NOT* BE NEEDED BY YOUR OWN
 * IMPLEMENTATION, AND IT IS COMPLETELY FINE TO ERASE THEM. THE CHOICE IS YOURS.
 * ********************************************************************************** */

import demos.GenericArrays;
import pqueue.exceptions.*;
import pqueue.fifoqueues.FIFOQueue;
import pqueue.heaps.ArrayMinHeap;

import java.util.*;
/**
 * <p>{@link LinearPriorityQueue} is a {@link PriorityQueue} implemented as a linear {@link java.util.Collection}
 * of common {@link FIFOQueue}s, where the {@link FIFOQueue}s themselves hold objects
 * with the same priority (in the order they were inserted).</p>
 *
 * <p>You  <b>must</b> implement the methods in this file! To receive <b>any credit</b> for the unit tests related to
 * this class, your implementation <b>must</b>  use <b>whichever</b> linear {@link Collection} you want (e.g
 * {@link ArrayList}, {@link LinkedList}, {@link java.util.Queue}), or even the various {@link List} and {@link FIFOQueue}
 * implementations that we provide for you. You can also use <b>raw</b> arrays, but take a look at {@link GenericArrays}
 * if you intend to do so. Note that, unlike {@link ArrayMinHeap}, we do not insist that you use a contiguous storage
 * {@link Collection}, but any one available (including {@link LinkedList}) </p>
 *
 * @param <T> The type held by the container.
 *
 * @author  ---- AUSTIN HAN ----
 *
 * @see MinHeapPriorityQueue
 * @see PriorityQueue
 * @see GenericArrays
 */
public class LinearPriorityQueue<T> implements PriorityQueue<T> {

	/* ***********************************************************************************
	 * Write any private data elements or private methods for LinearPriorityQueue here...*
	 * ***********************************************************************************/
	private static class Node<T>{
		private T element;
		private int priority;
	}

	private ArrayList<Node<T>> data;
	private final int size = 50;


	/* *********************************************************************************************************
	 * Implement the following public methods. You should erase the throwings of UnimplementedMethodExceptions.*
	 ***********************************************************************************************************/

	/**
	 * Default constructor initializes the element structure with
	 * a default capacity. This default capacity will be the default capacity of the
	 * underlying element structure that you will choose to use to implement this class.
	 */
	public LinearPriorityQueue(){
		data = new ArrayList<Node <T>>(size);
	}

	/**
	 * Non-default constructor initializes the element structure with
	 * the provided capacity. This provided capacity will need to be passed to the default capacity
	 * of the underlying element structure that you will choose to use to implement this class.
	 * @see #LinearPriorityQueue()
	 * @param capacity The initial capacity to endow your inner implementation with.
	 * @throws InvalidCapacityException if the capacity provided is less than 1.
	 */
	public LinearPriorityQueue(int capacity) throws InvalidCapacityException{	// DO *NOT* ERASE THE "THROWS" DECLARATION!
		if(capacity < 1)
			throw new InvalidCapacityException("bad capacity argument in constructor");
		data = new ArrayList<Node<T>>(capacity);
	}

	@Override
	public void enqueue(T element, int priority) throws InvalidPriorityException{	// DO *NOT* ERASE THE "THROWS" DECLARATION!
		if(priority < 1)
			throw new InvalidPriorityException("bad priority number");
		Node<T> node = new Node<T>();
		node.element = element;
		node.priority = priority;
		if (data.isEmpty()){
			data.add(node);
		}else {
			int x = 0;
			while (x < data.size() && data.get(x).priority <= priority) {
				x++;
			}
			if(x == size) {
				data.add(node);
			}else 
				data.add(x, node);
		}
	}

	@Override
	public T dequeue() throws EmptyPriorityQueueException { 	// DO *NOT* ERASE THE "THROWS" DECLARATION!
		if(data.isEmpty())
			throw new EmptyPriorityQueueException(null);
		Node<T> temp = data.remove(0);
		return temp.element;
	}

	@Override
	public T getFirst() throws EmptyPriorityQueueException {	// DO *NOT* ERASE THE "THROWS" DECLARATION!
		if(data.isEmpty())
			throw new EmptyPriorityQueueException(null);
		else
			return data.get(0).element;
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
		return new Iterator<T>() {
			int x = 0;
			@Override
			public boolean hasNext() {
				return data.get(x+1) != null;
			}
			@Override
			public T next() {
				if(hasNext()) {
					T element = data.get(x).element;
					x++;
					return element;
				}
				return null;
			}
		
		};
	}

}