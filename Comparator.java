/*
 * @(#)Comparator.java
 */

package ds.util;

/**
 * A comparison function, which imposes a <i>total ordering</i> on some
 * collection of objects.  Comparators can be passed to a sort method (such as
 * <tt>Arrays.sort</tt> and <tt>Heaps.heapsort</tt>) to allow precise control
 * over the sort order. The <tt>compare</tt> method returns an integer value that
 * is negative, zero, or positive indicating the relative order to the two arguments.
 *
 * @see Arrays
 * @see Heaps
 * @see Less
 * @see Greater
 */

public interface Comparator<T>
{
    /**
     * Compares its two arguments for order.  Returns a negative integer,
     * zero, or a positive integer indicating whether the first argument is &quot;
     * less than&quot, &quot;equal to&quot, or &quot;greater than&quot the second
     * argument respectively.<p>
     *
     * @param x the first object to be compared.
     * @param y the second object to be compared.
     * @return a negative integer if the first argument is &quot;less than&quot
     *         the second argument.<br>
     *         zero if the two arguments are &quot;equal&quot.<br>
     *         a positive integer if the first argument is &quot;
     *         greater than&quot the second argument.
     */
    int compare(T x, T y);
}
