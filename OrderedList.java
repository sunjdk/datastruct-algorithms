/*
 * @(#)OrderedList.java
 */

package ds.util;

/**
 * This class extends LinkedList by overriding <tt>add</tt> to insert a new
 * item in order.
 */

public class OrderedList<T> extends LinkedList<T>
{
   public OrderedList()
   { super(); }

   public void add(int index, T item)
   {
      throw new UnsupportedOperationException
                 ("OrderedList add(): Invalid operation");
   }

   public void addFirst(T item)
   {
      throw new UnsupportedOperationException
                 ("OrderedList addFirst(): Invalid operation");
   }


   public void addLast(T item)
   {
      throw new UnsupportedOperationException
                 ("OrderedList addLast(): Invalid operation");
   }


	public T set(int index, T item)
	{
      throw new UnsupportedOperationException
                 ("OrderedList set(): Invalid operation");
	}

   // insert item into the ordered list
   public  boolean add(T item)
   {
      // curr starts at first list element
      ListIterator<T> curr = listIterator();

      // move forward until encountering the end of the list or
      // locating the insertion point inside the list
      while (curr.hasNext())
         // compare item to the current list element
         if(((Comparable)item).compareTo(curr.next()) <= 0)
         {
            // item <= than the current list value and goes
            // before it. however, next() moved the iterator
            // forward. move curr back to where it was and break
            // out of the loop
            curr.previous();
            break;
         }

      // add item before curr. if curr is at the end of the list
      // adds item as the last element of the list
      curr.add(item);
      return true;
   }
}
