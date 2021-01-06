package sorting;

public class QuickSort<T extends Comparable<T>> implements Sort<T> {
    @Override
    public void sort(T[] arr) {
        sort(arr, 0, arr.length-1);
    }

    private void sort(T[] arr, int lo, int hi) {
        if (lo >= hi)
            // base case, when the array contains 0 or 1 element
            return;

        // first retrieve the pivot index of the partitioned array
        int p = partition(arr, lo, hi);

        // and then recursively sort the arrays left and right of the pivot
        sort(arr, lo, p-1);
        sort(arr, p+1, hi);
    }

    // partitions the array and return the index p so that all elements are
    // arr[lo..p-1] <= arr[p] <= arr[p+1..hi]
    private int partition(T[] arr, int lo, int hi) {
        // find the pivot index
        int p = medianOfThree(arr, lo, (lo+hi)/2, hi);
        // swap the pivot element with the lo element, so now lo is pointing
        // at the pivot element
        swap(arr, lo, p);
        T pivot = arr[lo];
        
        int l = lo+1;
        int h = hi;

        while (true) {
            // move l as right as possible, stop if arr[l] is less than
            // the pivot, and then increment l
            while (l <= h && less(arr[l], pivot)) l++;
            // move h as left as possible, stop if arr[h] is greater than
            // the pivot, and then increment h
            while (h >= l && greater(arr[h], pivot)) h--;
            // to make sure we don't have index out of bounds exception
            if (l > h)
                break;
            // swap them at the end and increment both pointers
            swap(arr, l++, h--);
        }
        // swap the pivot element with the last element in the part that are
        // smaller than the pivot so that we get a nice partition
        swap(arr, lo, h);
        return h;
    }

    // given three indices and find the median of these three elements,
    // return either i1, i2 or i3
    private int medianOfThree(T[] arr, int i1, int i2, int i3) {
        // don't ask how this works, just imagine
        boolean b1 = less(arr[i1], arr[i2]);
        boolean b2 = less(arr[i2], arr[i3]);
        boolean b3 = less(arr[i1], arr[i3]);

        if (b1) {
            if (b2)      return i2;
            else if (b3) return i3;
            else         return i1;
        } else {
            if (b3)      return i1;
            else if (b2) return i3;
            else         return i2;
        }
    }
}
