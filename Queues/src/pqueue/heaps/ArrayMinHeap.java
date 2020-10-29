package pqueue.heaps; // ******* <---  DO NOT ERASE THIS LINE!!!! *******

/* *****************************************************************************************
 * THE FOLLOWING IMPORT IS NECESSARY FOR THE ITERATOR() METHOD'S SIGNATURE. FOR THIS
 * REASON, YOU SHOULD NOT ERASE IT! YOUR CODE WILL BE UNCOMPILABLE IF YOU DO!
 * ********************************************************************************** */

import pqueue.exceptions.UnimplementedMethodException;

import java.util.Iterator;


/**
 * <p>{@link ArrayMinHeap} is a {@link MinHeap} implemented using an internal array. Since heaps are <b>complete</b>
 * binary trees, using contiguous storage to store them is an excellent idea, since with such storage we avoid
 * wasting bytes per {@code null} pointer in a linked implementation.</p>
 *
 * <p>You <b>must</b> edit this class! To receive <b>any</b> credit for the unit tests related to this class,
 * your implementation <b>must</b> be a <b>contiguous storage</b> implementation based on a linear {@link java.util.Collection}
 * like an {@link java.util.ArrayList} or a {@link java.util.Vector} (but *not* a {@link java.util.LinkedList} because it's *not*
 * contiguous storage!). or a raw Java array. We provide an array for you to start with, but if you prefer, you can switch it to a
 * {@link java.util.Collection} as mentioned above. </p>
 *
* @author -- Austin Han ---
 *
 * @see MinHeap
 * @see LinkedMinHeap
 * @see demos.GenericArrays
 */

public class ArrayMinHeap<T extends Comparable<T>> implements MinHeap<T> {

	/* *****************************************************************************************************************
	 * This array will store your data. You may replace it with a linear Collection if you wish, but
	 * consult this class' 	 * JavaDocs before you do so. We allow you this option because if you aren't
	 * careful, you can end up having ClassCastExceptions thrown at you if you work with a raw array of Objects.
	 * See, the type T that this class contains needs to be Comparable with other types T, but Objects are at the top
	 * of the class hierarchy; they can't be Comparable, Iterable, Clonable, Serializable, etc. See GenericArrays.java
	 * under the package demos* for more information.
	 * *****************************************************************************************************************/
	private Object[] data;

	/* *********************************************************************************** *
	 * Write any further private data elements or private methods for LinkedMinHeap here...*
	 * *************************************************************************************/
	private int size = 50, count = 0;


	/* *********************************************************************************************************
	 * Implement the following public methods. You should erase the throwings of UnimplementedMethodExceptions.*
	 ***********************************************************************************************************/

	/**
	 * Default constructor initializes the data structure with some default
	 * capacity you can choose.
	 */
	public ArrayMinHeap(){
		data = new Object[size];
	}

	/**
	 *  Second, non-default constructor which provides the element with which to initialize the heap's root.
	 *  @param rootElement the element to create the root with.
	 */
	public ArrayMinHeap(T rootElement){
		data[0] = rootElement;
		count++;
	}

	/**
	 * Copy constructor initializes {@code this} as a carbon copy of the {@link MinHeap} parameter.
	 *
	 * @param other The MinHeap object to base construction of the current object on.
	 */
	public ArrayMinHeap(MinHeap<T> other){
		int pos = 0;
		Iterator<T> iter = other.iterator();
		while(iter.hasNext()) {
			data[pos] = iter.next();
			pos++;
			count++;
		}
	}

	/**
	 * Standard {@code equals()} method. We provide it for you: DO NOT ERASE! Consider its implementation when implementing
	 * {@link #ArrayMinHeap(MinHeap)}.
	 * @return {@code true} if the current object and the parameter object
	 * are equal, with the code providing the equality contract.
	 * @see #ArrayMinHeap(MinHeap)
	 */
	@Override
	public boolean equals(Object other){
		if(other == null || !(other instanceof MinHeap))
			return false;
		Iterator itThis = iterator();
		Iterator itOther = ((MinHeap) other).iterator();
		while(itThis.hasNext())
			if(!itThis.next().equals(itOther.next()))
				return false;
		return !itOther.hasNext();
	}
	
	@Override
	public void insert(T element) {
		if(data[0] == null) {
			data[0] = element;
			count++;
		}else {
			int pos = 0;
			/*increase array size as needed*/
			if(count >= size) {
				size*=2;
				Object[] updated = new Object[size];
				for(int x = 0; x < data.length; x++) {
					updated[x] = data[x];
				}
				data = updated;
			}
			/*find the correct place to insert*/
			while(pos < data.length && data[pos] != null && ((Comparable<T>) data[pos]).compareTo(element) <= 0) {
				pos++;
			}
			/*shifting elements up*/
			for(int x = data.length-2; x >= pos; x--) {
				data[x+1] = data[x];	
			}
			data[pos] = element;
			count++;
		}
	}

	@Override
	public T deleteMin() throws EmptyHeapException { // DO *NOT* ERASE THE "THROWS" DECLARATION!
		if(data[0] == null)
			throw new EmptyHeapException(null);
		Object ret = data[0];
		/*shifting down*/
		for(int x = 0; x < data.length-1; x++) {
			data[x] = data[x+1];
		}
		count--;
		return (T) ret;
	}

		@Override
	public T getMin() throws EmptyHeapException {	// DO *NOT* ERASE THE "THROWS" DECLARATION!
		if(data[0] == null)
			throw new EmptyHeapException(null);
		return (T) data[0];
	}

	@Override
	public int size() {
		
		return count;
	}

	@Override
	public boolean isEmpty() {
		if(data[0] == null)
			return true;
		else
			return false;
	}

	/**
	 * Standard equals() method.
	 * @return {@code true} if the current object and the parameter object
	 * are equal, with the code providing the equality contract.
	 */


	@Override
	public Iterator<T> iterator() {
		throw new UnimplementedMethodException();
	}

}
