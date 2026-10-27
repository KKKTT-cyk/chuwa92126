# HW13 — 


## 1. Why is a simple loop unsuitable for large batch jobs?

A simple loop is not suitable for large batch jobs for three reasons:

1. **Memory usage:** Loading the entire file into memory may exhaust available memory. Spring Batch uses `ItemReader` and chunk-oriented processing to process data incrementally.
2. **Failure recovery:** If the job fails, a large amount of work may need to be repeated. Spring Batch supports restartability through job metadata and `ExecutionContext`.
3. **Transaction management:** Saving items individually can create transaction overhead and make recovery more difficult. Chunk-oriented processing writes items in batches within transaction boundaries, allowing a failed chunk's database changes to be rolled back.


## 2. Job, Step, JobInstance, JobExecution, and StepExecution

- **Job:** The overall batch process, such as a daily settlement job.
- **Step:** A phase of a Job, such as reading transactions, processing them, or writing results.
- **JobInstance:** A logical instance identified by the Job and its identifying parameters. For example, different runDate values usually create different JobInstances.
- **JobExecution:** A single attempt to run a JobInstance. If a job fails and is restarted, it gets a new JobExecution but remains the same JobInstance.
- **StepExecution:** A record of one Step's execution within a JobExecution, including its status and item counts.


## 3. Chunk-Oriented Processing

Spring Batch processes items in chunks using three stages: reading, processing, and writing. It reads a group of items, processes each item, and writes the results together.

A transaction typically covers one chunk. If the chunk fails, its database changes are rolled back.

The chunk size affects performance, memory usage, and failure recovery:
- **Speed:** Larger chunks can reduce transaction and write overhead.
- **Memory:** Larger chunks generally require more memory to hold items.
- **Failure recovery:** A failed chunk may need to be processed again, so larger chunks can result in more repeated work.

Therefore, the chunk size balances performance, memory usage, and recovery cost.



## 4. Tasklet Step vs. Chunk-Oriented Step

A **Tasklet Step** executes a single task, such as deleting temporary files or running a stored procedure. It is suitable for operations that do not require processing individual items in chunks.

A **Chunk-Oriented Step** reads, processes, and writes items in groups, typically within a transaction for each chunk. It is suitable for processing large datasets, such as reading transaction records and saving results to a database.



## 5. ItemReader, ItemProcessor, and ItemWriter

- **ItemReader:** Reads one item at a time. It returns `null` when no more input is available.
- **ItemProcessor:** Processes an item and returns the result. Returning `null` means the item is filtered out and will not be passed to the writer.
- **ItemWriter:** Receives a `Chunk` of items rather than a single item, allowing batch writes to improve efficiency and work within chunk-level transaction boundaries.


## 6. Filtering, Skipping, and Retrying

**Filtering vs. Skipping:**

- **Filtering:** An item is intentionally excluded by business rules. For example, a processor returns `null` for a transaction with a zero amount.
- **Skipping:** An item is ignored because a configured error occurs. For example, a malformed CSV record causes a `FlatFileParseException`.

**Skip vs. Retry:**

- **Skip:** The job ignores an item with an allowed error and continues processing. It is suitable for permanently invalid records.
- **Retry:** The job attempts an operation again because the error may be temporary. For example, a temporary database connection failure may succeed on retry.


## 7. Why Does a Reader Need `@StepScope`?

A reader that uses `#{jobParameters['inputFile']}` must be declared with `@StepScope` because job parameters are available at runtime, not when the application context is initially created.

`@StepScope` delays the reader's initialization until the Step executes, allowing Spring to resolve the `inputFile` parameter correctly. It also allows the reader to be configured with different file paths for different job executions.



## 8. Restarting a Failed Job

When a failed job is restarted with the same identifying parameters, it uses the same `JobInstance` but creates a new `JobExecution`. Spring Batch can resume processing using saved execution state.

- **Reader:** Must support saving and restoring its state, typically through the `ExecutionContext`, to resume from the appropriate position.
- **Writer:** Must handle restarts safely, avoiding duplicate writes through transactions or idempotent operations.
- **JobInstanceAlreadyCompleteException:** This exception is thrown when a job instance has already completed successfully and is launched again with the same identifying parameters.


## 8. Restarting a Failed Job

When a failed job is restarted with the same identifying parameters, it uses the same `JobInstance` but creates a new `JobExecution`. Spring Batch can resume processing using saved execution state.

- **Reader:** Must support saving and restoring its state, typically through the `ExecutionContext`, to resume from the appropriate position.
- **Writer:** Must handle restarts safely, avoiding duplicate writes through transactions or idempotent operations.
- **JobInstanceAlreadyCompleteException:** This exception is thrown when a job instance has already completed successfully and is launched again with the same identifying parameters.


## 9. Multi-threaded Step vs. Partitioning

A **multi-threaded step** uses multiple threads to process items concurrently within the same Step. A **partitioned step** divides the work into separate partitions, each processed by its own Step execution.

In a multi-threaded step:
- **Reader:** Must be thread-safe or synchronized to prevent concurrent access problems.
- **Processor:** Must be thread-safe because multiple threads may call it concurrently.
- **Writer:** Must be thread-safe to handle concurrent writes correctly.

Partitioning can simplify parallel processing by giving each worker its own data partition and, when configured appropriately, its own component instances.


## 10. Pitfalls of Using `@Scheduled` in Multiple Instances

1. **Duplicate job launches:** Each application instance runs its own scheduled method, so multiple instances may launch the same job. Use a distributed lock or a centralized scheduler to ensure only one instance starts the job at a time.
2. **Concurrent execution and duplicate processing:** Multiple instances may attempt to process the same data concurrently. Use a shared `JobRepository` to track job executions and coordinate launches with a distributed lock or centralized scheduler.