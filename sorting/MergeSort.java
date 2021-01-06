package sorting;

@SuppressWarnings("unchecked")
public class MergeSort<T> implements Sort<T> {
    @Override
    public void sort(T[] arr) {
        // create a new auxiliary array, we use this to prevent creating
        // additional auxiliary arrays during the recursions
        T[] aux = (T[]) (new Object[arr.length]);
        sort(arr, aux, 0, arr.length-1);
    }

    private void sort(T[] arr, T[] aux, int lo, int hi) {
        if (hi <= lo)
            // the base case, when the array length is <=1
            return;

        // for better performance, you can use insertion sort on small arrays
        /*
            if (hi - lo < THRESHOLD)
                insertionSort(arr);
            else
                ...
        */

        int mid = (lo + hi) / 2;
        // sort the left halve of the array
        sort(arr, aux, lo, mid);
        // sort teh right halve of the array;
        sort(arr, aux, mid+1, hi);
        // merge the two sorted sides together
        merge(arr, aux, lo, mid, hi);
    }

    // merge the elements in arr[lo..mid] and arr[mid+1..hi] with the help
    // of the auxiliary array, assuming both arr[lo..mid] and arr[mid+1..hi]
    // are sorted
    private void merge(T[] arr, T[] aux, int lo, int mid, int hi) {
        // first we copy all content from the original array to the
        // auxiliary array
        for (int i = lo; i <= hi; i++) {
            aux[i] = arr[i];
        }
        // the two pointers we use to determine the start of each sorted array
        int p1 = lo;
        int p2 = mid+1;
        // merge the two sorted arrays
        for (int i = lo; i <= hi; i++) {
            if (p1 >= mid+1)
                // all elements in the first array has been added,
                // add the rest of the second array
                arr[i] = aux[p2++];
            else if (p2 > hi)
                // all elements in the second array has been added,
                // add the rest of the first array
                arr[i] = aux[p1++];
            else if (less(aux[p2], aux[p1]))
                arr[i] = aux[p2++];
            else
                arr[i] = aux[p1++];
        }
    }
}
