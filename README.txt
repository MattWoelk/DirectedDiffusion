NodeTest tells Node to transmit interest message
Each Node that receives it sends it along and stores it as well
  - if we already have this interest in our list, we don't re-pass it along
  - if we have the type of data requested, we publish this data by transmitting
    EXPLORATORY DATA along the previously created Gradients. The gradient is
    just which node sent you the interest.

- Interest contains name of the neighbouring node which requested and type of data
  desired. It also contains the interval and quantity of data it wants.

- When the sink (the original sender, which will have added this interest to its
  own list, and have itself as the sender so it would know it was the sink.) gets
  the requested exploratory data, it "reinforces" its single fastest neighbour.
  (whichever got the message to it first.) This is done by sending it a
  REINFORCEMENT MESSAGE.

- Any node which receives a reinforcement message will, in turn, reinforce its
  fastest upstream neighbor (so it has kept a list of exploratory data received,
  and which node sent it to it first).


Lists:
- interests               broadcast (datum is null)
- exploratory data        broadcast
- reinforcements          monocast  (datum is null)
- unsent reinforced data  monocast
  - sent to the corresponding reinforcement id
- reinforced data requests(Packet)
- Interests send as the sink
  - so we can refer back and see which was sent by the current node.
- Interests to respond to as the source
  - interests to be responded to as reinforced data

Take in packet. Parse it. It is either an interest, exp data, or reinforcement

Interests: broadcast; known: sender and type and id
ExpData  : broadcast; known: sender and type and id
Reinforce: monocast ; known: to and data
reinfData: monocast ; known: to and data


What to do if the following is received:

- all share the same ID

interest
  - store who sent it for this id (only store one per id)
  - if we have the data, send expdata
  - broadcast (broadcast on run(), so store for each interest whether it has
    been sent yet or not. Then, in the run() command, send all of the unsent
    ones.)

expdata
  - store who sent it for this id (only store one per id)
  - broadcast (broadcast on run(), so store for each expdata whether it has been
    sent yet or not. Then, in the run() command, send all of the unsent ones.)

reinforcement
  - store unsent reinforcement packets in a list
  - in run(), send to the fastest expdata sender for this id

reinforced data
  - store unsent reinforced data packets in a list
  - in run(), send to the fastest interest (reinforcement?) sender for this id





How to do timings:
- Have a run() command for each node which runs each of its functions for one
  iteration.
- A global counter would be incremented which would be used to store timestamps.
  It will be accessible from a static getCurrentTime() function in the Node
  class.

In run(), all of the sending of information happens.
  - send interest
  - send expdata
  - send reinforcement
  - generate data (send reinforced data)
    - wait for the reinforcement data before sending these for the first time
    - after the first time, just send them every few time ticks or something
  - each "ROUND" is one time-stamp's worth of time.

Energy is stored for the entire system in a TotalEnergy
- a broadcast uses the same amount of energy as a monocast



Limitations of the code to keep in mind:
- Nodes currently hold on to every packet they receive forever. It may be good
(for ram-usage) to give them a time limit.
- Each node can only have one type of data which it generates in the current
implementation at any one time.
- All packets are sent from any node all at the same time.
  - the only time which is taken into account is the time required to send all
    nodes at once.
  - this could be changed; we could have a queue of packets; one being sent at
    each run().
  - this will not affect the calculation of energy, which is stored in the
    TotalEnergy variable.
- There is a global counter of id so that none are ever repeated
  - it is just a time-stamp! :)
    - make sure that two nodes don't both send an interest for the same thing at
      the same time. (This would not be a limitation in the real world, because
the time-stamp would be accurate enough that the probability of this happening
would be very small.)
    - we could use a counter instead, then say that in real-life we'd use a
      time-stamp. //TODO
- there can only be one sink for any time of data at any one point in time
  - multiple gradients will NOT be made; only one per type at any point.




Output Legend:
oE       : Interest from sink               (broadcast)
-E       : Interest passing through         (broadcast)

o -E     : Exploratory Data from Source     (broadcast)
- -E     : Exploratory Data passing through (broadcast)

o - -+   : Reinforcement from Sink          (monocast)
- - -+   : Reinforcement passing through    (monocast)

o - - -+ : Reinforced Data from Source      (monocast)
- - - -+ : Reinforced Data passing through  (monocast)
  - - -o : Reinforced Data Hit Sink         (monocast) (doesn't use energy)
