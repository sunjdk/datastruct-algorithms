/*
 * @(#)TreeMap.java
 *
 */

package ds.util;

import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;
import java.io.*;

/**
 * An STree implementation of the <tt>OrderedMap</tt> interface. The node value
 * for elements in the tree are key-value pairs defined as Entry objects.
 */

public class TreeMap<K,V> implements OrderedMap<K,V>
{
	// TreeMap implemented using a binary search tree whose
	// nodes are Entry objects. each Entry object contains
	// a key, a value, and references to the left, right, and
	// parent subtrees
	private Entry<K,V> root;
	// size of the map and record of changes to the map for iterators
	private int mapSize, modCount;

	// iteratively traverse a path from the root to the entry
	// key; return a reference to the node containing key or null
	// if the search fails
	private Entry<K,V> getEntry(K key)
	{
	   // entry is current node in traversal
	   Entry<K,V> entry = root;
	   int orderValue;

	   // terminate on on empty subtree
	   while(entry != null)
	   {
	      // compare item and the current node value
	      orderValue = ((Comparable<K>)key).compareTo(entry.key);

	      // if a match occurs, return true; otherwise, go left
	      // or go right following search tree order
	      if (orderValue == 0)
	         return entry;
	      else if (orderValue < 0)
	         entry = entry.left;
	      else
	         entry = entry.right;
	   }

	   return null;
	}

	// private method used by removeEntry() and the iterator
	// remove() to delete an entry from the tree
   private void removeNode(Entry<K,V> dNode)
   {
      if (dNode == null)
         return;

      // dNode = reference to node D that is deleted
      // pNode = reference to parent P of node D
      // rNode = reference to node R that replaces D
      Entry<K,V> pNode, rNode;

      // assign pNode as a reference to P
      pNode = dNode.parent;

      // if D has a null child, the
      // replacement node is the other child
      if (dNode.left == null || dNode.right == null)
      {
         if (dNode.right == null)
            rNode = dNode.left;
         else
            rNode = dNode.right;

         if (rNode != null)
            // the parent of R is now the parent of D
            rNode.parent = pNode;

			// complete the link to the parent node.

			// deleting the root node. assign new root
			if (pNode == null)
				root = rNode;
			// attach R to the correct branch of P
			else if (((Comparable<K>)dNode.key).compareTo(pNode.key) < 0)
				pNode.left = rNode;
			else
				pNode.right = rNode;
      }
      // both children of dNode are non-null.
      else
      {
         // find and unlink replacement node for D.
         // starting at the right child of node D,
         // find the node whose value is the smallest of all
         // nodes whose values are greater than the value in D.
         // unlink the node from the tree.

         // pOfRNode is reference to parent of replacement node
         Entry<K,V> pOfRNode = dNode;

			// first possible replacement is right child of D
			rNode = dNode.right;

			// descend down left subtree of the right child of D,
			// keeping a record of current node and its parent.
			// when we stop, we have found the replacement
			while(rNode.left != null)
			{
				pOfRNode = rNode;
				rNode = rNode.left;
			}

			// copy the value in R to D
			dNode.key = rNode.key;
			dNode.value = rNode.value;

			if (pOfRNode == dNode)
				dNode.right = rNode.right;
			else
				pOfRNode.left = rNode.right;

			// the parent of the right child of R is the
			// parent of R
			if (rNode.right != null)
				rNode.right.parent = pOfRNode;


			// we want to dispose of rNode
			dNode = rNode;
		}

		// make the reference to the deleted node null
		dNode = null;
   }

    /**
     * Creates an empty TreeMap.
     */
   public TreeMap()
   {
      root = null;
      mapSize = 0;
      modCount = 0;
   }

    /**
     * Removes all of the elements from this map. This map will be empty after
     * this call returns.
     */
   public void clear()
   {
      modCount++;
      mapSize = 0;
      root = null;
   }

    /**
     * Returns <tt>true</tt> if this map contains an Entry pair with the specified key.
     *
     * @param key key component for an Entry pair whose presence in this map is to be tested.
     * @return <tt>true</tt> if this map contains the specified Entry pair..
     */
	public boolean containsKey(Object key)
	{
	   return getEntry((K)key) != null;
	}

   /**
    * Returns the value component for an Entry pair specified by the key component.
    * Returns <tt>null</tt> if no Entry pair exits.
    * @param key  key component for an Entry pair.
    * @return value of the Entry pair or <tt>null</tt> if a pair does not exist.
    */
	public V get(Object key)
	{
	   Entry<K,V> p = getEntry((K)key);

	   if (p == null)
	      return null;
	   else
	      return p.getValue();
	}

     /**
     * Returns <tt>true</tt> if this map contains no elements.
     *
     * @return <tt>true</tt> if this map contains no elements.
     */
   public boolean isEmpty()
   { return mapSize == 0; }

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
		// entry is current node in traversal, parent the
		// previous node
		Entry<K,V> entry = root, parent = null, newNode;
		int orderValue = 0;

		// terminate on on empty subtree
		while(entry != null)
		{
			// update the parent reference.
			parent = entry;

			// compare key to the current node key
			orderValue = ((Comparable<K>)key).compareTo(entry.key);

			// if a match occurs, replace the value in entry
			// and return the previous value; otherwise, go left
			// or go right following search tree order
			if (orderValue == 0)
				return entry.setValue(value);
			else if (orderValue < 0)
				entry = entry.left;
			else
				entry = entry.right;
		}

		// create the new node
		newNode = new Entry<K,V>(key, value, parent);

		if (parent == null)
			// this is the first node added. make it root
			root = newNode;
		else if (orderValue < 0)
			// attach newNode as the left child of parent
			parent.left = newNode;
		else
			// attach newNode as the right child of parent
			parent.right = newNode;

		// increment the tree size and modCount
		mapSize++;
		modCount++;

		// we added a new pair to the tree
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
		// search tree for key
		Entry<K,V> dNode  = getEntry((K)key);

		if (dNode == null)
			return null;

	   V returnObj = dNode.getValue();
	   removeNode(dNode);

		mapSize--;
		modCount++;

	   return returnObj;
	}

     /**
     * Returns the number of Entry pairs in this map.
     *
     * @return the number of Entry pairs in this map.
     */
   public int size()
   {
      return mapSize;
   }

    /**
     * Returns the first (minimum) key in this ordered map.
     *
     * @return the first (minimum) key in this ordered map.
     * @throws    NoSuchElementException if the ordered map is empty.
     */
	public K firstKey()
	{
		Entry<K,V> nextNode = root;

		// if the map is empty, return null
		if (nextNode == null)
			return null;

		// first node is the furthest node left from root
		while (nextNode.left != null)
			nextNode = nextNode.left;

		return nextNode.key;
	}

    /**
     * Returns the first (maximum) key in this ordered map.
     *
     * @return the first (maximum) key in this ordered map.
     * @throws    NoSuchElementException if the ordered map is empty.
     */
	public K lastKey()
	{
		Entry<K,V> nextNode = root;

		// if the set is empty, return null
		if (nextNode == null)
			return null;

		// last node is the furthest node right from root
		while (nextNode.right != null)
			nextNode = nextNode.right;

		return nextNode.key;
	}

   /**
    * Returns a string representation of this set. The
    * representation is a comma separated list of Entry objects in ascending order
    * of their keys enclosed in curly braces; each Entry object has the form
    * key=value.
    */
   public String toString()
   {
      int max = mapSize - 1;
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
	private Set<Map.Entry<K,V>> entrySet = null;

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
					return TreeMap.this.size();
				}

				public boolean contains(Object item)
				{
					return containsKey(item);
				}

				public boolean remove(Object item)
				{
					int oldSize = mapSize;
					TreeMap.this.remove(item);

					return mapSize != oldSize;
				}

				public void clear()
				{
					TreeMap.this.clear();
				}

				public boolean isEmpty()
				{
					return TreeMap.this.isEmpty();
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

				public boolean add(K key)
				{
      			throw new UnsupportedOperationException();
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
					return TreeMap.this.size();
				}

				public boolean contains(Object item)
				{
					if (!(item instanceof Map.Entry))
						return false;

					Entry<K,V> entry = (Entry<K,V>)item;
					V value = entry.getValue();
					Entry<K,V> p = getEntry(entry.getKey());

					return p != null && p.getValue().equals(value);
				}

				public boolean remove(Object item)
				{
					if (!(item instanceof Map.Entry))
						return false;

					Entry<K,V> entry = (Entry<K,V>)item;
					K key = entry.getKey();

					return TreeMap.this.remove(key) != null;
				}

				public void clear()
				{
					TreeMap.this.clear();
				}

				public boolean isEmpty()
				{
					return TreeMap.this.isEmpty();
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
					return TreeMap.this.toString();
				}

				public boolean add(Map.Entry<K,V> obj)
				{
      			throw new UnsupportedOperationException();
				}
			};
		}

		return entrySet;
	}

	// iterator for the tree of Entry objects. it is abstract
	// because it does not implement next(). that method returns
	// either an object of type K or Entry<K,V> depending on
	// what type of iterator we want
   private class IteratorImpl<T> implements Iterator<T>
   {
		// set expectedModCount to the number of tree changes
		// at the time of iterator creation
		private int expectedModCount = modCount;
		// node of the last value returned by next() if that
		// value was deleted by the iterator method remove()
		private Entry<K,V> lastReturned = null;
		// node whose value is returned a subsequent call to next()
		private Entry<K,V> nextNode = null;

		// constructor
		IteratorImpl()
		{
			nextNode = root;

			// if the tree is not empty, the first node
			// inorder is the farthest node left from root
			if (nextNode != null)
				while (nextNode.left != null)
					nextNode = nextNode.left;
		}

		// returns true if the tree has more
		// unvisited elements
		public boolean hasNext()
		{
			// elements remain if nextNode is not null
			return nextNode != null;
		}

		// returns the next element in the iteration.
		// throws NoSuchElementException if the iteration
		// has no more elements
		final Entry<K,V> nextEntry()
		{
			// check that the iterator is in a consistent state.
			// throws ConcurrentModificationException if it
			// it is not
			checkIteratorState();

			// check if the iteration has an another element
			// if not, throw NoSuchElementException
			if (nextNode == null)
				throw new NoSuchElementException(
						"Iteration has no more elements");

			// save current value of next in lastReturned.
			lastReturned = nextNode;

			// set nextNode to the next node in order
			Entry<K,V> p;

			if (nextNode.right != null)
			{
				// successor is the furthest left node of
				// right subtree
				nextNode = nextNode.right;

				while (nextNode.left != null)
					nextNode = nextNode.left;
			}
			else
			{
				// have already processed the left subtree, and
				// there is no right subtree. move up the tree,
				// looking for a parent for which nextNode is a left child,
				// stopping if the parent becomes null. a non-null parent
				// is the successor. if parent is null, the original node
				// was the last node inorder
				p = nextNode.parent;

				while (p != null && nextNode == p.right)
				{
					nextNode = p;
					p = p.parent;
				}

				// if we were previously at the right-most node in
				// the tree, nextNode = null
				nextNode = p;
			}

			return lastReturned;
		}

      // removes the last element returned by next() from the
      // underlying collection. this method can be called only
      // once per call to next(). it is an error to modify
      // the underlying collection while the iteration is in
      // progress in any way other than by calling this method.
      // throws IllegalStateException if next() has not yet been
      // called,or remove() has already been called after the last
      // call to next()
      public void remove()
      {
         // check for a missing call to next() or previous()
         if (lastReturned == null)
            throw new IllegalStateException(
               "Iterator call to next() " +
               "required before calling remove()");

         // make sure our state is good
         checkIteratorState();

			// if lastReturned has two children, the replacement node
			// during deletion is nextNode. the value in nextNode
			// is copied to lastReturned. nextNode must be
			// lastReturned
			if (lastReturned.left != null && lastReturned.right != null)
				 nextNode = lastReturned;
         removeNode(lastReturned);

         // list has been modified
         modCount++;
         expectedModCount = modCount;

         // we did a deletion. indicate this by setting lastReturned
         // to null and decrementing treeSize
         lastReturned = null;
         mapSize--;
      }

      public T next()
      { return null; }

      // protected so MiniListIteratorImpl class can use it also
      private void checkIteratorState()
      {
         if (expectedModCount != modCount)
            throw new ConcurrentModificationException(
               "Inconsistent iterator");
      }
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

	// declares a binary search tree node object
	private static class Entry<K,V> implements Map.Entry<K,V>
	{
		// node data
	   K key;
	   V value;

		// child links and link to the node's parent
		Entry<K,V> left, right, parent;

        /**
         * Create new entry.
         */
		public Entry(K key, V value, Entry<K,V> parent)
		{
			this.key = key;
			this.value = value;
			left = null;
			right = null;
			this.parent = parent;
		}

	   /**
	    * Returns the key component of this entry.
	    * @return the key component of this entry
	    */
	   public K getKey()
	   {
	     return key;
	   }

	   /**
	    * Returns the value associated with the key.
	    * @return the value associated with the key.
	    */
	   public V getValue()
	   {
	      return value;
	   }

	   /**
	    * Replaces the value currently associated with the key with the given
	    * value. returns the value associated with the key before this method
	    * was called.
	    * @param value  a new value associated with the key in this entry.
	    * @return the value associated with the key before this method was called.
	    */
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
