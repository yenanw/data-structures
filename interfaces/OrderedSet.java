package interfaces;

public interface OrderedSet<T> extends Set<T> {
    /**
     * Retrieves the smallest element in the set
     * 
     * @return The smallest element in the set
     */
    T min();

    /**
     * Retrieves the biggest element in the set
     * 
     * @return The biggest element in the set
     */
    T max();

    /**
     * Finds the biggest item that is smaller than or equal to the specified
     * item
     * 
     * @param item The specified item
     * @return The biggest item that is smaller than or equal to the specified
     *         item, if no such element exists, return null
     */
    T floor(T item);

    /**
     * Finds the smallest item that is greater than or equal to the specified
     * item
     * 
     * @param item The specified item
     * @return The smallest item that is greater than or equal to the specified
     *         item, if no such element exists, return null
     */
    T ceil(T item);
}
