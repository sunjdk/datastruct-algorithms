/*
 * @(#)OrderedMap.java
 */

package ds.util;

/**
 * An interface that extends <tt>Map<K,V></tt> by defining the methods
 * <tt>firstKey</tt> and <tt>lastKey</tt> that return the
 * minimum and maximum key in the collection of key-value pairs;
 * an iterator scan the entries in the collection is ascending order
 * of their key.<p>
 */

public interface OrderedMap<K,V> extends Map<K,V>
{
    /**
     * Returns the first (minimum) key in this ordered map.
     *
     * @return the first (minimum) key in this ordered map.
     * @throws    NoSuchElementException if the ordered map is empty.
     */
	K firstKey();

    /**
     * Returns the first (maximum) key in this ordered map.
     *
     * @return the first (maximum) key in this ordered map.
     * @throws    NoSuchElementException if the ordered map is empty.
     */
	K lastKey();
}
