/** @file   Stack.java
 *  @brief  Implementation of a stack by using a linked list class.
 *  @author Mustafa Siddiqui
 *  @date   02/20/21
 */

/* 
    Stack class using the linked list class as skeleton.
*/
public class Stack {
    private LinkedList stack;

    /*
        Constructor for stack class.
    */
    public Stack() {
        stack = new LinkedList();
    }

    /*
        Push element onto stack.
    */
    public void push(Object in) {
        stack.insertAtFront(in);
    }

    /*
        Pop element from stack.
    */
    public Object pop() {
        return stack.getFromFront();
    }

    /*
        Check if stack is empty or not.
    */
    public boolean isEmpty() {
        return (stack.getHead() == null);
    }

    /*
        Print each element on a single line.
    */
    public void printStack() {
        stack.printList();
    }
}
