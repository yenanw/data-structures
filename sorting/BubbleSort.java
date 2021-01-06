package sorting;

public class BubbleSort<T> implements Sort<T> {
    @Override
    public void sort(T[] arr) {
        int len = arr.length;
        for (int i = 0; i < len-1; i++) {
            for (int j = 0; j < len-1-i; j++) {
                // compares the two adjacent elements, if the left is greater
                // than the right, then swap places
                if (greater(arr[j], arr[j+1]))
                    swap(arr, j, j+1);
            }
        }
    }
}
