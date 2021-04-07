import sorters.AllThreadSorter;
import sorters.MaybeLessNaiveSorter;
import sorters.NaiveSorter;
import sorters.SorterBase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
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

    public static void main(String[] args) {

//        SorterBase<Double> sorter1 = new NaiveSorter<>();
        SorterBase<Double> sorter2 = new MaybeLessNaiveSorter<>();
        SorterBase<Double> sorter3 = new AllThreadSorter<>();

        int n = 1000000;

        List<Double> nizic1 = getNRandomDoubles(n);
        List<Double> nizic2 = new ArrayList<>(nizic1);
        List<Double> nizic3 = new ArrayList<>(nizic2);

//        var naive = timeIt(()->sorter1.sort(nizic1));
        //var supernaive = timeIt(()->sorter2.sort(nizic2));
        var javaBog = timeIt(()-> nizic2.sort(Double::compareTo));
        var maybenaive = timeIt(()->sorter3.sort(nizic3));

//        System.out.println("Obicni: "+naive);
        //System.out.println("Paralelosi: "+supernaive);
        System.out.println("MozdaBolje: "+maybenaive);
        System.out.println("java internal: "+javaBog);
    }
}
