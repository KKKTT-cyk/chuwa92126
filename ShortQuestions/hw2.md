1. Why must a checked exception be caught or declared while an unchecked exception does not require
either
Checked exception are not runtime exception, it is usually a problems outside of the project, like a file not exist, network error, or databse error, so we need to catch those problems and throws the exception. Unchecked exceptions means runtime exception, usually indicate programming bugs, so we have to fix them, not catch them.



2. Why must a catch block for a subclass come before a catch block for its superclass
Because the order of catch block is from top to bottom, the first one catches the exception, all following catch will not run if they are subclass, so we want to catch the subclass exception, and the higher level will at the end to help to catch something previous catch did not find.



3. What happens to an AutoCloseable resource when an exception occurs inside a try with resources block
the resources will close, and java will call the close method first, then enter the try catch and finally.

4. How do throw and throws differ in where they are used and what they do
Throws declares what will be throw in a function, it just teoll callers this method might throw these exceptions, and you need to handle them. And throw is a action we reallyused inside a method body.



5. Why can returning a value from a finally block hide an exception
Because the method will return the result and does not care if there is error and exception found in the finally block, which means it has chance to miss exceptions.



6. How can an enum use a field and a method to store and return a value
In java, an enum is actually a special class, and each constant is an instance of that class. So just like a regular class, an enum can have fields, a constructor, and methods. To store a value, we need to declare a private final field, and add a constructor takes the value and assign it. The constants must be listed first, and the constructor is private, so no one can create a instance outisde, and the fields are final, and unmutable.


7. When would you choose an ArrayList instead of a LinkedList
Arraylist has O(1) for get and add at the end, and linkedlist is O(1) on insertion and removal at the head and the tail, so I will use arraylist in most cases except I need to do a lot of insert and remove at the front and end.


8. How do HashSet and LinkedHashSet differ when you iterate over their elements
The difference between those two is that linkedhashset maintain a extra list that remember the order of each values add in, so it cost extra memory, but iteration can be faster because it follows the order of the linked list instead of scanning through empty buckets.



9. How do HashMap and TreeMap differ in the order of their keys
HashMap has no order on the key, treemap's key is from small to large order, so the key of treemap is always organized and ordered.



10. Why is an Iterator useful when removing elements from a collection during iteration
An Iterator is useful because it's the safe way to remove elements while you're still iterating over a collection.
