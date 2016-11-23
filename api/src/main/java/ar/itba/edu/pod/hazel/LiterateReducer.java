package ar.itba.edu.pod.hazel;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

public class LiterateReducer implements ReducerFactory<String, Integer, Double> {

    @Override
    public Reducer<Integer, Double> newReducer(String s) {
        return new Reducer<Integer, Double>() {
            private int count;
            private int total;

            @Override
            public void beginReduce() { 
                count = 0;
                total = 0; 
            }

            @Override
            public void reduce(final Integer value) { 
                count+=value==2?1:0;
                total++; 
            }

            @Override
            public Double finalizeReduce() {
                System.out.println("Finalize reduce");
                return ((double)count/total); }
        };
    }
}
