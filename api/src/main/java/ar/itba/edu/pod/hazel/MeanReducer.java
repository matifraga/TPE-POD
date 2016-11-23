package ar.itba.edu.pod.hazel;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import java.util.HashSet;
import java.util.Set;

public class MeanReducer implements ReducerFactory<String, Integer, Double> {

    @Override
    public Reducer<Integer, Double> newReducer(String s) {
        return new Reducer<Integer, Double>() {
            private Set<Integer> homes;
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
                System.out.println("Finalize reduce");
                return ((double)count/homes.size());
            }
        };
    }
}
