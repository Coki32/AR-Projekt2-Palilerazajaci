package sorters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class SorterBase<T extends Comparable<T>> {

    public abstract void sort(List<T> array);

    public void sort(T[] array) {//bukvalno beskorisno, ali eto, dosadno mi je
        List<T> listica = new ArrayList<T>(Arrays.asList(array));
        sort(listica);
        for (int i = 0; i < array.length; i++)
            array[i] = listica.get(i);
    }

    protected List<T> getFirstHalf(List<T> l, int length) {
        return l.stream().limit(length).collect(Collectors.toCollection(ArrayList::new));
    }

    protected List<T> getAfter(List<T> l, int skip, long length) {
        return l.stream().skip(skip).limit(length).collect(Collectors.toCollection(ArrayList::new));
    }

    @SafeVarargs
    protected final T pickSmallest(List<T>... lists) {
        if (lists.length == 1)
            return lists[0].remove(0);
        var smallest = lists[0];
        for (int i = 1; i < lists.length; i++)
            if (lists[i].get(0).compareTo(smallest.get(0)) < 0)
                smallest = lists[i];
        return smallest.remove(0);
    }

    protected T pickSmallest(List<List<T>> lists) {
        if (lists.size() == 1)
            return lists.get(0).remove(0);
        var smallest = lists.get(0);
        for (int i = 1; i < lists.size(); i++)

            if (!lists.get(i).isEmpty()) {//i-ta nije prazna
                if (!smallest.isEmpty()) {//najmanja nije prazna
                    if (smallest.get(0).compareTo(lists.get(i).get(0)) > 0)//i-ta je manja od najmanje
                        smallest = lists.get(i);
                } else//najmanja je prazna, i-ta nije
                    smallest = lists.get(i);
            }

        return smallest.remove(0);
    }
}
