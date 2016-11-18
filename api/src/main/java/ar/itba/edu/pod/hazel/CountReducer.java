package ar.itba.edu.pod.hazel;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

public class CountReducer implements ReducerFactory<String, Integer, Integer> {

    @Override
    public Reducer<Integer, Integer> newReducer(String s) {
        return new Reducer<Integer, Integer>() {
            private int count;

            @Override
            public void beginReduce() { count = 0; }

            @Override
            public void reduce(final Integer value) { count++; }

            @Override
            public Integer finalizeReduce() {
                return count;
            }
        };
    }
}
