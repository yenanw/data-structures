package sorting;

public class InsertionSort<T extends Comparable<T>> implements Sort<T> {
    @Override
    public void sort(T[] arr) {
        int len = arr.length;
        // by default the first element is always sorted
        for (int i = 1; i < len; i++) {
            int insert = insertionPoint(arr, i);
            T itemToInsert = arr[i];
            // shift the element in the array to make place for insertion
            System.arraycopy(arr, insert, arr, insert+1, i-insert);
            arr[insert] = itemToInsert;
        }

    }

    private int insertionPoint(T[] arr, int i1) {
        for (int i = 0; i < i1; i++) {
            // if an item in the sorted part is bigger than the item at the
            // given index, then we will want to insert the item there
            if (greater(arr[i], arr[i1]))
                return i;
        }
        // if no insertion point found in the interval, the it must be at
        // the end of the sorted part
        return i1;
    }
}
