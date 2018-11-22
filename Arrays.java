/*
 * @(#)Arrays.java
 */

package ds.util;

/**
 * This class contains methods for searching, sorting, and listing the elements
 * in an array. <p>
 *
 * For the basic searching and sorting methods, the class features versions for
 * int arrays and for generic array with a specified type.<p>

 * The general <tt>sort</tt> method includes a version with a <tt>Comparator</tt>
 * argument that controls the order (ascending or descending) of the sort
 *
 * @see     Comparator
 */

public class Arrays
{
	// assure that the programmer cannot instantiate
	// an Arrays object
   private Arrays()
   {}

	/**
	 * Sorts an array of integer values in ascending order using the selection
	 * sort algorithm.<p>
	 *
	 * This algorithm has O(n*n) runtime efficiency for best, worst, and
	 * average case.
	 *
	 * @param arr the array to be sorted.
	 */
	public static void selectionSort(int[] arr)
	{
		int smallIndex; // index of smallest element in the sublist
		int pass, j, n = arr.length;
		int temp;

		// pass has the range 0 to n-2
		for (pass = 0; pass < n-1; pass++)
		{
			// scan the sublist starting at index pass
			smallIndex = pass;

			// j traverses the sublist arr[pass+1] to arr[n-1]
			for (j = pass+1; j < n; j++)
				// if smaller element found, assign smallIndex
				// to that position
				if (arr[j] < arr[smallIndex])
					smallIndex = j;

			// swap the next smallest element into arr[pass]
			temp = arr[pass];
			arr[pass] = arr[smallIndex];
			arr[smallIndex] = temp;
		}
	}

   /**
    * A generic version of the selection sort algorithm that orders an array of elements
    * of type <tt>T</tt> in ascending order.  The algorithm requires that the generic
    * type implements the <tt>Comparable</tt> interface.<p>
    *
    * This algorithm has O(n*n) runtime efficiency for best, worst, and
    * average case.
    *
    * @param arr the array to be sorted.
    */
   public static <T extends Comparable<? super T>>
   void selectionSort(T[] arr)
   {
      int smallIndex; // index of smallest element in the sublist
         int pass, j, n = arr.length;
      T temp;

      // pass has the range 0 to n-2
      for (pass = 0; pass < n-1; pass++)
      {
         // scan the sublist starting at index pass
         smallIndex = pass;

         // j traverses the sublist arr[pass+1] to arr[n-1]
         for (j = pass+1; j < n; j++)
            // if smaller element found, assign smallIndex
            // to that position
            if (arr[j].compareTo(arr[smallIndex]) < 0)
               smallIndex = j;

			// swap the next smallest element into arr[pass]
			temp = arr[pass];
			arr[pass] = arr[smallIndex];
			arr[smallIndex] = temp;
      }
   }

   /**
    * A generic version of the insertion sort algorithm that orders an array of elements
    * of type <tt>T</tt> in ascending order.  The algorithm requires that the generic
    * type implements the <tt>Comparable</tt> interface.<p>
    *
    * This algorithm has O(n*n) runtime efficiency for best, worst, and
    * average case.
    *
    * @param arr the array to be sorted.
    */
	public static <T extends Comparable<? super T>>
	void insertionSort(T[] arr)
	{
		int i, j, n = arr.length;
		T target;

		// place element at index i into the sublist
		//   from index 0 to i-1 where  1 <= i < n,
		//   so it is in the correct position
		for (i = 1; i < n; i++)
		{
			// index j scans down list from index i looking for
			// correct position to locate target. assigns it to
			// v at index j
			j = i;
			target = arr[i];
			// locate insertion point by scanning downward as long
			// as target < arr[j] and we have not encountered the
			// beginning of the array
			while (j > 0 && target.compareTo(arr[j-1]) < 0)
			{
				// shift elements up list to make room for insertion
				arr[j] = arr[j-1];
				j--;
			}
			// the location is found; insert target
			arr[j] = target;
		}
	}

  	// support function for radixSort()
   // distribute array elements into one of 10 queues
   // using the digit corresponding to power
   //   power = 1    ==> 1's digit
   //   power = 10   ==> 10's digit
   //   power = 100  ==> 100's digit
   //   ...
   private static void distribute(
   		int[] arr,
   		LinkedQueue[] digitQueue,
   		int power)
   {
      int i;

      // loop through the array, inserting each element into
      // the queue (arr[i] / power) % 10
      for (i = 0; i < arr.length; i++)
         digitQueue[(arr[i] / power) % 10].push(arr[i]);
   }

   // support function for radixSort()
   // gather elements from the queues and copy back to the array
   private static void collect(LinkedQueue[] digitQueue, int[] arr)
   {
      int i = 0, digit;

      // scan the array of queues using indices 0, 1, 2, etc.
      for (digit = 0; digit < 10; digit++)
         // collect items until queue empty and copy items back
         // to the array
         while (!digitQueue[digit].isEmpty())
         {
            arr[i] =
            	((Integer)digitQueue[digit].pop()).intValue();
            i++;
         }
   }

   /**
    * Sorts an array of integer values in ascending order
    * using the radix sort algorithm; the parameter d is an upper bound
    * on the digit size of an element.<p>
    *
    * This algorithm has O(n*d) runtime efficiency.
    *
    * @param arr the array to be sorted.
    * @param d upper bound on the digit size of an element..
    */
   public static void radixSort(int[] arr, int d)
   {
      int i;
      // current digit found by dividing by 10^power
      int power = 1;
      // allocate 10 null references to a LinkedQueue
      LinkedQueue[] digitQueue = new LinkedQueue[10];

      // initialize each element of digitQueue to be
      // an empty queue
      for (i=0;i < digitQueue.length;i++)
         digitQueue[i] = new LinkedQueue();

      for (i=0;i < d;i++)
      {
         distribute(arr, digitQueue, power);
         collect(digitQueue, arr);
         power *= 10;
      }
   }


	// private method the implements the recursive merge sort for the sublist
	// from first to last. The method assumes the elements in the array
	// implemenet the Comparable interface which provides the natural ordering
	private static void
	msort(Object[] arr, Object[] tempArr, int first, int last)
	{
		// if the sublist has more than 1 element continue
		if (first + 1 < last)
		{
			// for sublists of size 2 or more, call msort()
			// for the left and right sublists and then
			// merge the sorted sublists using merge()
			int midpt = (last + first) / 2;

			msort(arr, tempArr,first, midpt);
			msort(arr, tempArr, midpt, last);

			// if list is already sorted, just copy from src to
			// dest. this is an optimization that results in faster
			// sorts for nearly ordered lists.
			if (((Comparable)arr[midpt-1]).compareTo(arr[midpt]) <= 0)
				return;

			// the elements in the ranges [first,mid) and [mid,last) are
			// ordered. merge the ordered sublists into
			// an ordered sequence in the range [first,last) using
			// the temporary array
			int indexA, indexB, indexC;

			// set indexA to scan sublist A (index range [first,mid)
			// and indexB to scan sublist B (index range [mid, last)
			indexA = first;
			indexB = midpt;
			indexC = first;

			// while both sublists are not exhausted, compare arr[indexA] and
			// arr[indexB]; copy the smaller to tempArr
			while (indexA < midpt && indexB < last)
			{
				if (((Comparable)arr[indexA]).compareTo(arr[indexB]) < 0)
				{
					tempArr[indexC] = arr[indexA];	// copy element to tempArr
					indexA++;								// increment indexA
				}
				else
				{
					tempArr[indexC] = arr[indexB];	// copy element to tempArr
					indexB++;								// increment indexB
				}
				// increment indexC
				indexC++;
			}

			// copy the tail of the sublist that is not exhausted
			while (indexA < midpt)
			{
				tempArr[indexC] = arr[indexA];		// copy element to tempArr
				indexA++;
				indexC++;
			}

			while (indexB < last)
			{
				tempArr[indexC] = arr[indexB];		// copy element to tempArr
				indexB++;
				indexC++;
			}

			// copy elements from temporary array to original array
			for (int i = first; i < last; i++)
				arr[i] = tempArr[i];
		}
	}

	// private method the implements the recursive merge sort for the sublist
	// from first to last. The Comparator provides the natural ordering of elements
	private static void
	msort(Object[] arr, Object[] tempArr,
			int first, int last, Comparator comp)
	{
		// if the sublist has more than 1 element continue
		if (first + 1 < last)
		{
			// for sublists of size 2 or more, call msort()
			// for the left and right sublists and then
			// merge the sorted sublists using merge()
			int midpt = (last + first) / 2;

			msort(arr, tempArr,first, midpt, comp);
			msort(arr, tempArr, midpt, last, comp);

			// the elements in the ranges [first,mid) and [mid,last) are
			// ordered. merge the ordered sublists into
			// an ordered sequence in the range [first,last) using
			// the temporary array
			int indexA, indexB, indexC;

			// set indexA to scan sublist A (index range [first,mid)
			// and indexB to scan sublist B (index range [mid, last)
			indexA = first;
			indexB = midpt;
			indexC = first;

			// while both sublists are not exhausted, compare arr[indexA] and
			// arr[indexB]; copy the smaller to tempArr
			while (indexA < midpt && indexB < last)
			{
				if (comp.compare(arr[indexA], arr[indexB]) < 0)
				{
					tempArr[indexC] = arr[indexA];	// copy element to tempArr
					indexA++;								// increment indexA
				}
				else
				{
					tempArr[indexC] = arr[indexB];	// copy element to tempArr
					indexB++;								// increment indexB
				}
				// increment indexC
				indexC++;
			}

			// copy the tail of the sublist that is not exhausted
			while (indexA < midpt)
			{
				tempArr[indexC] = arr[indexA];		// copy element to tempArr
				indexA++;
				indexC++;
			}

			while (indexB < last)
			{
				tempArr[indexC] = arr[indexB];		// copy element to tempArr
				indexB++;
				indexC++;
			}

			// copy elements from temporary array to original array
			for (int i = first; i < last; i++)
				arr[i] = tempArr[i];
		}
	}

   /**
    * Sorts the specified array of objects into
    * ascending order using the <tt>Comparble</tt> method <tt>compareTo()</tt> to
    * order the elements.<p>
    *
    * The sorting algorithm is mergesort which offers guaranteed
    * O(n*log(n)) performance.
    *
    * @param arr the array to be sorted.
    */
	public static void sort(Object[] arr)
	{
		// create a temporary array to store partitioned elements
		Object[] tempArr = arr.clone();

		// call mergesort with arrays arr and tempArr along with
		// the index range
		msort(arr, tempArr, 0, arr.length);
	}

   /**
    * A generic sort the orders an array of elements of type <tt>T</tt> into
    * ascending order using the <tt>Comparble</tt> method <tt>compareTo()</tt> to
    * order the elements.<p>
    *
    * The sorting algorithm is mergesort which offers guaranteed
    * O(n*log(n)) performance.
    *
    * @param arr the array to be sorted.
    */
	public static <T extends Comparable<? super T>>
	void sort(T[] arr)
	{
		// create a temporary array to store partitioned elements
		T[] tempArr = (T[])arr.clone();

		// call mergesort with arrays arr and tempArr along with
		// the index range
		msort(arr, tempArr, 0, arr.length);
	}

   /**
    * A generic sort the orders an array of elements of type <tt>T</tt> into
    * ascending order using the <tt>Comparator</tt> comp to provide the natural
    * ordering of elements.<p>
    *
    * The sorting algorithm is mergesort which offers guaranteed
    * O(n*log(n)) performance.
    *
    * @param arr the array to be sorted.
    * @param comp a <tt>Comparator</tt> that provides the natural
    *             ordering of elements.
    */
	public static <T>
	void sort(T[] arr, Comparator<? super T> comp)
	{
		// create a temporary array to store partitioned elements
		T[] tempArr = (T[])arr.clone();

		// call mergesort with arrays arr and tempArr along with
		// the index range
		msort(arr, tempArr, 0, arr.length, comp);
	}

   /**
    * Partitions array elements in the range [first, last) about the pivot
    * and returns the index of the resulting pivot position. Elements in
    * the lower sublist are less than or equal to the pivot value and elements
    * in the upper sublist are greater than or equal to the pivot.
    *
    * @param arr array to paritition.
    * @param first starting index for the sublist.
    * @param last upper bound for the sublist.
    * @return index of the pivot value after partitioning the array sublist.
    */
	public static <T extends Comparable<? super T>>
	int pivotIndex(T[] arr, int first, int last)
	{
		// index for the midpoint of [first,last) and the
		// indices that scan the index range in tandem
		int mid, scanUp, scanDown;
		// pivot value and object used for exchanges
		T pivot, temp;

		if (first == last)
			// empty sublist
			return last;
		else if (first == last-1)
			// 1-element sublist
			return first;
		else
		{
			mid = (last + first)/2;
			pivot = arr[mid];

			// exchange the pivot and the low end of the range
			// and initialize the indices scanUp and scanDown.
			arr[mid] = arr[first];
			arr[first] = pivot;

			scanUp = first + 1;
			scanDown = last - 1;

			// manage the indices to locate elements that are in
			// the wrong sublist; stop when scanDown <= scanUp
			for(;;)
			{
				// move up the lower sublist; continue so long as scanUp is
				// less than or equal to scanDown and the array value is
				// less than pivot
				while (scanUp <= scanDown &&
							arr[scanUp].compareTo(pivot) < 0)
					scanUp++;

				// move down the upper sublist so long as the array value is
				// greater than the pivot
				while (pivot.compareTo(arr[scanDown]) < 0)
					scanDown--;

				// if indices are not in their sublists, partition complete
				if (scanUp >= scanDown)
					break;

				// indices are still in their sublists and identify
				// two elements in wrong sublists. exchange
				temp = arr[scanUp];
				arr[scanUp] = arr[scanDown];
				arr[scanDown] = temp;

				scanUp++;
				scanDown--;
			}

			// copy pivot to index (scanDown) that partitions sublists
			// and return scanDown
			arr[first] = arr[scanDown];
			arr[scanDown] = pivot;
			return scanDown;
		}
	}

	// private method the implements the recursive quicksort algorithm for the sublist
	// from first to last. The method assumes the elements in the array
	// implemenet the Comparable interface which provides the natural ordering.
	private static <T extends Comparable<? super T>>
	void qsort(T[] arr, int first, int last)
	{
		// index of the pivot
		int pivotLoc;
		// temp used for an exchange when [first,last) has
		// two elements
		T temp;

		// if the range is not at least two elements, return
		if (last - first <= 1)
			return;

		// if sublist has two elements, compare v[first] and
		// v[last-1] and exchange if necessary
		else if (last - first == 2)
		{
			if (arr[last-1].compareTo(arr[first]) < 0)
			{
				temp = arr[last-1];
				arr[last-1] = arr[first];
				arr[first] = temp;
			}
			return;
		}
		else
		{
			pivotLoc = pivotIndex(arr, first, last);

			// make the recursive call
			qsort(arr, first, pivotLoc);

			// make the recursive call
			qsort(arr, pivotLoc +1, last);
		}
	}

   /**
    * The generic quicksort algorithm that orders an array of elements of type <tt>T</tt>
    * into ascending order using the <tt>Comparble</tt> method <tt>compareTo()</tt> to
    * order the elements.<p>
    *
    * The sorting algorithm has runtime efficiency O(n*log(n)) for the average case..
    *
    * @param arr the array to be sorted.
    */
	public static <T extends Comparable<? super T>>
	void quicksort(T[] arr)
	{
		qsort(arr, 0, arr.length);
	}

	public static <T extends Comparable<? super T>>
	void findKth(T[] arr, int first, int last, int k)
	{
		int index;

		// partition range [first,last) in arr about the
		// pivot arr[index]
		index = pivotIndex(arr, first, last);

		// if index == k, we are done. kth largest is arr[k]
		if (index == k)
			return;
		else if(k < index)
			// search in lower sublist [first,index)
			findKth(arr, first, index, k);
		else
			// search in upper sublist [index+1,last)
			findKth(arr, index+1, last, k);
	}

   /**
    * The generic heapSort the orders an array of elements of type <tt>T</tt> into
    * ascending order using the <tt>Comparator</tt> comp to provide the natural
    * ordering of elements. If <tt>comp</tt> is an instance of <tt>Greater</tt>,
    * the array is sorted in ascending order. If <tt>comp</tt> is an instance of
    * <tt>Less</tt>the array is sorted in descending order.<p>
    *
    * The sorting algorithm is has runtime efficiency O(n*log(n)) for the average case..
    *
    * @param arr the array to be sorted.
    * @param comp a <tt>Comparator</tt> that provides order for the sort; An instance
    *             of <tt>Greater</tt> sorts in ascending order; an instance of
    *             <tt>Less</tt>sorts in descending order.
    */
	public static <T>
	void heapSort(T[] arr, Comparator<? super T> comp)
	{
	   // "heapify" the array  arr
	   Heaps.makeHeap(arr, comp);

	   int i, n =  arr.length;

	   // iteration that determines elements arr[n-1] ... arr[1]
	   for(i = n; i > 1;i--)
	   {
	      // call popHeap() to move next largest to  arr[n-1]
	      Heaps.popHeap(arr, i, comp);
	   }
	}

	/**
	* Searches the sublist [first, last) in an integer array for the specified
	* target value using the sequential search algorithm; the return value is
	* the first occurrence of a match or -1 if not match is found.
	*
	* @param arr the integer array to be searched.
	* @param first starting index for the sublist.
	* @param last upper bound for the sublist.
	* @param target the search key to locate in the sublist.
	*
	* @return index of the search key, if it is contained in the list;
	*	       otherwise, <tt>- 1</tt>.
	*/
	public static int seqSearch(int[] arr, int first, int last, int target)
	{
		// scan indices in the range first <= i < last; return the index
		// indicating the position if a match occurs; otherwise return -1
		for (int i = first; i < last; i++)
			if (arr[i] == target)
				return i;

		// no return yet if match is not found; return -1
		return -1;  // i = last, thus no match
	}

	/**
	* Searches the sublist [first, last) in a specified array of objects for the
	* specified target value using the sequential search algorithm; comparison
	* is made using the <tt>equals()</tt> method; the return value is
	* the first occurrence of a match or -1 if not match is found.
	*
	* @param arr the array to be searched.
	* @param first starting index for the sublist.
	* @param last upper bound for the sublist.
	* @param target the search key to locate in the sublist.
	*
	* @return index of the search key, if it is contained in the list;
	*	       otherwise, <tt>- 1</tt>.
	*/
	public static
	int seqSearch(Object[] arr, int first, int last, Object target)
	{
		// scan indices in the range first <= i < last; return the index
		// indicating the position if a match occurs; otherwise return -1
		for (int i = first; i < last; i++)
			if (arr[i].equals(target))
				return i;

		// no return yet if match is not found; return -1
		return -1;  // i = last, thus no match
	}

	/**
	* Searches an integer array for the specified target value using the binary
	* search algorithm.  The array must be sorted into ascending order.
	*
	* @param arr the integer array to be searched.
	* @param first starting index for the sublist.
	* @param last upper bound for the sublist.
	* @param target the search key to locate in the sublist.
	*
	* @return index of the search key, if it is contained in the list;
	*	       otherwise, <tt>- 1</tt>.
	*/
	public static int binSearch(
	int[] arr, int first, int last, int target)
	{
		int mid;                // index of the midpoint
		int midValue;           // object that is assigned arr[mid]

		while (first < last)    // test for nonempty sublist
		{
			mid = (first+last)/2;
			midValue = arr[mid];

			if (target == midValue)
				return mid;       // have a match
			// determine which sublist to search
			else if (target < midValue)
				last = mid;       // search lower sublist. reset last
			else
				first = mid+1;    // search upper sublist. reset first
		}

		return -1;             // target not found
	}


   /**
     * Searches the sublist [first, last) in a specified array of objects for the
     * specified target value using the binary search algorithm; the generic type
     * <tt>T</tt> must implement the <tt>Comparable</tt> interface with the array
     * sorted in ascending order; comparison
     * is made using the <tt>equals()</tt> method; the return value is
     * the first occurrence of a match or -1 if not match is found.
     * search algorithm.
     *
     * @param arr the integer array to be searched.
     * @param first starting index for the sublist.
     * @param last upper bound for the sublist.
     * @param target the search key to locate in the sublist.
     *
     * @return index of the search key, if it is contained in the list;
     *	       otherwise, <tt>- 1</tt>.
     */
   public static <T extends Comparable<? super T>>
   int binSearch(T[] arr, int first, int last, T target)
   {
      int mid;                // index of the midpoint
      T midvalue;        // object that is assigned arr[mid]
      int origLast = last;    // save original value of last

      while (first < last)    // test for nonempty sublist
      {
         mid = (first+last)/2;
         midvalue = arr[mid];
         if (target.compareTo(midvalue) == 0)
            return mid;       // have a match
         // determine which sublist to search
         else if (target.compareTo(midvalue) < 0)
            last = mid;       // search lower sublist. reset last
         else
            first = mid+1;    // search upper sublist. reset first
      }

      return -1;              // target not found
   }

   /**
    * Returns a string that displays the elements in the array in sequential
    * order. The description is a comma separated list of
    * elements enclosed in brackets.
    * @return string that contains the list of elements in the array.
    */

   // returns a string that represents an array of objects
	public static String toString(Object[] arr)
	{
		if (arr == null)
			return "null";
		else if (arr.length == 0)
			return "[]";

		// start with the left bracket
		String str = "[" + arr[0];

		// append all but the last element, separating items with a comma
		// polymorphism calls toString() for the array type
		for (int i = 1; i < arr.length; i++)
			str +=  ", " + arr[i];

		str += "]";

		return str;
	}
}

