### Question 1.

Apache Kafka is a distributed platform designed for handling real-time event streams and data processing. It allows applications to communicate asynchronously by letting producers send events and consumers process them separately. Kafka is widely used because it supports large amounts of data, scales efficiently, provides reliable storage, and continues operating even when individual servers fail.

### Question 2.

A Kafka record is the smallest piece of information stored in Kafka. Each record includes a value and may also contain a key, headers, and a timestamp. Once Kafka stores the record, it receives a specific partition and offset within a topic. The key generally determines where the record is stored, while the value holds the actual information being transmitted.

### Question 3.

A Kafka producer is a service or application that sends records to Kafka topics. It handles tasks such as converting data into the appropriate format, choosing a partition, transmitting records to Kafka brokers, and managing retries or acknowledgments. Producers operate independently of consumers and do not need to know which applications will eventually read their messages.

### Question 4.

A Kafka consumer is an application responsible for reading and processing records from Kafka topics. Rather than receiving messages automatically, consumers actively request them using the `poll()` method. They also manage offsets to keep track of processed records, allowing them to continue from a previously recorded position after a restart or failure.

### Question 5.

A Kafka topic serves as a named channel where related events or messages are organized. Producers write data to specific topics, while consumers read from the topics they subscribe to. By separating messages into topics, Kafka makes it easier to manage different event types, configure processing independently, and scale applications.

### Question 6.

A Kafka broker is an individual server that stores partitions and responds to requests from producers and consumers. A collection of brokers forms a Kafka cluster. Running multiple brokers allows Kafka to distribute workloads, replicate data, increase processing capacity, and remain available when a broker experiences a failure.

### Question 7.

A Kafka partition is a sequential, append-only collection of records belonging to a topic. Topics can be divided into multiple partitions, allowing records to be distributed across different brokers and processed concurrently. Increasing the number of partitions can improve performance and scalability, but Kafka only preserves the order of records within an individual partition.

### Question 8.

An offset represents the location of a record inside a particular Kafka partition. Since offsets are maintained separately for each partition, they are not unique across an entire topic or cluster. A record can be uniquely located by specifying its topic, partition number, and offset.

### Question 9.

A consumer group consists of multiple consumers that share the same group ID and cooperate to process records from Kafka topics. Kafka assigns partitions among the consumers so that each partition is handled by only one consumer within that group at a time. A single consumer may process several partitions, making consumer groups useful for distributing workloads and increasing processing capacity.

### Question 10.

A message key is an optional part of a Kafka record that influences which partition receives the message. Kafka commonly applies a hashing algorithm to the key to determine the destination partition. When the partitioning configuration remains unchanged, records sharing the same key are generally placed in the same partition, helping preserve their processing order.

### Question 11.

A topic represents a logical grouping of related Kafka messages, whereas a partition is the actual ordered log where those messages are stored. Every topic contains at least one partition, and having multiple partitions allows Kafka to distribute data and support parallel consumption.

### Question 12.

A broker refers to one Kafka server responsible for storing data and handling client requests. A cluster represents the complete Kafka environment made up of one or more brokers. Although Kafka can operate with just one broker, production environments commonly use several brokers to improve reliability, distribute traffic, and provide fault tolerance.

### Question 13.

No. Kafka producers and consumers do not communicate directly or need information about each other. Producers simply publish records to topics, and consumers independently subscribe to those topics to retrieve data. This separation makes it possible to develop, deploy, and scale each service without creating direct dependencies between them.

### Question 14.

No. Kafka brokers generate offsets automatically as records are added to partitions. Consumers are responsible for maintaining their own progress by tracking and committing offsets. These committed positions help consumer groups resume processing after interruptions instead of having to read every record again from the beginning.

### Question 15.

No. Kafka maintains message ordering at the partition level rather than across an entire topic. Records with identical keys are typically directed to the same partition, which helps maintain ordering for related events. If an application requires strict ordering across all messages, it can use a single partition and process records sequentially, although doing so reduces opportunities for parallel processing.

### Question 16.

Only three consumers can actively read records from the topic simultaneously because the topic contains three partitions. Kafka allows each partition to be assigned to just one consumer within the same consumer group. Therefore, if the group contains five consumers, three can receive partition assignments while the other two remain idle until a rebalance changes the assignments.

### Question 17.

No. Consumers belonging to different consumer groups operate independently, so they can read the same messages without interfering with one another. Each group maintains its own offsets and processing progress. Partition assignments are shared among consumers within the same group, but separate groups can consume identical records at their own pace.