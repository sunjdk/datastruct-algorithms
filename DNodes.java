/*
 * @(#)DNodes.java
 */

package ds.util;

/**
 * This class contains methods for adding and removing a node from a doubly linked list;
 * it also has the method <tt>toString</tt> that describes the elements in the list.<p>
 */
public class DNodes
{
   /**
    * Returns a string representation of the doubly-linked list specified
    * by <tt>header</tt>. The representation is a comma separated list
    * from front to back, enclosed in square brackets.
    * @param header <tt>DNode</tt> reference that identifies the list.
    * @return a string that specifies the elements in the list.
    */
	public static <T> String toString(DNode<T> header)
	{
		if (header.next == header)
			return "null";

		// scan list starting at the first node; add value to string
		DNode<T> curr = header.next;
		String str = "[" + curr.nodeValue;

		// append all but last node, separating items with a comma
		// polymorphism calls toString() for the nodeValue type
		while(curr.next != header)
		{
			curr = curr.next;
			str +=  ", " + curr.nodeValue;
		}
		str += "]";
		return str;
	}

	/**
	 *  Deletes the DNode referenced by curr.
    *  @param curr <tt>DNode</tt> reference that identifies the list.
	 */
	public static <T> void remove(DNode<T> curr)
	{
		// return if the list is empty
		if (curr.next == curr)
			return;

		// declare references for the predecessor and successor nodes
		DNode<T> prevNode = curr.prev, succNode = curr.next;

		// update reference fields for predecessor and successor
		prevNode.next = succNode;
		succNode.prev = prevNode;
	}

	/**
	 *  Adds a <tt>DNode</tt> with value item immediately before the
	 *  the  element referenced by curr.
    *  @param curr <tt>DNode</tt> reference that identifies the list.
    *  @return the new <tt>DNode</tt> that is added to the list.
	 */
	public static <T> DNode<T> addBefore(DNode<T> curr, T item)
	{
		// declare reference variables for new node and previous node
		DNode<T> newNode, prevNode;

		// create new DNode with item as initial value
		newNode = new DNode<T>(item);

		// assign prevNode the reference value of node before p
		prevNode = curr.prev;

		// update reference fields in newNode
		newNode.prev = prevNode;
		newNode.next = curr;

		// update curr and prevNode to point at newNode
		prevNode.next = newNode;
		curr.prev = newNode;

		return newNode;
	}
}