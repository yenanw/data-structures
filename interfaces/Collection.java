package interfaces;

public interface Collection<T> extends Iterable<T> {
    /**
     * @return true if size() == 0 else false
     */
    boolean isEmpty();

    /**
     * Counts the amount of items the collection contains, not the capacity
     * of whatever the implementation has
     * 
     * @return The amount of items in the collection
     */
    int size();

    // extended from Iterable<T>
    // Iterator<T> iterator();
}
