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
- interests (with sender node, type, id, and ifsent)
- exploratory data (which sender node, type, id, and ifsent)
- unsent reinforcements
- unsent reinforced data

Take in packet. Parse it. It is either an interest, exp data, or reinforcement

Interests: broadcast; known: sender and type and id
ExpData  : broadcast; known: sender and type and id
Reinforce: monocast ; known: to and data
reinfData: monocast ; known: to and data


What to do if the following is received:

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
  - in run(), send to the fastest interest sender for this id





How to do timings:
- Have a run() command for each node which runs each of its functions for one
  iteration.
- A global counter would be incremented which would be used to store timestamps.
  It will be accessible from a static getCurrentTime() function in the Node
  class.

In run(), all of the sending of information happens.
