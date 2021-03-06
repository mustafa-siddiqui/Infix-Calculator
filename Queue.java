/** @file   Queue.java
 *  @brief  An implementation of the Queue data structure using
 *          a linked list class.
 *  @author Mustafa Siddiqui
 *  @date   02/20/2021
 */

/* 
    Queue class using the linked list class as skeleton.
*/
public class Queue {
    private LinkedList queue;

    /*
        Constructor for queue class.
    */
    public Queue() {
        queue = new LinkedList();
    }

    /*
        Enqueue operation which inserts node at the
        end of the queue.
    */
    public void enqueue(Object in) {
        queue.insertAtEnd(in);
    }

    /*
        Dequeue operation which returns the data of
        the node at the front of the queue.
        Removes that element from the queue.
    */
    public Object dequeue() {
        return queue.getFromFront();
    }

    /* 
        Print each element on a single line.
    */
    public void printQueue() {
        queue.printList();
    }
}
