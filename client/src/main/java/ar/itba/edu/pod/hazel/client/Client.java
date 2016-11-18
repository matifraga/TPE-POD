package ar.itba.edu.pod.hazel.client;

import ar.itba.edu.pod.hazel.AgeMapper;
import ar.itba.edu.pod.hazel.AgeReducer;
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
import java.util.Map;
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
                    break;
                case 3:
                    break;
                case 4:
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

}
