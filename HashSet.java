/*
 * @(#)HashSet.java
 *
 */

package ds.util;
import java.io.*;

/**
 * This class implements the <tt>Set</tt> interface, backed by a hash table
 * (actually a <tt>HashMap</tt> instance).  It makes no guarantees as to the
 * iteration order of the set.<p>
 *
 *
 * @see	    HashMap
 */

public class HashSet<T> implements Set<T>, Cloneable, Iterable<T>
{
	// value for each key in the map
   private static final Object PRESENT = new Object();

   // set implemented using a hash map
   private HashMap<T, Object> map;

    /**
     * Creates an empty HashSet. The value component has Object type.
     */
   public HashSet()
   {
      map = new HashMap<T,Object>();
   }
   
    /**
     * Adds the specified element to this set if it is not already present.
     * If this set already contains the specified element, the call leaves
     * this set unchanged and returns <tt>false</tt>.
     * @param item element to be added to this set.
     * @return <tt>true</tt> if this set did not already contain the specified
     *         element.
     */
   public boolean add(T item)
   {
      return map.put(item, PRESENT) == null;
   }

     /**
     * Removes all of the elements from this set. This set will be empty after
     * this call returns.
     */
   public void clear()
   {
      map.clear();
   }

    /**
     * Returns <tt>true</tt> if this set contains the specified element.
     *
     * @param obj element whose presence in this set is to be tested.
     * @return <tt>true</tt> if this set contains the specified element.
     */
   public boolean contains(Object obj)
   {
      return map.containsKey(obj);
   }

     /**
     * Returns <tt>true</tt> if this set contains no elements.
     *
     * @return <tt>true</tt> if this set contains no elements.
     */
  public boolean isEmpty()
   {
      return map.isEmpty();
   }

	/**
	* Returns an iterator that scan the elements in this set.
	*
	* @return an <tt>Iterator</tt> positioned at a first element in the set.
	*/
   public Iterator<T> iterator()
   {
      return map.keySet().iterator();
   }

     /**
     * Removes the specified element from this set if it is present. Returns
     * <tt>true</tt> if the set contained the specified element.
     *
     * @param item object to be removed from this set, if present.
     * @return true if the set contained the specified element.
     */
  public boolean remove(Object item)
   {
      return map.remove(item) == PRESENT;
   }

    /**
     * Returns the number of elements in this set.
     *
     * @return the number of elements in this set.
     */
   public int size()
   {
      return map.size();
   }

    /**
     * Returns an array containing all of the elements in this set in ascending order.
     *
     * @return an array containing all of the elements in this set in ascending order.
     */
   public Object[] toArray()
   {
      return map.keySet().toArray();
   }

    /**
    * Returns a string representation of this set. The
    * representation is a comma separated list in ascending order
    * enclosed in square brackets.
    */
  public String toString()
   {
      return map.keySet().toString();
   }

    /**
     * Returns a shallow copy of this <tt>HashSet</tt>. Note that a HashSet
     * includes a HashMap using object composition. the HashMap object must be
     * cloned seaprately.
     *
     * @return a shallow copy of this <tt>HashSet</tt> instance.
     */
   public Object clone()
   {
		HashSet<T> copy = null;

		try
		{
			copy = (HashSet<T>)super.clone();
		}
		catch (CloneNotSupportedException cnse)
		{ throw new InternalError(); }

		copy.map = (HashMap<T,Object>)map.clone();

		return copy;
   }

	/**
	 * Save the state of the <tt>HashSet</tt> instance to a stream.
	 * @param out  serialize this instance to the specified ObjectOutputStream.
	 */
	private void writeObject(ObjectOutputStream out)
		throws java.io.IOException
	{
		// write out element count
		out.defaultWriteObject();

	}

	/**
	 * Reconstitute the <tt>HashSet</tt> instance from a stream.
	 * @param in  reconstitute (deserialize) this instance from the ObjectInputStream.
	 */
	private void readObject(ObjectInputStream in)
		throws IOException, ClassNotFoundException
	{
		// read in list size
		in.defaultReadObject();

	}

}