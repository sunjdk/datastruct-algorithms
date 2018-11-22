/*
 * @(#)AVLTree.java
 */

package ds.util;

import java.util.NoSuchElementException;
import java.lang.IllegalStateException;
import java.util.ConcurrentModificationException;
import java.awt.*;
import ds.graphics.Shape;
import ds.graphics.*;


/**
 * This class is a balanced binary tree that implements the Collection interface AVL
 * tree single and double rotation methods.
 * @see	    RBTree
 */

public class AVLTree<T>
	implements Collection<T>, Iterable<T>
{
    private static int LEFTHEAVY = 1;
    private static int BALANCED = 0;
    private static int RIGHTHEAVY = -1;
   // reference to tree root
   private AVLNode<T> root;

   // number of elements in the tree
   private int treeSize;

   // increases whenever the tree changes. used by an iterator
   // to verify that it is in a consistent state
   private int modCount;

    // delete the tree with a postorder scan of the nodes
    private void deleteTree(AVLNode<T> t)
    {
      // if current root node is not null, delete its left
      // subtree, its right subtree and then set the node to null
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
    private AVLNode<T> findNode(Object item)
    {
        // t is current node in traversal
        AVLNode<T> t = root;
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
     * Constructs an empty AVL tree. All elements inserted into the
     * tree must implement the <tt>Comparable</tt> interface.
     */
    public AVLTree()
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
		try
		{
			root = addNode(root, item);
		}

		catch (IllegalStateException ise)
		{ return false; }

		// increment the tree size and modCount
		treeSize++;
		modCount++;

		// we added a node to the tree
		return true;
	}

	private AVLNode<T> addNode(AVLNode<T> t, T item)
	{
		if( t == null )
			t = new AVLNode<T>(item);
		else if (((Comparable<T>)item).compareTo(t.nodeValue) < 0)
		{
			t.left = addNode( t.left, item);

			if (height(t.left) - height(t.right) == 2 )
				if (((Comparable<T>)item).compareTo
							(t.left.nodeValue) < 0)
					t = singleRotateRight(t);
				else
					t = doubleRotateRight(t);
		}
		else if (((Comparable<T>)item).compareTo
							(t.nodeValue) > 0 )
		{
			t.right = addNode(t.right, item );

			if (height(t.left) - height(t.right) == -2)
				if (((Comparable<T>)item).compareTo
							(t.right.nodeValue) > 0)
					t = singleRotateLeft(t);
				else
					t = doubleRotateLeft(t);
		}
		else
			// duplicate; throw IllegalStateException
			throw new IllegalStateException();

		t.height = max( height(t.left), height(t.right) ) + 1;

		return t;
	}

	 private static <T> int height(AVLNode<T> t)
	 {
	     if (t == null)
	        return -1;
	   else
	     return t.height;
	 }

    private static int max(int a, int b)
    {
        if (a > b)
            return a;
        else
            return b;
    }


	// perform a single right rotation for parent p
	private static <T> AVLNode<T> singleRotateRight(AVLNode<T> p)
	{
		AVLNode<T> lc = p.left;

		p.left = lc.right;
		lc.right = p;
		p.height = max( height( p.left ), height( p.right ) ) + 1;
		lc.height = max( height( lc.left ), lc.height ) + 1;

		return lc;
	}

	// perform a single left rotation for parent p
	private static <T> AVLNode<T> singleRotateLeft(AVLNode<T> p)
	{
		AVLNode<T> rc = p.right;

		p.right = rc.left;
		rc.left = p;
		p.height = max(height(p.left), height(p.right)) + 1;
		rc.height = max(height(rc.right), rc.height) + 1;

		return rc;
	}

	// perform a double right rotation for parent p
	private static <T> AVLNode<T> doubleRotateRight(AVLNode<T> p)
	{
		p.left = singleRotateLeft(p.left);
		return singleRotateRight(p);
	}

	// perform a single left rotation for parent p
	private static <T> AVLNode<T> doubleRotateLeft(AVLNode<T> p)
	{
		p.right = singleRotateRight(p.right);
		return singleRotateLeft(p);
	}

    /**
     * Removes all of the elements from this tree. The resulting tree is empty
     * after the method executes.
     */
	public void clear()
	{
		deleteTree(root);
		root = null;
		treeSize = 0;
	}

   /**
    * Returns a string that gives a level-order display of this AVL tree; the
    * argument <tt>maxCharacters</tt> specifies an upper bound on the string
    * length of the node values. The height of each node is enclosed in parentheses.
    * @param maxCharacters an upper bound on the string length of the node values.
    * @return string that gives a level-order display of this AVL tree.
    */
    public String displayTree(int maxCharacters)
    {
      return displayShadowTree(root, maxCharacters);
    }

    /**
     * Returns <tt>true</tt> if this tree contains the specified element.
     *
     * @param item the object to be checked for containment in this tree.
     * @return <tt>true</tt> if this tree contains the specified element.
     */
    public boolean contains(Object item)
    {
      AVLNode<T> t = findNode(item);
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
      return new IteratorImpl(root);
    }

    /**
     * Removes the specified item from this tree if it is present.
     *
     * @param item object to be removed from this tree, if present.
     * @return <tt>true</tt> if the tree contained the specified element.
     */
		public boolean remove (Object item)
		{ return true; }

     /**
     * Returns the number of elements in this tree.
     *
     * @return the number of elements in this tree.
     */
		public int size()
		{ return treeSize; }

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

    private class IteratorImpl implements Iterator<T>
    {
        private ALStack<AVLNode<T>> stack = null;
        private AVLNode<T> curr = null;

        // set expectedModCount to the number of list changes
        // at the time of iterator creation
        private int expectedModCount = modCount;

        // go far left from t, pushing all the nodes with left
        // children on stack
        private AVLNode<T> goFarLeft(AVLNode<T> t)
        {
            if (t == null)
                 return null;
            while (t.left != null)
            {
                 stack.push(t);
                 t = t.left;
            }
            return t;
        }

        public IteratorImpl(AVLNode<T> root)
        {
            stack = new ALStack<AVLNode<T>>();
            curr = goFarLeft(root);
        }

        public boolean hasNext()
        {
            return curr != null;
        }

        public T next()
        {
            // check that the iterator is in a consistent state.
            // throws ConcurrentModificationException if not
            checkIteratorState();

            if (curr == null)
                 throw new NoSuchElementException(
                 		"No elements remaining");

            // capture the value in the node
            T returnValue = (T)curr.nodeValue;

            if (curr.right != null)	// have a right subtree
                 // stack nodes on left subtree
                 curr = goFarLeft(curr.right);
            else if (!stack.isEmpty())
                 // no right subtree there are other nodes
                 // to visit. pop the stack
                 curr = (AVLNode<T>)stack.pop();
            else
                 curr = null;	// end of tree; set curr to null

            return returnValue;
        }

        public void remove()
        {
            // no implementation
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
	private static class AVLNode<T>
	{
		// node data
		public T nodeValue;

		// child links and link to the node's parent
		public AVLNode<T> left, right;

		// public int height;
		public int height;

		// constructor that initializes the value, balance factor
		// and parent fields and sets the link fields left and
		// right to null
		public AVLNode(T item)
		{
			nodeValue = item;
			left = null;
			right = null;
			height = 0;
		}
	}

   // objects hold a formatted label string and the level,column
	// coordinates for a shadow tree node
	private static class AVLNodeShadow
	{
		public static int columnValue;
		public String nodeValueStr;         // formatted node value
		public int level,column;
		public AVLNodeShadow left, right;
		public int height;

		public AVLNodeShadow()
		{}
	}

    // build a shadow tree that is used for tree display
    private AVLNodeShadow buildShadowTree(AVLNode<T> t,
    													int level)
    {
        // new shadow tree node
        AVLNodeShadow newNode = null;
        // text and ostr used to perform format conversion
        String text;

        if (t != null)
        {
            // create the new shadow tree node
            newNode = new AVLNodeShadow();

            // allocate node for left child at next level in
            // tree; attach node
            AVLNodeShadow newLeft =
            	buildShadowTree(t.left, level+1);
            newNode.left = newLeft;

            // initialize variables of the new node
            text = (t.nodeValue).toString(); // format conversion
            newNode.nodeValueStr = text;
            newNode.height = t.height;
            newNode.level = level;
            newNode.column = AVLNodeShadow.columnValue;

            // update column to next cell in the table
            AVLNodeShadow.columnValue++;

            // allocate node for right child at next level in
            // tree; attach node
            AVLNodeShadow newRight =
            	buildShadowTree(t.right, level+1);
            newNode.right = newRight;
        }
        return newNode;
    }

    // remove the shadow tree used for tree display
    private AVLNodeShadow clearShadowTree(AVLNodeShadow t)
    {
      // if current root node is not null, delete its left
      // subtree, its right subtree and then the node itself
      if (t != null)
      {
         t.left = clearShadowTree(t.left);
         t.right = clearShadowTree(t.right);
         return null;
      }
      else
         return null;
   }

   private String displayShadowTree(AVLNode<T> t,
   											int maxCharacters)
   {
      String displayStr = "";
      int level = 0, column = 0;
      int colWidth = maxCharacters + 4;
      int currLevel = 0, currCol = 0;

      AVLNodeShadow.columnValue = 0;


      if (t == null)
         return new String();

      // build the shadow tree
      AVLNodeShadow shadowRoot = buildShadowTree(t, level);

      // use during the level order scan of the shadow tree
      AVLNodeShadow currNode;

      // store siblings of each AVLNodeShadow object in a queue
      // so that they are visited in order at the next level of
      // the tree
      LinkedQueue<AVLNodeShadow> q =
      	new LinkedQueue<AVLNodeShadow>();

      // insert the root in the queue and set current level to 0
      q.push(shadowRoot);

      // continue the iterative process until the queue is empty
      while(!q.isEmpty())
      {
         // delete front node from queue and make it the current
         // node
         currNode = q.pop();

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

         // if a right child exists, insert the child in queue
         if(currNode.right != null)
            q.push(currNode.right);

         // output formatted node label
         if (currNode.column > currCol)
         {
            displayStr += formatString(
            		(currNode.column-currCol) * colWidth, " ");
            currCol = currNode.column;
         }
         displayStr += formatString(
         			colWidth, currNode.nodeValueStr)
                + "(" + (heightShadow(currNode.left) -
                heightShadow(currNode.right) + ")");
         currCol++;
      }

      // delete the shadow tree
      shadowRoot = clearShadowTree(shadowRoot);
      return displayStr;
   }

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
	private static <T> AVLNodeShadow
	buildShadowTreeD(AVLNode<T> t, int level)
	{
		// new shadow tree node
		AVLNodeShadow newNode = null;
		// TShadowNode
		String str;

		if (t != null)
		{
			// create the new shadow tree node
			newNode = new AVLNodeShadow();

			// allocate node for left child at next level in tree;
			// attach node
			AVLNodeShadow newLeft = buildShadowTreeD(t.left, level+1);
			newNode.left = newLeft;

			// initialize variables of the new node
			str = (t.nodeValue).toString(); // format conversion
			newNode.nodeValueStr = str;
			newNode.level = level;
			newNode.column = AVLNodeShadow.columnValue;

			// update column to next cell in the table
			AVLNodeShadow.columnValue++;

			// allocate node for right child at next level in tree;
			// attach node
			AVLNodeShadow newRight =
				buildShadowTreeD(t.right, level+1);
			newNode.right = newRight;
		}

		return newNode;
	}

   /**
    * Displays this binary search tree in a graphical window and then disposes of
    * the window; the argument <tt>maxCharacters</tt> specifies an upper bound
    * on the string length of the node values. The height of each node
    * is enclosed in parentheses.
    * @param maxCharacters an upper bound on the string length of the node values..
    */
	public void drawTree(int maxCharacters)
	{
		AVLNodeShadow.columnValue = 0;
		drawTreeFrame(maxCharacters+1, true);
	}

   /**
    * Displays this binary binary tree in a graphical window but keeps active the window for
    * a new frame the becomes available after pressing the <tt>ENTER</tt> key; the
    * argument <tt>maxCharacters</tt> specifies an upper bound on the string
    * length of the node values.
	 * The height of each node is enclosed in parentheses.
	 * @param maxCharacters an upper bound on the string length of the node values..
    */
	public void drawTrees(int maxCharacters)
	{
		AVLNodeShadow.columnValue = 0;
		drawTreeFrame(maxCharacters+1, false);
	}

	private void drawTreeFrame(int maxCharacters,
										boolean simpleDraw)
	{
		// approximate width of a character drawn by textShape
		final double UNITS_PER_CHAR = .17;

		// estimated width of a formatted node value.
		// add 2 to allow space outside the label
		double cellSide = maxCharacters*UNITS_PER_CHAR + 0.2;

		String label;

		int level = 0, column = 0;

		// build the shadow tree
		AVLNodeShadow shadowRoot = buildShadowTreeD(root, level);

		// use during the level order scan of the shadow tree
		AVLNodeShadow currNode;

		// x, y coordinates of a circle center on the screen
		double x=0, childx, y=0, childy;

		// open the drawing window
		DrawTools.openWindow();

		// store siblings of each ShadowTreeNode object in a queue
		// so that they are visited in order at the next level of
		// the tree
		LinkedQueue<AVLNodeShadow> q =
			new LinkedQueue<AVLNodeShadow>();

		// insert the root in the queue
		q.push(shadowRoot);

		// continue the iterative process until the queue is empty
		while(!q.isEmpty())
		{
			// delete front node from queue and make it the current
			// node
			currNode = q.pop();

			// assign formatted node label to string label
			label = (String)currNode.nodeValueStr;

			// convert each (level, column) coordinate into screen
			// coordinates for the center of the node. add .1 so
			// we stay away from the edges of the screen
			x = currNode.column * cellSide + cellSide/2.0 + 0.1;
			y = currNode.level * cellSide + cellSide/2.0 + 0.1;

			// if a left child exists, draw an edge from the current
			// node center to the child node center. insert the
			// child in the queue
			if(currNode.left != null)
			{
				// edge.move(x, y);
				// compute the center of the left child node
				childx = currNode.left.column * cellSide +
						cellSide/2.0 + 0.1;
				childy = currNode.left.level * cellSide +
						cellSide/2.0 + 0.1;
				// edge.setEndPoint(childx, childy);
				LineShape edge =
					new LineShape(x, y, childx, childy, Shape.BLACK);
				edge.draw();
				q.push(currNode.left);
			}

			// if a right child exists, draw an edge from the
			// current node center to the child node center. insert
			// the child in the queue
			if(currNode.right != null)
			{
				// edge.move(x, y);
				// compute the center of the right child node
				childx = currNode.right.column * cellSide +
						cellSide/2.0 + 0.1;
				childy = currNode.right.level * cellSide +
						cellSide/2.0 + 0.1;
				// edge.setEndPoint(childx, childy);
				LineShape edge =
					new LineShape(x, y, childx, childy, Shape.BLACK);
				edge.draw();
				q.push(currNode.right);
			}

			// draw the current node
			// node.move(x,y);
			CircleShape node = new CircleShape(
				x,y,(cellSide + 0.1)/2.0, Shape.LIGHTGRAY);
			node.draw();

			// draw the node data. use an appropriate offset from the node
			// center so the text is approximately aligned in the node
			int balanceFactor = heightShadow(currNode.left) -
					heightShadow(currNode.right);
			// text.move(x, y + 0.1);
			label = label + " (" + balanceFactor + ")";
			TextShape text = new TextShape(x, y,label, Shape.BLACK);
			// text.setText(label);
			text.draw();
		}
		DrawTools.viewWindow();

		if (simpleDraw)
			DrawTools.closeWindow();
		else
			DrawTools.eraseWindow();
	}

   private static int heightShadow(AVLNodeShadow t)
   {
       if (t == null)
           return -1;
       else
           return t.height;
   }
}