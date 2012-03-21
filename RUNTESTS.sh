for rng in 1 2 3 4 5
do
  for dim in 10 15 20
  do
    javac NodeTest.java && java NodeTest --filename-last-only=data/DD_dim="$dim"_rng="$rng"_"$(date +%s)".csv --dimension="$dim" --numNodes=55 --range=$rng --suppressOutput
  done
done
