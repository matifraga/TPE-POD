package ar.itba.edu.pod.hazel;

import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;
import model.Entry;

import java.util.StringTokenizer;

public class AgeMapper implements Mapper<Integer, Entry, String, Integer> {

    private static final long serialVersionUID = -5535922778765480945L;

    @Override
    public void map(Integer integer, Entry entry, Context<String, Integer> context) {
        int age = entry.getAge();
        String ageRange;

        if(age < 15) {
            ageRange = "0-14";
        } else if (age < 65) {
            ageRange = "15-65";
        } else {
            ageRange = "65-?";
        }

        context.emit(ageRange, 1);
    }
}
