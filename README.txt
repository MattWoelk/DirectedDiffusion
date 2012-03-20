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
- interestsSentAsTheSink
  - so we can refer back and see which was sent by the current node.
- interstsToRespondToAsTheSource
  - interests to be responded to as reinforced data


What to do if the following is received:

- all share the same ID as the original interest which was sent

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





How Timings Are Done:
- Have a run() command for each node which runs each of its functions for each
  iteration.
- A global counter would be incremented which would be used to store timestamps.

In run(), all of the sending of information happens.
  - send interest
  - send expdata
  - send reinforcements
  - send reinforcement data
  - generate data (send reinforced data)
    - wait for the reinforcement data before sending these for the first time
    - after the first time, just send them every assigned period
  - each "ROUND" is one time-stamp's worth of time.

Energy is stored for each node in nodeEnergyUsed
- a broadcast uses the same amount of energy as a monocast
- each message sent uses 1 unit of energy.



Limitations of the code to keep in mind:
- Nodes currently hold on to every packet they receive forever. It may be good
  (for ram-usage) to give them a time limit.
  - this includes nodeLevels.
- Each node can only have one type of data which it generates in the current
  implementation at any one time.
- All packets are sent from any node all at the same time.
  - the only time which is taken into account is the time required to send all
    nodes at once.
  - this could be changed; we could have a queue of packets; one being sent at
    each run().
  - therefore, time-stamps don't really mean anything important in this
    simulator.
  - this will not affect the calculation of energy.
- There is a global counter of id so that none are ever repeated
  - it is just a time-stamp! :)
    - make sure that two nodes don't both send an interest for the same thing at
      the same time. (This would not be a limitation in the real world, because
      the time-stamp would be accurate enough that the probability of this happening
      would be very small.)
    - we could use a counter instead, then say that in real-life we'd use a
      time-stamp. //TODO
- there can only be one sink for any time of data at any one point in time
  - multiple gradients will NOT be made; only one per type at any point in time.




Output Legend:
oE       : Interest from sink               (broadcast)
-E       : Interest passing through         (broadcast)

o -{     : Exploratory Data from Source     (multicast)
- -{     : Exploratory Data passing through (multicast)

o - -+   : Reinforcement from Sink          (monocast)
- - -+   : Reinforcement passing through    (monocast)

o - - -+ : Reinforced Data from Source      (monocast)
- - - -+ : Reinforced Data passing through  (monocast)
  - - -o : Reinforced Data Hit Sink         (receiving; doesn't use energy)


Extension: HDA - Hierarchical Data Aggregation

- Hierarchical levels are stored in each node and in each interest packet
- Sink is level 0
When receiving an interest:
  - if we do not have one for this id yet, keep it
  - if the sending node's id is equal to or more than ours, ignore the packet
  - if the sending node's id is one less than ours, add it to the list
  - if the sending node's id is two or more than ours, delete the list and add this one
When sending exploratory data:
  - MULTICAST to all nodes which are in the list for this id.
  - send the source's id so that the intermediate nodes can count from how many they received them.

When receiving exp data:
  - if this is from a new id, add it. Also, add 1 to the number of connected sources this node has for this id. *List*
  - if this is from a known id but a new source, add it.
  - if you received one with this id from this source already, dump it.

When sending reinforcements:
  - send the number of connected sources this node has for this id.
  - monocast? broadcast? multicast?

When receiving reinforcements:
  - store all received

When sending reinforcement data:
  - send to the parent with the highest number of connected sources for this id.

SIMPLIFICATION:
- just do the "only send expdata to parents" thing and nothing else.
- maybe I'll start with just this and see where things go from there. :)
  - I'll call it: "A Hierarchal exploratory data optimization."
- because of how this simulator works, all nodes which are the closest will send their 

TODO:
[done] add level to node.
[done] add level to interest packet
[done] put logic in to put these two things together.
  [done] store more than one interest packet for a given id with the following logic:
    [done] if none exist with this id, add it and set our level to it+1. (Send along it+1.)
    [done] if some exist with this id and the new one's level is the same as the previous, add it.
    [done] if the new one has an equal or higher level than us, ignore it.
[done] make multicast function
  [] change send-interest function to multicast to all with this id in the list.
  [] make sure multicast still counts as one unit of energy.
[] implement multicast to parent nodes.


QUESTIONS:
For having two sources:
- Will the sink have to wait a certain amount of time for all sources to get to it before sending it's reinforcement?
  - It may have to send reinforcements with separate ids; one for each of the sources.
  - For DD? For HDA?


TODO Basic DD:
[] put in logic to figure out if there is a route from the source to the sink or not.
[] make sure that a node will receive all of the packets in a turn before acting on them!!!!!!!!
