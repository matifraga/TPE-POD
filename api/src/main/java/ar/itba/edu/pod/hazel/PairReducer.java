package ar.itba.edu.pod.hazel;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import java.util.ArrayList;
import java.util.List;

public class PairReducer implements ReducerFactory<Integer, String, List<String>> {

    @Override
    public Reducer<String, List<String>> newReducer(final Integer i) {
        return new Reducer<String, List<String>>() {

            private List<String> ans;
            private List<String> all;

            @Override
            public void beginReduce() {
                ans = new ArrayList<>();
                all = new ArrayList<>();
            }

            @Override
            public void reduce(final String d1) {
                if (!all.isEmpty()){
                    for (String d2: all) {
                        ans.add(String.format("%s + %s", d2, d1));
                    }
                }
                all.add(d1);
            }

            @Override
            public List<String> finalizeReduce() {
                return ans;
            }
        };
    }
}
