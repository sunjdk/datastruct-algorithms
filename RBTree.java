/*
 * @(#)RBTree.java
 */

package ds.util;

import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;
import java.awt.*;
import ds.graphics.*;
import java.io.*;

import ds.util.LinkedQueue;

/**
 * This class implements the Collection interface using a red-black tree
 * as the underlying storage structure.
 * @see	    AVLTree
 */

public class RBTree<T>
	implements Collection<T>, Iterable<T>
{

	private int treeSize;	// number of nodes in the tree
	private RBNode<T> root;	// root of the tree
	private int column;		// used to build the shadow tree in the
									// displayTree algorithm
	// increases whenever the tree changes. used by an iterator
	// to verify that it is in a consistent state
	private int modCount = 0;

	// used instead of null to represent an empty subtree. its
	// left and right subtrees are null, and its color is
	// RBNode.BLACK. it participates in rotations, just like any
	// other node
	private RBNode<T> NIL = null;

	// delete all the nodes in the tree with root t
	private void deleteTree(RBNode<T> t)
	{
		 // if current root node is not NIL, removeNode its left subtree,
		 // its right subtree and then the node itself
		 if (t != NIL)
		 {
			  deleteTree(t.left);
			  deleteTree(t.right);
			  t = null;
		 }
	}

	private void removeNode(RBNode<T> dNode)
	{
		// BOTTOM-UP ERASE

		// dNode = reference to node D that is deleted
		// pNode = reference to parent P of node D
		// rNode = reference to node R that replaces D
		// spliceOut = reference to the node that is spliced out of the tree
		// childOfSpliceOut = reference to the child of the node we splice out
		RBNode<T> pNode, rNode, spliceOut, childOfSpliceOut;
		// saves color of node that is spliced out
		int spliceoutColor;

		// assign pNode the address of P
		pNode = dNode.parent;

		// if D has a NIL child, the
		// replacement node is the other child
		if (dNode.left == NIL || dNode.right == NIL)
		{
			if (dNode.right == NIL)
				rNode = dNode.left;
			else
				rNode = dNode.right;

			// the parent of R is now the parent of D
			rNode.parent = pNode;

			// complete the link to the parent node.

			// deleting the root node. assign new root
			if (pNode == NIL)
				root = rNode;
			// attach R to the correct branch of P
			else if (((Comparable<T>)(dNode.nodeValue)).compareTo(pNode.nodeValue) < 0)
				pNode.left = rNode;
			else
				pNode.right = rNode;

			// we are splicing dNode out of the tree
			spliceOut = dNode;
			// save color of dNode
			spliceoutColor = spliceOut.color;
			// record the child of our spliceout node for possible
			// balancing operations
			childOfSpliceOut = rNode;
		}
		// both children of dNode are non-NIL.
		else
		{
			// find and unlink replacement node for D.
			// starting at the right child of node D,
			// find the node whose value is the smallest of all
			// nodes whose values are greater than the value in D.
			// unlink the node from the tree.

			// pOfRNode is reference to parent of replacement node
			RBNode<T> pOfRNode = dNode;

			// first possible replacement is right child of D
			rNode = dNode.right;

			// descend down left subtree of the right child of D,
			// keeping a record of current node and its parent.
			// when we stop, we have found the replacement
			while(rNode.left != NIL)
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
			rNode.right.parent = pOfRNode;

			// copy the value in R to D
			dNode.nodeValue = rNode.nodeValue;

			// we want to dispose of rNode
			dNode = rNode;

			// we are splicing rNode out of the tree at its
			// current location
			spliceOut = rNode;
			// save color of the node we will splice out
			spliceoutColor = spliceOut.color;
			// record the child of our spliceout node for possible
			// balancing operations
			childOfSpliceOut = rNode.right;
		}

		// make the reference to the deleted node null
		dNode = null;

		// fixup the tree if the node spliced out is RBNode.BLACK
		if (spliceoutColor == RBNode.BLACK)
			rbDeleteFixup(childOfSpliceOut);

		// decrement tree size
		treeSize--;
	}

	// search for item in the tree. if it is in the tree,
	// return a reference to its node otherwise, return NIL.
	// used by find() and removeNode()
	private RBNode<T> findNode(Object item)
	{
		// cycle t through the tree starting with root
		RBNode<T> t = root;

		// terminate on on empty subtree
		while(t != NIL && ((Comparable<T>)item).compareTo(t.nodeValue) != 0)
		{
			if (((Comparable<T>)item).compareTo(t.nodeValue) < 0)
				t = t.left;
			else
				t = t.right;
		}

		// return reference to node; NIL if not found
		return t;
	}

	// create an empty tree. used by two constructors
	private void makeEmptyTree()
	{
		if (NIL == null)
			// first time an RBTree object created. initialize
			// NIL. all its references are null, and its color
			// is RBNode.BLACK
			NIL = new RBNode<T>(null, null, null, null, RBNode.BLACK);

		// root is NIL, tree size is 0
		root = NIL;
		treeSize = 0;
	}

	// fix up the tree when x is the child of
	// a RBNode.BLACK node that was unlinked from its
	// position in the tree
	private void rbDeleteFixup(RBNode<T> x)
	{
		RBNode<T> siblingOfx;

		while (x != root && x.color == RBNode.BLACK)

			if (x == x.parent.left)
			{
				siblingOfx = x.parent.right;

				if (siblingOfx.color == RBNode.RED)
				{
					// CASE 1:
					//	 sibling of x is RBNode.RED. perform
					//	 color changes and a left rotation.
					//	 this produces a configuration that
					//	 corresponds to cases 2, 3, or 4
					siblingOfx.color = RBNode.BLACK;
					x.parent.color = RBNode.RED;
					rotateLeft(siblingOfx);
					siblingOfx = x.parent.right;
				}

				if (siblingOfx.left.color == RBNode.BLACK &&
					 siblingOfx.right.color == RBNode.BLACK)
				{
					// CASE 2:
					//	 both the children of the siblingOfx are RBNode.BLACK.
					//	 take a RBNode.BLACK off of x and siblingOfx. x now
					//	 has only one RBNode.BLACK, and siblingOfx is RBNode.RED.
					//	 consider parent(x) to have an extra RBNode.BLACK.
					//	 if we enter this case after executing case 1,
					//	 x becomes RBNode.RED, and the loop terminates
					siblingOfx.color = RBNode.RED;
					x = x.parent;
				}
				else
				{
					if (siblingOfx.right.color == RBNode.BLACK)
					{
						// CASE 3:
						//	 siblingOfx is RBNode.BLACK, its left child is
						//	 RBNode.RED, and its right child is RBNode.BLACK. perform
						//	 color changes and right rotation. this
						//	 transforms the node configuration into
						//	 case 4, which termintes the loop

						siblingOfx.left.color = RBNode.BLACK;
						siblingOfx.color = RBNode.RED;
						rotateRight(siblingOfx.left);
						siblingOfx = x.parent.right;
					}

					// CASE 4:
					//	 siblingOfx is RBNode.BLACK and siblingOfx has a
					//	 RBNode.RED right child. after color changes
					//	 and a left rotation, the extra RBNode.BLACK on
					//	 x is removed. set x to the root to terminate
					//	 the loop
					siblingOfx.color = x.parent.color;
					x.parent.color = RBNode.BLACK;
					siblingOfx.right.color = RBNode.BLACK;
					rotateLeft(siblingOfx);
					x = root;
				}
			}
			else  // same as x == x.parent.left, except that
					// "left" and "right" are interchanged
			{
				siblingOfx = x.parent.left;

				if (siblingOfx.color == RBNode.RED)
				{
					siblingOfx.color = RBNode.BLACK;
					x.parent.color = RBNode.RED;
					rotateRight(siblingOfx);
					siblingOfx = x.parent.left;
				}

				if (siblingOfx.right.color == RBNode.BLACK &&
					 siblingOfx.left.color == RBNode.BLACK)
				{
					siblingOfx.color = RBNode.RED;
					x = x.parent;
				}
				else
				{
					if (siblingOfx.left.color == RBNode.BLACK)
					{
						siblingOfx.right.color = RBNode.BLACK;
						siblingOfx.color = RBNode.RED;
						rotateLeft(siblingOfx.right);
						siblingOfx = x.parent.left;
					}

					siblingOfx.color = x.parent.color;
					x.parent.color = RBNode.BLACK;
					siblingOfx.left.color = RBNode.BLACK;
					rotateRight(siblingOfx);
					x = root;
				}
			}

			x.color = RBNode.BLACK;
	}

	// perform a single left rotation
	private void rotateLeft (RBNode<T> pivot)
	{
		RBNode<T> p = pivot.parent, g = pivot.parent.parent;

		p.right = pivot.left;
		pivot.left = p;

		pivot.parent = g;
		p.parent = pivot;
		if (p.right != NIL)
			p.right.parent = p;

		if (p == root)
		  root = pivot;
		else if (p == g.right)
			g.right = pivot;
		else
			g.left = pivot;
	}

	// perform a single right rotation
	private void rotateRight (RBNode<T> pivot)
	{
		// need the parent and grandparent of pivot
		RBNode<T> p = pivot.parent, g = pivot.parent.parent;

		// adjust right and left references
		p.left = pivot.right;
		pivot.right = p;

		// adjust parent references
		pivot.parent = g;
		p.parent = pivot;
		// don't reset the parent link of the left child of p
		// if the left child is NIL. this will interfere with
		// the use of NIL in rbDeleteFixup()
		if (p.left != NIL)
			p.left.parent = p;

		if (p == root)
			// pivot is the new root
			root = pivot;
		else if (p == g.right)
			// right link of g must point at pivot now
			g.right = pivot;
		else
			// left link of g must point at pivot now
			g.left = pivot;
	}

	// break up a 4-node, performing a rotation, if necessary
	private void split4Node(RBNode<T> x)
	{
		// perform the color flip
		x.color = RBNode.RED;
		x.left.color = RBNode.BLACK;
		x.right.color = RBNode.BLACK;

		// if we split the root, we are done
		if (x == root)
			return;

		// to see if a rotation is required, we need the
		// parent of x. x is not root, so p != NIL
		RBNode<T> p = x.parent;

		// a rotation is needed if the parent of x is RBNode.RED
		if (p.color == RBNode.RED)
		{
			// we need the grandparent of x. since the root
			// is RBNode.BLACK, p cannot be root, so the grandparent
			// exists
			RBNode<T> g = x.parent.parent;

			// the grandparent of x will be RBNode.RED
			g.color = RBNode.RED;

			// a double rotation is required if x is an inside
			// grandchild. check this by seeing if the orientations
			// of p to g and x to p are different
			if ( p == g.left && x == p.right )
			{
				// perform a double right rotation
				// first move x up one level and p down
				rotateLeft(x);

				// node x will be RBNode.BLACK
				x.color = RBNode.BLACK;
				// prepare for a right single rotation
				p = x;
			}
			else if ( p == g.right && x == p.left )
			{
				// perform a double left rotation
				// first move x up one level and p down
				rotateRight(x);

				// node x will be RBNode.BLACK
				x.color = RBNode.BLACK;
				// prepare for a left single rotation
				p = x;
			}
			else
				// single rotation. parent will be RBNode.BLACK
				p.color = RBNode.BLACK;

			// perform a single rotation
			// move p up and g down
			if (p == g.left)
				rotateRight(p);
			else
				rotateLeft(p);
		}
	}

    /**
     * Constructs an empty RedBlack tree. All elements inserted into the
     * tree must implement the <tt>Comparable</tt> interface.
     */
	public RBTree()
	{
		makeEmptyTree();
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
		// TOP-DOWN INSERTION

		// declare references to the current node and its parent
		RBNode<T> curr = root, parent = NIL, newNode;

		// loop until we find the value in the tree or locate
		// the insertion point
		while (curr != NIL)
		{
			if (curr.nodeValue.equals(item))
				// item is in the tree. return false
				return false;

			// a node split is required if both children of curr are
			// RBNode.RED
			if (curr.left.color == RBNode.RED &&
				 curr.right.color == RBNode.RED)
				split4Node(curr);

			// move down the tree
			parent = curr;
			if (((Comparable<T>)item).compareTo(curr.nodeValue) < 0)
				curr = curr.left;
			else
				curr = curr.right;
		}

		// create the new node
		newNode = new RBNode<T>(item,
										(RBNode<T>)NIL,
										(RBNode<T>)NIL,
										(RBNode<T>)parent,
										 RBNode.RED);

		// is item the first node inserted into the tree?
		if (parent == NIL)
			// item is at the root of a brand new tree.
			root = newNode;
		else
		{
			// link the new node into its parent
			if (((Comparable<T>)item).compareTo(parent.nodeValue) < 0)
				// insert as left child
				parent.left = newNode;
			else
				// insert as right child
				parent.right = newNode;

			// if the new node's parent is RBNode.RED, we
			// must perform a rotation
			if (parent.color == RBNode.RED)
				split4Node(newNode);
		}

		// the color of the root must be BLACK
		root.color = RBNode.BLACK;

		// the tree has one more node
		treeSize++;

		// the tree has been modified
		modCount++;

		// we did an insertion. set success to true and return
		// an iterator pointing at item
		return true;
	}

    /**
     * Removes all of the elements from this tree. The resulting tree is empty
     * after the method executes.
     */
	public void clear()
	{
		// removeNode the nodes in the tree
		deleteTree(root);

		// tree is emtpy
		root = NIL;
		treeSize = 0;

		// the tree has been modified
		modCount++;
	}

    /**
     * Returns <tt>true</tt> if this tree contains the specified element.
     *
     * @param item the object to be checked for containment in this tree.
     * @return <tt>true</tt> if this tree contains the specified element.
     */
	public boolean contains(Object item)
	{
		RBNode<T> curr;

		// search tree for item
		curr = findNode(item);

		// if item found, return reference to the object
		// otherwise, return null
		if (curr != NIL)
			return true;
		else
			return false;
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
		// assume we will remove item
		boolean wasRemoved = true;
		// search tree for item
		RBNode<T> p  = findNode(item);

		// if item found, delete the node
		if (p != NIL)
		{
			removeNode(p);

			// the tree has been modified
			modCount++;
	}
		else
			wasRemoved = false;


		return wasRemoved;
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
     * in the tree
     *
     * @return an array containing all of the elements in this tree
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
		StringBuffer output = new StringBuffer();
		Iterator iter = iterator();
		int pos = 0, n = treeSize-1;

		output.append("[");
		while (iter.hasNext())
		{
			output.append(iter.next());
			// append comma for all but the last element
			if (pos < n)
				output.append(", ");
			pos++;
		}
		output.append("]");

		return output.toString();
	}

	private class IteratorImpl implements Iterator<T>
	{
		// set expectedModCount to the number of list changes
		// at the time of iterator creation
		private int expectedModCount = modCount;
		// node of the last value returned by next() or header if that
		// value was deleted by the iterator method remove()
		private RBNode<T> lastReturned = NIL;
		// node whose value is returned on a subsequent call to next()
		private RBNode<T> nextNode = NIL;

		// constructor
		IteratorImpl()
		{
			nextNode = root;

			// if the tree is not empty, the first node
			// inorder is the farthest node left from root
			if (nextNode != NIL)
				while (nextNode.left != NIL)
					nextNode = nextNode.left;
		}

		// returns true if the tree has more
		// unvisited elements
		public boolean hasNext()
		{
			// elements remain if nextNode is not NIL
			return nextNode != NIL;
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
			if (nextNode == NIL)
				throw new NoSuchElementException(
						"Iteration has no more elements");

			// save current value of next in lastReturned.
			lastReturned = nextNode;

			// set nextNode to the next node in order
			RBNode<T> p;

			if (nextNode.right != NIL)
			{
				// successor is the furthest left node of
				// right subtree
				nextNode = nextNode.right;

				while (nextNode.left != NIL)
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

				while (p != NIL && nextNode == p.right)
				{
					nextNode = p;
					p = p.parent;
				}

				// if we were previously at the right-most node in
				// the tree, nextNode = NIL
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
			if (lastReturned == NIL)
				throw new IllegalStateException(
					"Iterator call to next() " +
					"required before calling remove()");

			// make sure our state is good
			checkIteratorState();

			removeNode(lastReturned);

			// list has been modified
			modCount++;
			expectedModCount = modCount;

			// we did a deletion. indicate this by setting lastReturned
			// to null and decrementing treeSize
			lastReturned = NIL;
			treeSize--;
		}

		// protected so MiniListIteratorImpl class can use it also
		private void checkIteratorState()
		{
			if (expectedModCount != modCount)
				throw new ConcurrentModificationException(
					"Inconsistent iterator");
		}
	}

	// recursive function that builds a subtree of the shadow tree
	// corresponding to node t of the tree we are drawing. level is the
	// level-coordinate for the root of the subtree. the instance variable
	// column is the changing column-coordinate of the tree nodes
	private RBNodeShadow buildShadowTree(RBNode<T> t, int level)
	{
		// reference to new shadow tree node
		RBNodeShadow newNode = null;

		if (t != NIL)
		{
			// create the new shadow tree node
			newNode = new RBNodeShadow();

			// allocate node for left child at next level in tree; attach node
			RBNodeShadow newLeft = buildShadowTree(t.left, level+1);
			newNode.left = newLeft;

			// initialize variables of the new node
			if (t.color == RBNode.RED)
				newNode.nodeValueStr = t.nodeValue + "*";
			else
				newNode.nodeValueStr = t.nodeValue.toString();

			newNode.level = level;
			newNode.column = column;

			// update column to next cell in the table
			column++;

			// allocate node for right child at next level in tree; attach node
			RBNodeShadow newRight = buildShadowTree(t.right, level+1);
			newNode.right = newRight;
		}

		return newNode;
	}

   /**
    * Returns a string that gives a level-order display of this red-black tree; the
    * argument <tt>maxCharacters</tt> specifies an upper bound on the string
    * length of the node values. Red nodes are denoted with an asterisk ('*').
    * @param maxCharacters an upper bound on the string length of the node values..
    * @return string that gives a level-order display of this AVL tree.
    */
	public String displayTree(int maxCharacters)
	{
		StringBuffer tree = new StringBuffer();
		int level = 0;
		int colWidth = maxCharacters + 1;
		//
		int currLevel = 0, currCol = 0;

		if (treeSize == 0)
			return tree.toString();

		column = 0;

		// build the shadow tree
		RBNodeShadow shadowRoot = buildShadowTree(root, level);

		// use during the level order scan of the shadow tree
		RBNodeShadow currNode;

		// store siblings of each RBNodeShadow object in a queue so that
		// they are visited in order at the next level of the tree
		LinkedQueue<RBNodeShadow> q =
			new LinkedQueue<RBNodeShadow>();

		// insert the root in the queue and set current level to 0
		q.push(shadowRoot);

		// continue the iterative process until the queue is empty
		while(!q.isEmpty())
		{
			// delete front node from queue and make it the current node
			currNode = q.pop();

			// if level changes, output a newline
			if (currNode.level > currLevel)
			{
				currLevel = currNode.level;
				currCol = 0;
				tree.append("\n");
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
				tree.append(formatString((currNode.column-currCol) * colWidth, " "));
				currCol = currNode.column;
			}
			tree.append(formatString(colWidth, currNode.nodeValueStr));
			currCol++;
		}
		tree.append("\n");

		// delete the shadow tree
		deleteShadowTree(shadowRoot);

		return tree.toString();
	}

	// remove the shadow tree from memory after displayTree()
	// displays the red-black tree
	private void deleteShadowTree(RBNodeShadow t)
	{
		// if current root node is not null, delete its left subtree,
		// its right subtree and then the node itself
		if (t != null)
		{
			deleteShadowTree(t.left);
			deleteShadowTree(t.right);
			t = null;
		}
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
	private RBNodeShadow buildShadowTreeD(RBNode<T> t, int level)
	{
		// new shadow tree node
		RBNodeShadow newNode = null;
		String str;;

		if (t != NIL)
		{
			// create the new shadow tree node
			newNode = new RBNodeShadow();

			// allocate node for left child at next level in tree; attach node
			RBNodeShadow newLeft = buildShadowTreeD(t.left, level+1);
			newNode.left = newLeft;

			// initialize variables of the new node
			str = (t.nodeValue).toString(); // format conversion
			newNode.nodeValueStr = str;
			// initialize variables of the new node
			newNode.nodeValueStr = t.nodeValue.toString();
			newNode.level = level;
			newNode.column = RBNodeShadow.columnValue;
			newNode.color = t.color;

			// update column to next cell in the table
			RBNodeShadow.columnValue++;

			// allocate node for right child at next level in tree; attach node
			RBNodeShadow newRight = buildShadowTreeD(t.right, level+1);
			newNode.right = newRight;
	}

		return newNode;
	}

   /**
    * Displays this binary search tree in a graphical window and then disposes of
    * the window; the argument <tt>maxCharacters</tt> specifies an upper bound
    * on the string length of the node values.
	 * Red nodes are denoted with a red circle and black nodes with a light gray circle.
    * @param maxCharacters an upper bound on the string length of the node values..
    */
	public void drawTree(int maxCharacters)
	{
		RBNodeShadow.columnValue = 0;
		drawTreeFrame(maxCharacters, true);
	}

   /**
    * Displays this binary binary tree in a graphical window but keeps active the window for
    * a new frame the becomes available after pressing the <tt>ENTER</tt> key; the
    * argument <tt>maxCharacters</tt> specifies an upper bound on the string
    * length of the node values.
	 * Red nodes are denoted with a red circle and black nodes with a light gray circle.
	 * @param maxCharacters an upper bound on the string length of the node values..
    */
	public void drawTrees(int maxCharacters)
	{
		RBNodeShadow.columnValue = 0;
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
		RBNodeShadow shadowRoot = buildShadowTreeD(root, level);

		// use during the level order scan of the shadow tree
		RBNodeShadow currNode;

		// x, y coordinates of a circle center on the screen
		double x, childx, y, childy;

		// open the drawing window
		DrawTools.openWindow();

		// store siblings of each ShadowTreeNode object in a queue so that
		// they are visited in order at the next level of the tree
		LinkedQueue<RBNodeShadow> q =
			new LinkedQueue<RBNodeShadow>();

		// insert the root in the queue
		q.push(shadowRoot);

		// continue the iterative process until the queue is empty
		while(!q.isEmpty())
		{
			// delete front node from queue and make it the current node
			currNode = q.pop();

			// assign formatted node label to string label
			label = (String)currNode.nodeValueStr;

			// convert each (level, column) coordinate into screen
			// coordinates for the center of the node. add .1 so
			// we stay away from the edges of the screen
			x = currNode.column * cellSide*3/4 + cellSide/2.0 + 0.1;
			y = currNode.level * cellSide*3/4 + cellSide/2.0 + 0.1;

			// if a left child exists, draw an edge from the current
			// node center to the child node center. insert the child
			// in the queue
			if(currNode.left != null)
			{
				// edge.move(x, y);
				// compute the center of the left child node
				childx = currNode.left.column * cellSide*3/4 + cellSide/2.0 + 0.1;
				childy = currNode.left.level * cellSide*3/4 + cellSide/2.0 + 0.1;
				// edge.setEndPoint(childx, childy);
				LineShape edge = new LineShape(
					x,y,childx,childy,ds.graphics.Shape.BLACK);
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
				childx = currNode.right.column * cellSide*3/4 + cellSide/2.0 + 0.1;
				childy = currNode.right.level * cellSide*3/4 + cellSide/2.0 + 0.1;
				// edge.setEndPoint(childx, childy);
				LineShape edge = new LineShape(
					x,y,childx,childy,ds.graphics.Shape.BLACK);
				edge.draw();
				q.push(currNode.right);
			}

			// draw the current node
			// node.move(x,y);
			CircleShape node = new CircleShape(
				x, y, cellSide/2.0, ds.graphics.Shape.LIGHTGRAY);
			if (currNode.color == RBNode.RED)
				node.setColor(ds.graphics.Shape.RED);
			else
				node.setColor(ds.graphics.Shape.LIGHTGRAY);
			node.draw();

			// draw the node data. use an appropriate offset from the node
			// center so the text is approximately aligned in the node
			// text.move(x, y + 0.1);
			// text.setText(label);
			TextShape text = new TextShape(
				x, y, label, ds.graphics.Shape.BLACK, TextShape.TR12B);
			text.draw();
		}

		// pause to view the tree
		DrawTools.viewWindow();

		if (simpleDraw)
			DrawTools.closeWindow();
		else
			DrawTools.eraseWindow();
	}

	private static class RBNode<T>
	{
		// node colors
		public final static int BLACK = 0, RED = 1;

		public RBNode<T> parent;	// node's parent
		public RBNode<T> left;		// node's left child
		public RBNode<T> right;		// node's right child
		public int color;				// node's color
		public T nodeValue;

		//constructors
		public RBNode(T item, RBNode<T> left, RBNode<T> right,
					 	  RBNode<T> parent, int color)
		{
			nodeValue = item;
			this.left = left;
			this.right = right;
			this.parent = parent;
			this.color = color;
		}
	}

	// objects hold a formatted label String and the level,column
	// coordinates for a shadow tree node
	private static class RBNodeShadow
	{
		public static int columnValue;
		public String nodeValueStr;	// formatted node value
		public int level,column, color;
		public RBNodeShadow left, right;

		public RBNodeShadow ()
		{
			left = null;
			right = null;
		}
	}
}
