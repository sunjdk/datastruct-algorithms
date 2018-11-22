/*
 * @(#)STree.java
 */

package ds.util;

import java.util.NoSuchElementException;
import java.lang.IllegalStateException;
import java.util.ConcurrentModificationException;
import java.awt.*;
import ds.graphics.Shape;
import ds.graphics.*;
import java.io.*;

/**
 * This class implements the Collection interface using a binary search
 * tree as the underlying storage structure.
 */

public class STree<T>
	implements Collection<T>, Iterable<T>, Cloneable, java.io.Serializable
{
	// reference to tree root
	transient private STNode<T> root;

	// number of elements in the tree
	private int treeSize;

	// increases whenever the tree changes. used by an iterator
	// to verify that it is in a consistent state
	transient private int modCount;

	// build a shadow tree that is used for tree display
	private STNodeShadow buildShadowTree(STNode<T> t, int level)
   {
      // new shadow tree node
      STNodeShadow newNode = null;
      // text and ostr used to perform format conversion
      String text;

      if (t != null)
      {
         // create the new shadow tree node
         newNode = new STNodeShadow();

         // allocate node for left child at next level in tree; attach node
         STNodeShadow newLeft = buildShadowTree(t.left, level+1);
         newNode.left = newLeft;

         // initialize variables of the new node
         text = (t.nodeValue).toString(); // format conversion
         newNode.nodeValueStr = text;
         newNode.level = level;
         newNode.column = STNodeShadow.columnValue;

         // update column to next cell in the table
         STNodeShadow.columnValue++;

         // allocate node for right child at next level in tree; attach node
         STNodeShadow newRight = buildShadowTree(t.right, level+1);
         newNode.right = newRight;
      }

      return newNode;
   }

	// remove the shadow tree used for tree display
   private STNodeShadow clearShadowTree(STNodeShadow t)
   {
      // if current root node is not null, delete its left subtree,
      // its right subtree and then the node itself
      if (t != null)
      {
         t.left = clearShadowTree(t.left);
         t.right = clearShadowTree(t.right);
         return null;
      }
      else
         return null;
   }

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
			else if (((Comparable<T>)dNode.nodeValue).
					compareTo(pNode.nodeValue) < 0)
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

			// copy the value in R to D
			dNode.nodeValue = rNode.nodeValue;

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
	      orderValue =
	         ((Comparable<T>)item).compareTo(t.nodeValue);

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
     * Creates an empty binary search tree. All elements inserted into the
     * tree must implement the <tt>Comparable</tt> interface.
     */
	public STree()
	{
	   root = null;
	   modCount = 0;
	   treeSize = 0;
	}

    /**
     * Adds the specified item to this tree if it is not already present.
     *
     * @param item element to be added to this tree.
     * @return <tt>true</tt> if the tree did not already contain the specified
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
		treeSize++;
		modCount++;

		// we added a node to the tree
		return true;
	}

    /**
     * Removes all of the elements from this tree. The resulting tree is empty
     * after the method executes.
     */
	public void clear()
   {
		modCount++;
      treeSize = 0;
      root = null;
   }

    /**
     * Returns <tt>true</tt> if this tree contains the specified element.
     *
     * @param item the object to be checked for containment in this tree.
     * @return <tt>true</tt> if this tree contains the specified element.
     */
   public boolean contains(Object item)
   {
      STNode<T> t = findNode(item);
      return (t == null) ? false : true;
   }

     /**
     * Returns <tt>true</tt> if this tree contains no elements.
     *
     * @return <tt>true</tt> if this tree contains no elements.
     */
   public boolean isEmpty()
   {
      return treeSize == 0;
   }

    /**
     * Returns an iterator over the elements in this tree.  The elements
     * are returned in ascending order.
     *
     * @return an iterator over the elements in this tree.
     */
   public Iterator<T> iterator()
   {
      return new IteratorImpl();
   }

    /**
     * Removes the specified item from this tree if it is present.
     *
     * @param item object to be removed from this tree, if present.
     * @return <tt>true</tt> if the tree contained the specified element.
     */
	public boolean remove(Object item)
	{
		// search tree for item
		STNode<T> dNode  = findNode(item);

		if (dNode == null)
			return false;

		removeNode(dNode);

		treeSize--;
		modCount++;

		return true;
	}


     /**
     * Returns the number of elements in this tree.
     *
     * @return the number of elements in this tree.
     */
  	public int size()
   {
      return treeSize;
   }

     /**
     * Returns an array containing all of the elements in this tree.
     * The order of elements in the array is the iterator order of elements
     * in the tree.
     *
     * @return an array containing all of the elements in this tree.
     */
	public Object[] toArray()
 	{
		Object[] arr = new Object[treeSize];
		Iterator iter = iterator();
		int i = 0;

		while (iter.hasNext())
		{
			arr[i] = iter.next();
			i++;
		}

		return arr;
	}

   /**
    * Returns a string representation of this tree. The
    * representation is a comma separated list in iterator order
    * enclosed in square brackets.
    */
   public String toString()
   {
      int i;

      // create the return string object
      String returnStr = "[";
      Iterator iter = this.iterator();

      // output values separated by commas
      for (i = 0; i < treeSize; i++)
      {
         returnStr += iter.next();
         if (i < treeSize - 1)
            returnStr += ", ";
      }
      returnStr += "]";

      // return the string
      return returnStr;
   }

	/**
	 * Searches for the specified item in the tree and returns
	 * the value of the node that matches item as a key.
	 *
	 * @param   item   serves as a key to locate an element in the tree..
	 * @return  the value of the node that corresponds to item as a key
	 *          or <tt>null</tt> if the element is not found.
	 */
	public T find(T item)
	{
		STNode<T> t = findNode(item);
		T value = null;

		if (t != null)
			value = t.nodeValue;

		return value;
	}

	private class IteratorImpl implements Iterator<T>
	{
		// set expectedModCount to the number of list changes
		// at the time of iterator creation
		private int expectedModCount = modCount;
		// node of the last value returned by next() if that
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
         // to null and decrementing treeSize
         lastReturned = null;
         treeSize--;
      }

      private void checkIteratorState()
      {
         if (expectedModCount != modCount)
            throw new ConcurrentModificationException(
               "Inconsistent iterator");
      }
   }

	/**
	 * Save the state of the <tt>STree</tt> instance to a stream.
	 * @param out  serialize this instance to the specified ObjectOutputStream.
	 */
	private void writeObject(java.io.ObjectOutputStream out)
	        throws java.io.IOException
	{
		// Write out treeSize and internal serialization magic
		out.defaultWriteObject();

		// write the elements in their ascending order
      for (Iterator<T> iter = iterator(); iter.hasNext(); )
      {
			out.writeObject(iter.next());
      }
	}

	/**
	 * Reconstitute the <tt>STree</tt> instance from a stream.
	 * @param in  reconstitute (deserialize) this instance from the ObjectInputStream.
	 */
	private void readObject(java.io.ObjectInputStream in)
        throws java.io.IOException, ClassNotFoundException
	{
		// Read in treeSize and internal serialization magic
		in.defaultReadObject();

		// method builds an equivalent search tree from ordered list
		// of elements that are stored in the stream
		root = buildEquivTree(0, treeSize-1, in);
	}

	private STNode<T> buildEquivTree(int low, int high,
					     java.io.ObjectInputStream in)
        throws  java.io.IOException, ClassNotFoundException
	{
        /*
         * Strategy: The root is the middlemost element. To get to it, we
         * have to first recursively construct the entire left subtree,
         * so as to grab all of its elements. We can then proceed with right
         * subtree.
         *
         * The low and high arguments are the minimum and maximum
         * indices to pull out of stream for the current subtree.
         * They are not actually indexed, we just proceed sequentially,
         * ensuring that items are extracted in corresponding order.
         */

        if (high < low) return null;

        int mid = (low + high) / 2;

        // a left child in the bottom up recursive building of the tree
        STNode<T> left  = null;
        if (low < mid)
            left = buildEquivTree(low, mid - 1, in);

        // extract value from the stream
        T value = (T)in.readObject();

        // create a new node with the value and a null parent; the parent value
        // will be assigned when the parent if created in the bottom up
        // building of the tree
        STNode<T> newNode =  new STNode<T>(value, null);

        // update the left pointer of the new node and assign the new node
        // as the parent of the left child
        if (left != null)
        {
            newNode.left = left;
            left.parent = newNode;
        }

        // check to see if a node is created in the right subtree
        if (mid < high)
        {
        		// a right child of the new node in the bottom up recursive
        		// building of the tree
            STNode<T> right = buildEquivTree(mid+1, high, in);
         	// update the right pointer of the new node and assign the new node
        		// as the parent of the right child
           	newNode.right = right;
            right.parent = newNode;
        }
        return newNode;
    }

	/**
	 * Return a copy of this <tt>STree</tt> instance.
	 */
	public Object clone()
	{
		STree<T> copy = null;

		try
		{
			copy = (STree<T>)super.clone();
		}
		catch (CloneNotSupportedException cnse)
		{ throw new InternalError(); }

		copy.root = copyTree(root);
		// return the cloned object
		return copy;
	}

   /*
    * Uses the static method copyTree() in the class BinaryTree as a model
    * to create a duplicate of the tree with root <tt>t</tt> and returns
	 * a reference to the root of the copied tree.  In this case, the elements
	 * are STNode<T> objects
    */
	private static <T> STNode<T> copyTree(STNode<T> t)
	{
		// newNode points at a new node that the algorithm
		// creates. newLptr. and newRptr point to the subtrees
		// of newNode
		STNode<T> newLeft, newRight, newNode;

		// stop the recursive scan when we arrive at empty tree
		if (t == null)
			return null;

		// build the new tree from the bottom up by building the two
		// subtrees and then building the parent. at node t, make
		// a copy of the left subtree and assign its root node reference
		// to newLeft. make a copy of the right subtree and assign its
		// root node reference to newRight
		newLeft = copyTree(t.left);
		newRight = copyTree(t.right);

		// create a new node whose value is the same as the value in t
		// and whose children are the copied subtrees
		newNode = new STNode<T> (t.nodeValue, null);
		newNode.left = newLeft;
		newNode.right = newRight;
		if (newLeft != null)
			newLeft.parent = newNode;
		if (newRight != null)
			newRight.parent = newNode;

		// return a reference to the root of the newly copied tree
		return newNode;
	}

	private String displayShadowTree(STNode<T> t, int maxCharacters)
	{
		String displayStr = "";
		int level = 0, column = 0;
		int colWidth = maxCharacters + 1;
		int currLevel = 0, currCol = 0;

		STNodeShadow.columnValue = 0;

		if (t == null)
			return new String();

		// build the shadow tree
		STNodeShadow shadowRoot = buildShadowTree(t, level);

		// use during the level order scan of the shadow tree
		STNodeShadow currNode;

		// store siblings of each STNodeShadow object in a queue so that
		// they are visited in order at the next level of the tree
		LinkedQueue<STNodeShadow> q = new LinkedQueue<STNodeShadow>();

		// insert the root in the queue and set current level to 0
		q.push(shadowRoot);

		// continue the iterative process until the queue is empty
		while(!q.isEmpty())
		{
			// delete front node from queue and make it the current node
			currNode = q.pop();
			//currNode = null;

			// if level changes, output a newline
			if (currNode.level > currLevel)
			{
				currLevel = currNode.level;
				currCol = 0;
				displayStr += "\n";
			}

			// if a left child exists, insert the child in the queue
			if(currNode.left != null)
				q.push(currNode.left);

			// if a right child exists, insert the child in the queue
			if(currNode.right != null)
				q.push(currNode.right);

			// output formatted node label
			if (currNode.column > currCol)
			{
				displayStr += formatString((currNode.column-currCol) * colWidth, " ");
				currCol = currNode.column;
			}
			displayStr += formatString(colWidth, currNode.nodeValueStr);
			currCol++;
		}

		// delete the shadow tree
		shadowRoot = clearShadowTree(shadowRoot);

		return displayStr;
	}

   /**
    * Returns a string that gives a level-order display of this binary search tree; the
    * argument <tt>maxCharacters</tt> specifies an upper bound on the string
    * length of the node values.
    * @param maxCharacters an upper bound on the string length of the node values..
    * @return string that gives a level-order display of this binary search tree.
    */
	public String displayTree(int maxCharacters)
	{
		return displayShadowTree(root, maxCharacters);
	}

	// right-justify s in a field of w print positions
	private String formatString(int w, String s)
	{
		// capture the length of s
		int sLength = s.length();

		// if length of s is at least w, just return s
		if (sLength >= w)
			return s;

		String str = "";

		// append w - sLength blanks to str
		for (int i=0;i < w - sLength;i++)
			str += " ";
		// append s after the leading blanks
		str += s;

		// return the formatted string
		return str;
	}

	// build a shadow tree that is used for tree display
	private static STNodeShadow buildShadowTreeD(STNode t, int level)
	{
		// new shadow tree node
		STNodeShadow newNode = null;
		// TShadowNode
		String str;;

		if (t != null)
		{
			// create the new shadow tree node
			newNode = new STNodeShadow();

			// allocate node for left child at next level in tree; attach node
			STNodeShadow newLeft = buildShadowTreeD(t.left, level+1);
			newNode.left = newLeft;

			// initialize variables of the new node
			str = (t.nodeValue).toString(); // format conversion
			newNode.nodeValueStr = str;
			newNode.level = level;
			newNode.column = STNodeShadow.columnValue;

			// update column to next cell in the table
			STNodeShadow.columnValue++;

			// allocate node for right child at next level in tree; attach node
			STNodeShadow newRight = buildShadowTreeD(t.right, level+1);
			newNode.right = newRight;
		}

		return newNode;
	}

   /**
    * Displays this binary search tree in a graphical window and then disposes of
    * the window; the argument <tt>maxCharacters</tt> specifies an upper bound
    * on the string length of the node values.
    * @param maxCharacters an upper bound on the string length of the node values..
    */
	public void drawTree(int maxCharacters)
	{
		STNodeShadow.columnValue = 0;
		drawTreeFrame(maxCharacters, true);
	}

   /**
    * Displays this binary binary tree in a graphical window but keeps active the window for
    * a new frame the becomes available after pressing the <tt>ENTER</tt> key; the
    * argument <tt>maxCharacters</tt> specifies an upper bound on the string
    * length of the node values.
    * @param maxCharacters an upper bound on the string length of the node values..
    */
	public void drawTrees(int maxCharacters)
	{
		STNodeShadow.columnValue = 0;
		drawTreeFrame(maxCharacters, false);
	}

	private void drawTreeFrame(int maxCharacters, boolean simpleDraw)
	{
		// approximate width of a character drawn by textShape
		final double UNITS_PER_CHAR = .15;

		// estimated width of a formatted node value.
		// add 2 to allow space outside the label
		double cellSide = maxCharacters*UNITS_PER_CHAR + 0.2;

		String label;

		int level = 0, column = 0;

		// build the shadow tree
		STNodeShadow shadowRoot = buildShadowTreeD(root, level);

		// use during the level order scan of the shadow tree
		STNodeShadow currNode;

		// node draws the circle shape that represents a node.
		// the radius is (cellSide + .20)/2.0
		// CircleShape node = new CircleShape(0,0,cellSide/2.0, Shape.LIGHTGRAY);

		// text draws the formatted value in a node
		// TextShape text = new TextShape(0,0,"", Shape.BLACK);

		// edge draws edges in the tree
		// LineShape edge = new LineShape(0,0,0,0,Shape.BLACK);

		// x, y coordinates of a circle center on the screen
		double x = 0, childx, y = 0, childy;

		// open the drawing window
		DrawTools.openWindow();

		// store siblings of each ShadowTreeNode object in a queue so that
		// they are visited in order at the next level of the tree
		LinkedQueue<STNodeShadow> q = new LinkedQueue<STNodeShadow>();

		// insert the root in the queue
		q.push(shadowRoot);

		// continue the iterative process until the queue is empty
		while(!q.isEmpty())
		{
			// delete front node from queue and make it the current node
			currNode = (STNodeShadow)q.pop();

			// assign formatted node label to string label
			label = (String)currNode.nodeValueStr;

			// convert each (level, column) coordinate into screen
			// coordinates for the center of the node. add .1 so
			// we stay away from the edges of the screen
			x = currNode.column * cellSide + cellSide/2.0 + 0.1;
			y = currNode.level * cellSide + cellSide/2.0 + 0.1;

			// if a left child exists, draw an edge from the current
			// node center to the child node center. insert the child
			// in the queue
			if(currNode.left != null)
			{
				// edge.move(x, y);
				// compute the center of the left child node
				childx = currNode.left.column * cellSide + cellSide/2.0 + 0.1;
				childy = currNode.left.level * cellSide + cellSide/2.0 + 0.1;
				// edge.setEndPoint(childx, childy);
				LineShape edge = new LineShape(x,y,childx,childy,Shape.BLACK);
				edge.draw();
				q.push(currNode.left);
			}

			// if a right child exists, draw an edge from the current
			// node center to the child node center. insert the child
			// in the queue
			if(currNode.right != null)
			{
				// edge.move(x, y);
				// compute the center of the right child node
				childx = currNode.right.column * cellSide + cellSide/2.0 + 0.1;
				childy = currNode.right.level * cellSide + cellSide/2.0 + 0.1;
				// edge.setEndPoint(childx, childy);
				LineShape edge = new LineShape(x,y,childx,childy,Shape.BLACK);
				edge.draw();
				q.push(currNode.right);
			}

			// draw the current node
			// node.move(x,y);
			CircleShape node = new CircleShape(x,y,cellSide/2.0, Shape.LIGHTGRAY);
			node.draw();

			// draw the node data. use an appropriate offset from the node
			// center so the text is approximately aligned in the node
			// text.move(x, y + 0.1);
			// text.setText(label);
			TextShape text = new TextShape(x,y,label, Shape.BLACK);
			text.draw();
		}

		// pause to view the tree
		DrawTools.viewWindow();

		if (simpleDraw)
			DrawTools.closeWindow();
		else
			DrawTools.eraseWindow();
	}

	// declares a binary search tree node object
	private static class STNode<T>
	{
		// STNode is used to implement the binary search tree class
		// making the data public simplifies building the class functions

		// node data
		T nodeValue;

		// child links and link to the node's parent
		STNode<T> left, right, parent;

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

	// objects hold a formatted label string and the level,column
	// coordinates for a shadow tree node
	private static class STNodeShadow
	{
		public static int columnValue;
		public String nodeValueStr;         // formatted node value
		public int level,column;
		public STNodeShadow left, right;

		public STNodeShadow ()
		{}
	}

    private static final long serialVersionUID = 1000009L;
}