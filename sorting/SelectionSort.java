package sorting;

public class SelectionSort<T> implements Sort<T> {
    @Override
    public void sort(T[] arr) {
        int len = arr.length;
        for (int i = 0; i < len; i++) {
            // find the smallest item
            int minIndex = min(arr, i, len);
            // add them to the end of the part which are already sorted
            swap(arr, i, minIndex);
        }
    }

    // search the given array within the given interval for the smallest item
    private int min(T[] arr, int i1, int i2) {
        int minIndex = i1;
        for (int i = i1+1; i < i2; i++) {
            if (less(arr[i], arr[minIndex]))
                minIndex = i;
        }
        // return the index of the smallest item
        return minIndex;
    }    
}
