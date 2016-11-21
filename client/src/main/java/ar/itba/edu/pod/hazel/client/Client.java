package ar.itba.edu.pod.hazel.client;

import ar.itba.edu.pod.hazel.*;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;
import model.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class Client {

    private static final String MAP_NAME = "raptor";
    private static PrintWriter printWriter;
    private static Logger logger = LoggerFactory.getLogger(Client.class);

    public static void main(final String[] args) {

        try {
            final String name = System.getProperty("name", "52051-53214");
            final String pass = System.getProperty("pass", "147852");

            final ClientConfig ccfg = new ClientConfig();
            ccfg.getGroupConfig().setName(name).setPassword(pass);

            final String addresses = System.getProperty("addresses", "127.0.0.1");

            if (addresses != null) {
                final String[] arrayAddresses = addresses.split("[,;]");
                final ClientNetworkConfig net = new ClientNetworkConfig();
                net.addAddress(arrayAddresses);
                ccfg.setNetworkConfig(net);
            }
            final HazelcastInstance client = HazelcastClient.newHazelcastClient(ccfg);

            final int query = Integer.valueOf(System.getProperty("query"));

            final String outFilePath = System.getProperty("outPath");
            printWriter = new PrintWriter(outFilePath, "UTF-8");

            if (query < 1 || query > 5) {
                System.exit(1);
            }

            switch (query) {
                case 1:
                    query1(client);
                    break;
                case 2:
                    query2(client);
                    break;
                case 3:
                    query3(client);
                    break;
                case 4:
                    query4(client);
                    break;
                case 5:
                    break;
            }

            printWriter.close();

            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void query1(HazelcastInstance client) throws ExecutionException, InterruptedException {

        final String inFilePath = System.getProperty("inPath");

        final IMap<Integer, Entry> map = client.getMap(MAP_NAME);
        logger.info("Inicio de la lectura del archivo");
        InputReader.parseInput(map, inFilePath);
        logger.info("Fin de lectura del archivo");

        final JobTracker tracker = client.getJobTracker("default");
        final KeyValueSource<Integer, Entry> source = KeyValueSource.fromMap(map);
        final Job<Integer, Entry> job = tracker.newJob(source);

        logger.info("Inicio del trabajo map/reduce");
        final ICompletableFuture<Map<String, Integer>> future = job
                .mapper(new AgeMapper())
                .reducer(new AgeReducer())
                .submit();

        final Map<String, Integer> a = future.get();
        logger.info("Fin del trabajo map/reduce");

        printWriter.println("0-14 = " + a.getOrDefault("0-14", 0));
        printWriter.println("15-65 = " + a.getOrDefault("15-65", 0));
        printWriter.println("65-? = " + a.getOrDefault("65-?", 0));

    }

    private static void query2(HazelcastInstance client) throws ExecutionException, InterruptedException {

        final String inFilePath = System.getProperty("inPath");

        final IMap<Integer, Entry> map = client.getMap(MAP_NAME);
        logger.info("Inicio de la lectura del archivo");
        InputReader.parseInput(map, inFilePath);
        logger.info("Fin de lectura del archivo");

        final JobTracker tracker = client.getJobTracker("default");
        final KeyValueSource<Integer, Entry> source = KeyValueSource.fromMap(map);
        final Job<Integer, Entry> job = tracker.newJob(source);

        logger.info("Inicio del trabajo map/reduce");
        final ICompletableFuture<Map<String, Double>> future = job
                .mapper(new MeanMapper())
                .reducer(new MeanReducer())
                .submit();

        final Map<String, Double> a = future.get();
        logger.info("Fin del trabajo map/reduce");

        String[] keys = a.keySet().toArray(new String[a.size()]);
        Arrays.sort(keys);

        for(String key: keys) {
            printWriter.printf("%s = %.2f\n", key, a.get(key));
        }

    }

    private static void query3(HazelcastInstance client) throws ExecutionException, InterruptedException {

        final String inFilePath = System.getProperty("inPath");
        final Integer N = Integer.valueOf(System.getProperty("n"));

        final IMap<Integer, Entry> map = client.getMap(MAP_NAME);
        logger.info("Inicio de la lectura del archivo");
        InputReader.parseInput(map, inFilePath);
        logger.info("Fin de lectura del archivo");

        final JobTracker tracker = client.getJobTracker("default");
        final KeyValueSource<Integer, Entry> source = KeyValueSource.fromMap(map);
        final Job<Integer, Entry> job = tracker.newJob(source);

        logger.info("Inicio del trabajo map/reduce");
        final ICompletableFuture<List<Map.Entry<String,Double>>> future = job
                .mapper(new LiterateMapper())
                .reducer(new LiterateReducer())
                .submit(new LiterateCollator(N));

        final List<Map.Entry<String,Double>> a = future.get();
        logger.info("Fin del trabajo map/reduce");

        for(Map.Entry<String,Double> key: a) {
            printWriter.printf("%s = %.2f\n", key.getKey(), key.getValue());
        }

    }

    private static void query4(HazelcastInstance client) throws ExecutionException, InterruptedException {

        final String inFilePath = System.getProperty("inPath");
        final String province = System.getProperty("prov");
        final Integer tope = Integer.valueOf(System.getProperty("tope"));

        final IMap<Integer, Entry> map = client.getMap(MAP_NAME);
        logger.info("Inicio de la lectura del archivo");
        InputReader.parseInput(map, inFilePath);
        logger.info("Fin de lectura del archivo");

        final JobTracker tracker = client.getJobTracker("default");
        final KeyValueSource<Integer, Entry> source = KeyValueSource.fromMap(map);
        final Job<Integer, Entry> job = tracker.newJob(source);

        logger.info("Inicio del trabajo map/reduce");
        final ICompletableFuture<List<Map.Entry<String,Integer>>> future = job
                .mapper(new CountMapper(province))
                .reducer(new CountReducer())
                .submit(new CountCollator(tope));

        final List<Map.Entry<String,Integer>> a = future.get();
        logger.info("Fin del trabajo map/reduce");

        for(Map.Entry<String,Integer> key: a) {
            printWriter.printf("%s = %d\n", key.getKey(), key.getValue());
        }

    }
}
