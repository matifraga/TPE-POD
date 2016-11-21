package ar.itba.edu.pod.hazel;

import com.hazelcast.mapreduce.Collator;

import java.util.*;

public class LiterateCollator implements Collator<Map.Entry<String, Double>, List<Map.Entry<String, Double>>> {

        int N;

        public LiterateCollator(int limit){
            this.N = limit;
        }

        @Override
        public List<Map.Entry<String, Double>> collate(Iterable<Map.Entry<String, Double>> values) {
            List<Map.Entry<String, Double>> aux = new ArrayList<>();
            for (Map.Entry<String, Double> value : values) {
                aux.add(value);
            }
            Collections.sort(aux,new DoubleComparator());
            return aux.subList(0,N);
        }

        private static class DoubleComparator
                implements Comparator<Map.Entry<String, Double>> {

            @Override
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                return Double.compare(o2.getValue(), o1.getValue());
            }
        }
}
