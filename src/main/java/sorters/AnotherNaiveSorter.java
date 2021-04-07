package sorters;

import java.util.List;

/**
 * So, the naive sorter was slooooooooow <br/>
 * You've just learnt about threads and you wanna speed it up by sorting pieces on separate threads.<br/>
 * Good job, you've just made AnotherNaiveSorter, Good luck.<br/>
 * <br /> <br /><br />
 * Warning: It <i>Literally <b>CAN NOT</b></i> sort a lot of elements, like, 250k crashes it, runs out of threads (at about 65k)
 * @param <T> Type of the stuff you want to sort, also gotta be Comparable, duh
 */
public class AnotherNaiveSorter<T extends Comparable<T>> extends NaiveSorter<T> {


    @Override
    public void sort(List<T> array) {
        anotherSortImpl(array, 0, array.size()-1);
    }

    protected void anotherSortImpl(List<T> arr, int begin, int end) {
        if (begin < end) {
            int mid = (begin + end) / 2, l1 = mid + 1, l2 = end - mid;
            var left = this.getFirstHalf(arr, l1);
            var right = this.getAfter(arr, l1, l2);

            arr.clear();

            var t1 = new Thread(() -> anotherSortImpl(left, 0, l1 - 1));
            var t2 = new Thread(() -> anotherSortImpl(right, 0, l2 - 1));

            t1.start();
            t2.start();
            try {
                t1.join();
                t2.join();
            } catch (InterruptedException ex) {
            }

            //merge halves
            int il = 0, ir = 0, ia = 0;
            while (!left.isEmpty() && !right.isEmpty())//<0 ===  ==-1 tj prvi manji
                arr.add(left.get(0).compareTo(right.get(0)) < 0 ? left.remove(0) : right.remove(0));
            arr.addAll(left);
            arr.addAll(right);
        }
    }

}
