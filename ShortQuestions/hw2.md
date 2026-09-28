# Q1.
A checked exception represents a recoverable condition that is outside the program's control,  
Even a correctly written program can encounter these situations, 
so need to either catch the exception or declare it with throws.  
An unchecked exception usually indicates a programming bug, 
such as a NullPointerException or an ArrayIndexOutOfBoundsException,  
These should be prevented by fixing the code rather than handled at runtime. 
Since almost any statement could throw them, 
requiring every method to catch or declare them would clutter the code with meaningless handling and provide little benefit.


# Q2.
Because the catch codes are executed from top to bottom, and a subclass exception
is also an instance of its superclass, so a catch block for the superclass
will catch all of its subclass exceptions, and java treats this unreachable code as a compile time error.

# Q3.
The resource will be closed, because the resource close method will be called.

# Q4.
throw is used inside a method body as a statement to throw an exception.
throws is used in a method declaration after the parameter list, to clear types of exceptions.

# Q5.
Finally block always execute, when an exception is thrown in the try block, it's held
while finally runs, and be thrown after the finally code finish normally.
If the finally code return a value and the exception will be discarded and the method will throw 
a value like a normal method, so the error is lost.

# Q6.
Enum is a special class that represents a group of constant, so it can have fields, constructors and methods.

# Q7.
ArrayList is based on a dynamic array, while LinkedList is based on a doubly linked list.
So ArrayList is good at accessing elements by index, and appending elements at the end.
while LinkedList is good at inserting values at the beginning or the end of the list or a position already reached with an iterator.

# Q8.
When iterating, a Hashset doesnt guarantee any particular order, elements are stored in buckets based on hash code.
A LinkedHashSet is a subclass of HashSet, maintains a doubly linked list running through all its elements, so it iterates over them in the order they were inserted.

# Q9.
A HashMap does not guarantee any order of its keys. It stores entries in a hash table based on the keys hash codes.
A TreeMap is implemented as a red black tree and always keeps its keys in sorted order, according to their natural ordering or according to a Comparator.

# Q10.
If remove elements from a collection directly, while iterating over it with an enhanced for loop, 
the program usually throws a ConcurrentModificationException.
An Iterator solves this problem with its own remove() method, 
removes the last element returned by next() and keeps the iterator's internal state consistent with the collection. 
This makes it the safe and correct way to remove elements during iteration.


