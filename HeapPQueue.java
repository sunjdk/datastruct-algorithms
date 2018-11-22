/*
 * @(#)HeapPQueue.java
 */

package ds.util;

import java.util.NoSuchElementException;

/**
 * An unbounded priority queue based an a priority heap.
 * @see	    Comparator
 */

public class HeapPQueue<T> implements PQueue<T>
{
   // heapElt holds the priority queue elements
   private T[] heapElt;
   // number of elments in the priority queue
   private int numElts;

   // Comparator used for comparisons
   private Comparator<T> comp;

   /**
    * Creates an empty maximum priority queue that holds elements with the
    * specified generic type T.
    */
	public HeapPQueue()
	{
	   comp = new Greater<T>();
	   numElts = 0;
	   heapElt = (T[]) new Object[10];
	}

   /**
    * Creates an empty priority queue where the Comparator comp determines
    * priority type.
    */
   public HeapPQueue(Comparator<T> comp)
   {
      this.comp = comp;
      numElts = 0;
      heapElt = (T[]) new Object[10];
   }

   /**
    * Insert item into the priority queue.
    * @param item  insert item into the priority queue.
    */
	public void push(T item)
	{
		// if the current capacity is used up, reallocate
		// with double the capacity
		if (numElts == heapElt.length)
			enlargeCapacity();

		// insert item into the heap.
		Heaps.pushHeap(heapElt, numElts, item, comp);
		numElts++;
	}

   /**
    * Remove the element of highest priority and return its value.
    * @return value of the element of highest priority.
    * @throws <tt>NoSuchElementException</tt> if the priority queue is empty.
    */
	public T pop()
	{
		// check for an empty priority queue
		if (numElts == 0)
			throw new NoSuchElementException("HeapPQueue pop(): empty queue");

		// pop the heap and save the return value in top
		 T top = Heaps.popHeap(heapElt, numElts, comp);

		// heap has one less element
		numElts--;

		return top;
	}

   /**
    * Return the value of the element of highest priority.
    * @return value of element of highest priority.
    * @throws <tt>NoSuchElementException</tt> if the priority queue is empty.
    */
	public T peek()
	{
	   // check for an empty heap
	   if (numElts == 0)
	      throw
	         new NoSuchElementException("HeapPQueue peek(): empty queue");

	   // return the root of the heap
	   return heapElt[0];
	}

   /**
    * Return a boolean value that indicates whether the priority queue is empty.
    * Return true if empty and false if not empty.
    * @return true if the priority queue is empty and false otherwise.
    */
   public boolean isEmpty()
   {
      return numElts == 0;
   }

   /**
    * Return the number of elements currently in the priority queue.
    * @return number of elements in the queue.
    */
   public int size()
   {
      return numElts;
   }

   /**
    * Returns a string that displays the elements in the priority queue from
    * highest to lowest priority. The description is a comma separated list of
    * elements enclosed in brackets.
    * @return string that contains the list of elements in the priority queue.
    */
   	public String toString()
   	{
		T[] tmpArr = (T[]) new Object[numElts];
		for (int i = 0; i < numElts; i++)
			tmpArr[i] = heapElt[i];

		Arrays.sort(tmpArr, comp);

      return Arrays.toString(tmpArr);
	}

   // increases the capacity of this HeapPQueue instance
   // to double its current capacity
   private void enlargeCapacity()
   {
      // we want to double the existing array size
      int newCapacity = 2 * heapElt.length;

      // capture a reference to the old array
      T[] oldheapElt = heapElt;

      // create the new array with the new capacity
      heapElt = (T[])new Object[newCapacity];

      // copy the old data to the new array. let garbage
      // collection get rid of the old array
      for (int i=0;i < numElts;i++)
         heapElt[i] = oldheapElt[i];

      // delete references to the old array. garbage
      // collection will delete it
      oldheapElt = null;
   }
}