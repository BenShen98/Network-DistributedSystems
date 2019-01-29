#!/bin/bash

msgSizes=(20 200 300 400 1000 2000 3000 4000 5000 6000 7000 8000 9000)
# msgSizes=(20 200)
repeat=5
UDPTimeout_s=8

rm -f log/client.csv
rm -f log/client*.log

for msgSize in ${msgSizes[@]}; do
  for ((i=0; i<= ${repeat}; i++)); do
    ### LOGGING STREAM
    echo udp log ${msgSize} $i
    ./scripts/udpclient.sh $1 6666 $msgSize > log/client.msg${msgSize}.${i}.udp.log 2>> log/client.csv
    sleep ${UDPTimeout_s}

    echo rmi log ${msgSize} $i
    ./scripts/rmiclient.sh $1 $msgSize > log/client.msg${msgSize}.${i}.rmi.log 2>> log/client.csv

    ### DISABLE LOGGING STREAM
    echo udp nolog ${msgSize} $i
    ./scripts/udpclient.sh $1 6666 $msgSize > log/client.msg${msgSize}.nolog.${i}.udp.log 2>> log/client.csv
    sleep ${UDPTimeout_s}

    echo rmi nolog ${msgSize} $i
    ./scripts/rmiclient.sh $1 $msgSize > log/client.msg${msgSize}.nolog.${i}.rmi.log 2>> log/client.csv

  done
done
