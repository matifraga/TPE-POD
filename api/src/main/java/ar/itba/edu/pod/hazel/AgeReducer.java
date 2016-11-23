package ar.itba.edu.pod.hazel;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

public class AgeReducer implements ReducerFactory<String, Integer, Integer> {

    @Override
    public Reducer<Integer, Integer> newReducer(String s) {
        return new Reducer<Integer, Integer>() {
            private int sum;

            @Override
            public void beginReduce() { sum = 0; }

            @Override
            public void reduce(final Integer value) { sum += value; }

            @Override
            public Integer finalizeReduce() {
                System.out.println("Finalize reduce");
                return sum;
            }
        };
    }
}
