/*
 * @(#)Sets.java
 */

package ds.util;

/**
 * This class contains static methods for creating the union, intersection, and difference
 * of two sets as well as determining the "subset" relation between two sets.
 */

public class Sets
{
	/**
	 * Creates a new <tt>TreeSet</tt> or <tt>HashSet</tt> object that is the
	 * set of all elements that are either in the first set or the second set. The
	 * method returns a set with the object type of the first argument.
	 *
	 * @param setA  the first set for the union
	 * @param setB  the second set for the union
	 * @return a set that contains all of the elements that are either in the
	 *         first set or in the second set.
	 */
	public static <T> Set<T> union (Set<T> setA, Set<T> setB)
	{
		Set<T> setUnion;

		// create a new TreeSet or HashSet object that
		// contains the same values as setA
		if (setA instanceof OrderedSet)
			setUnion = new TreeSet<T>();
		else
			setUnion = new HashSet<T>();

		// use iterator to add elements from setA
		Iterator<T> iterA = setA.iterator();
		while (iterA.hasNext())
			setUnion.add(iterA.next());

		// use iterator to add elements from setB
		Iterator<T> iterB = setB.iterator();
		while (iterB.hasNext())
			setUnion.add(iterB.next());

		return setUnion;
	}

	/**
	 * Creates a new <tt>TreeSet</tt> or <tt>HashSet</tt> object that is the
	 * set of all elements that are in both the first set and the second set. The
	 * method returns a set with the object type of the first argument.
	 *
	 * @param setA  the first set for the union
	 * @param setB  the second set for the union
	 * @return a set that contains all of the elements that are in both the
	 *         first set and in the second set.
	 */
	public static <T> Set<T> intersection (Set<T> setA, Set<T> setB)
	{
		Set<T> setIntersection;
		T item;

		// create a new TreeSet or HashSet object that
		// contains the same values as setA
		if (setA instanceof OrderedSet)
			setIntersection = new TreeSet<T>();
		else
			setIntersection = new HashSet<T>();

		// scan elements in setA and check whether they are
		// also elements in setB.
		Iterator<T> iterA = setA.iterator();
		while (iterA.hasNext())
		{
			item = iterA.next();
			if (setB.contains(item))
				setIntersection.add(item);
		}

		return setIntersection;
	}

   /**
	 * Creates a new <tt>TreeSet</tt> or <tt>HashSet</tt> object that is the
	 * set of all elements that are in the first set bu not in the second set. The
	 * method returns a set with the object type of the first argument.
	 *
	 * @param setA  the first set for the union
	 * @param setB  the second set for the union
	 * @return a set that contains all of the elements that are in the
	 *         first set but not in the second set.
	 */
	public static <T> Set<T> difference (Set<T> setA, Set<T> setB)
	{
		Set<T> setDifference;
		T item;

		// create a new TreeSet or HashSet object that
		// contains the same values as setA
		if (setA instanceof OrderedSet)
			setDifference = new TreeSet<T>();
		else
			setDifference = new HashSet<T>();

		// scan elements in setA and check whether they are
		// not in setB.
		Iterator<T> iterA = setA.iterator();
		while (iterA.hasNext())
		{
			item = iterA.next();
			if (!setB.contains(item))
				setDifference.add(item);
		}

		return setDifference;
	}

	/**
	 * Returns <tt>true</tt> if the first set is a subset of the
	 * second set; that is if all of the elements in the first set are also in the second set.
	 *
	 * @param setA  the first set for the union
	 * @param setB  the second set for the union
	 * @return 	<tt>true</tt> if all of the elements in the first set are also in the
	 *           second set.
	 */
	public static <T> boolean subset(Set<T> setA, Set<T> setB)
	{
		return intersection(setA, setB).size() == setA.size();
	}

	// if more elements remain, return the next value; otherwise,
	// return null
	private static <T> T advance(Iterator<T> iter)
	{
	    T value = null;

		if (iter.hasNext())
			value = iter.next();

		return value;
	}

	/**
	 * A more efficient version of <tt>intersection</tt> when the two set are initially
	 * ordeed.
	 *
	 * @param lhs  the first set for the union
	 * @param rhs  the second set for the union
	 * @return a set that contains all of the elements that are in both the
	 *         first set and in the second set.
	 */
	public static <T extends Comparable<? super T>>
	TreeSet<T> orderedIntersection(TreeSet<T> lhs,TreeSet<T> rhs)
	{
		// constuct intersection
		TreeSet<T> setIntersection = new TreeSet<T>();
		// iterators that traverse the sets
		Iterator<T> lhsIter = lhs.iterator(), rhsIter = rhs.iterator();
		T lhsValue, rhsValue;

		lhsValue = advance(lhsIter);
		rhsValue = advance(rhsIter);

		// move forward as long as we have not reached the end of
		// either set
		while (lhsValue != null && rhsValue != null)
		{
			if (lhsValue.compareTo(rhsValue) < 0)
				// lhsValue < rhsValue. move to next value in lhs
				lhsValue = advance(lhsIter);
			else if (rhsValue.compareTo(lhsValue) < 0)
				// rhsValue < lhsValue. move to next value in rhs
				rhsValue = advance(rhsIter);
			else
			{
				// lhsValue == rhsValue. add it to intersection and
				// move to next value in both sets
				setIntersection.add(lhsValue);
				lhsValue = advance(lhsIter);
				rhsValue = advance(rhsIter);
			}
		}

		return setIntersection;
	}
}






