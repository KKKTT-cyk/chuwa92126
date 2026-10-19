## Java Exception, Enum, Collection

#### Conceptual Questions

1. **Why must a checked exception be caught or declared while an unchecked exception does not require either?**

   Answer:

   ​	Checked exception is checked by compiler, so Java requires us to handle it by using try-catch or declare it with throws. This can make caller know this method may cause an exception. Unchecked exception usually extends RuntimeException and often caused by programming errors, so compiler does not force us to catch or declare it.

2. **Why must a catch block for a subclass come before a catch block for its superclass?**

   Answer:

   ​	Because a subclass exception is also an instance of its superclass. If the superclass catch block comes first, it will already catch the subclass exception, so the subclass catch block can never be reached. 

3. **What happens to an AutoCloseable resource when an exception occurs inside a try with resources block?**

   Answer:

   ​	The resource will still be closed automatically. Because Java will call the close() method after the try block end, even if an exception happens inside the block. This helps avoid resource leak and we do not need to close it manually in finally.

4. **How do throw and throws differ in where they are used and what they do?**

   Answer:

   ​	throw is used inside a method or code block to actually create and throw one exception object, 

   ​	throws is used in the method declaration to tell the caller that this method may pass one or more exceptions to it.

5. **Why can returning a value from a finally block hide an exception?**

   Answer:

   ​	The finally block always runs before the method exits. If an exception happens in try but finally has a return, the return from finally will replace the exception and the exception will not be passed to the caller.

6. **How can an enum use a field and a method to store and return a value?**

   Answer:

   ​	Use parentheses for fields and use getter to return the value.

   ```java
   enum Level {
     LOW(1), MEDIUM(2), HIGH(3);
   
     private final int value;
   
     Level(int value) {
       this.value = value;
     }
   
     public int getValue() {
       return value;
     }
   }
   //Level.HIGH.getValue() will return 3
   ```
   
7. **When would you choose an ArrayList instead of a LinkedList?**

   Answer:

   ​	I would choose ArrayList when I need to access elements by index frequently, because ArrayList can get an element in O(1) time. LinkedList is more useful when there are many insertions or deletions through a known node or iterator.

8. **How do HashSet and LinkedHashSet differ when you iterate over their elements?**

   Answer:

   ​	HashSet does not guarantee the iteration order, so the elements may come out in a different order from how they were added. LinkedHashSet keeps the insertion order, so when we iterate it, the elements usually appear in the same order that we inserted them.

9. **How do HashMap and TreeMap differ in the order of their keys?**

   Answer:

   ​	HashMap does not guarantee any order for its keys. TreeMap keeps the keys sorted by their natural order or by a Comparator that we provide, so if we need find quantitive order in a map, I will use TreeMap.

10. **Why is an Iterator useful when removing elements from a collection during iteration?**

    Answer:

    ​	Iterator provides a safe way to remove the current element while looping through a collection by using `iterator.remove()`. If we directly remove elements from the collection inside a for-each loop, it may cause `ConcurrentModificationException`.

    ```java
    Iterator<Integer> iterator = list.iterator();
    
    while (iterator.hasNext()) {
      int value = iterator.next();
    
      if (value < 0) {
        iterator.remove();
      }
    }
    ```
