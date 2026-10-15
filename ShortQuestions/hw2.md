# Java Exception, Enum, and Collection - Homework 2

## Question 1

**Why must a checked exception be caught or declared while an unchecked exception does not require either?**

Checked exceptions are checked by the compiler. If a method can throw a checked exception, the code must either handle it with a `try-catch` block or declare it with `throws` in the method signature.

Unchecked exceptions are subclasses of `RuntimeException`. They usually represent programming errors, such as invalid indexes or null references, so Java does not require them to be caught or declared.

---

## Question 2

**Why must a catch block for a subclass come before a catch block for its superclass?**

A catch block is checked from top to bottom. A superclass exception can also catch instances of its subclasses.

Therefore, if the superclass catch block appears first, it would catch the subclass exception before the subclass catch block could be reached. The subclass catch block would become unreachable and cause a compile-time error.

Example:

```java
try {
    // code
} catch (FileNotFoundException e) {
    // more specific exception first
} catch (IOException e) {
    // more general exception second
}
```

---

## Question 3

**What happens to an AutoCloseable resource when an exception occurs inside a try-with-resources block?**

A resource declared in a try-with-resources statement is closed automatically when the block finishes, even if an exception occurs.

Java calls the resource's `close()` method automatically. If both the code inside the `try` block and the `close()` method throw exceptions, the exception from the `try` block is the primary exception and the exception from `close()` is stored as a suppressed exception.

---

## Question 4

**How do `throw` and `throws` differ in where they are used and what they do?**

`throw` is used inside a method or block to actually throw a specific exception object.

Example:

```java
throw new IllegalArgumentException("Invalid value");
```

`throws` is used in a method declaration to state that the method may pass one or more exceptions to its caller.

Example:

```java
public void readFile() throws IOException {
    // code
}
```

In short, `throw` throws an exception, while `throws` declares possible exceptions.

---

## Question 5

**Why can returning a value from a finally block hide an exception?**

A `finally` block runs after the `try` or `catch` block before the method finishes.

If an exception is thrown in the `try` block but the `finally` block executes a `return`, the return from `finally` can replace the pending exception. As a result, the caller receives the returned value instead of seeing the exception.

For this reason, returning from a `finally` block should be avoided.

---

## Question 6

**How can an enum use a field and a method to store and return a value?**

An enum can define fields, constructors, and methods just like a class. Each enum constant can pass a value to the enum constructor, store it in a field, and return it through a method.

Example:

```java
enum Status {
    NEW(1),
    PROCESSING(2),
    DONE(3);

    private final int code;

    Status(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
```

For example, `Status.DONE.getCode()` returns `3`.

---

## Question 7

**When would you choose an ArrayList instead of a LinkedList?**

I would choose an `ArrayList` when the program frequently accesses elements by index or mainly adds elements to the end of the list.

`ArrayList` provides fast random access because it is backed by an array. It also usually has lower memory overhead than a `LinkedList`.

A `LinkedList` can be useful when frequent insertions or removals are performed through an iterator or at the ends of the list, but it is slower for random index access.

---

## Question 8

**How do HashSet and LinkedHashSet differ when you iterate over their elements?**

`HashSet` does not guarantee iteration order.

`LinkedHashSet` maintains the insertion order of its elements. Therefore, when iterating through a `LinkedHashSet`, elements normally appear in the same order in which they were inserted.

Both collections store unique elements.

---

## Question 9

**How do HashMap and TreeMap differ in the order of their keys?**

`HashMap` does not guarantee any particular order of its keys.

`TreeMap` keeps its keys sorted according to their natural ordering or according to a provided `Comparator`.

Therefore, use `HashMap` when ordering is not required, and use `TreeMap` when sorted keys are needed.

---

## Question 10

**Why is an Iterator useful when removing elements from a collection during iteration?**

An `Iterator` provides a safe way to remove the current element while iterating through many collections.

Using `collection.remove()` directly inside an enhanced `for` loop can cause a `ConcurrentModificationException`.

Instead, the iterator's `remove()` method can be used:

```java
Iterator<String> iterator = list.iterator();

while (iterator.hasNext()) {
    String value = iterator.next();

    if (value.isEmpty()) {
        iterator.remove();
    }
}
```

This allows the collection to be modified in a controlled way during iteration.
