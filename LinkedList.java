/** @file   LinkedList.java
 *  @brief  A class for a linked list with methods enabling insertion
 *          at front and end of the list, deletion, and printing.
 *  @author Mustafa Siddiqui
 *  @date   02/19/21
 */

 /*
    Linked List class.
 */
public class LinkedList {
    private Node head;

    /*
        Insert node at end of the list.
    */
    public void insertAtEnd(Object in) {
        Node currentNode = head;

        // create node for insertion
        Node insert = new Node();
        insert.data = in;
        insert.next = null;

        if (currentNode == null) {
            head = insert;
            head.next = null;
            return;
        }

        // if list not empty, traverse till end
        while (currentNode.next != null) {
            currentNode = currentNode.next;
        }

        currentNode.next = insert;
    }

    /*
        Insert node at head of the list.
    */
    public void insertAtFront(Object in) {
        // create node for insertion
        Node insert = new Node();
        insert.data = in;

        // if empty, initialize head node
        if (head == null) {
            head = insert;
            head.next = null;
            return;
        }

        // if not empty, insert before head
        insert.next = head;
        head = insert;
    }

    /*
        Get element from front of the list.
        Returns the data at the node and removes the
        node from the list.
    */
    public Object getFromFront() {
        Node currentNode = head;
        head = currentNode.next;

        return currentNode.data;
    }

    /*
        Get element from end of the list.
        Returns the data at the node and removes the
        node from the list.
    */
    public Object getFromEnd() {
        Node currentNode = head;
        Node previousNode = null;

        // traverse till end of list
        while (currentNode.next != null) {
            previousNode = currentNode;
            currentNode = currentNode.next;
        }

        previousNode.next = null;

        return currentNode.data;
    }

    /*
        Print list from head to tail.
    */
    public void printList() {
        Node currentNode = head;

        while (currentNode != null) {
            System.out.println(currentNode.data);
            currentNode = currentNode.next;
        }
    }

    /*
        Method to access the head node.
    */
    public Node getHead() {
        return head;
    }

    /*
        Recursive method to print the linked list in reverse.
    */
    public void printReverse(Node head) {
        Node current = head;
        if (current.next != null) {
            printReverse(current.next);
        }

        System.out.println(current.data);
        return;
    }

    /*
        Reverse the list recursively.
    */
    public void reverseList(Node curr, Node prev) {
        // traverse to end of list while keeping track
        // of current and previous nodes
        if (curr.next != null) {
            reverseList(curr.next, curr);
        }

        // update head of list
        if (curr.next == null) 
            head = curr;
        
        // update next node
        curr.next = prev;
        return;
    }
}