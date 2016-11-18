echo "[LOG] Starting..."
echo "[LOG] Installing..."
mvn clean install -q
echo "[LOG] Finished installing."
echo "[LOG] Uncompressing..."
tar -xf server/target/tpe-pod-server-1.0-SNAPSHOT-bin.tar.gz -C server/target
chmod u+x server/target/tpe-pod-server-1.0-SNAPSHOT/run-node.sh

tar -xf client/target/tpe-pod-client-1.0-SNAPSHOT-bin.tar.gz -C client/target
chmod u+x client/target/tpe-pod-client-1.0-SNAPSHOT/run-client.sh

echo "[LOG] Done."
