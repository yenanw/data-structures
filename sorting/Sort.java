package sorting;

/**
 * The basic interface for all sorting algorithms, ideally it's best to use
 * a Comparator instead of enforcing comparable types, but meh
 * 
 * @param <T> Any non-primitive type that implements the Comparable<T> interface
 */
public interface Sort<T extends Comparable<T>> {
    /**
     * Sorts the array in ascending order in place, meaning the array passed
     * as the argument will be modified and no result is returned
     * 
     * @param arr The array to be sorted
     */
    void sort(T[] arr);

    // --------------------------basic helper methods--------------------------
    default boolean less(T t1, T t2) {
        return t1.compareTo(t2) < 0;
    }

    default boolean greater(T t1, T t2) {
        return t1.compareTo(t2) > 0;
    }

    default boolean equal(T t1, T t2) {
        return t1.compareTo(t2) == 0;
    }

    default void swap(T[] arr, int i1, int i2) {
        T temp = arr[i1];
        arr[i1] = arr[i2];
        arr[i2] = temp;
    }
}
