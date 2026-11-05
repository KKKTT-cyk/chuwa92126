# Homework 9 - Kafka

## 1. What Is Kafka, and What Problems Does It Solve?

Apache Kafka is a distributed event streaming platform for publishing, storing, and consuming events. It helps decouple services, support asynchronous processing, and buffer data when consumers are slower than producers. Stored events can also be replayed while they remain available under the retention policy.

For example, an order service publishes an OrderCreated event to Kafka. The email service reads it and sends a confirmation email independently. The order service does not need to wait for the email to be sent after Kafka successfully accepts the event.

## 2. What Is a Message (Record), and What Does It Contain?

A Kafka message, also called a record, is a unit of data published to Kafka. It commonly contains a value (the actual data), an optional key, a timestamp, and optional headers for extra metadata. The timestamp can represent creation time or broker append time, depending on configuration. Stored records are also associated with a topic, partition, and offset.

For example, the key could be "123" and the value could be `{"orderId":123,"status":"CREATED"}`. The key is separate from fields inside the value and must be set explicitly. Values do not have to use JSON; applications serialize their data into bytes.

## 3. What Is a Producer?

A producer is an application that publishes records to Kafka topics. It prepares the data and uses a Kafka producer client to serialize and send it, with acknowledgments and retries handled according to configuration. For example, an order service publishes an OrderCreated event without waiting for the email service to process it.

## 4. What Is a Consumer?

A consumer is an application that reads records from Kafka topics and processes them. For example, an email service reads an OrderCreated event and sends a confirmation email. Reading a record does not delete it from Kafka; its retention is controlled separately.

## 5. What Is a Topic, and Why Are Messages Organized into Topics?

A topic is a named stream of records in Kafka. Topics organize messages by purpose, such as orders or payments, so consumers can subscribe to the data they need. Producers specify the destination topic; Kafka does not automatically classify records by their contents. A topic can have multiple producers and consumers.

## 6. What Is a Broker, and Why Can a Cluster Have Multiple Brokers?

A broker is a Kafka server process that receives, stores, and serves records to clients. Multiple brokers can share storage and request load, allowing a cluster to scale. With replication configured, partition replicas on different brokers provide fault tolerance if a broker fails. Having multiple brokers does not mean every record is stored on every broker.

## 7. What Is a Partition, and Why Can a Topic Have Multiple Partitions?

A partition is an ordered log of records within a topic. Each record is appended to one partition. Multiple partitions allow a topic's data and workload to be distributed across brokers and processed in parallel by consumers. Kafka guarantees ordering within a partition, not across the entire topic. Partitioning divides the data; replication creates copies for fault tolerance.

## 8. What Is an Offset?

An offset identifies a record's position within a particular partition. It is not a global message number: different partitions can have the same offset. A record is located using its topic, partition, and offset. Consumers use offsets to track their reading progress.

## 9. What Is a Consumer Group, and How Do Its Members Share Work?

A consumer group is a set of consumers using the same group.id to share consumption of subscribed topics. Each partition is assigned to at most one consumer in the group at a time, while one consumer can handle multiple partitions. Assignments can change when members join, leave, or fail. Different consumer groups can independently read the same partitions and records.

## 10. What Is a Message Key, and How Does It Affect Partition Selection?

A key is an optional field that can be used to group related records. With the usual key-based partitioning, the producer hashes the serialized key to select a partition. The same key goes to the same partition within a topic when the partition count, key serialization, and partitioning rules remain unchanged. An explicit partition or custom partitioner can override this behavior.

For example, using the order ID as the key keeps events for the same order together. Keys do not have to be unique, and different keys can map to the same partition. Without a key, the producer uses its applicable no-key partitioning strategy.

## 11. What Is the Relationship Between a Topic and Its Partitions?

A topic is a named stream of messages divided into one or more partitions, each of which stores an ordered sequence of records.

## 12. What Is the Difference Between a Broker and a Kafka Cluster?

A broker is one Kafka server process, while a cluster is the overall Kafka system containing one or more brokers working together. A broker is not the same concept as a cluster, although a development cluster can have just one broker. With suitable replicas already available, other brokers can take over affected partitions when a broker fails. Without an available replica, those partitions may become unavailable even while other parts of the cluster continue working.

## 13. Do Producers and Consumers Need to Know Each Other?

Producers and consumers do not need to know each other directly because they communicate through Kafka topics. They can evolve independently, as long as they agree on the message format. For example, a new analytics service can subscribe to an existing orders topic without the producer directly calling it. It should use a separate consumer group if it needs independent consumption.

## 14. Who Assigns Offsets, and Why Do Consumers Track Their Position?

The broker acting as the partition leader assigns offsets when records are appended; the producer does not choose them. Consumers track and commit their position for each partition so they can resume after a restart or reassignment. A committed offset identifies the next record to consume. For example, after processing offsets 0, 1, and 2, the consumer commits 3. Each consumer group tracks its own progress. Committing before processing finishes can cause unfinished work to be skipped after a failure.

## 15. Does Kafka Guarantee Strict Ordering Across an Entire Topic?

Not in general. Kafka guarantees record ordering within each partition, not across multiple partitions. A topic with only one partition has a single ordered log. However, a consumer that processes records in parallel may complete its business operations out of order.

## 16. What Happens with Three Partitions and Five Consumers in One Group?

At most three consumers can consume this topic at the same time, because each partition is assigned to at most one consumer in the group. The other two consumers have no partitions from this topic and remain idle for it. They may receive assignments after a rebalance, such as when another member leaves, or handle partitions from other subscribed topics.

## 17. Do Different Consumer Groups Compete for Messages?

No. Different consumer groups consume independently and maintain their own offsets. Each group can read the same records from the subscribed topic, depending on its starting position and the records still available under the retention policy. Reading a record in one group does not remove it or advance another group's position. Consumers within the same group share partitions instead.
