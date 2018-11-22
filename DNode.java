/*
 * @(#)DNode.java
 */

package ds.util;

/**
 * DNode objects are the elements in a doubly linked list; they have three fields
 * that designate the value and links to the successor node and to the predecessor
 * node. The value field <tt>nodeValue</tt> has a specified type and the link fields
 * <tt>next</tt> and <tt>prev</tt> identify the adjacent nodes in the sequence<p>
 * A circular doubly linked list with a sentinel is the underlying storage structure
 * for the LinkedList class. The sentinel is the DNode <tt>header</tt>. A sequential scan
 * of the list in the forward direction begins at <tt>header.next</tt> (the first node)
 * and moves up the list using the link field <tt>next</tt> until the scan reaches the
 * header node. A sequential scan in the backward direction begins at
 * <tt>header.prev</tt> (the last node) and moves down the list until the scan reaches
 * the header node.<p>
 *
 * The variables are public since DNode objects are used only in implementation
 * structures.<p>
 *
 * @see	    DNodes
 */

public class DNode<T>
{
   /**
    * The value field in a node with a specified type
    */
    public T nodeValue;


   /**
    * The reference fields that identify the successor and predecessor nodes
    */
   public DNode<T> prev;		// previous node in the list
   public DNode<T> next;		// next node in the list


	/**
	 * Creates an instance with both the <tt>nodeValue</tt> and
	 * the <tt>next</tt> field having initial value null.
	 */
   public DNode()
   {
      nodeValue = null;
      next = this;   // the next node is the current node
      prev = this;   // the previous node is the current node
   }

   // creates object whose value is item and whose references
	// point to the node itself
	/**
	 * Creates an instance with the specified item as the <tt>nodeValue</tt> but
	 * with the <tt>next</tt> and <tt>prev</tt> fields set to null.
	 */
   public DNode(T item)
   {
      nodeValue = item;
      next = this;   // the next node is the current node
      prev = this;   // the previous node is the current node
   }
}
