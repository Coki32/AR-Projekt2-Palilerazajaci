import sorters.*;

import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainFazon {

    static List<Double> getNRandomDoubles(int limit){
        var r = new Random();
        return Stream.generate(r::nextDouble).limit(limit).collect(Collectors.toList());
    }

    static long timeIt(Runnable action){
        var t1 = System.currentTimeMillis();
        action.run();
        return System.currentTimeMillis() - t1;
    }

    static class SorerInfo{
        SorterBase<Double> sorter;
        String name;

        public SorerInfo(SorterBase<Double> sorter, String name) {
            this.sorter = sorter;
            this.name = name;
        }

        public SorterBase<Double> getSorter() {
            return sorter;
        }
    }

    public static void main(String[] args) {
        int n = 10;
        int factor = 5;
        var sorters = new SorerInfo[]{
                new SorerInfo(new NaiveSorter<>(),"merge"),
                new SorerInfo(new HybridSorter<>(),"merge_hybrid"),
                new SorerInfo(new MaybeLessNaiveSorter<>(),"merge_two_thread"),
                new SorerInfo(new AllThreadSorter<>(),"merge_n_threads"),
                new SorerInfo(new AllThreadHybridSorter<>(),"merge_n_threads_hybrid"),
        };
        System.out.println("n,"+Arrays.stream(sorters).map(si->si.name).reduce((pr,cu)->pr+","+cu).orElse(""));
        for(int inkrement = 0; inkrement<13; inkrement++){

            double[] rezultati = new double[sorters.length];//za ovo N
            Arrays.fill(rezultati,0.0);

            for(int reps = 0; reps<10; reps++) {//broj ponavljanja
                var niz = getNRandomDoubles(n);//napravi toliko double-ova
                var kopija = new ArrayList<Double>(n);
                for (int sorterIdx=0; sorterIdx<sorters.length; sorterIdx++) {//za svaki sorter
                    kopija.clear();
                    kopija.addAll(niz);//kopiraj prije sorta
                    var sorter = sorters[sorterIdx].getSorter();
                    var time = timeIt(()->sorter.sort(kopija));
                    rezultati[sorterIdx] += ((double)time)/10;//bice 10 ponavljanja
                }
            }

            System.out.println(n+","+Arrays.stream(rezultati).map(d->(double)Math.round(d*1000)/1000).mapToObj(Double::toString).reduce((pr, cu)->pr+","+cu).orElse(""));
            n*=factor;
            factor = factor==5 ? 2 : 5;
        }
    }

    public static void main2(String[] args) {
        var nizic = getNRandomDoubles(10).stream().map(d->(double)(Math.round(d*10))).collect(Collectors.toList());
        new SelectionSorter<Double>().sort(nizic);
        System.out.println(nizic);
    }
}
