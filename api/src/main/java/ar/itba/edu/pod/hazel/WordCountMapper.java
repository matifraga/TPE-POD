package ar.itba.edu.pod.hazel;

import java.util.StringTokenizer;

import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

// Parametrizar con los tipos de keyInput, ,valueInput, keyoutput, valueOutput
public class WordCountMapper implements Mapper<String, String, String, Integer> {
    private static final long serialVersionUID = -5535922778765480945L;

    @Override
    public void map(final String keyinput, final String valueinput, final Context<String, Integer> context) {
        final StringTokenizer tokenizer = new StringTokenizer(valueinput, "\n\r\t ,:;.()=><");
        System.out.println(String.format("Key Input: %s", keyinput));
        System.out.println(String.format("Value Input: '%s'", valueinput));
        while (tokenizer.hasMoreTokens()) {
            final String word = tokenizer.nextToken().toLowerCase();

            context.emit(word, 1);
            System.out.println(String.format("Mapping (%s, 1)", word));
        }
    }
}
