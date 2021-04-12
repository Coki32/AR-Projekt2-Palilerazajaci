package sorters;

import java.util.List;

/**
 * Splits into two groups, does the work on 2 threads, merges on one thread.
 * Still, quite a bit faster than regular old sorter.
 * @param <T>
 */
public class MaybeLessNaiveSorter<T extends Comparable<T>> extends NaiveSorter<T> {

    @Override
    public void sort(List<T> array) {
        lessNaiveImpl(array, 0, array.size()-1);
    }

    private void lessNaiveImpl(List<T> arr, int begin, int end) {
        if (begin < end) {
            int mid = (begin + end) / 2, l1 = mid + 1, l2 = end - mid;
            var left = this.getFirstHalf(arr, l1);
            var right = this.getAfter(arr, l1, l2);

            arr.clear();

            var t1 = new Thread(() -> super.sort(left));
            var t2 = new Thread(() -> super.sort(right));

            t1.start();
            t2.start();
            try {
                t1.join();
                t2.join();
            } catch (InterruptedException ignored) { }

            //merge halves
            int il = 0, ir = 0, ia = 0;
            while (!left.isEmpty() && !right.isEmpty())//<0 ===  ==-1 tj prvi manji
                arr.add(left.get(0).compareTo(right.get(0)) < 0 ? left.remove(0) : right.remove(0));
            arr.addAll(left);
            arr.addAll(right);
        }
    }

}
