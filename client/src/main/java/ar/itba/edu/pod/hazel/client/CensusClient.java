package ar.itba.edu.pod.hazel.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;

import ar.itba.edu.pod.hazel.WordCountMapper;
import ar.itba.edu.pod.hazel.WordCountReducer;

public class CensusClient {
    private static final String MAP_NAME = "52051-53214-raptor";
    private Map<String, String> parameters;

    // el directorio wc dentro en un directorio llamado "resources"
    // al mismo nivel que la carpeta src, etc.
    private static final String[] FILES_DATASET = { "files/refran1.txt", "files/refran2.txt",
            "files/refran3.txt" };

    public static void main(final String[] args) throws InterruptedException, ExecutionException, IOException, URISyntaxException {

        final String name = "52051-53214";
        String pass = System.getProperty("pass");
        if (pass == null) {
            pass = "dev-pass";
        }

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

        System.out.println(client.getCluster());

        // Preparar la particion de datos y distribuirla en el cluster a traves
        // del IMap
        final IMap<String, String> map = client.getMap(MAP_NAME);

        for (final String file : FILES_DATASET) {
            final InputStream is = CensusClient.class.getClassLoader().getResourceAsStream(file);

            final LineNumberReader reader = new LineNumberReader(new InputStreamReader(is));

            final StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }

            map.put(file, sb.toString());
        }

        // Ahora el JobTracker y los Workers!
        final JobTracker tracker = client.getJobTracker("default");

        // Ahora el Job desde los pares(key, Value) que precisa MapReduce
        final KeyValueSource<String, String> source = KeyValueSource.fromMap(map);

        final Job<String, String> job = tracker.newJob(source);

        // Orquestacion de Jobs y lanzamiento
        final ICompletableFuture<Map<String, Integer>> future = job
                                                             .mapper(new WordCountMapper())
                                                             .reducer(new WordCountReducer())
                                                             .submit();

        // Tomar resultado e Imprimirlo
        final Map<String, Integer> a = future.get();

        System.out.println(a);

        System.exit(0);
    }
}
