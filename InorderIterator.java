/*
 * @(#)InorderIterator.java
 */

package ds.util;

import java.util.NoSuchElementException;
import java.lang.NoSuchMethodException;
/**
 * This class is partial implementation of an <tt>Iterator</tt> that produces
 * an iterative inorder scan of the elements in a binary tree specified by
 * a <tt>TNode</tt> reference root. The class implements the methods
 * <tt>hasNext</tt> and <tt>next</tt> but does not provide an implementation
 * for the <tt>remove</tt> method. <p>
 *
 * The implementation provides a model for the <tt>STree</tt> and <tt>TreeSet</tt>,
 * iterators.
 */

public class InorderIterator<T> implements Iterator<T>
{
	private ALStack<TNode<T>> s = null;
	private TNode<T> curr = null;

	// go far left from t, pushing
	// all the nodes with left children
	// on stack s
	private TNode<T> goFarLeft(TNode<T> t)
	{
		if (t == null)
			return null;
		while (t.left != null)
		{
			s.push(t);
			t = t.left;
		}
		return t;
	}

	/**
	 * Creates an <tt>InorderIterator</tt> positioned at the left-most element
	 * in the binary tree.
	 */
	public InorderIterator(TNode<T> root)
	{
		s = new ALStack<TNode<T>>();
		curr = goFarLeft(root);
	}

    /**
     * Returns <tt>true</tt> if the iteration has more elements.
     *
     * @return <tt>true</tt> if the iterator has more elements.
     */
	 public boolean hasNext()
	{
		return curr != null;
	}

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration.
     * @throws NoSuchElementException if the iteration has no more elements.
     */
	public T next()
	{
		if (curr == null)
			throw new
				NoSuchElementException("InorderIterator: no elements remaining");

		// capture the value in the node
		T returnValue = curr.nodeValue;

		if (curr.right != null)	// have a right subtree
			// stack nodes on left subtree
			curr = goFarLeft(curr.right);
		else if (!s.isEmpty())
			// no right subtree there are other nodes
			// to visit. pop the stack
			curr = (TNode<T>)s.pop();
		else
			curr = null;			// end of tree; set curr to null

		return returnValue;
	}

    /**
     * This </tt>Iterator</tt> method is not implemented.
     *
     * @throws UnsupportedOperationException if it is called.
     */
	public void remove()
	{
		// no implementation
		throw new UnsupportedOperationException (
		                "InorderIterator remove(): not implemented");
	}
}
