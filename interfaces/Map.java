package interfaces;

public interface Map<K,V> {
    /**
     * Associates a value with a key and stores the key-value entry
     * 
     * @param key   The key
     * @param value The value associated to the key
     */
    void put(K key, V value);

    /**
     * Finds the value associated with the specified key
     * 
     * @param key The key
     * @return The value associated with the key if the key exists, else null
     */
    V get(K key);

    /**
     * Removes the key-value entry from the map, if such key exists
     * 
     * @param key The key
     */
    void remove(K key);

    /**
     * Checks whether or not the key exists in the map
     * 
     * @param key The specified key
     * @return true if the specified key exists in the map, else false
     */
    boolean containsKey(K key);

    /**
     * Retreives an iterable over the set of keys
     * 
     * @see iterator(), keySet()
     * @return The iterable of the keys
     */
    Iterable<K> keys();

    /**
     * @return true if size() == 0 else false
     */
    boolean isEmpty();

    /**
     * Counts the amount of key-value entries the map contains
     * 
     * @return The amount of entries in the collection
     */
    int size();
}
