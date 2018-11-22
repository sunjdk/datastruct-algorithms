/*
 * @(#)Heaps.java
 */

package ds.util;

import ds.util.LinkedQueue;
import java.awt.*;
import ds.graphics.Shape;
import ds.graphics.*;

/**
 * This class contains the heap methods <tt>popHeap</tt>,<tt>pushHeap</tt>, and
 * <tt>heapsort</tt>. <p>
 *
 * @see     Comparator
 */

public class Heaps
{
	/** Assuming the array elements in the range [0, last-1) are a heap, the method
	 *  inserts item into the heap so that the range [0, last) is
	 *  a heap; the <tt>Comparator</tt> comp performs the comparisons.
	 *
	 *  @param arr	the heap array
	 *  @param last upper bound for the heap after item is added.
	 *  @param comp  <tt>Comparator</tt> that specifies ordering among the elements.
	 */
	public static <T> void pushHeap(T[] arr, int last, T item,
	   									   Comparator<? super T> comp)
	{
	   // assume the new item is at location arr[last] and that
	   // the elements arr[0] to arr[last-1] are in heap order
	   int currPos, parentPos;

	   // currPos is an index that traverses path of parents.
	   // item is assigned in the path
	   currPos = last;
	   parentPos = (currPos-1)/2;

	   // traverse path of parents up to the root
	   while (currPos != 0)
	   {
	      // compare target and parent value
	      if (comp.compare(item,arr[parentPos]) < 0)
	      {
	         // move data from parent position to current
	         // position. update current position to parent
	         // position. compute next parent
	         arr[currPos] = arr[parentPos];
	         currPos = parentPos;
	         parentPos = (currPos-1)/2;
	      }
	      else
	         // heap condition is ok. break
	         break;
	   }
	   // the correct location has been discovered. assign target
	   arr[currPos] = item;
	}

	// filter the array element arr[first] down the heap with index
	// range [first, last)
	public static <T> void adjustHeap(T[] arr, int first, int last,
					   						 Comparator<? super T> comp)
	{
	   int currentPos, childPos;
	   T target;

	   // start at first and filter target down the heap
	   currentPos = first;
	   target = arr[first];

	   // compute the left child index and begin a scan down
	   // path of children, stopping at end of list (last)
	   // or when we find a place for target
	   childPos = 2 * currentPos + 1;
	   while (childPos < last)
	   {
	      // index of right child is childPos+1. compare the
	      // two children. change childPos if
	      // comp.compare(arr[childPos+1], arr[childPos]) < 0
	      if ((childPos+1 < last) &&
	            comp.compare(arr[childPos+1], arr[childPos]) < 0)
	         childPos = childPos + 1;

	      // compare selected child to target
	      if (comp.compare(arr[childPos],target) < 0)
	      {
	         // comp.compare(selected child, target) < 0.
	         // move selected child to the parent;
	         // position of selected child is now vacated
	         arr[currentPos] = arr[childPos];

	         // update indices to continue the scan
	         currentPos = childPos;
	         childPos = 2 * currentPos + 1;
	      }
	      else
	         // target belongs at currentPos
	         break;
	   }
	   arr[currentPos] = target;
	}

	/** Assuming the array elements in the range [0, last) are a heap, the method
	 *  removes the element at index 0 and returns its value; the elements
	 *  in the range [0, last-1) is a heap; the <tt>Comparator comp</tt> performs
	 *  the comparisons.
	 *
	 *  @param arr	the heap array
	 *  @param last upper bound for the heap before removing the first element.
	 *  @param comp  <tt>Comparator</tt> that specifies ordering among the elements.
	 */
	public static <T> T popHeap(T[] arr, int last,
	   								 Comparator<? super T> comp)
	{
	   // element that is popped from the heap
	   T temp = arr[0];

	   // exchange last element in the heap with the deleted
	   // (root) element
	   arr[0] = arr[last-1];
	   arr[last-1] = temp;

	   // filter down the root over the range [0, last-1)
	   adjustHeap(arr, 0, last-1, comp);
	   return temp;
	}

	/** Arranges the array elements into a heap; the <tt>Comparator</tt> comp performs
	 *  the comparisons.
	 *
	 *  @param arr	an array to reordered as a heap.
	 *  @param comp  <tt>Comparator</tt> that specifies ordering among the elements.
	 */
	public static <T>
	void makeHeap(T[] arr, Comparator<? super T> comp)
	{
	   int heapPos, lastPos;

	   // compute the size of the heap and the index
	   // of the last parent
	   lastPos = arr.length;
	   heapPos = (lastPos - 2)/2;

	   // filter down every parent in order from last parent
	   // down to root
	   while (heapPos >= 0)
	   {
	      adjustHeap(arr, heapPos, lastPos, comp);
	      heapPos--;
	   }
	}


   /**
    * The generic heapSort the orders an array of elements of type <tt>T</tt> into
    * ascending order using the <tt>Comparator</tt> comp to provide the natural
    * ordering of elements. If <tt>comp</tt> is an instance of <tt>Greater</tt>,
    * the array is sorted in ascending order. If <tt>comp</tt> is an instance of
    * <tt>Less</tt>the array is sorted in descending order.<p>
    *
    * The sorting algorithm is has runtime efficiency O(n*log(n)) for the average case..
    *
    * @param arr the array to be sorted.
    * @param comp a <tt>Comparator</tt> that provides order for the sort; An instance
    *             of <tt>Greater</tt> sorts in ascending order; an instance of
    *             <tt>Less</tt>sorts in descending order.
    */
	public static <T>
	void heapSort(T[] arr, Comparator<? super T> comp)
	{
		// "heapify" the array  arr
		Heaps.makeHeap(arr, comp);

		int i, n =  arr.length;

		// iteration that determines elements arr[n-1] ... arr[1]
		for(i = n; i > 1;i--)
		{
			// call popHeap() to move next largest to  arr[n-1]
			Heaps.popHeap(arr, i, comp);
		}
	}


	static ArrayShadowTree[] displayArr;

	// build a shadow tree that is used for tree display
	private static void buildArrayShadowTree(Object[] arr, int n, int index, int level)
   {
      // new shadow tree node
      if (displayArr == null)
      	displayArr = new ArrayShadowTree[n];

      // text and ostr used to perform format conversion
      String text;

      if (index < n)
      {
         // allocate element for left child at next level in tree;
         buildArrayShadowTree(arr, n, 2*index + 1, level+1);
         displayArr[index] = new ArrayShadowTree(arr[index].toString(), level,
                   ArrayShadowTree.columnValue, index);

         // update column to next cell in the table
         ArrayShadowTree.columnValue++;

         // allocate node for right child at next level in tree; attach node
         buildArrayShadowTree(arr, n, 2*index + 2, level+1);
      }
   }

    /**
    * Returns a string that gives a level-order display of the heap; the
    * argument <tt>maxCharacters</tt> specifies an upper bound on the string
    * length of the values in the heap.
    * @param arr the array whose sublist [0,n) is a heap.
    * @param n upper bound for the sublist [0,n) which is a heap.
    * @param maxCharacters an upper bound on the string length of the elements
    *                   in the heap.
    * @return string that gives a level-order display of the heap.
    */
  public static String displayHeap(Object[] arr, int n, int maxCharacters)
	{
		String displayStr = "";
		int level = 0, column = 0;
		int colWidth = maxCharacters + 1;
		//
		int currLevel = 0, currCol = 0;

		ArrayShadowTree.columnValue = 0;
		displayArr = null;

		if (arr.length == 0)
			return new String();

		// build the shadow tree
		buildArrayShadowTree(arr, n, 0, level);

		// use during the level order scan of the shadow tree
		ArrayShadowTree curr;

		// store siblings of each ArrayShadowTree object in a queue so that
		// they are visited in order at the next level of the tree
		LinkedQueue<ArrayShadowTree> q = new LinkedQueue<ArrayShadowTree>();

		// insert the root in the queue and set current level to 0
		q.push(displayArr[0]);

		// continue the iterative process until the queue is empty
		while(!q.isEmpty())
		{
			// delete front node from queue and make it the current node
			curr = q.pop();

			// if level changes, output a newline
			if (curr.level > currLevel)
			{
				currLevel = curr.level;
				currCol = 0;
				displayStr += "\n";
			}

			// if a left child exists, insert the child in the queue
			if(2 * curr.index + 1 < n)
				q.push(displayArr[2 * curr.index+1]);

			// if a right child exists, insert the child in the queue
			if(2 * curr.index + 2 < n)
				q.push(displayArr[2 * curr.index+2]);

			// output formatted node label
			if (curr.column > currCol)
			{
				displayStr += formatString((curr.column-currCol) * colWidth, " ");
				currCol = curr.column;
			}
			displayStr += formatString(colWidth, curr.valueStr);
			currCol++;
		}

		// delete the shadow tree
		displayArr = null;
		return displayStr;
	}

	// right-justify s in a field of w print positions
	private static String formatString(int w, String s)
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

   /**
    * Displays a single heap in a graphical window and then disposes of
    * the window; the argument <tt>maxCharacters</tt> specifies an upper bound
    * on the string length of the elements in the heap.
    * @param arr the array whose sublist [0,n) is a heap.
    * @param n upper bound for the sublist [0,n) which is a heap.
    * @param maxCharacters an upper bound on the string length of the elements
    *                   in the heap.
    */
	public static void drawHeap(Object[] arr, int n, int maxCharacters)
	{
		ArrayShadowTree.columnValue = 0;
		drawHeapFrame(arr, n, maxCharacters, true);
	}

   /**
    * Displays a heap in a graphical window but keeps active the window for
    * a new frame the becomes available after pressing the <tt>ENTER</tt> key; the
    * argument <tt>maxCharacters</tt> specifies an upper bound on the string
    * length of the heap values.
    *
    * @param arr the array whose sublist [0,n) is a heap.
    * @param n upper bound for the sublist [0,n) which is a heap.
    * @param maxCharacters an upper bound on the string length of the elements
    *                   in the heap.
    */
	public static void drawHeaps(Object[] arr, int n, int maxCharacters)
	{
		ArrayShadowTree.columnValue = 0;
		drawHeapFrame(arr, n, maxCharacters, false);
	}

	private static void drawHeapFrame(Object[] arr, int n,
				int maxCharacters, boolean simpleDraw)
	{
		// approximate width of a character drawn by textShape
		final double UNITS_PER_CHAR = .15;

		// estimated width of a formatted node value.
		// add 2 to allow space outside the label
		double cellSide = maxCharacters * UNITS_PER_CHAR + 0.2;

		String valueStr;

		int level = 0, column = 0;
		String label;

		// build the shadow tree
		buildArrayShadowTree(arr, n, 0, level);

		// use during the level order scan of the shadow tree
		ArrayShadowTree curr;
		double x = 0, childx, y = 0, childy, maxX = 0.0, maxY = 0.0;

		// open the drawing window
		DrawTools.openWindow();

		// store siblings of each ShadowTreeNode object in a queue so that
		// they are visited in order at the next level of the tree
		LinkedQueue<ArrayShadowTree> q = new LinkedQueue<ArrayShadowTree>();

		TextShape text = null;

		// insert the root in the queue
		q.push(displayArr[0]);

		// continue the iterative process until the queue is empty
		while(!q.isEmpty())
		{
			// delete front node from queue and make it the current node
			curr = q.pop();

			// assign formatted node label to string label
			label = (String)curr.valueStr;

			// convert each (level, column) coordinate into screen
			// coordinates for the center of the node. add .1 so
			// we stay away from the edges of the screen
			x = curr.column * cellSide + cellSide/2.0 + 0.5;
			y = curr.level * cellSide + cellSide/2.0 + 1.5;
			if (maxX < x)
				maxX = x;
			if (maxY < y)
				maxY = y;

			// if a left child exists, draw an edge from the current node center
			// to the child node center. insert the child in the queue
			if(2 * curr.index + 1 < n)
			{
				// edge.move(x, y);
				// compute the center of the left child node
				childx = displayArr[2 * curr.index + 1].column * cellSide + cellSide/2.0 + 0.5;
				childy = displayArr[2 * curr.index + 1].level * cellSide + cellSide/2.0 + 1.5;
				LineShape edge = new LineShape(x,y,childx,childy,Shape.BLACK);
				edge.draw();
				q.push(displayArr[2 * curr.index+1]);
			}

			// if a right child exists, draw an edge from the current
			// node center to the child node center. insert the child
			// in the queue
			if(2 * curr.index + 2 < n)
			{
				// edge.move(x, y);
				// compute the center of the right child node
				childx = displayArr[2 * curr.index + 2].column * cellSide + cellSide/2.0 + 0.5;
				childy = displayArr[2 * curr.index + 2].level * cellSide + cellSide/2.0 + 1.5;
				LineShape edge = new LineShape(x,y,childx,childy,Shape.BLACK);
				edge.draw();
				q.push(displayArr[2 * curr.index+2]);
			}

			// draw the current node
			CircleShape node = new CircleShape(x,y,cellSide/2.0, Shape.LIGHTGRAY);
			node.draw();

			// draw the node data. use an appropriate offset from the node
			// center so the text is approximately aligned in the node
			text = new TextShape(x,y,label, Shape.BLACK);
			text.draw();
		}

		// pause to view the tree
		DrawTools.viewWindow();

		if (simpleDraw)
			DrawTools.closeWindow();
		else
			DrawTools.eraseWindow();
		displayArr = null;
	}

	// objects hold a formatted label string and the level,column
	// coordinates for a shadow tree node
	private static class ArrayShadowTree
	{
		public static int columnValue;
		public String valueStr;         // formatted node value
		public int level,column;
		public int index;

		public ArrayShadowTree (String valueStr, int level, int column, int index)
		{
			this.valueStr = valueStr;
			this.level = level;
			this.column = column;
			this.index = index;
		}
	}
}
