Question 1:
Checked exceptions are enforced by the compiler as recoverable conditions the caller should anticipate — code won't compile unless the exception is either caught or declared with throws. Unchecked exceptions (subclasses of RuntimeException) typically represent programming errors (like NullPointerException or ArrayIndexOutOfBoundsException) rather than expected recoverable conditions, so the compiler doesn't force handling — though you still should handle them defensively.

Question 2:
Catch blocks are checked top-to-bottom, and the first matching type wins. Since a superclass catch block (e.g., catch (Exception e)) also matches any of its subclasses, placing it first would make the subclass's catch block unreachable — the material shows this causes a compile error, since the compiler detects that the subclass catch can never execute. The specific exception must come first so it actually gets a chance to handle its case before the broader catch takes over.

Question 3:
Resources declared in a try-with-resources block implement AutoCloseable, and their close() method is called automatically when the block exits — whether it exits normally or because of an exception. With multiple resources, they're closed in reverse order of declaration. This guarantees cleanup happens even if an exception is thrown, without needing an explicit finally block.

Question 4:

- `throw` is used **inside a method body** to actually raise a specific exception instance at that point in the code (e.g., `throw new IllegalArgumentException(...)`).
- `throws` appears **in the method signature** and merely declares which exception types the method might propagate, so callers know to handle them — it doesn't throw anything itself, it just documents the possibility.

Question 5:
If a finally block contains a return statement, that return overrides any return (or exception) from the try or catch blocks. So even if an exception was thrown or a different value was about to be returned, the finally block's return silently replaces/suppresses it — which is why the material calls returning from finally bad practice.

Question 6:
An enum can declare private fields, initialize them via a constructor called for each constant, and expose them through methods. For example, Planet stores mass and radius as fields set in its constructor, with getter methods getMass()/getRadius(), and even a computed method surfaceGravity() that uses those fields to return a derived value.

Question 7:
Choose ArrayList when needing fast random access (O(1) get(index)), reads are frequent, modifications are relatively rare, and the size is fairly stable — it has less memory overhead and better cache locality. LinkedList is preferred instead when you need frequent insertions/removals (especially at the ends) or queue/deque-style operations.

Question 8:
HashSet has no guaranteed order — iteration order depends on hash bucket placement and can appear random. LinkedHashSet maintains a linked list alongside the hash table to preserve insertion order, so iterating over it returns elements in the order they were added, at the cost of slightly more memory.

Question 9:
HashMap has no defined order for its keys — order depends on hash codes and bucket placement. TreeMap keeps keys in sorted order (natural ordering via Comparable, or a custom Comparator), which also enables range operations like headMap(), tailMap(), and firstKey()/lastKey().

Question 10:
Removing elements directly from a collection while iterating with a for-each loop causes a ConcurrentModificationException, because the collection's structure changes unexpectedly mid-iteration. An Iterator provides a remove() method that safely deletes the current element through the iterator itself, keeping the iterator's internal state consistent with the collection and avoiding that exception.
