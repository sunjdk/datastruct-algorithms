/*
 * @(#)TreeSet.java
 *
 */

package ds.util;

import java.util.NoSuchElementException;
import java.lang.IllegalStateException;
import java.util.ConcurrentModificationException;
import java.io.*;


/**
 * An implementation of the <tt>OrderedSet</tt> using <tt>STNode</tt> elements
 * and binary search tree (<tt>STree</tt> algorithms.
 *
 * @see	    HashSet
 * @param <T> the type of elements held in this collection
 */

public class TreeSet<T> implements OrderedSet<T>, Iterable<T>
{
   // reference to tree root
   private STNode<T> root;

   // number of elements in the tree
   private int setSize;

   // increases whenever the tree changes. used by an iterator
   // to verify that it is in a consistent state
   private int modCount;

	// private method used by remove() and the iterator
	// remove() to delete a node
   private void removeNode(STNode<T> dNode)
   {
      if (dNode == null)
         return;

      // dNode = reference to node D that is deleted
      // pNode = reference to parent P of node D
      // rNode = reference to node R that replaces D
      STNode<T> pNode, rNode;

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
			else if (((Comparable<T>)(dNode.nodeValue)).compareTo(pNode.nodeValue) < 0)
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
         STNode<T> pOfRNode = dNode;

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

			if (pOfRNode == dNode)
				dNode.right = rNode.right;
			else
				pOfRNode.left = rNode.right;

			// the parent of the right child of R is the
			// parent of R
			if (rNode.right != null)
				rNode.right.parent = pOfRNode;

			// copy the value in R to D
			dNode.nodeValue = rNode.nodeValue;

			// we want to dispose of rNode
			dNode = rNode;
		}

		// make the reference to the deleted node null
		dNode = null;
   }

    // delete the tree with a postorder scan of the nodes
   private void deleteTree(STNode<T> t)
   {
      // if current root node is not null, delete its left subtree,
      // its right subtree and then set the node to null
      if (t != null)
      {
         deleteTree(t.left);
         deleteTree(t.right);
         t = null;
      }
   }

	// iteratively traverse a path from the root to the node
	// whose value is item; return a reference to the node
	// containing item or null if the search fails
   private STNode<T> findNode(Object item)
   {
      // t is current node in traversal
      STNode<T> t = root;
      int orderValue;

      // terminate on on empty subtree
      while(t != null)
      {
         // compare item and the current node value
         orderValue = ((Comparable<T>)item).compareTo(t.nodeValue);

         // if a match occurs, return true; otherwise, go left
         // or go right following search tree order
         if (orderValue == 0)
            return t;
         else if (orderValue < 0)
            t = t.left;
         else
            t = t.right;
      }
      return null;
   }

    /**
     * Creates an empty TreeSet.
     */
   public TreeSet()
   {
      root = null;
      modCount = 0;
      setSize = 0;
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
		// t is current node in traversal, parent the previous node
		STNode<T> t = root, parent = null, newNode;
		int orderValue = 0;

		// terminate on on empty subtree
		while(t != null)
		{
			// update the parent reference.
			parent = t;

			// compare item and the current node value
			orderValue = ((Comparable<T>)item).compareTo(t.nodeValue);

			// if a match occurs, return false; otherwise, go left
			// or go right following search tree order
			if (orderValue == 0)
				return false;
			else if (orderValue < 0)
				t = t.left;
			else
				t = t.right;
		}

		// create the new node
		newNode = new STNode<T>(item,parent);

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
		setSize++;
		modCount++;

		// we added a node to the tree
		return true;
	}

    /**
     * Removes all of the elements from this set. This set will be empty after
     * this call returns.
     */
	public void clear()
   {
      deleteTree(root);
      root = null;
      setSize = 0;
   }

    /**
     * Returns <tt>true</tt> if this set contains the specified element.
     *
     * @param item element whose presence in this set is to be tested.
     * @return <tt>true</tt> if this set contains the specified element.
     */
   public boolean contains(Object item)
   {
      STNode<T> t = findNode(item);
      return (t == null) ? false : true;
   }

    /**
     * Returns <tt>true</tt> if this set contains no elements.
     *
     * @return <tt>true</tt> if this set contains no elements.
     */
   public boolean isEmpty()
   {
      return setSize == 0;
   }

	/**
	* Returns an iterator that scan the elements in this set in ascending order.
	*
	* @return an <tt>Iterator</tt> positioned at the minimum element in the set.
	*/
   public Iterator<T> iterator()
   {
      return new IteratorImpl();
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
		// search tree for item
		STNode<T> dNode  = findNode(item);

		if (dNode == null)
			return false;

		removeNode(dNode);

		setSize--;
		modCount++;

		return true;
	}

    /**
     * Returns the number of elements in this set.
     *
     * @return the number of elements in this set.
     */
   public int size()
   {
      return setSize;
   }

    /**
     * Returns the first (minimum) element currently in this sorted set.
     *
     * @return the first (minimum) element currently in this sorted set.
     * @throws    NoSuchElementException sorted set is empty.
     */
	public T first()
	{
		STNode<T> nextNode = root;

		// if the set is empty, return null
		if (nextNode == null)
			return null;

		// first node is the furthest node left from root
		while (nextNode.left != null)
			nextNode = nextNode.left;

		return nextNode.nodeValue;
	}

    /**
     * Returns the last (maximum) element currently in this sorted set.
     *
     * @return the last (maximum) element currently in this sorted set.
     * @throws    NoSuchElementException sorted set is empty.
     */
	public T last()
	{
		STNode<T> nextNode = root;

		// if the set is empty, return null
		if (nextNode == null)
			return null;

		// last node is the furthest node right from root
		while (nextNode.right != null)
			nextNode = nextNode.right;

		return nextNode.nodeValue;
	}

    /**
     * Returns an array containing all of the elements in this set in ascending order.
     *
     * @return an array containing all of the elements in this set in ascending order.
     */
   public Object[] toArray()
   {
      Object[] arr = new Object[setSize];

      Iterator iter = iterator();

      // output values separated by commas
      for (int i = 0; i < setSize; i++)
      	arr[i] = iter.next();

      // return the array
      return arr;
   }

   /**
    * Returns a string representation of this set. The
    * representation is a comma separated list in ascending order
    * enclosed in square brackets.
    */
   public String toString()
   {
      int i;

      // create the return string object
      String returnStr = "[";
      Iterator iter = this.iterator();

      // output values separated by commas
      for (i = 0; i < setSize; i++)
      {
         returnStr += iter.next();
         if (i < setSize - 1)
            returnStr += ", ";
      }
      returnStr += "]";

      // return the string
      return returnStr;
   }

   private class IteratorImpl implements Iterator<T>
   {
		// set expectedModCount to the number of list changes
		// at the time of iterator creation
		private int expectedModCount = modCount;
		// node of the last value returned by next() or header if that
		// value was deleted by the iterator method remove()
		private STNode<T> lastReturned = null;
		// node whose value is returned a subsequent call to next()
		private STNode<T> nextNode = null;

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
		public T next()
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
			STNode<T> p;

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

			return lastReturned.nodeValue;
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
         // to null and decrementing setSize
         lastReturned = null;
         setSize--;
      }

      // protected so MiniListIteratorImpl class can use it also
      private void checkIteratorState()
      {
         if (expectedModCount != modCount)
            throw new ConcurrentModificationException(
               "Inconsistent iterator");
      }
   }


	// declares a binary search tree node object
	private static class STNode<T>
	{
		// STNode<T> is used to implement the binary search tree class
		// making the data public simplifies building the class functions

		// node data
		public T nodeValue;

		// child links and link to the node's parent
		public STNode<T> left, right, parent;

		// constructor that initializes the value and parent fields and sets
		// the link fields left and right to null
		public STNode (T item,STNode<T> parentNode)
		{
			nodeValue = item;
			left = null;
			right = null;
			parent = parentNode;
		}
	}
}