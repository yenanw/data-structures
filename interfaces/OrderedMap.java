package interfaces;

public interface OrderedMap<K,V> extends Map<K,V> {
    /**
     * Retrieves the smallest key in the map
     * 
     * @return The smallest key in the map
     */
    K minKey();

    /**
     * Retrieves the biggest key in the map
     * 
     * @return The biggest key in the map
     */
    K maxKey();

    /**
     * Finds the biggest key that is smaller than or equal to the specified key
     * 
     * @param item The specified item
     * @return The biggest key that is smaller than or equal to the specified
     *         key, if no such key exists, return null
     */
    K floorKey(K key);

    /**
     * Finds the smallest key that is bigger than or equal to the specified key
     * 
     * @param item The specified item
     * @return The smallest key that is bigger than or equal to the specified
     *         key, if no such key exists, return null
     */
    K ceilingKey(K key);
}
