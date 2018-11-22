/*
 * @(#)Node.java
 */

package ds.util;

/**
 * Node objects are the elements in a singly linked list; they have two fields
 * that designate the value and a link to the successor node.
 * The value field <tt>nodeValue</tt> has a specified type and the link field
 * <tt>next</tt> identifies the next node in the sequence<p>
 * A sequential scan of the
 * list begins at <tt>front</tt> (first node) and moves forward using the
 * link field <tt>next</tt>.  The link field for the last node in the sequence
 * is null.<p>
 *
 * The variables are public since Node objects are used only in implementation
 * structures.<p>
 *
 * @see	    Nodes
 */

public class Node<T>
{
   /**
    * The value field in a node with a specified type.
    */
   public T nodeValue;

   /**
    * The reference field that identifies the successor node.
    */
   public Node<T> next;

	/**
	 * Creates an instance with both the <tt>nodeValue</tt> and
	 * the <tt>next</tt> field having initial value null.
	 */
   public  Node()
   {
      nodeValue = null;
      next = null;
   }

	/**
	 * Creates an instance with the specified item as its value but with the
	 * <tt>next</tt> field set to null.
	 */
   public Node(T item)
   {
      nodeValue = item;
      next = null;
   }
}
