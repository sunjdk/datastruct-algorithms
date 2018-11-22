/*
 * @(#)BinaryTree.java
 */

package ds.util;

import java.awt.*;
import ds.time.Time24;
import ds.graphics.Shape;
import ds.graphics.*;

/**
 * This class contains methods for the classic binary tree traversal algorithms,
 * console and graphical tree display methods, and miscellaneous methods to build
 * binary trees.
 */

public class BinaryTree
{
   /**
    * Builds three different binary trees with <tt>Character</tt> data and returns
    * the root;  the integer argument n in the range 0 to 2 designates the tree.
    * @param n the choice of Tree0, Tree1, or Tree2.
    * @return the root of the specified tree.
    */
	public static TNode<Character> buildTree(int n)
	{
	   // 9 TNode references; point to the 9 items in the tree
	   TNode<Character> root= null, b, c, d, e, f, g, h, i;

	   // parameter n specifies a tree in the range 0 - 2
	   switch(n)
	   {
	      // nodes d and e are leaf nodes
	      case 0:
	         d = new TNode<Character>('D');
	         e = new TNode<Character>('E');
	         b = new TNode<Character>('B',null, d);
	         c = new TNode<Character>('C',e, null);
	         root = new TNode<Character>('A', b, c);
	         break;

	      // nodes g, h, i, and d are leaf nodes
	      case 1:
	         g = new TNode<Character>('G');
	         h = new TNode<Character>('H');
	         i = new TNode<Character>('I');
	         d = new TNode<Character>('D');
	         e = new TNode<Character>('E', g, null);
	         f = new TNode<Character>('F', h, i);
	         b = new TNode<Character>('B', d, e);
	         c = new TNode<Character>('C', null, f);
	         root = new TNode<Character>('A',b, c);
	         break;

	      // nodes g, h, i and f are leaf nodes
	      case 2:
	         g = new TNode<Character>('G');
	         h = new TNode<Character>('H');
	         i = new TNode<Character>('I');
	         d = new TNode<Character>('D', null, g);
	         e = new TNode<Character>('E', h, i);
	         f = new TNode<Character>('F');
	         b = new TNode<Character>('B', d, null);
	         c = new TNode<Character>('C', e, f);
	         root = new TNode<Character>('A',b, c);
	         break;
	   }

	   return root;
	}

   /**
    * Builds a 9-node binary trees with <tt>Time24</tt> data and returns
    * the root.
    * @return the root of the tree.
    */
   public static TNode<Time24> buildTime24Tree()
   {
      // 9 TNode references; point to the 9 items in the tree
      TNode<Time24> root= null, b, c, d, e, f, g, h, i;

		g = new TNode<Time24>(new Time24(5,15));
		h = new TNode<Time24>(new Time24(7,30));
		i = new TNode<Time24>(new Time24(9,15));
		d = new TNode<Time24>(new Time24(10,45), null, g);
		e = new TNode<Time24>(new Time24(12,00), h, i);
		f = new TNode<Time24>(new Time24(15,30));
		b = new TNode<Time24>(new Time24(18,35), d, null);
		c = new TNode<Time24>(new Time24(20,55), e, f);
		root = new TNode<Time24>(new Time24(3,15),b, c);

      return root;
   }

   /**
    * Returns a string that displays the elements in the binary tree using
    * the preorder (NLR) scan.  The description is a listing of
    * elements separated by two blanks.
    * @param t a <tt>TNode</tt> that designates the root of the tree.
    * @return string that contains the list of elements in the array.
    */
   public static String preorderDisplay(TNode<?> t)
   {
		// return value
		String s = "";

      // the recursive scan terminates on a empty subtree
      if (t != null)
      {
         s += t.nodeValue + "  ";  			// display the node
         s += preorderDisplay(t.left);		// descend left
         s += preorderDisplay(t.right);	// descend right
      }

      return s;
   }

   /**
    * Returns a string that displays the elements in the binary tree using
    * the inorder (LNR) scan.  The description is a listing of
    * elements separated by two blanks.
    * @param t a <tt>TNode</tt> that designates the root of the tree.
    * @return string that contains the list of elements in the array.
    */
	public static String inorderDisplay(TNode<?> t)
	{
		// return value
		String s = "";

		// the recursive scan terminates on a empty subtree
		if (t != null)
		{
			s += inorderDisplay(t.left);	// descend left
			s += t.nodeValue + "  ";  		// display the node
			s += inorderDisplay(t.right);	// descend right
		}

		return s;
	}

   /**
    * Returns a string that displays the elements in the binary tree using
    * the postorder (LRN) scan.  The description is a listing of
    * elements separated by two blanks.
    * @param t a <tt>TNode</tt> that designates the root of the tree.
    * @return string that contains the list of elements in the array.
    */
	public static String postorderDisplay(TNode<?> t)
	{
		// return value
		String s = "";

		// the recursive scan terminates on a empty subtree
		if (t != null)
		{
			s += postorderDisplay(t.left);	// descend left
			s += postorderDisplay(t.right);	// descend right
			s += t.nodeValue + "  ";  			// display the node
		}

		return s;
	}

   /**
    * Returns a string that displays the elements in the binary tree using
    * the level order scan of the nodes. The description is a listing of
    * elements separated by two blanks.
    * @param t a <tt>TNode</tt> that designates the root of the tree.
    * @return string that contains the list of elements in the array.
    */
	public static <T> String levelorderDisplay(TNode<T> t)
	{
		// store siblings of each node in a queue so that they are
		// visited in order at the next level of the tree
		LinkedQueue<TNode<T>> q = new LinkedQueue<TNode<T>>();
		TNode<T> p;
		// return value
		String s = "";

		// initialize the queue by inserting the root in the queue
		q.push(t);

		// continue the iterative process until the queue is empty
		while(!q.isEmpty())
		{
			// delete a node from queue and output the node value
			p = q.pop();
			s += p.nodeValue + "  ";

			// if a left child exists, insert it in the queue
			if(p.left != null)
				q.push(p.left);
			// if a right child exists, insert next to its sibling
			if(p.right != null)
				q.push(p.right);
		}

		return s;
	}

   /**
    * Returns the height of the tree specified by the root <tt>t</tt> using a postorder scan.
    * @param t a <tt>TNode</tt> that designates the root of the tree.
    * @return the heigth of the tree.
    */
	public static <T> int height(TNode<T> t)
	{
		int heightLeft, heightRight, heightval;

		if (t == null)
			// height of an empty tree is -1
			heightval = -1;
		else
		{
			// find the height of the left subtree of t
			heightLeft= height(t.left);
			// find the height of the right subtree of t
			heightRight= height(t.right);
			// height of the tree with root t is 1 + maximum
			// of the heights of the two subtrees
			heightval = 1 +
				(heightLeft > heightRight ? heightLeft : heightRight);
		}

		return heightval;
	}

   /**
    * Creates a duplicate of the tree with root <tt>t</tt> and returns
	 * a reference to the root of the copied tree.
    * @param t a <tt>TNode</tt> that designates the root of the tree.
    * @return the root of the copied tree.
    */
	// create a duplicate of the tree with root t and return
	// a reference to its root
	public static <T> TNode<T> copyTree(TNode<T> t)
	{
		// newNode points at a new node that the algorithm
		// creates. newLptr. and newRptr point to the subtrees
		// of newNode
		TNode<T> newLeft, newRight, newNode;

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
		newNode = new TNode<T> (t.nodeValue, newLeft, newRight);

		// return a reference to the root of the newly copied tree
		return newNode;
	}

	/**
    * Creates the tree with root <tt>t</tt> by setting each of the
    * node to <tt>null</tt>.
    * @param t a <tt>TNode</tt> that designates the root of the tree.
    */
	public static <T> void clearTree(TNode<T> t)
	{
		// postorder scan. delete left and right
		// subtrees of t and then node t
		if (t != null)
		{
			clearTree(t.left);
			clearTree(t.right);
			t = null;
		}
	}

   /**
    * Builds binary expression tree that corresponds to operators and
    * operands in a postfix expression.
    * @param postfixExp	a string specifying a postfix expression.
    * @return the root of the tree.
    */
	public static TNode<Character> buildExpTree(String postfixExp)
	{
		// newNode is a reference to the root of subtrees
		// we build, and newLeft/newRight its are its
		// children
		TNode<Character> newNode, newLeft, newRight;
		char token;
		// subtrees go into and off the stack
		ALStack<TNode<Character>> s= new ALStack<TNode<Character>>();
		int i = 0, n = postfixExp.length();

		// loop until i reaches the end of the string
		while(i != n)
		{
			// skip blanks and tabs in the expression
			while (postfixExp.charAt(i) == ' ' ||
					 postfixExp.charAt(i) == '\t')
				i++;

			// if the expression has trailing whitespace, we could
			// be at the end of the string
			if (i == n)
				break;

			// extract the current token and increment i
			token = postfixExp.charAt(i);
			i++;

			// see if the token is an operator or an operand
			if (token == '+' || token == '-' ||
				 token == '*' || token == '/')
			{
				// current token is an operator. pop two subtrees off
				// the stack.
				newRight = s.pop();
				newLeft = s.pop();

				// create a new subtree with token as root and subtees
				// newLeft and newRight and push it onto the stack
				newNode =
					new TNode<Character>(token,newLeft,newRight);
				s.push(newNode);
			}
			else // must be an operand
			{
				// create a leaf node and push it onto the stack
				newNode = new TNode<Character>(token);
				s.push(newNode);
			}
		}

		// if the expression was not empty, the root of the expression
		// tree is on the top of the stack
		if (!s.isEmpty())
			return s.pop();
		else
			return null;
	}

	/**
	 * Traverses an expression tree and returns a string that displays
	 * the equivalent fully parenthesized expression.
	 * @param t a <tt>TNode<Character></tt> that designates the root of the
	 * expression tree.
	 * @return a string that displays the equivalent fully parenthesized expression.
	 */
	public static <T> String fullParen(TNode<Character> t)
	{
		String s = "";
	
		if (t != null)
		{
			if (t.left == null && t.right == null)
				s += t.nodeValue;			// visit a leaf node
			else
			{
				s += "(";					// visit on left
				s += fullParen(t.left);
				s += t.nodeValue;			// visit from below
				s += fullParen(t.right);
				s += ")";					// visit on right
			}
		}
	
		return s;
	}

	// recursive inorder scan used to build the shadow tree
	private static <T> TNodeShadow buildShadowTree(TNode<T> t, int level)
	{
		// reference to new shadow tree node
		TNodeShadow newNode = null;

		if (t != null)
		{
			// create the new shadow tree node
			newNode = new TNodeShadow();

			// allocate node for left child at next level in tree; attach node
			newNode.left = buildShadowTree(t.left, level+1);

			// initialize variables of the new node
			// format conversion
			newNode.nodeValueStr = t.nodeValue.toString();
			newNode.level = level;
			newNode.column = TNodeShadow.columnValue;

			// update column to next cell in the table
			TNodeShadow.columnValue++;

			// allocate node for right child at next level in tree; attach node
			newNode.right = buildShadowTree(t.right, level+1);
		}

		return newNode;
	}

   /**
    * Returns a string that gives a level-order display of a binary tree; the
    * argument <tt>maxCharacters</tt> specifies an upper bound on the string
    * length of the node values.
    * @param t a <tt>TNode</tt> that designates the root of the tree.
    * @param maxCharacters an upper bound on the string length of the node values..
    * @return string that gives a level-order display of a binary tree.
    */
	public static <T> String displayTree(TNode<T> t, int maxCharacters)
	{
		// use for the level-order scan of the shadow tree
		LinkedQueue<TNodeShadow> q =
				new LinkedQueue<TNodeShadow>();
		String displayStr = "";
		int colWidth = maxCharacters + 1;
		int currLevel = 0, currCol = 0;

		TNodeShadow.columnValue = 0;

		if (t == null)
			return displayStr;

		// build the shadow tree
		TNodeShadow shadowRoot = buildShadowTree(t, 0);

		// use during the level order scan of the shadow tree
		TNodeShadow currNode;

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
				displayStr += '\n';
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
				displayStr +=
					formatChar((currNode.column-currCol) * colWidth, ' ');
				currCol = currNode.column;
			}

			displayStr += formatString(colWidth, currNode.nodeValueStr);
			currCol++;
		}

		displayStr += '\n';

		// delete the shadow tree
		shadowRoot = clearShadowTree(shadowRoot);

		return displayStr;
	}

   private static TNodeShadow clearShadowTree(TNodeShadow t)
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

	private static String formatChar(int w, char ch)
	{
		String str = "";

		// append w - sLength blanks to str
		for (int i=0;i < w - 1;i++)
			str += " ";
		// append ch after the leading blanks
		str += ch;

		// return the formatted string
		return str;
	}

	// build a shadow tree that is used for tree display
	private static <T> TNodeShadow buildShadowTreeD(TNode<T> t, int level)
	{
		// new shadow tree node
		TNodeShadow newNode = null;
		// TShadowNode
		String str;;

		if (t != null)
		{
			// create the new shadow tree node
			newNode = new TNodeShadow();

			// allocate node for left child at next level in tree; attach node
			TNodeShadow newLeft = buildShadowTreeD(t.left, level+1);
			newNode.left = newLeft;

			// initialize variables of the new node
			str = (t.nodeValue).toString(); // format conversion
			newNode.nodeValueStr = str;
			newNode.level = level;
			newNode.column = TNodeShadow.columnValue;

			// update column to next cell in the table
			TNodeShadow.columnValue++;

			// alloc node for right child at next level in tree; attach node
			TNodeShadow newRight = buildShadowTreeD(t.right, level+1);
			newNode.right = newRight;
		}

		return newNode;
	}

   /**
    * Displays a single binary tree in a graphical window and then disposes of
    * the window; the argument <tt>maxCharacters</tt> specifies an upper bound
    * on the string length of the node values.
    * @param t a <tt>TNode</tt> that designates the root of the tree.
    * @param maxCharacters an upper bound on the string length of the node values..
    */
	public static <T> void drawTree(TNode<T> t, int maxCharacters)
	{
		TNodeShadow.columnValue = 0;
		drawTreeFrame(t, maxCharacters+1, true);
	}

   /**
    * Displays a binary tree in a graphical window but keeps active the window for
    * a new frame the becomes available after pressing the <tt>ENTER</tt> key; the
    * argument <tt>maxCharacters</tt> specifies an upper bound on the string
    * length of the node values.
    * @param t a <tt>TNode</tt> that designates the root of the tree.
    * @param maxCharacters an upper bound on the string length of the node values..
    */
	public static <T> void drawTrees(TNode<T> t, int maxCharacters)
	{
		TNodeShadow.columnValue = 0;
		drawTreeFrame(t,maxCharacters+1, false);
	}

	private static <T> void drawTreeFrame(TNode<T> t, int maxCharacters,
												 boolean simpleDraw)
	{
		// approximate width of a character drawn by textShape
		final double UNITS_PER_CHAR = .15;

		// estimated width of a formatted node value.
		// add 2 to allow space outside the label
		double cellSide = maxCharacters*UNITS_PER_CHAR + 0.2;

		String label;

		int level = 0, column = 0;

		// build the shadow tree
		TNodeShadow root = buildShadowTreeD(t, level);

		// use during the level order scan of the shadow tree
		TNodeShadow currNode;

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
		LinkedQueue<TNodeShadow> q =
			new LinkedQueue<TNodeShadow>();

		// insert the root in the queue
		q.push(root);

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
			x = currNode.column * cellSide + cellSide/2.0 + 0.1;
			y = currNode.level * cellSide + cellSide/2.0 + 0.1;

			// if a left child exists, draw an edge from the current
			// node center to the child node center. insert the child
			// in the queue
			if(currNode.left != null)
			{
				//edge.move(x, y);
				// compute the center of the left child node
				childx = currNode.left.column * cellSide + cellSide/2.0 + 0.1;
				childy = currNode.left.level * cellSide + cellSide/2.0 + 0.1;
				//edge.setEndPoint(childx, childy);
				LineShape edge = new LineShape(x, y, childx, childy, Shape.BLACK);
				edge.draw();
				q.push(currNode.left);
			}

			// if a right child exists, draw an edge from the current
			// node center to the child node center. insert the child
			// in the queue
			if(currNode.right != null)
			{
				//edge.move(x, y);
				// compute the center of the right child node
				childx = currNode.right.column * cellSide + cellSide/2.0 + 0.1;
				childy = currNode.right.level * cellSide + cellSide/2.0 + 0.1;
				//edge.setEndPoint(childx, childy);
				LineShape edge = new LineShape(x, y, childx, childy, Shape.BLACK);
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

	private static class TNodeShadow
	{
	   public static int columnValue;
	   public String nodeValueStr;         // formatted node value
	   public int level, column;
	   public TNodeShadow left, right;

	   public TNodeShadow ()
	   {}
	}
}
