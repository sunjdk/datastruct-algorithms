/*
 * @(#)TNode.java
 */

package ds.util;

/**
 * TNode objects are the elements in a binary tree; they have three fields
 * that designate the value and links to the left and right child.
 * The value  (<tt>nodeValue</tt>) has a specified type and the two link fields
 * <tt>left</tt> and <tt>right</tt> identify the left and right subtrees of the node.<p>
 *
 * A TNode object with both link fields null is a leaf node. A TNode object
 * with at least one nonnull link field is an interior node. <p>
 *
 * TNode objects are the building blocks for binary tree structures. The data
 * members are public.<p>
 *
 *
 * @see	    BinaryTree
 */

public class TNode<T>
{
   /**
   * The value field in a node with a specified type
   */
	public T nodeValue;


   /**
   * The reference fields that identify the left and right child of the node
   */
	public TNode<T> left, right;

	/**
	 * Creates an instance with the specified value and with null
	 * link fields.  The subtrees are empty.
	 */
	public TNode(T item)
	{
		nodeValue = item;
		left = right = null;
	}

	/**
	 * Creates an instance with the specified value and with initial
	 * references for <tt>left</tt> and <tt>right</tt>.  .
	 */
	public TNode (T item, TNode<T> left, TNode<T> right)
	{
		nodeValue = item;
		this.left = left;
		this.right = right;
	}
}
