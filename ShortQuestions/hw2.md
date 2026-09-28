1. Why must a checked exception be caught or declared while an unchecked exception does not require either
Because a checked exception could be caught or declared with throws because the compiler checks it at compile time.
While an unchecked exception is a subclass of RuntimeException which is caused by
programming error, so the compiler does not require it to be caught or declared.

2. Why must a catch block for a subclass come before a catch block for its superclass
Because if a superclass exception was caught first, other subclass exceptions would be
unreachable, thus the specific exceptions should come before the superclass exceptions.

3. What happens to an AutoCloseable resource when an exception occurs inside a try with resources block
When an exception occurs inside a try with resource block, the Autocloseable resource is still closed
automatically. Java calls close() method, so the resource does not need to be closed explicitly.

4. How do throw and throws differ in where they are used and what they do
throws is used after the method signature, and there is no need to take care of the exceptions inside the method and
should be handled by its caller. While throw is declared inside the method or block to explicitly throw a specific
exception object.

5. Why can returning a value from a finally block hide an exception
Because the return value in try or catch block would not return until the finally block returns value, and the value in
the finally block will override the waiting any pending return value or exception from the try or catch blocks.
As a result, the method returns normally and the exception is suppressed,

6. How can an enum use a field and a method to store and return a value
A enum can define a field to store a value and use a constructor to assign a value to each enum constant, and provide a
method return that value.
Example:
enum Status{
    SUCCESS(200),
    ERROR(500);

    private final int code;

    Status(int code){
        this.code=code;
    };
    public int getCode(){
        return code;
    };
}
In the upper example, each enum constant passes a value to the constructor. The constructor stores
it in the code field, and the getCode() method returns that value.

7. When would you choose an ArrayList instead of a LinkedList
When it comes to these situations, choose an ArrayList:
need to use index get elements, append elements at the end, need to often iterate, read and get elements;
seldom insert and delete elements in the list, and want to use less memory.


8. How do HashSet and LinkedHashSet differ when you iterate over their elements
When we iterate the elements, there is no guarantee of the iteration order in the HashSet, while we iterate the
elements according to the insertion order.

9. How do HashMap and TreeMap differ in the order of their keys
HashMap does not guarantee any order for its keys while TreeMap maintains its keys sorted according to their natural
order or by a provided comparator.

10. Why is an Iterator useful when removing elements from a collection during iteration
Because an Iterator allows elements to be removed safely while iterating over a collection. Using iterator.remove()
avoids the ConcurrentModificationException that may occur when the collection is modified directly during iteration.








