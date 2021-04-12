import sorters.*;

import java.util.*;
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
        int n = 2;
        var sorters = new SorerInfo[]{
                //new SorerInfo(new NaiveSorter<>(),"merge"),
                //new SorerInfo(new SelectionSorter<>(),"select"),
                //new SorerInfo(new MaybeLessNaiveSorter<>(),"less naive"),
                //new SorerInfo(new AnotherNaiveSorter<>(),"super naive"),
                new SorerInfo(new AllThreadSorter<>(),"all threads"),
                new SorerInfo(new AllThreadHybridSorter<>(),"all hybrid")
        };
        HashMap<String,HashMap<Integer,List<Integer>>> vremena = new HashMap<>();//<Ime, <BrojElem, Vremena[]>>
        for(var s: sorters) vremena.put(s.name, new HashMap<>());
        for (int k=0; k<10; k++) {
            n=1000000;
            for (int i = 0; i < 5; i++) {
                for(int reps = 0; reps<3; reps++) {
                    var niz = getNRandomDoubles(n);
                    //System.out.println("Pokrecem sortiranje za " + n + " elemenata:");
                    for (var info : sorters) {
                        var kopija = new ArrayList<>(niz);
                        var time = timeIt(() -> info.getSorter().sort(kopija));
                        if (!vremena.get(info.name).containsKey(n))
                            vremena.get(info.name).put(n, new ArrayList<>());
                        vremena.get(info.name).get(n).add((int) time);
                        //System.out.println(String.format("[%s]: %d", info.name, time));
                    }
                }
                n += 100000;
            }
        }
        vremena.forEach((naziv, listaVremena)->{
            System.out.println(naziv);
            listaVremena.forEach((_n,vremenaUMs)->{
                var avgVrijeme = vremenaUMs.stream().mapToInt(i->(int)i).average().orElse(0.0);
                System.out.println(String.format("Za %d elemenata prosjek je %f", _n, avgVrijeme));
            });
        });
    }

    public static void main2(String[] args) {
        var nizic = getNRandomDoubles(10).stream().map(d->(double)(Math.round(d*10))).collect(Collectors.toList());
        new SelectionSorter<Double>().sort(nizic);
        System.out.println(nizic);
    }
}
