package ar.itba.edu.pod.hazel;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

public class CountReducer implements ReducerFactory<String, Integer, Double> {

    @Override
    public Reducer<Integer, Double> newReducer(String s) {
        return new Reducer<Integer, Double>() {
            private Set<Integer,Integer> homes;
            private int count;

            @Override
            public void beginReduce() { 
                homes = new HashSet<>();
                count = 0; 
            }

            @Override
            public void reduce(final Integer value) { 
                homes.add(value);
                count++;
            }

            @Override
            public Double finalizeReduce() {
                return (Double)(count/homes.size());
            }
        };
    }
}
