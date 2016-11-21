package ar.itba.edu.pod.hazel.client;

import com.hazelcast.core.IMap;
import model.Entry;

import java.io.*;

public class InputReader {

    public static void parseInput(final IMap<Integer, Entry> entryIMap, String filePath) {
        try {
            final InputStream is = new FileInputStream(filePath);
            final LineNumberReader reader = new LineNumberReader(new InputStreamReader(is));
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                entryIMap.put(reader.getLineNumber(), parseLine(line));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Entry parseLine(final String line) {
        String[] tokens = line.split(",");
        return new Entry(Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4]), Integer.parseInt(tokens[0]), tokens[6], tokens[7], Integer.parseInt(tokens[8]));
    }

}
