package sorters;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AllThreadHybridSorter<T extends Comparable<T>> extends HybridSorter<T> {
    @Override
    public void sort(List<T> array) {
        allThreadHybridSorterImpl(array, 0, array.size()-1);
    }

    private void allThreadHybridSorterImpl(List<T> arr, int begin, int end) {
        if(begin<end){
            int nThreads = Runtime.getRuntime().availableProcessors();
            var segmentSize = Math.round(Math.ceil(1.0*(end-begin+1)/nThreads));
            var currentStart = 0;
            List<List<T>> segments = new ArrayList<>(nThreads);
            for(int i=0; i<nThreads; i++,currentStart+=segmentSize)
                segments.add(this.getAfter(arr,currentStart,segmentSize));
            arr.clear();
            var threads = segments.stream().map(seg->new Thread(()->super.sort(seg))).collect(Collectors.toList());
            threads.forEach(Thread::start);
            threads.forEach( t-> {try{t.join();}catch (InterruptedException ignored){}});
            while(segments.stream().anyMatch(s->!s.isEmpty())) {//svi imaju elemenata
                arr.add(this.pickSmallest(segments));
            }
            segments.forEach(arr::addAll);
        }
    }
}
