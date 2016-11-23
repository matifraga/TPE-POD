package ar.itba.edu.pod.hazel;

import com.hazelcast.mapreduce.Collator;

import java.util.*;

public class PairCollator implements Collator<Map.Entry<Integer, List<String>>, List<Map.Entry<Integer, List<String>>>> {

    @Override
    public List<Map.Entry<Integer, List<String>>> collate(Iterable<Map.Entry<Integer, List<String>>> values) {
        List<Map.Entry<Integer, List<String>>> aux = new ArrayList<>();
        for (Map.Entry<Integer, List<String>> value : values) {
            if(!value.getValue().isEmpty())
                aux.add(value);
        }
        Collections.sort(aux,new AuxComparator());
        return aux;
    }

    private static class AuxComparator
            implements Comparator<Map.Entry<Integer, List<String>>> {

        @Override
        public int compare(Map.Entry<Integer, List<String>> o1, Map.Entry<Integer, List<String>> o2) {
            return o1.getKey().compareTo(o2.getKey());
        }
    }
}