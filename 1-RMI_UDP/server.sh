#!/bin/bash

msgSizes=(20 200 300 400 1000 2000 3000 4000 5000)
# msgSizes=(20 200)
repeat=5

rm -f log/server.csv
rm -f log/server*.log

for msgSize in ${msgSizes[@]}; do
  for ((i=0; i<= ${repeat}; i++)); do

    ### LOGGING STREAM
    echo udp log ${msgSize} $i
    ./scripts/udpserver.sh 6666 > log/server.msg${msgSize}.${i}.udp.log 2>> log/server.csv

    echo rmi log ${msgSize} $i
    ./scripts/rmiserver.sh > log/server.msg${msgSize}.${i}.rmi.log 2>> log/server.csv

    ### DISABLE LOGGING STREAM
    echo udp nolog ${msgSize} $i
    ./scripts/udpserver.sh 6666 nolog > log/server.msg${msgSize}.nolog.${i}.udp.log 2>> log/server.csv

    echo rmi nolog ${msgSize} $i
    ./scripts/rmiserver.sh nolog > log/server.msg${msgSize}.nolog.${i}.rmi.log 2>> log/server.csv

  done
done
