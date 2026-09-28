# java-exception-enum-collection-hw


## 1. Why must a checked exception be caught or declared while an unchecked exception does not require either
Checked exception must be caught or declared with throws because the compiler checks at compile time. 
Unchecked exception extends RuntimeException and usually represents programming errors, so the compiler does not require it to be caught or declared.


## 2. Why must a catch block for a subclass come before a catch block for its superclass
A subclass catch block must come before its superclass catch block because the superclass can catch exceptions of its subclasses. If the superclass comes first, the subclass catch block would be unreachable.


## 3. What happens to an AutoCloseable resource when an exception occurs inside a try with resources block
The resource automatically closes when the try-with-resources block exits even there is an exception.


## 4. How do throw and throws differ in where they are used and what they do
Throw is used inside a method to actually throw an exception object.
Throws is used in a method signature to declare that the method may throw one or more exceptions.


## 5. Why can returning a value from a finally block hide an exception
"return" in a finally block can override the normal return value or suppress a pending exception, so the exception might be lost.


## 6. How can an enum use a field and a method to store and return a value
Enum can define a field to store a value, initialize that field through its constructor and provide a method such as a getter to return the value.


## 7. When would you choose an ArrayList instead of a LinkedList
ArrayList would be chosen when I need fast random access by index and not frequently insert or remove elements in the middle of the list.


## 8. How do HashSet and LinkedHashSet differ when you iterate over their elements
HashSet does not guarantee iteration order, while LinkedHashSet preserves the insertion order of its elements.


## 9. How do HashMap and TreeMap differ in the order of their keys
HashMap does not guarantee the order of its keys, while TreeMap keeps its keys sorted according to their ordering.


## 10. Why is an Iterator useful when removing elements from a collection during iteration
An Iterator allows safely remove the current element while looping, instead of modifying the collection directly.