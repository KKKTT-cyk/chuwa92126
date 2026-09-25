HW2
1. Checked exceptions must be caught or declared because Java’s compiler requires handling them. Unchecked exceptions (RuntimeException) usually represent programming errors, so the compiler does not force you to handle them.
2. Because the superclass catch can also catch the subclass exception. If it comes first, the subclass catch would never be reached, so Java gives a compile-time error.
3. It is automatically closed, even if an exception occurs inside the try block. Java calls the resource’s close() method for you.
4. throw is used inside a method to actually throw an exception.throws is used in the method declaration to say the method may throw an exception.
5. Because finally always runs last. If it has a return, that return can override the exception thrown earlier
6. An enum can have a field to store a value, a constructor to set it, and a method to return it.
7. Choose ArrayList when you need fast random access and mostly add/remove items at the end.
8. HashSet does not guarantee iteration order. LinkedHashSet keeps elements in insertion order.
9. HashMap does not guarantee any key order. TreeMap keeps keys sorted by natural order or a Comparator. 
10. Because an Iterator lets you safely remove the current element while iterating.If you modify the collection directly during iteration, you may get a ConcurrentModificationException
