package ar.itba.edu.pod.hazel;

import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;
import model.Entry;

public class PairMapper implements Mapper<Integer, Entry, String, Integer> {

    private static final long serialVersionUID = -5535922773335480945L;

    @Override
    public void map(Integer integer, Entry entry, Context<String, Integer> context) {
        context.emit(new StringBuilder(entry.getDepartmentName()).append(" (").append(entry.getProvinceName()).append(")").toString(), 1);
    }
}
