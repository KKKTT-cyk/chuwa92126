# Q1.
Kafka is a distributed event streaming platform, a durable high throughput log that some apps write messages to and others read from.
Message stay after being read so multiple can read the same data or replay it. Used for decoupling services async processing absorbing traffic spikes, log collection and real time stream processing.

# Q2.
record is basic unit of data of kafka, representing one event, it contains key, value, heasders, ts.

# Q3.
A product is an app that sends message to kafka, it serializes data picks the topic and partition, batches and compresses messages for throughput, and handles reliability through acks, retries and idempotence.

# Q4.
A consumer is an app that reads and processes messages from kafka. It subscribes to topics, pulls messages at its own space, and commits offsets to track progress. Consumers in the same consume group split a topics partitions between them for parallel processing.

# Q5.
A topic is named category messages, split into partitions for scaling, why use topic, consumers only read the data they care about, each topic can have its own partition count, replication and retention, isolates workloads and keeps data organized like tables in database.

# Q6.
A broker is a single kafka server, it stores partitions and handles read and write request, a cluster has multiple brokers to spread storage and load, increase throughput and stay available when one fails.

# Q7.
Partition is an ordered append only log that holds part of a topics message, multiple partitions let a topic be spread across brokers and processed in parallel by multiple producers and consumers.

# Q8.
offset is a position of a message within a single partition, not a global number.

# Q9.
Consumer group is a set of consumers sharing the same group id, the topics partitions are divided among them and each partition is read by only one consumer in the group at a time.

# 10.
key is an optional value attached to a message, by default, a partition is hash key of number of partitions so the same key always goes to the same partition, messages without a key are spread across partitions.

# Q11.
topic is a logical category and partitions are its physical shards, one topic is made up of one or more partitions.

# Q12.
A broker is one server, a cluster is a group of brokers, a single broker can run as a one node cluster but has no fault tolerance.

# Q13.
producers and consumers they dont know each, they only talk to kafka and agree on a topic name.

# Q14.
The broker assigns the offset when a message is written to a partition, not the producer.
consumers commit their position so that after a restart or rebalance they continue where they left off.

# Q15.
incorrect. order is only guaranteed within a single partition, to keep related message in order, give them same key.

# Q16.
At most 3 consumers are active, one per partition, the other 2 sit idle and take over if an active consumer fails.

# Q17.
they dont compete, each group independently receives all messages in the topic and keeps its own offsets.











