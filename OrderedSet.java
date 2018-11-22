/*
 * @(#)OrderedSet.java
 */

package ds.util;

/**
 * An interface that extends <tt>Set<T></tt> by defining the methods
 * <tt>first</tt> and <tt>last</tt> that return the
 * minimum and and maximum value in the collection;  an iterator scans the
 * elements in the collection is ascending order.<p>
 */

public interface OrderedSet<T> extends Set<T>
{
    /**
     * Returns the first (minimum) element currently in this ordered set.
     *
     * @return the first (minimum) element currently in this ordered set.
     * @throws    NoSuchElementException ordered set is empty.
     */
	public T first();

    /**
     * Returns the last (maximum) element currently in this ordered set.
     *
     * @return the last (maximum) element currently in this ordered set.
     * @throws    NoSuchElementException ordered set is empty.
     */
	public T last();
}
