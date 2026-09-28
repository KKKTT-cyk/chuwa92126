# 1. Why must a checked exception be caught or declared while an unchecked exception does not require either
Java requires checked exceptions to be caught or declared because they are part of a method’s contract. They represent failures the caller is expected to consider, such as an `IOException`. The compiler ensures the caller either handles the failure with `try-catch` or passes responsibility upward using `throws`.

Unchecked exceptions—`RuntimeException`, `Error`, and their subclasses—don’t have this requirement. They typically indicate programming bugs or serious runtime failures. Requiring them everywhere would clutter method signatures without meaningfully improving error handling.

Both occur at runtime; the difference is whether the compiler enforces catch-or-declare.

- **Checked:** External, foreseeable system issues $\rightarrow$ _Require a fallback or recovery strategy.
- **Unchecked:** Internal developer mistakes $\rightarrow$ _Fix the logic and validate inputs._


# 2. Why must a catch block for a subclass come before a catch block for its superclass
Java evaluates catch blocks from top to bottom and executes the first matching block. A superclass catch also matches exceptions of its subclasses, so placing it first would make the subclass catch unreachable. Java rejects this with a compile-time error.

For example, `catch (IOException e)` must come before `catch (Exception e)` so that an `IOException` receives its specific handling.

The rule is: **catch more specific exceptions before more general ones.**

# 3. What happens to an AutoCloseable resource when an exception occurs inside a try with resources block
An AutoCloseable resource is automatically closed when the try-with-resources block exits, even if an exception occurs. If both the block and `close()` throw exceptions, the exception from the block is propagated, and the exception from `close()` is added as a suppressed exception.

# 4. How do throw and throws differ in where they are used and what they do
`throw` is used **inside a method or block** to actually throw an exception object. It interrupts normal execution and transfers control to an appropriate exception handler.

`throws` is used **in a method declaration** to declare exceptions that may propagate to the caller. It does not throw anything itself.
```java
void readFile() throws IOException {       // Declares the possibility
    throw new IOException("Read failed");  // Actually throws an exception
}
```

# 5. Why can returning a value from a finally block hide an exception
Because a `finally` block executes before a pending exception propagates. If `finally` returns a value, that return overrides the pending exception, so the caller receives the value instead of the exception.
```java
static int example() {
    try {
        throw new RuntimeException("Something failed");
    } finally {
        return 42; // Overrides the exception
    }
}
```
steps: try trows exception → execute finally → return 42 → exception 

# 6. How can an enum use a field and a method to store and return a value
An enum can have a field to store a value, a constructor to initialize that value for each constant, and a getter method to return it. Each enum constant is an instance with its own stored value.

``` java
enum Status {
    SUCCESS(200),
    NOT_FOUND(404);

    private final int code;

    Status(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
```

For example, `Status.SUCCESS.getCode()` returns `200`, while `Status.NOT_FOUND.getCode()` returns `404`.

# 7. When would you choose an ArrayList instead of a LinkedList
I would choose an ArrayList when I need fast access by index, frequent iteration, or mostly add elements at the end. It provides O(1) indexed access and amortized O(1) appends, and typically uses less memory than a LinkedList.

A LinkedList can be useful for frequent insertions or removals through an iterator that is already at the desired position. However, finding that position takes O(n), so it is not automatically faster for inserting or removing elements.

For most general-purpose list operations, ArrayList is my default choice.

# 8. How do HashSet and LinkedHashSet differ when you iterate over their elements
`HashSet` does not guarantee iteration order, and that order may change over time. `LinkedHashSet` preserves insertion order, so elements are iterated in the order they were added.

For example, if you add `B`, `A`, then `C`, a `LinkedHashSet` iterates as `B, A, C`, while a `HashSet` has no guaranteed order.

# 9. How do HashMap and TreeMap differ in the order of their keys
`HashMap` does not guarantee any order for its keys. `TreeMap` keeps its keys sorted, either by their natural ordering or by a custom `Comparator`.

For example, if you insert keys `3`, `1`, then `2`, a `TreeMap` iterates over them as `1, 2, 3`, while a `HashMap` has no guaranteed order.


# 10. Why is an Iterator useful when removing elements from a collection during iteration
An `Iterator` lets you remove the element most recently returned by `next()` using `iterator.remove()`, while keeping the iteration state consistent. Modifying the collection directly during iteration can cause a `ConcurrentModificationException`.

```java
Iterator<Integer> it = numbers.iterator();

while (it.hasNext()) {
    if (it.next() < 0) {
        it.remove();
    }
}
```
Note that removal must be supported by the iterator, and `remove()` can only be called once per call to `next()`.