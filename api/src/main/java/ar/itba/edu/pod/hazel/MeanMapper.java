package ar.itba.edu.pod.hazel;

import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;
import model.Entry;

import java.util.StringTokenizer;

public class MeanMapper implements Mapper<Integer, Entry, String, Integer> {

    private static final long serialVersionUID = -3535922778765480945L;

    @Override
    public void map(Integer integer, Entry entry, Context<String, Integer> context) {
        context.emit(String.valueOf(entry.getHomeType()), entry.getHomeId());
    }
}

