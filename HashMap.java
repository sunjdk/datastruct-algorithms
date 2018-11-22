/*
 * @(#)HashMap.java
 *
 */

package ds.util;

import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;
import java.io.*;

/**
 * This class implements the <tt>Map</tt> interface using a hash table as the
 * underlying storage structure. <p>
 */

public class HashMap<K,V>
	implements Map<K,V>, Cloneable, java.io.Serializable
{
	// the hash table of key-value pairs
	transient Entry[] table;
	int hashMapSize;
	static final double MAX_LOAD_FACTOR = .75;
	int tableThreshold;

	// for iterator consistency checks
	transient int modCount;

	private void rehash(int newTableSize)
	{
		// allocate the new hash table and record a reference
		// to the current one in oldTable
		Entry[] newTable = new Entry[newTableSize],
				  oldTable = table;
		Entry<K,V> entry, nextEntry;
		int index;

		// cycle through the current hash table
		for (int i=0; i < table.length;i++)
		{
			// record the current entry
			entry = table[i];
			// see if there is a linked list present
			if (entry != null)
			{
				// have at least 1 element in a linked list. assign
				// current table element to null to facilitate
				// faster garbage collection
				table[i] = null;
				do
				{
					// record the next entry in the original linked
					// list
					nextEntry = entry.next;
					// compute the new table index. notice how
					// the saved hashValue prevents a possibly
					// expensive call to hashCode()
					index = entry.hashValue % newTableSize;
					// insert entry at the front of the new table's
					// linked list at location index
					entry.next = newTable[index];
					newTable[index] = entry;
					// assign the next entry in the original linked
					// list to entry
					entry = nextEntry;
				} while (entry != null);
			}
		}

		// the table is now newTable
		table = newTable;
		// update the table threshold
		tableThreshold = (int)(table.length * MAX_LOAD_FACTOR);
		// let garbage collection get rid of oldTable
		oldTable = null;
	}

	// return a reference to the entry with the specified key
	// if there is one in the hash map; otherwise, return null
	public Entry<K,V> getEntry(Object key)
	{
		int index = (key.hashCode() & Integer.MAX_VALUE) % table.length;
	   Entry<K,V> entry;

		entry = table[index];

		while (entry != null)
		{
			if (entry.key.equals(key))
				return entry;
			entry = entry.next;
		}

		return null;
	}

    /**
     * Creates an empty HashMap with 17 buckets.
     */
	public HashMap()
	{
		table = new Entry[17];
		hashMapSize = 0;
		tableThreshold = (int)(table.length * MAX_LOAD_FACTOR);
		modCount = 0;
	}

    /**
     * Removes all of the elements from this map. This map will be empty after
     * this call returns.
     */
	public void clear()
	{
		// make all hash table entries null
		for (int i=0;i < table.length;i++)
			table[i] = null;

		// we have modified the hash map, and it has
		// no entries
		modCount++;
		hashMapSize = 0;
	}

    /**
     * Returns <tt>true</tt> if this map contains an Entry pair with the specified key.
     *
     * @param key key component for an Entry pair whose presence in this map is to be tested.
     * @return <tt>true</tt> if this map contains the specified Entry pair..
     */
   public boolean containsKey(Object key)
   {
      return getEntry(key) != null;
   }

   /**
    * Returns the value component for an Entry pair specified by the key component.
    * Returns <tt>null</tt> if no Entry pair exits.
    * @param key  key component for an Entry pair.
    * @return value of the Entry pair or <tt>null</tt> if a pair does not exist.
    */
	public V get(Object key)
	{
	   Entry<K,V> p = getEntry(key);

	   if (p == null)
	      return null;
	   else
	      return p.value;
	}

     /**
     * Returns <tt>true</tt> if this map contains no elements.
     *
     * @return <tt>true</tt> if this map contains no elements.
     */
   public boolean isEmpty()
   { return hashMapSize == 0; }

    /**
     * Associates the specified value with the specified key in this map
     * If the map previously contained an Entry pair for
     * this key, the old value is replaced by the specified value.
     *
     * @param key key with which the specified value is to be associated.
     * @param value value to be associated with the specified key.
     * @return previous value associated with specified key, or <tt>null</tt>
     *	       if there was no mapping for key.
     */
	public V put(K key, V value)
	{
		// compute the hash table index
		int hashValue = key.hashCode() & Integer.MAX_VALUE,
			 index = hashValue % table.length;
		Entry<K,V> entry;

		// entry references the front of a linked list of colliding
		// values
		entry = table[index];

		// scan the linked list. if key matches the key in an
		// entry, return entry.setValue(value). this
		// replaces the value in the entry and returns the
		// previous value
		while (entry != null)
		{
			if (entry.key.equals(key))
				return entry.setValue(value);

			entry = entry.next;
		}

		// we will add item, so increment modCount
		modCount++;

		// create the new table entry so its successor
		// is the current head of the lsit
		entry = new Entry<K,V>(key, value, hashValue,
									  (Entry<K,V>)table[index]);
		// add it at the front of the linked list and increment
		// the size of the hash map
		table[index] = entry;
		hashMapSize++;

		if (hashMapSize >= tableThreshold)
			rehash(2*table.length + 1);

		return null;
	}

     /**
     * Removes the Entry pair for this key from this map if it is present.
     * Returns the value to which the map previously associated the key, or
     * <tt>null</tt> if the map contained no mapping for this key.
     *
     * @param key key whose mapping is to be removed from the map.
     * @return previous value associated with specified key, or <tt>null</tt>
     *	       if there was no mapping for key.
     */
   public V remove(Object key)
	{
		// compute the hash table index
		int index = (key.hashCode() & Integer.MAX_VALUE) % table.length;
		Entry<K,V> curr, prev;

		// curr references the front of a linked list of colliding
		// values. initialize prev to null
		curr = table[index];
		prev = null;
		// scan the linked list for item
		while (curr != null)
			if (curr.key.equals(key))
			{
				// we have located the key-value pair and will remove
				// it. increment modCount
				modCount++;
				// if prev is not null, curr is not the front
				// of the list. just skip over curr
				if (prev != null)
			 		prev.next = curr.next;
				else
					// curr is front of the list. the new front
					// of the list is curr.next
			 		table[index] = curr.next;

				// decrement hash table size and return true
				hashMapSize--;

				return curr.value;
		 	}
		 	else
		 	{
				// move prev and curr forward
				prev = curr;
				curr = curr.next;
			}

		return null;
	}

     /**
     * Returns the number of Entry pairs in this map.
     *
     * @return the number of Entry pairs in this map.
     */
   public int size()
   { return hashMapSize; }

   /**
    * Returns a string representation of this set. The
    * representation is a comma separated list of Entry objects in ascending order
    * of their keys enclosed in curly braces; each Entry object has the form
    * key=value.
    */
   public String toString()
   {
      int max = hashMapSize - 1;
      StringBuffer buf = new StringBuffer();
      Iterator<Map.Entry<K,V>> iter = entrySet().iterator();

      buf.append("{");
      for (int j = 0; j <= max; j++)
      {
         Map.Entry<K,V> e = iter.next();
         buf.append(e.getKey() + "=" + e.getValue());
         if (j < max)
            buf.append(", ");
      }
      buf.append("}");

      return buf.toString();
   }

   // views

   private Set<K> keySet = null;
   private transient Set<Map.Entry<K,V>> entrySet = null;

    /**
     * Returns a set view of the keys contained in this map.  The set is
     * backed by the map, so changes to the map are reflected in the set, and
     * vice-versa.  If the map is modified while an iteration over the set is
     * in progress (except through the iterator's own <tt>remove</tt>
     * operation), the results of the iteration are undefined.  The set
     * supports element removal, which removes the corresponding mapping from
     * the map, via the <tt>Iterator.remove</tt>, <tt>Set.remove</tt>,
     * <tt>removeAll</tt> <tt>retainAll</tt>, and <tt>clear</tt> operations.
     * It does not support the add or <tt>addAll</tt> operations.
     *
     * @return a set view of the keys contained in this map.
     */
   public Set<K> keySet()
   {
      if (keySet == null)
      {
         keySet = new Set<K>()
         {
            public Iterator<K> iterator()
            {
               return new KeyIterator();
            }

            public int size()
            {
               return HashMap.this.size();
            }

            public boolean isEmpty()
            {
               return HashMap.this.size() == 0;
            }

            public boolean contains(Object item)
            {
               return containsKey(item);
            }

            public boolean remove(Object item)
            {
               int oldSize = size();

               HashMap.this.remove(item);

               return size() != oldSize;
            }

            public void clear()
            {
               HashMap.this.clear();
            }

            // add is not allowed in a key set
            public boolean add(K item)
            {
               throw new UnsupportedOperationException();
            }

				public Object[] toArray()
				{
					Object[] arr = new Object[size()];
					Iterator<K> iter = iterator();

					for (int i=0;i < arr.length;i++)
						arr[i] = iter.next();

					return arr;
				}

				public String toString()
				{
					int max = size() - 1;
					StringBuffer buf = new StringBuffer();
					Iterator<K> iter = iterator();

					buf.append("[");
					while (iter.hasNext())
					{
						buf.append(iter.next());
						if (iter.hasNext())
							buf.append(", ");
					}
					buf.append("]");

					return buf.toString();
				}
         };
      }

      return keySet;
   }

    /**
     * Returns a set view of the mappings contained in this map.  Each element
     * in the returned set is a {@link Map.Entry}.  The set is backed by the
     * map, so changes to the map are reflected in the set, and vice-versa.
     * If the map is modified while an iteration over the set is in progress
     * (except through the iterator's own <tt>remove</tt> operation, or through
     * the <tt>setValue</tt> operation on a map entry returned by the iterator)
     * the results of the iteration are undefined.  The set supports element
     * removal, which removes the corresponding mapping from the map, via the
     * <tt>Iterator.remove</tt>, <tt>Set.remove</tt>, <tt>removeAll</tt>,
     * <tt>retainAll</tt> and <tt>clear</tt> operations.  It does not support
     * the <tt>add</tt> or <tt>addAll</tt> operations.
     *
     * @return a set view of the mappings contained in this map.
     */
   public Set<Map.Entry<K,V>> entrySet()
   {
      if (entrySet == null)
      {
         entrySet = new Set<Map.Entry<K,V>>()
         {
            public Iterator<Map.Entry<K,V>> iterator()
            {
               return new EntryIterator();
            }

            public int size()
            {
               return HashMap.this.size();
            }

            public boolean isEmpty()
            {
               return size() == 0;
            }

 				public boolean contains(Object item)
				{
					if (!(item instanceof Map.Entry))
						return false;

					Entry<K,V> entry = (Entry<K,V>)item;
					V value = entry.value;
					Entry<K,V> p = getEntry(entry.key);

					return p != null && p.getValue().equals(value);
				}

            public boolean remove(Object item)
            {
               Entry<K,V> entry = (Entry<K,V>)item;
               K key = entry.key;

               return HashMap.this.remove(key) != null;
            }

            public void clear()
            {
               HashMap.this.clear();
            }

            // add is not allowed in an entry set
            public boolean add(Map.Entry<K,V> item)
            {
               throw new UnsupportedOperationException();
            }

				public Object[] toArray()
				{
					Object[] arr = new Object[size()];
					Iterator<Map.Entry<K,V>> iter = iterator();

					for (int i=0;i < arr.length;i++)
						arr[i] = iter.next();

					return arr;
				}

            public String toString()
            {
					return HashMap.this.toString();
				}
         };
      }

      return entrySet;
   }

	// private static final long serialVersionUID = 1000006L;

	/**
	 * Save the state of the <tt>HashMap</tt> instance to a stream.
	 * @param out  serialize this instance to the specified ObjectOutputStream.
    * @serialData The <i>table length</i> of the HashMap (the length of the
    *		   bucket array) is emitted (int), followed  by the
    *		   <i>size</i> of the HashMap (the number of key-value
    *		   mappings), followed by the key (Object) and value (Object)
    *		   for each key-value mapping represented by the HashMap
    *         The key-value mappings are emitted in the order that they
    *         are returned by <tt>entrySet().iterator()</tt>.
    */
	private void writeObject(ObjectOutputStream out)
        throws IOException
	{
		try
		{
		// Write out the tableThreshold and any hidden stuff
		out.defaultWriteObject();

		// Write out number of buckets
		out.writeInt(table.length);

		// Write out size (number of Mappings)
		out.writeInt(hashMapSize);

		// Write out keys and values (alternating)
		Set s = entrySet();
		for (Iterator<Map.Entry<K,V>> iter = s.iterator(); iter.hasNext(); )
		{
			Map.Entry<K,V> me = iter.next();
			out.writeObject(me.getKey());
			out.writeObject(me.getValue());
		}
	}
	catch (Exception e)
	{ System.err.println(e);
	  e.printStackTrace();
	}
	}


	/**
	 * Reconstitute the <tt>HashMap</tt> instance from a stream (i.e.,
	 * deserialize it).
	 */
   private void readObject(ObjectInputStream in)
         throws IOException, ClassNotFoundException
   {
		// Read in the tableThreshold and any hidden stuff
		in.defaultReadObject();

		// Read in number of buckets and allocate the bucket array;
		int tableSize = in.readInt();
		table = new Entry[tableSize];
		hashMapSize = 0;

		// Read in size (number of Mappings)
		int size = in.readInt();
		// init();

		// Read the keys and values, and put the mappings in the HashMap
		for (int i=0; i < size; i++)
		{
	    	K key = (K) in.readObject();
	    	V value = (V) in.readObject();
	    	put(key, value);
		}
	}

	//private void init()
	//{  }



   /**
     * Returns a shallow copy of this <tt>HashMap</tt>. (The elements
     * themselves are not cloned.)
     *
     * @return a shallow copy of this <tt>HashMap</tt> instance.
     */
	public Object clone()
	{
		HashMap<K,V> copy = null;

		try
		{
			copy = (HashMap<K,V>)super.clone();
		}
		catch (CloneNotSupportedException cnse)
		{ throw new InternalError(); }

		copy.table = new Entry[table.length];
		copy.modCount = 0;
		copy.hashMapSize = hashMapSize;
		//copy.init();

		Entry<K,V> currEntry, currCopyEntry, newCopyEntry;
		int index;

		// cycle through the current hash table and create the clone table
		for (int i=0; i < table.length;i++)
		{
			// record the current entry
			currEntry = table[i];

			if (currEntry != null)
			{
				currCopyEntry =
					new Entry<K,V>(currEntry.key, currEntry.value,
											currEntry.hashValue, null);
				copy.table[i] = currCopyEntry;

				currEntry = currEntry.next;

				while (currEntry != null)
				{
					newCopyEntry =
						new Entry<K,V>(currEntry.key, currEntry.value,
											currEntry.hashValue, null);
					currCopyEntry.next = newCopyEntry;
					currCopyEntry = newCopyEntry;
					currEntry = currEntry.next;
				}
			}
		}

		return copy;
	}

	// inner class that implements an iterator for the hash table
	private class IteratorImpl<T> implements Iterator<T>
	{
		Entry<K,V> next;			// next entry to return
		int expectedModCount;	// to check iterator consistency
		int index;         		// index of current hash table bucket
		// reference to the last value returned by next()
		Entry<K,V> lastReturned;

		IteratorImpl()
		{
			int i = 0;
			Entry<K,V> n = null;

			// the expected modCount starts at modCount
			expectedModCount = modCount;

			// find the first non-empty bucket
			if (hashMapSize != 0)
				while (i < table.length && ((n = table[i]) == null))
					i++;

			next = n;
			index = i;
			lastReturned = null;
		}

		public boolean hasNext()
		{
			// we are at the end of the table if next == null
			return next != null;
		}

		final Entry<K,V> nextEntry()
		{
			// check for iterator consistency
			if (modCount != expectedModCount)
				 throw new ConcurrentModificationException();

			// we will return the value in Entry object next
			Entry<K,V> entry = next;

			// if entry is null, we are at the end of the table
			if (entry == null)
				 throw new NoSuchElementException();

			// capture the entry we will return
			lastReturned = entry;
			// move to the next entry in the current linked list
			Entry<K,V> n = entry.next;
			// record the current bucket index
			int i = index;

			if (n == null)
			{
				// we are at the end of a bucket. search for the
				// next non-empty bucket
				i++;
				while (i < table.length && (n = table[i]) == null)
					i++;
			}

			index = i;
			next = n;

			return lastReturned;
		}

	  	public void remove()
	  	{
         // check for a missing call to next() or previous()
         if (lastReturned == null)
            throw new IllegalStateException(
               "Iterator call to next() " +
               "required before calling remove()");
			if (modCount != expectedModCount)
				 throw new ConcurrentModificationException();

			// remove lastReturned by calling remove() in Hash.
			// this call will increment modCount
			HashMap.this.remove(lastReturned.key);
			expectedModCount = modCount;
			lastReturned = null;
		}

		public T next()
		{ return null; }
	}

   private class KeyIterator extends IteratorImpl<K>
    {
        public K next()
        {
            return nextEntry().key;
        }
    }

    private class EntryIterator extends IteratorImpl<Map.Entry<K,V>>
    {
        public Map.Entry<K,V> next()
        {
            return nextEntry();
        }
    }

	private static class Entry<K,V> implements Map.Entry<K,V>
	{
	   K key;
	   V value;
	   Entry<K,V> next;
	   int hashValue;

        /**
         * Create new entry.
         */
	   Entry(K key, V value, int hashValue, Entry<K,V> next)
	   {
	      this.key = key;
	      this.value = value;
	      this.hashValue = hashValue;
	      this.next = next;
	   }

	   /**
	    * Returns the key.
	    */
	   public K getKey()
	   {
	      return key;
	   }

	   /**
	    * Returns the value associated with the key.
	    */
	   public V getValue()
	   {
	      return value;
	   }

		public Entry<K,V> getNext()
		{
			return next;
		}

	   // replaces the value currently associated with the key with the given
	   // value. returns the value associated with the key before this method
	   // was called
	   public V setValue(V value)
	   {
	      V oldValue = this.value;

	      this.value = value;
	      return oldValue;
	   }

	   public String toString()
	   {
	      return key + "=" + value;
	   }
	}
}
