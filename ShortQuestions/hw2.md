# HW2 – Java Exception Handling, Enums, and Collections

## Question 1.

**Checked exceptions** (e.g. `IOException`, `SQLException`, `FileNotFoundException`) must be handled at
compile time, and **the compiler enforces this rule**: the code will not compile unless the exception is
either caught with `try-catch` or declared with `throws`. They represent **recoverable conditions** outside
the program's control (a missing file, a database error), so the caller is forced to decide how to recover.

```java
// Option 1: Handle with try-catch
public void readFile() {
    try {
        FileReader reader = new FileReader("data.txt");
    } catch (FileNotFoundException e) {
        System.err.println("File not found: " + e.getMessage());
    }
}

// Option 2: Declare with throws
public void readFile() throws FileNotFoundException {
    FileReader reader = new FileReader("data.txt");
}
```

**Unchecked exceptions** extend `RuntimeException` (e.g. `NullPointerException`,
`ArrayIndexOutOfBoundsException`) and don't require explicit handling. They usually indicate
**programming errors**, which should be fixed in the code rather than caught everywhere.

---

## Question 2.

Catch blocks are checked **in order**, and the rule is to order them from **most specific to least specific**.
A superclass catch block also catches all of its subclasses. If it comes first, the subclass catch block can
never be reached, so the compiler reports it as **unreachable code (compile error)**.

```java
// WRONG - Will not compile!
try {
    processFile("data.txt");
} catch (Exception e) {              // Catches everything first
    System.err.println("Error");
} catch (FileNotFoundException e) {  // Unreachable code!
    System.err.println("File not found");
}

// CORRECT
try {
    processFile("data.txt");
} catch (FileNotFoundException e) {  // Most specific
    System.err.println("File not found: " + e.getMessage());
} catch (IOException e) {            // Less specific
    System.err.println("I/O error: " + e.getMessage());
} catch (Exception e) {              // Least specific
    System.err.println("Unexpected error: " + e.getMessage());
}
```

---

## Question 3.

The resource is **still closed automatically**. Its `close()` method is called whether the `try` block
finishes normally or throws an exception. If there are multiple resources,
they are closed **in reverse order of declaration**.

This is why a resource must implement `AutoCloseable`: try-with-resources calls its `close()` method.

```java
try (BufferedReader reader = new BufferedReader(new FileReader("file.txt"))) {
    String line = reader.readLine();   // exception may happen here
} catch (IOException e) {
    e.printStackTrace();
}
// reader.close() is called automatically!
```

Without try-with-resources, we would have to close the resource manually in a `finally` block.

---

## Question 4.

| | `throw` | `throws` |
|---|---|---|
| Where it is used | Inside the method body | In the method signature |
| What follows it | An exception **instance** | Exception **class names** |
| How many | Throws one exception | Can declare multiple exceptions |
| What it does | **Actually throws** the exception | **Just declares** that the method may throw these exceptions, so the caller must handle or declare them |

```java
// throw - actually throwing an exception
public void validateAge(int age) {
    if (age < 0) {
        throw new IllegalArgumentException("Age cannot be negative: " + age);
    }
}

// throws - declaring exceptions
public void processData(String path) throws FileNotFoundException, IOException {
    // ... risky operations
}
```

---

## Question 5.

The `finally` block **always executes**, and a `return` in `finally` **overrides** any other return or
exception from the `try`/`catch` block. If the `try` block throws an exception, the `return` in `finally`
makes the method return normally, and the **exception is discarded**. The caller never knows it happened.

```java
public int getValue() {
    try {
        return 1;
    } catch (Exception e) {
        return 2;
    } finally {
        return 3; // This overrides all other returns!
    }
}
// Returns: 3 (finally block's return wins)
```

That is why returning from `finally` is bad practice: **it suppresses exceptions**. `finally` should only
be used for cleanup, not for returning values.

---

## Question 6.

An enum can have **fields, constructors, and methods**. Each enum constant passes its value to the
constructor, the constructor stores it in a field, and a method returns it. The constructor **must be private
or package-private**.

```java
public enum OrderStatus {
    PENDING("Order received"),
    SHIPPED("Order shipped"),
    DELIVERED("Order delivered");

    private final String description;   // field

    OrderStatus(String description) {   // constructor
        this.description = description;
    }

    public String getDescription() {    // method
        return description;
    }
}

// Usage
System.out.println(OrderStatus.SHIPPED.getDescription());   // "Order shipped"
```

---

## Question 7.

`ArrayList` is a **dynamic array**, so it has **O(1) random access** by index, **less memory overhead**,
and **better cache locality**. Inserting or deleting in the middle is O(n), because elements must be shifted.

Choose `ArrayList` when:
- there are **frequent reads** (e.g. accessing elements by index with `get(i)`)
- modifications are **rare**
- the size is **mostly stable**

`LinkedList` is better only for frequent adds/removes at the ends, or for Queue/Deque operations.

---

## Question 8.

Both are Sets (no duplicate elements), but they iterate in different orders:

- **HashSet**: **no guaranteed order**. Elements are stored by `hashCode`, so iteration order can look
  random.
- **LinkedHashSet**: iterates in **insertion order**. It uses extra memory to remember the order in which
  elements were added.

```java
// Adding: 3, 1, 4, 1, 5
HashSet       → {1, 3, 4, 5}   (any order)
LinkedHashSet → {3, 1, 4, 5}   (insertion order preserved)
```

Both have O(1) operations. HashSet has the best performance.

---

## Question 9.

- **HashMap**: **no order**. Keys are stored in buckets by `hashCode`. O(1) operations, allows a null key.
- **TreeMap**: keys are kept in **sorted order** (natural ordering or a `Comparator`). O(log n) operations,
  no null keys, and supports range operations like `firstKey()`, `headMap()`.

```java
TreeMap<String, Integer> treeMap = new TreeMap<>();
treeMap.put("Charlie", 3);
treeMap.put("Alice", 1);
treeMap.put("Bob", 2);
// Keys automatically sorted: {Alice=1, Bob=2, Charlie=3}
```

---

## Question 10.

Removing elements from a collection directly while iterating over it with a for-each loop throws
`ConcurrentModificationException`. `Iterator.remove()` is the **safe way to remove** the current element
during iteration.

```java
List<String> list = new ArrayList<>(Arrays.asList("A", "B", "C", "D"));

// Using Iterator
Iterator<String> iterator = list.iterator();
while (iterator.hasNext()) {
    String element = iterator.next();
    if (element.equals("B")) {
        iterator.remove();   // Safe removal during iteration
    }
}
// list is now [A, C, D]

// WRONG - ConcurrentModificationException
List<String> list2 = new ArrayList<>(Arrays.asList("A", "B", "C", "D"));
for (String s : list2) {
    if (s.equals("B")) {
        list2.remove(s);   // Throws ConcurrentModificationException!
    }
}
```
