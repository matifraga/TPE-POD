package ar.itba.edu.pod.hazel;

import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;
import model.Entry;

import java.util.StringTokenizer;

public class CountMapper implements Mapper<Integer, Entry, String, Integer> {

    private static final long serialVersionUID = -5535922778765480333L;

    private String prov;

    public CountMapper(String province) {
        this.prov = province;
    }

    @Override
    public void map(Integer integer, Entry entry, Context<String, Integer> context) {
        String eProv = entry.getProvinceName();

        if(eProv.equals(this.prov))
            context.emit(entry.getDepartmentName(), 1);
    }
}
