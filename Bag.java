/*
 * @(#)Bag.java
 */

package ds.util;

import java.util.Random;
import ds.util.Arrays;
import java.util.NoSuchElementException;

/**
 * This class implements the Collection interface as a user-specified finite array;
 * random access to an element is provided by the <tt>grab</tt> method.
 */
public class Bag<T> implements Collection<T>, Iterable<T>
{
	private T[] bagArr;		// storage structure
	private int bagSize;		// size of collection

	// used by grab()
	private static Random rnd = new Random();

	// remove the element bagArr[i] by shifting the tail of the
	// array left one position and decrementing bagSize
	private void remove(int i)
	{
		// copy bagArr[i+1] ... bagArr[bagSize-1]
		// left one position
		for (int j=i; j < bagSize-1; j++)
			bagArr[j] = bagArr[j+1];

		// decrement bagSize
		bagSize--;
	}

	/**
	 * Constructs an empty bag with a specified capacity.
	 * @param capacity  maximum number of elements that can be stored in the bag.
	 */
	public Bag(int capacity)
	{
		// value of capacity is maximum number of elements
		bagArr = (T[])new Object[capacity];
		bagSize = 0;
	}

    /**
     * Inserts item in the bag if space is available; that is, it the size is less
     * than the capacity.
     * Returns <tt>true</tt> if a new element is added and <tt>false</tt> otherwise.
     *
     * @param item element that is added if space is available..
     * @return <tt>true</tt> if a new element is inserted in the bag.
     *
     */
	public boolean add(T item)
	{
		boolean returnValue;

		if (bagSize >= bagArr.length)
			return false;
		else
		{
			// append item at index bagSize
			bagArr[bagSize] = item;

			// increment bagSize and return true
			bagSize++;

			return true;
		}
	}

    /**
     * Removes all of the elements from this bag.
     * The bag will be empty after this method returns.
     */
	public void clear()
	{
		// the bag has no elements
		bagSize = 0;
	}

	/**
	 * Returns <tt>true</tt> if this bag contains the specified
	 * element and false otherwise.
	 *
	 * @param item element whose occurrence in the bag is checked
	 * @return <tt>true</tt> if this bag contains the specified
	 *         element
	 */
	public boolean contains(Object item)
	{
		// search positions from 0 to bagSize-1
		for (int i=0; i < bagSize; i++)
			if (bagArr[i].equals(item))
				return true;

		return false;
	}

	/**
	* Returns <tt>true</tt> if this collection contains no elements.
	*
	* @return <tt>true</tt> if this collection contains no elements
	*/
	public boolean isEmpty()
	{ return bagSize == 0;	}

    /**
     * Removes a single instance of item from this
     * bag and returns <tt>true</tt> if it is present; otherwise returns <tt>false</tt>.
     *
     * @param item element to be removed from this bag, if present.
     * @return <tt>true</tt> if this bag is modified
     */
	public boolean remove(Object item)
	{
		// search positions from 0 to bagSize-1
		for (int i=0;i < bagSize;i++)
			if (bagArr[i].equals(item))
			{
				// call remove to delete bagArr[i]
				remove(i);
				return true;
			}
		return false;
	}

	  /**
		* Returns the number of elements in this bag.
		*
		* @return the number of elements in this bag
		*/
	public int size()
	{ return bagSize; }

    /**
     * Returns an array containing all of the elements in this bag.
     *
     * @return an array containing all of the elements in this bag
     */
	public Object[] toArray()
	{
		Object[] returnArray = new Object[bagSize];

		for (int i=0; i < bagSize; i++)
			returnArray[i] = bagArr[i];

		return returnArray;
	}

   /**
    * Returns a string that displays the elements in the bag.
    * The description is a comma separated list of
    * elements enclosed in brackets.
    * @return string that contains the list of elements in the bag.
    */
	public String toString()
	{
		Object[] arr = toArray();

		return Arrays.toString(arr);
	}

	/**
	* Returns an iterator over the elements in this bag.
	*
	* @return an <tt>Iterator</tt> over the elements in this bag
	*/
	public Iterator<T> iterator()
	{ return new IteratorImpl(); }

   /**
    * Returns the value of a random element in the bag.
    * @return value of a random element in the bag.
    * @throws <tt>NoSuchElementException</tt> if the bag is empty.
    */
	// return value of random object in range [0,bagSize)
	public T grab()
	{
      // if the bag is empty, throw  NoSuchElementException
      if (bagSize == 0)
         throw new
            NoSuchElementException ("Bag grab(): bag empty");
		return bagArr[rnd.nextInt(bagSize)];
	}

	// subclass that defines an iterator object
	private class IteratorImpl implements Iterator<T>
	{
		// node whose value is returned a subsequent call to next()
		int nextIndex = 0;
		int lastIndex = -1;

		// constructor. not strictly necessary
		IteratorImpl()
		{}

		// returns true if the collection being traversed
		// has more elements
		public boolean hasNext()
		{
			// elements remain if the next node is not the header
			return nextIndex != bagSize;
		}

		// returns the next element in the interation or throws exception if no more elements
		public T next()
		{
			// check if the iteration has an another element; if not throw exception
			if (nextIndex == bagSize)
				throw new RuntimeException("Iteration has no more elements");

			// save current value of next as lastReturned; nextNode
			lastIndex = nextIndex;
			nextIndex++;

			return bagArr[lastIndex];
		}

		// removes the last element returned by next(). this method can be called only once
		// per call to next(). throws IllegalStateException if next() has not yet been called
		// or remove() has already been called after the last call to next()
		public void remove()
		{
			// check for a missing call to next()
			if (lastIndex == -1)
				throw new RuntimeException("Iterator call to next() " +
							"required before calling remove()");

			Bag.this.remove(lastIndex);

			// we did a deletion.indicate this by setting lastIndex to -1
			// nextIndex is reset to the left one position
			nextIndex--;
			lastIndex = -1;
		}
	}
}

