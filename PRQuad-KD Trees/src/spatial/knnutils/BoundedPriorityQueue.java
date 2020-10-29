package spatial.knnutils;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

import spatial.exceptions.UnimplementedMethodException;


/**
 * <p>{@link BoundedPriorityQueue} is a priority queue whose number of elements
 * is bounded. Insertions are such that if the queue's provided capacity is surpassed,
 * its length is not expanded, but rather the maximum priority element is ejected
 * (which could be the element just attempted to be enqueued).</p>
 *
 * <p><b>YOU ***** MUST ***** IMPLEMENT THIS CLASS!</b></p>
 *
 * @author  <a href = "https://github.com/jasonfillipou/">Jason Filippou</a>
 *
 * @see PriorityQueue
 * @see PriorityQueueNode
 */
public class BoundedPriorityQueue<T> implements PriorityQueue<T>{

	/* *********************************************************************** */
	/* *************  PLACE YOUR PRIVATE FIELDS AND METHODS HERE: ************ */
	/* *********************************************************************** */

	private T[] pq;
	private int size;
	private int count;
	private Node<T> root;
	private int sync;
	
	private static class Node<T>{
		T data;
		double priority;
		Node<T> next;
	}

	/* *********************************************************************** */
	/* ***************  IMPLEMENT THE FOLLOWING PUBLIC METHODS:  ************ */
	/* *********************************************************************** */

	/**
	 * Constructor that specifies the size of our queue.
	 * @param size The static size of the {@link BoundedPriorityQueue}. Has to be a positive integer.
	 * @throws IllegalArgumentException if size is not a strictly positive integer.
	 */
	public BoundedPriorityQueue(int size) throws IllegalArgumentException{
		if(size < 1) {
			throw new IllegalArgumentException();
		}else {
			this.size = size;
			count = 0;
			root = null;
			sync = 0;
		}
	}

	/**
	 * <p>Enqueueing elements for BoundedPriorityQueues works a little bit differently from general case
	 * PriorityQueues. If the queue is not at capacity, the element is inserted at its
	 * appropriate location in the sequence. On the other hand, if the object is at capacity, the element is
	 * inserted in its appropriate spot in the sequence (if such a spot exists, based on its priority) and
	 * the maximum priority element is ejected from the structure.</p>
	 * 
	 * @param element The element to insert in the queue.
	 * @param priority The priority of the element to insert in the queue.
	 */
	@Override
	public void enqueue(T element, double priority) {
		Node<T> temp = new Node<T>();
		temp.data = element;
		temp.priority = priority;
		if(isEmpty()) {
			root = new Node<T>();
			root.data = element;
			root.priority = priority;
			count++;
		}else {
			Node<T> curr = root, prev = null;
			while(curr != null && curr.priority <= priority) {
				prev = curr;
				curr = curr.next;
			}
			if(prev == null) {
				Node<T> newRoot = root;
				root = temp;
				temp.next = newRoot;
			}else {
				prev.next = temp;
				temp.next = curr;
			}
			count++;
			if(count > size) {
				curr = root;
				prev = null;
				while(curr.next != null) {
					prev = curr;
					curr = curr.next;
				}
				prev.next = null;
				count--;
			}
		}
		sync++;
	}

	@Override
	public T dequeue() {
		if(isEmpty()) {
			return null;
		}
		Node<T> temp = root;
		root = root.next;
		count--;
		sync++;
		return temp.data;
	}

	@Override
	public T first() {
		if(isEmpty()) {
			return null;
		}else {
			return root.data;
		}
	}
	
	/**
	 * Returns the last element in the queue. Useful for cases where we want to 
	 * compare the priorities of a given quantity with the maximum priority of 
	 * our stored quantities. In a minheap-based implementation of any {@link PriorityQueue},
	 * this operation would scan O(n) nodes and O(nlogn) links. In an array-based implementation,
	 * it takes constant time.
	 * @return The maximum priority element in our queue, or null if the queue is empty.
	 */
	public T last() {
		if(isEmpty()) {
			return null;
		}else {
			Node<T> curr = root;
			while(curr.next != null) {
				curr = curr.next;
			}
			return curr.data;
		}
	}

	/**
	 * Inspects whether a given element is in the queue. O(N) complexity.
	 * @param element The element to search for.
	 * @return {@code true} iff {@code element} is in {@code this}, {@code false} otherwise.
	 */
	public boolean contains(T element){
		if(isEmpty()) {
			return false;
		}else {
			Node<T> curr = root;
			while(curr != null) {
				if(curr.data.equals(element)) {
					return true;
				}
				curr = curr.next;
			}
			return false;
		}
	}

	@Override
	public int size() {
		return count;
	}

	@Override
	public boolean isEmpty() {
		if(root == null) {
			return true;
		}else {
			return false;
		}
	}

	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			int index = 0;
			Node<T> curr = root;
			int concur = sync;
			
			@Override
			public boolean hasNext() {
				if(concur == sync) {
					return (index < size);
				}else {
					throw new ConcurrentModificationException();
				}
			}
			@Override
			public T next() {
				
				if(concur != sync) {
					throw new ConcurrentModificationException();
				}
				T element = null;
				
				if(hasNext()) {
					element = curr.data;
					curr = curr.next;
				}
				index++;
				return element;
			}
		
		};
	}
}
