package ar.itba.edu.pod.hazel;

import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;
import model.Entry;

import java.util.StringTokenizer;

public class LiterateMapper implements Mapper<Integer, Entry, String, Integer> {

    private static final long serialVersionUID = -1292922778765480945L;

    @Override
    public void map(Integer integer, Entry entry, Context<String, Integer> context) {
        context.emit(entry.getDepartmentName(), entry.getLiterate());
    }
}
