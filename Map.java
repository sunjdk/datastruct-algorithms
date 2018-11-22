/*
 * @(#)Map.java
 *
 */

package ds.util;

/**
 * A Map object maps keys to values; a map cannot contain duplicate keys and
 * each key can map to at most one value.<p>
 *
 * The <tt>Map</tt> interface provides two <i>collection views</i>, which
 * allow a map's contents to be viewed as a set of keys, or set of key-value mappings.
 * The <i>order</i> of a map is defined as the order in which the iterators on
 * the map's collection views return their
 * elements.  Some map implementations, like the <tt>TreeMap</tt> class, make
 * specific guarantees as to their order; others, like the <tt>HashMap</tt>
 * class, do not.
 */

public interface Map<K,V>
{

     /**
     * Returns the number of Entry pairs in this map.
     *
     * @return the number of Entry pairs in this map.
     */
   int size();

     /**
     * Returns <tt>true</tt> if this map contains no elements.
     *
     * @return <tt>true</tt> if this map contains no elements.
     */
  boolean isEmpty();

    /**
     * Returns <tt>true</tt> if this map contains an Entry pair with the specified key.
     *
     * @param key key component for an Entry pair whose presence in this map is to be tested.
     * @return <tt>true</tt> if this map contains the specified Entry pair..
     */
   boolean containsKey(Object key);

   /**
    * Returns the value component for an Entry pair specified by the key component.
    * Returns <tt>null</tt> if no Entry pair exits.
    * @param key  key component for an Entry pair.
    * @return value of the Entry pair or <tt>null</tt> if a pair does not exist.
    */
    V get(Object key);


    /**
     * Associates the specified value with the specified key in this map
     * If the map previously contained an Entry pair for
     * this key, the old value is replaced by the specified value.
     *
     * @param key key with which the specified value is to be associated.
     * @param value value to be associated with the specified key.
     * @return previous value associated with specified key, or <tt>null</tt>
     *	       if there was no mapping for key.
     */
  	V put(K key, V value);

     /**
     * Removes the Entry pair for this key from this map if it is present.
     * Returns the value to which the map previously associated the key, or
     * <tt>null</tt> if the map contained no mapping for this key.
     *
     * @param key key whose mapping is to be removed from the map.
     * @return previous value associated with specified key, or <tt>null</tt>
     *	       if there was no mapping for key.
     */
  	V remove(Object key);

    /**
     * Removes all of the elements from this map. This map will be empty after
     * this call returns.
     */
   void clear();


    /**
     * Returns a set view of the keys contained in this map.  The set is
     * backed by the map, so changes to the map are reflected in the set, and
     * vice-versa.
     *
     * @return a set view of the keys contained in this map.
     */
   Set<K> keySet();

    /**
     * Returns a set view of the mappings contained in this map.  Each element
     * in the returned set is a <tt>Map.Entry</tt>.  The set is backed by the
     * map, so changes to the map are reflected in the set, and vice-versa.
     *
     * @return a set view of the mappings contained in this map.
     */
    Set<Map.Entry<K, V>> entrySet();

    /**
     * A map entry (key-value pair).  The <tt>Map.entrySet</tt> method returns
     * a collection-view of the map, whose elements are of this class.  The
     * <i>only</i> way to obtain a reference to a map entry is from the
     * iterator of this collection-view.
     */
   interface Entry<K,V>
   {
		/**
		 * Returns the key corresponding to this entry.
		 *
		 * @return the key corresponding to this entry.
		 */
		K getKey();

		/**
		 * Returns the value corresponding to this entry.
		 *
		 * @return the value corresponding to this entry.
		 */
		V getValue();

		/**
		 * Replaces the value corresponding to this entry with the specified
		 * value.
		 *
		 * @param value new value to be stored in this entry.
		 * @return old value corresponding to the entry.
		 */
		V setValue(V value);

   }
}
