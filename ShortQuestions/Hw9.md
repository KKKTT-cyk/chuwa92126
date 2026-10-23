## 1. 什么是 Kafka？它通常⽤来解决什么问题？
Kafka is a distributed event streaming platform that allows applications to publish, store, and consume streams of events. It is commonly used to handle large volumes of real-time data and enable asynchronous communication between services.



## 2. 什么是消息（Message/Record）？⼀条 Kafka 消息通常可以包含哪些信息？
A message is a unit of data that producers send to Kafka and consumers can read. It typically contains a value, which is the actual data,an optional key, which is commonly used to select a partition; a timestamp; and optional headers containing metadata, such as a trace ID or event type.


## 3. 什么是⽣产者（Producer）？它在 Kafka 中承担什么职责？
A producer is a application that publishes messages to Kafka topics. It creates records, serializes their keys and values into bytes, and determines which partition each record goes to, often based on the key. It also batches records for efficient transmission, sends them to the appropriate brokers, and handles acknowledgments and retries according to its configuration.

## 4. 什么是消费者（Consumer）？它在 Kafka 中承担什么职责？
A consumer is a client application that reads and processes messages from Kafka topics. It fetches records from assigned partitions, deserializes their keys and values, and performs business logic, such as updating inventory or sending notifications. It also tracks its position using offsets and can commit them so processing can resume after a restart.

## 5. 什么是主题（Topic）？为什么 Kafka 要使⽤ Topic 对消息进⾏分类？
A topic is a named category of messages in Kafka, Producers publish records to a topic, and consumers subscribe to the topics they need. Topics organize data so different event streams can be managed and consumed independently, with their own retention settings and access permissions. Each topic is divided into partitions, allowing Kafka to distribute its data across brokers and support parallel processing.

## 6. 什么是 Broker？Kafka 集群中为什么可以有多个 Broker？
A broker is a server process in a Kafka cluster that stores partition replicas, handles producer writes and consumer reads, and participates in replication. A cluster can have multiple brokers to distribute storage and processing workloads, allowing Kafka to scale horizontally. Partition replicas can also be placed on different brokers, so if one broker fails, another broker with an eligible replica can take over as the partition leader, improving availability.



## 7. 什么是分区（Partition）？⼀个 Topic 为什么可能需要多个 Partition？
A partition is an ordered, append-only log that stores topic’s records. Each record receives an offset that identifies its position within that partition. A topic can have multiple partitions to distribute storage and traffic across brokers and allow consumers in the same consumer group to process data in parallel. This increases throughput and scalability, but Kafka guarantees record ordering only within a partition, not across the entire topic.


## 8. 什么是 Offset？它表示的是消息的全局编号，还是消息在某个 Partition 中的位置？
An offset is a record number assigned to a record that represents its position within a partition. It is not a global message ID. A record is uniquely identified within a Kafka cluster by its topic, partition, and offset. Consumers track and commit offsets to record their progress and resume reading after a restart.

## 9. 什么是消费者组（Consumer Group）？同⼀个消费者组中的多个消费者如何分⼯？
A consumer group is a set of consumers that share the same group.id and work together to consume topics. Kafka distributes partitions among the consumers so they can process messages in parallel. In the standard consumer group model, each partition is assigned to only one consumer within the group at a time, while one consumer can handle multiple partitions.




## 10. 什么是消息键（Key）？Key 会如何影响消息被写⼊哪个 Partition？
A key is an optional field in a Kafka record that often identifies a related entity, such as an order ID or user ID. When no partition is explicitly specified, the producer typically hashes the key to select a partition. Records with the same key therefore go to the same partition, provided the partition count and partitioning strategy remain unchanged, allowing related events to maintain their order within that partition. If no key is provided, the producer uses its configured partitioning strategy to distribute records.

## 11. Topic 和 Partition 有什么区别？请⽤⼀句话描述它们之间的关系。
A topic is a named stream of related records, while a partition is an ordered portion of that stream.


## 12. Broker 和 Kafka 集群有什么区别？⼀个 Broker 是否就等于⼀个 Kafka 集群？
A broker is a single Kafka server process, while a Kafka cluster is a group of brokers working together to store and serve records.

## 13. Producer 和 Consumer 是否需要彼此知道对⽅是谁？请说明原因。
No. Producers and consumers do not need to know each other. Producers publish records to topics, while consumers subscribe to those topics and read the records. They need to agree on the topic and message format, but not each other’s identity or location.




## 14. Offset 是由 Producer 决定的吗？消费者为什么需要记录⾃⼰的消费位置？
No. An offset is assigned by the partition’s leader broker when a record is appended to its log, not by the producer. Consumers track their position in each partition so they know which record to read next.


## 15. “Kafka 能保证整个 Topic 中的所有消息严格有序。”这句话是否正确？请说明 Kafka 的顺序保证范围。
No, Kafka guarantees record ordering within a partition, not across an entire topic with multiple partitions. Each partition has its own ordered log and offsets, so records in different partitions have no guaranteed relative order. A topic with only one partition provides a single log order, but reduces opportunities for parallel consumption.





## 16. 如果⼀个 Topic 有 3 个 Partition，同⼀个消费者组中有 5 个 Consumer，那么最多有⼏个 Consumer 能同时消费这个Topic？其余 Consumer 会怎样？
At most 3 consumers can consume this topic within the same consumer group, because each partition can be assigned to only one consumer in that group at a time. The remaining 2 consumers stay idle for this topic.


## 17. 两个不同的消费者组订阅同⼀个 Topic，它们会互相抢消息吗？请解释它们各⾃能看到哪些消息。
No. Two different consumer groups do not compete for messages. Each group maintains its own committed offsets for each partition and can independently read all available records in the topic, depending on its starting position and Kafka’s retention policy. Consuming a record in one group does not remove it or advance the other group’s offsets.
