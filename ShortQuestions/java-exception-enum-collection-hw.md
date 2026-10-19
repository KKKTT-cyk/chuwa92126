1. Why must a checked exception be caught or declared while an unchecked exception does not require either?
Checked exceptions must be handled at compile time. The compiler enforces this rule.Unchecked exceptions extend RuntimeException and don't require explicit handling.
2. Why must a catch block for a subclass come before a catch block for its superclass?
A catch block for a subclass must come before a catch block for its superclass because Java checks catch blocks from top to bottom. If the superclass is caught first, it will also catch all exceptions from its subclasses. This means the subclass catch block would never be reached, causing a compile-time error. Therefore, more specific exceptions should always be caught before more general exceptions.
3. What happens to an AutoCloseable resource when an An AutoCloseable resource is automatically closed after the try block completes, even when an exception is thrown. This helps prevent resource leaks and eliminates the need to manually close the resource in a finally block.
4. How do throw and throws differ in where they are used and what they do?
Throws happens inside the body while throw is in method signature. Throw throws handles one exception while throws declare possible exceptions. Throw is followed by exception instance while throws is followed by exception classnames. Throw is actually throws while throw just declares. 
5. Why can returning a value from a finally block hide an exception
Returning a value from a finally block can hide an exception because the finally block always runs. If it contains a return statement, that return takes priority over an exception thrown in the try block, causing the exception to be ignored instead of passed to the caller.
6. How can an enum use a field and a method to store and return a value?
An enum can store a specific value for each constant using a field and constructor. A method, such as a getter, can then be used to access and return that stored value.
7. When would you choose an ArrayList instead of a LinkedList
An ArrayList is a better choice when you need quick access to elements by index and mostly read data or add new elements to the end of the list. It is especially useful when insertions and deletions in the middle are not common. In short, when the data is frequently read with rare modifications and stabled sizes.
8. How do HashSet and LinkedHashSet differ when you iterate over their elements?
HashSet does not preserve the order of its elements during iteration, while LinkedHashSet maintains the insertion order, so elements are returned in the same sequence in which they were added.
9. How do HashMap and TreeMap differ in the order of their keys?
HashMap stores keys without maintaining a particular order, while TreeMap keeps its keys automatically sorted based on their natural ordering or a specified comparator.
10. Why is an Iterator useful when removing elements from a collection during iteration?
An Iterator lets you remove elements safely while going through a collection. Using its remove() method prevents errors such as ConcurrentModificationException that may happen when modifying the collection directly during iteration.