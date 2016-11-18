package ar.itba.edu.pod.hazel.client;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

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

public class CensusClient {
    private static final String MAP_NAME = "raptor";
    private Map<String, String> parameters;

    // el directorio wc dentro en un directorio llamado "resources"
    // al mismo nivel que la carpeta src, etc.

    private static final String FILE = "files/dataset-1000.csv";

    public static void main(final String[] args) throws InterruptedException, ExecutionException, IOException, URISyntaxException {

        final String name = "52051-53214";
        final String pass = "dev-pass";

        final ClientConfig ccfg = new ClientConfig();
        ccfg.getGroupConfig().setName(name).setPassword(pass);

        // no hay descubrimiento automatico,
        // pero si no decimos nada intentará usar LOCALHOST
        final String addresses = System.getProperty("addresses");
        if (addresses != null) {
            final String[] arrayAddresses = addresses.split("[,;]");
            final ClientNetworkConfig net = new ClientNetworkConfig();
            net.addAddress(arrayAddresses);
            ccfg.setNetworkConfig(net);
        }
        final HazelcastInstance client = HazelcastClient.newHazelcastClient(ccfg);

        // Preparar la particion de datos y distribuirla en el cluster a traves
        // del IMap
        final IMap<Integer, Entry> map = client.getMap(MAP_NAME);

        // Rellenamos el IMap
        InputReader.parseInput(map, FILE);

        // Ahora el JobTracker y los Workers!
        final JobTracker tracker = client.getJobTracker("default");

        // Ahora el Job desde los pares(key, Value) que precisa MapReduce
        final KeyValueSource<Integer, Entry> source = KeyValueSource.fromMap(map);

        final Job<Integer, Entry> job = tracker.newJob(source);

        // Orquestacion de Jobs y lanzamiento
        final ICompletableFuture<Map<String, Integer>> future = job
                                                             .mapper(new AgeMapper())
                                                             .reducer(new AgeReducer())
                                                             .submit();

        // Tomar resultado e Imprimirlo
        final Map<String, Integer> a = future.get();

        System.out.println(a);

        System.exit(0);
    }
}
