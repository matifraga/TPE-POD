#!/bin/bash

java -cp 'lib/jars/*' -Daddresses='127.0.0.1' -Dquery=1 -Dn=5 -Dprov='Chaco' -Dtope=1000 -DinPath='dataset-1000.csv' -DoutPath='output.txt' "ar.itba.edu.pod.hazel.client.Client" $*

