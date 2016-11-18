#!/bin/bash

java -cp 'lib/jars/*' -Daddresses='127.0.0.1'  "ar.itba.edu.pod.hazel.client.CensusClient" $*

