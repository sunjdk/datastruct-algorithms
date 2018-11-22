/*
 * @(#)BQueue.java
 */

package ds.util;

import java.util.NoSuchElementException;

/**
 * This class implements a finite queue using a circular queue model
 * to store and retrieve elements.
 */

public class BQueue<T> implements Queue<T>
{
   public static final int MAXQSIZE = 50;

   // array holding the queue elements
   private T[] queueArray;
   // index of the front and back of the queue
   private int qfront, qback;
   // the number of elements in the queue, 0 <= count <= MAXQSIZE
   private int count;

   /**
    * Creates an empty queue that holds a maximum of 50 elements with the
    * specified generic type T.
    */
   public BQueue()
   {
      this(MAXQSIZE);
   }

   /**
    * Creates an empty queue that has a specified fixed size for elements with the
    * specified generic type T.
    * @param size  maximum number of elements in the queue.
    */
   public BQueue(int size)
   {
      queueArray = (T[])new Object[size];
      qfront = 0;
      qback = 0;
      count = 0;
   }

   /**
    * Insert item at the back of the queue provided the queue is not full. Throws
    * an IndexOutOfBoundsException exception if the queue is full.
    * @param item  insert item at the back of the queue
    * @throws IndexOutOfBoundsException if the queue is full.
    */
   public void push(T item)
   {
      // is the array filled up? if so, throw IndexOutOfBoundsException
      if (count == MAXQSIZE)
         throw new IndexOutOfBoundsException("BQueue push(): queue full");

      // perform a circular queue insertion
      queueArray[qback] = item;
      qback = (qback+1) % MAXQSIZE;

      // increment the queue size
      count++;
   }

   /**
    * Remove the element at the front of the queue and return its value.
    * @return value of the element removed from the front of the queue.
    * @throws <tt>NoSuchElementException</tt> if the queue is empty.
    */
   public T pop()
   {
      // if queue is empty, throw NoSuchElementException
      if (count == 0)
         throw new NoSuchElementException("BQueue pop(): empty queue");

      // save the front of the queue
      T queueFront = queueArray[qfront];

      // perform a circular queue deletion
      qfront = (qfront+1) % MAXQSIZE;

      // decrement the queue size
      count--;

      // return the front
      return queueFront;
   }

   /**
    * Return the value of the element at the front of the queue.
    * @return value of element at the front of the queue.
    * @throws <tt>NoSuchElementException</tt> if the queue is empty.
    */
   public T peek()
   {
      // if queue is empty, throw NoSuchElementException
      if (count == 0)
         throw new NoSuchElementException("BQueue front(): empty queue");

      return queueArray[qfront];
   }

   /**
    * Return the number of elements currently in the queue.
    * @return number of elements in the queue.
    */
   public int size()
   {
      return count;
   }

   /**
    * Return a boolean value that indicates whether the queue is empty. Return
    * true if empty and false if not empty.
    * @return true if the queue is empty and false otherwise.
    */
   public boolean isEmpty()
   {
      return count == 0;
   }

   /**
    * Return a boolean value that indicates whether the queue is full. Return
    * true if full and false if not full.
    * @return true if the queue is full and false otherwise.
    */
   public boolean full()
   {
      return count == MAXQSIZE;
   }

   /**
    * Returns a string that displays the elements in the queue from
    * front to back. The description is a comma separated list of
    * elements enclosed in brackets.
    * @return string that contains the list of elements in the queue.
    */
   public String toString()
   {
		if (queueArray == null)
			return "null";
		else if (queueArray.length == 0)
			return "[]";

		// start with the left bracket
		String str = "[" + queueArray[0];

		// append all but the last element, separating items with a comma
		// polymorphism calls toString() for the array type
		for (int i = 1; i < queueArray.length; i++)
			str +=  ", " + queueArray[i];

		str += "]";

		return str;
	}
}
