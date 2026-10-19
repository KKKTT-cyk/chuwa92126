# HW9 - Kafka

The answers follow the assignment's question order. Consumer groups here mean standard Kafka consumer groups using `KafkaConsumer` group subscriptions.

## 1. What is Kafka, and what problems does it solve?

Apache Kafka is a distributed event streaming platform for publishing, storing, and consuming streams of events. It supports asynchronous communication between services, buffers traffic bursts, and lets applications replay retained events. Common uses include messaging, log aggregation, data pipelines, and real-time analytics.

Reference: [Kafka design and motivation](https://kafka.apache.org/41/design/design/).

## 2. What is a Message/Record, and what information can it contain?

A record is one unit of data in Kafka, often representing an event such as an order being created. It can contain:

- **Key:** an optional identifier, such as an order ID, that can influence partition selection.
- **Value:** the payload, such as JSON describing the order.
- **Timestamp:** the event's creation time or broker append time, depending on the topic configuration.
- **Headers:** optional metadata, such as a trace ID or event type.

The producer specifies the destination topic and may specify a partition. A consumed record also exposes its topic, partition, and broker-assigned offset. The producer does not supply that offset.

References: [ProducerRecord](https://kafka.apache.org/41/javadoc/org/apache/kafka/clients/producer/ProducerRecord.html), [ConsumerRecord](https://kafka.apache.org/41/javadoc/org/apache/kafka/clients/consumer/ConsumerRecord.html).

## 3. What is a Producer, and what does it do?

A producer is a client application that writes records to Kafka topics. It serializes the key and value, chooses a destination partition, and sends records to a broker. The producer client can batch records and retry failed requests according to its configuration; the application should check send results and handle failures.

Reference: [KafkaProducer](https://kafka.apache.org/41/javadoc/org/apache/kafka/clients/producer/KafkaProducer.html).

## 4. What is a Consumer, and what does it do?

A consumer is a client application that fetches records from Kafka and processes them, for example by updating a database. It deserializes records and tracks its reading position. Fetching a record does not mean the application's business processing has succeeded.

Reference: [KafkaConsumer](https://kafka.apache.org/41/javadoc/org/apache/kafka/clients/consumer/KafkaConsumer.html).

## 5. What is a Topic, and why classify messages with Topics?

A topic is a named category of records, such as `orders` or `payments`. Topics separate event streams so producers publish to the appropriate category and consumers subscribe to relevant data. Topics can also have different retention and replication settings.

Reference: [Kafka introduction](https://kafka.apache.org/intro/).

## 6. What is a Broker, and why can a cluster have multiple Brokers?

A broker is a Kafka server that stores partition replicas and handles client requests. Multiple brokers distribute storage and traffic. Replicating partitions across brokers also supports recovery from broker failures; availability depends on the replicas and configuration.

Reference: [Kafka replication design](https://kafka.apache.org/41/design/design/#replication).

## 7. What is a Partition, and why might a Topic need multiple Partitions?

A partition is an ordered log within a topic. Records are appended to its end. Multiple partitions let a topic distribute data across brokers and allow parallel reads and writes, increasing capacity and throughput. Partitions are also the unit of work assignment within a consumer group.

Reference: [Kafka topics and partitions](https://kafka.apache.org/22/getting-started/introduction/).

## 8. What is an Offset? Is it a global message number?

An offset identifies a record's position within one partition. It is not a global number across the topic or cluster. For example, partition 0 and partition 1 can both contain offset 10, representing different records. A record is located using its topic, partition, and offset.

Reference: [ConsumerRecord.offset()](https://kafka.apache.org/41/javadoc/org/apache/kafka/clients/consumer/ConsumerRecord.html#offset()).

## 9. What is a Consumer Group, and how do its Consumers divide work?

A consumer group is a set of consumers sharing a `group.id` and acting as one logical subscriber. Kafka assigns each subscribed partition to one consumer in that group at a time. One consumer can own multiple partitions. When consumers join or leave, a rebalance redistributes assignments.

Reference: [Consumer groups and subscriptions](https://kafka.apache.org/41/javadoc/org/apache/kafka/clients/consumer/KafkaConsumer.html).

## 10. What is a Key, and how does it affect partition selection?

A key is an optional identifier attached to a record, such as `order-1001`. With the default producer partitioning logic, no explicit partition, and `partitioner.ignore.keys=false`, Kafka hashes the serialized key to choose a partition. The same key goes to the same partition while the partition count and partitioning logic stay unchanged.

An explicitly specified partition takes precedence. A custom partitioner or ignoring keys can change this behavior. Without a key or explicit partition, the default producer uses sticky partitioning for efficient batching; it does not guarantee per-record round-robin distribution.

Reference: [Producer partitioning configuration](https://kafka.apache.org/41/configuration/producer-configs/).

## 11. What is the relationship between Topic and Partition in one sentence?

A topic is a logical event stream divided into one or more partitions, each storing an ordered portion of that stream.

Reference: [Kafka topics and partitions](https://kafka.apache.org/22/getting-started/introduction/).

## 12. How does a Broker differ from a Kafka cluster?

A broker is one server in a Kafka cluster; the cluster is the overall cooperating system. A cluster can have only one broker for development, but the terms are not equivalent, and production clusters commonly use multiple brokers.

Reference: [Kafka servers and clusters](https://kafka.apache.org/intro/).

## 13. Do Producer and Consumer need to know each other?

No. They communicate through Kafka topics rather than directly with each other. A producer needs the Kafka connection and destination topic, not the consumers' identities or addresses. A consumer needs its subscription and compatible record format, not the producer's identity. They can run independently.

Reference: [Kafka producer and consumer decoupling](https://kafka.apache.org/intro/).

## 14. Does the Producer decide the Offset? Why record the consumption position?

No. The partition's leader broker assigns a record's offset when appending it. Consumers record a separate consumption position for each partition so processing can resume after a restart or reassignment. A committed offset represents where to resume, normally the next record to process, rather than the last processed record.

For example, after successfully processing offsets through 10, the consumer can commit 11. Committing before processing risks skipping unfinished work after a crash; processing before committing can cause replay after a crash, so duplicate handling may be needed.

Reference: [Kafka consumer position and delivery semantics](https://kafka.apache.org/41/design/design/).

## 15. Does Kafka guarantee strict ordering across an entire Topic?

No. Kafka preserves log order within a partition, not across different partitions. Consumers read each partition in offset order, but there is no single ordering between records from different partitions. A single-partition topic provides one log order; application processing must also preserve that order if required.

Reference: [Kafka ordering guarantees](https://kafka.apache.org/20/getting-started/introduction/).

## 16. With 3 Partitions and 5 Consumers in one group, how many can consume?

At most **3 consumers** can simultaneously consume this topic because each partition has only one assigned consumer in the group. For example:

| Consumer | Assignment |
| --- | --- |
| C1 | Partition 0 |
| C2 | Partition 1 |
| C3 | Partition 2 |
| C4 | None for this topic |
| C5 | None for this topic |

The remaining two stay idle for this topic. A later rebalance can assign them partitions if another consumer leaves or partitions are added. They may consume other subscribed topics.

Reference: [Consumer group partition assignment](https://kafka.apache.org/41/javadoc/org/apache/kafka/clients/consumer/KafkaConsumer.html).

## 17. Do two different Consumer Groups compete for a Topic's messages?

No. Each group has independent assignments and committed offsets. Both groups can read the same records; consuming in one group does not remove those records or advance the other group's position. Work is divided among consumers within each group.

Each group can read records still retained in the topic from its own position. It does not automatically read all historical records: when there is no valid committed offset, `auto.offset.reset` determines the starting position, such as `earliest` or `latest`, or raises an error with `none`. Retention policies can remove older records regardless of a group's progress.

References: [Consumer groups](https://kafka.apache.org/20/getting-started/introduction/), [Consumer offset reset configuration](https://kafka.apache.org/41/configuration/consumer-configs/).
