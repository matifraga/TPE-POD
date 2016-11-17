echo "Starting..."
mvn clean install

tar -xf server/target/clase8-ejer01-server-1.0-SNAPSHOT-bin.tar.gz -C server/target
chmod u+x server/target/clase8-ejer01-server-1.0-SNAPSHOT/run-node.sh


tar -xf client/target/clase8-ejer01-client-1.0-SNAPSHOT-bin.tar.gz -C client/target
chmod u+x client/target/clase8-ejer01-client-1.0-SNAPSHOT/run-client.sh

echo "Done"
