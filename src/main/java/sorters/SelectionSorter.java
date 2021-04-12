package sorters;

import java.util.List;

public class SelectionSorter<T extends Comparable<T>> extends SorterBase<T>{


    @Override
    public void sort(List<T> array) {
        selectionSortImpl(array);
    }

    private void selectionSortImpl(List<T> array){
        var n = array.size();
        for(int i=0; i<n-1; i++){
            int min = i;
            for(int j=i+1; j<n; j++)
                if(array.get(j).compareTo(array.get(min)) < 0)
                    min = j;
            if(min!=i){
                T tmp = array.get(i);
                array.set(i, array.get(min));
                array.set(min, tmp);
            }
        }
    }

}
