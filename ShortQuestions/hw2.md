1. A checked exception represents a problem that Java expects a program to anticipate and possibly recover from, such as a missing file or an I/O error. The compiler therefore requires the exception to either be handled with try-catch or declared using throws.
   An unchecked exception, such as NullPointerException or ArithmeticException, usually represents a programming error. These exceptions are subclasses of RuntimeException, so Java does not require them to be caught or declared.
2. A subclass exception must come first because a superclass catch block can also catch objects of its subclasses.
3. A resource implementing AutoCloseable is automatically closed, even if an exception occurs inside the try block. Java calls the resource’s close() method for you.
4. throw is used inside a method to actually create or send an exception.
   throws is used in a method declaration to indicate that the method may pass an exception to the method that called it.
5. A finally block normally executes even if an exception occurs.
   If the finally block contains a return statement, that return can override the exception that was originally being thrown.
6. Each enum constant can be associated with a value by using a field, constructor, and getter method.
7. It is usually preferred when:
   - frequent get(index) operations are needed;
   - elements are mostly added to the end;
   - there are relatively few insertions or deletions in the middle.
8. A HashSet does not guarantee iteration order. A LinkedHashSet, however, maintains the insertion order of its elements.
9. A HashMap does not guarantee any particular order for its keys.
   A TreeMap, in contrast, keeps its keys sorted according to their natural ordering or a provided Comparator.
10. An Iterator provides a safe way to remove elements while traversing a collection.
    In contrast, directly modifying a collection inside an enhanced for loop can cause a:ConcurrentModificationException