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
                for (int sorterIdx=0; sorterIdx<sorters.length; sorterIdx++) {//za svaki sorter
                    var kopija = new ArrayList<>(niz);//kopiraj prije sorta
                    int finalSorterIdx = sorterIdx;
                    var time = timeIt(()->sorters[finalSorterIdx].getSorter().sort(kopija));
                    rezultati[sorterIdx] += ((double)time)/10;//bice 10 ponavljanja
                }
            }

            System.out.println(n+","+Arrays.stream(rezultati).map(d->(double)Math.round(d*1000)/1000).mapToObj(Double::toString).reduce((pr, cu)->pr+","+cu).orElse(""));
            n*=factor;
            factor = factor==5 ? 2 : 5;
        }
//        for (int k=0; k<10; k++) {
//            n=1000000;
//            for (int i = 0; i < 5; i++) {
//                for(int reps = 0; reps<3; reps++) {
//                    var niz = getNRandomDoubles(n);
//                    //System.out.println("Pokrecem sortiranje za " + n + " elemenata:");
//                    for (var info : sorters) {
//                        var kopija = new ArrayList<>(niz);
//                        var time = timeIt(() -> info.getSorter().sort(kopija));
//                        if (!vremena.get(info.name).containsKey(n))
//                            vremena.get(info.name).put(n, new ArrayList<>());
//                        vremena.get(info.name).get(n).add((int) time);
//                        //System.out.println(String.format("[%s]: %d", info.name, time));
//                    }
//                }
//                n += 100000;
//            }
//        }
//        vremena.forEach((naziv, listaVremena)->{
//            System.out.println(naziv);
//            listaVremena.forEach((_n,vremenaUMs)->{
//                var avgVrijeme = vremenaUMs.stream().mapToInt(i->(int)i).average().orElse(0.0);
//                System.out.println(String.format("Za %d elemenata prosjek je %f", _n, avgVrijeme));
//            });
//        });
    }

    public static void main2(String[] args) {
        var nizic = getNRandomDoubles(10).stream().map(d->(double)(Math.round(d*10))).collect(Collectors.toList());
        new SelectionSorter<Double>().sort(nizic);
        System.out.println(nizic);
    }
}
