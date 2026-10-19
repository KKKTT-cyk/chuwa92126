# Java Practice Questions - Answers

## 1. Why must a checked exception be caught or declared while an unchecked exception does not require either?

A checked exception is checked by the compiler. Java requires the programmer to either catch it using `try-catch` or declare it using `throws`.

An unchecked exception is not checked by the compiler, so it does not need to be caught or declared.

```java
try {
    FileReader file = new FileReader("test.txt");
} catch (IOException e) {
    System.out.println("Error");
}
```

---

## 2. Why must a catch block for a subclass come before a catch block for its superclass?

A superclass exception can also catch exceptions from its subclasses.

Therefore, if the superclass comes first, the subclass catch block can never be reached.

Correct:

```java
try {
    // code
} catch (IOException e) {
    // specific exception
} catch (Exception e) {
    // general exception
}
```

The more specific exception should come before the more general exception.

---

## 3. What happens to an AutoCloseable resource when an exception occurs inside a try-with-resources block?

The resource is automatically closed, even if an exception occurs.

```java
try (FileReader file = new FileReader("test.txt")) {
    // use file
}
```

Java automatically calls `close()` on the resource when the block finishes.

---

## 4. How do `throw` and `throws` differ in where they are used and what they do?

`throw` is used inside a method to actually throw an exception.

```java
throw new IllegalArgumentException("Invalid value");
```

`throws` is used in a method declaration to indicate that the method may throw an exception.

```java
public void readFile() throws IOException {
    // code
}
```

In short:

- `throw` → actually throws an exception
- `throws` → declares possible exceptions

---

## 5. Why can returning a value from a `finally` block hide an exception?

A `finally` block executes even when an exception occurs.

If the `finally` block returns a value, that return can override the exception that was being thrown.

```java
public static int test() {
    try {
        throw new RuntimeException("Error");
    } finally {
        return 10;
    }
}
```

In this example, the method returns `10`, and the exception is hidden.

Therefore, returning a value from `finally` should generally be avoided.

---

## 6. How can an enum use a field and a method to store and return a value?

An enum can define a field, use a constructor to assign a value to that field, and use a method to return the value.

```java
enum Level {
    LOW(1),
    MEDIUM(2),
    HIGH(3);

    private int value;

    Level(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
```

Example:

```java
System.out.println(Level.HIGH.getValue());
```

Output:

```text
3
```

---

## 7. When would you choose an `ArrayList` instead of a `LinkedList`?

Choose an `ArrayList` when you frequently need to access elements by index.

```java
list.get(100);
```

`ArrayList` provides fast random access.

A simple rule is:

- `ArrayList` → good for accessing elements by index
- `LinkedList` → useful for certain frequent insertion/removal operations, especially at the ends

For most general-purpose lists, `ArrayList` is commonly used.

---

## 8. How do `HashSet` and `LinkedHashSet` differ when you iterate over their elements?

`HashSet` does not guarantee iteration order.

```java
Set<String> set = new HashSet<>();
```

`LinkedHashSet` maintains insertion order.

```java
Set<String> set = new LinkedHashSet<>();

set.add("A");
set.add("B");
set.add("C");
```

Iteration order:

```text
A
B
C
```

Therefore:

- `HashSet` → no guaranteed iteration order
- `LinkedHashSet` → insertion order

---

## 9. How do `HashMap` and `TreeMap` differ in the order of their keys?

`HashMap` does not guarantee the order of its keys.

```java
Map<Integer, String> map = new HashMap<>();
```

`TreeMap` keeps its keys sorted according to their natural ordering or a provided comparator.

```java
Map<Integer, String> map = new TreeMap<>();

map.put(3, "C");
map.put(1, "A");
map.put(2, "B");
```

The keys will be iterated in this order:

```text
1
2
3
```

Therefore:

- `HashMap` → no guaranteed key order
- `TreeMap` → keys are sorted

---

## 10. Why is an `Iterator` useful when removing elements from a collection during iteration?

An `Iterator` provides a safe way to remove the current element while iterating through a collection.

```java
List<Integer> numbers = new ArrayList<>();
numbers.add(1);
numbers.add(2);
numbers.add(3);

Iterator<Integer> iterator = numbers.iterator();

while (iterator.hasNext()) {
    int number = iterator.next();

    if (number == 2) {
        iterator.remove();
    }
}
```

Directly removing elements from many collections during an enhanced `for` loop can cause a `ConcurrentModificationException`.

```java
for (Integer number : numbers) {
    if (number == 2) {
        numbers.remove(number);
    }
}
```

Using `Iterator.remove()` allows the current element to be safely removed during iteration.