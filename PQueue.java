/*
 * @(#)PQueue.java
 */

package ds.util;

/**
 * A collection designed for holding elements prior to their execution.  The collection
 * stores elements with a priority order supplied by a comparator The interface defines methods
 * for  insertion (push), extraction (pop) and inspection (peek).<p>
 * The remove() method removes and returns the element of highest priority
 * from the priority or throws an exception if it is empty. The peek() method
 * returns the value of an element of highest priority from the queue, but does not
 * remove it.<p>
 * @see HeapPQueue
 */

public interface PQueue<T>
{
   /**
    * Insert item into the priority queue.
    * @param item  insert item into the priority queue.
    */
   public void push(T item);

   /**
    * Remove the element of highest priority and return its value.
    * @return value of the element of highest priority.
    * @throws <tt>NoSuchElementException</tt> if the priority queue is empty.
    */
   public T pop();

   /**
    * Return the value of the element of highest priority.
    * @return value of element of highest priority.
    * @throws <tt>NoSuchElementException</tt> if the priority queue is empty.
    */
   public T peek();

   /**
    * Return a boolean value that indicates whether the priority queue is empty.
    * Return true if empty and false if not empty.
    * @return true if the priority queue is empty and false otherwise.
    */
   public boolean isEmpty();

   /**
    * Return the number of elements currently in the priority queue.
    * @return number of elements in the queue.
    */
   public int size();
}
