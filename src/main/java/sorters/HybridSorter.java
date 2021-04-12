package sorters;

import java.util.List;

public class HybridSorter<T extends Comparable<T>> extends NaiveSorter<T> {
    @Override
    public void sort(List<T> array) {
        if (array.size() <= 128) {
            new SelectionSorter<T>().sort(array);
        } else { hybridSortImpl(array,0, array.size()-1);
        }
    }

    private void hybridSortImpl(List<T> arr, int begin, int end) {
        if (arr.size() < 128) {
            new SelectionSorter<T>().sort(arr);
        }
        else if (begin < end) {
            int mid = (begin + end) / 2, l1 = mid + 1, l2 = end - mid;
            var left = this.getFirstHalf(arr, l1);
            var right = this.getAfter(arr, l1, l2);

            arr.clear();

            hybridSortImpl(left, 0, l1 - 1);
            hybridSortImpl(right, 0, l2 - 1);

            //merge halves
            int il = 0, ir = 0, ia = 0;
            while (!left.isEmpty() && !right.isEmpty())//<0 ===  ==-1 tj prvi manji
                arr.add(left.get(0).compareTo(right.get(0)) < 0 ? left.remove(0) : right.remove(0));
            arr.addAll(left);
            arr.addAll(right);
        }
    }
}
