import java.util.Iterator;

/*
File: DSAQueue.java
Author: Tobias Jun
Unit: COMP1002
Purpose: Creates a queue using a linked list
References: 1. Modified DSAQueue.java file from practical 4
Last Modified: 22/05/2023
*/

public class DSAQueue implements Iterable
{
    private DSALinkedList queue;

    /*
    Name: DSAQueue()
    Purpose: Default Constructor method
    creates a queue
    Imports: Nothing
    Exports: Nothing
    */

    public DSAQueue()
    {
        queue = new DSALinkedList();
    }




    /*
    Name: isEmpty()
    Purpose: Checks if queue is empty
    Imports: Nothing
    Exports: boolean empty from queue.isEmpty()
    */

    public boolean isEmpty()
    {
        return queue.isEmpty();
    }




    /*
    Name: enqueue()
    Purpose: Adds an item to the queue
    Imports: Object value
    Exports: Nothing
    */

    public void enqueue( Object value )
    {
        queue.insertLast( value );
    }




    /*
    Name: dequeue()
    Purpose: Removes an item from the queue
    Imports: Nothing
    Exports: Object topVal
    */

    public Object dequeue()
    {
        Object topVal;

        topVal = queue.peekFirst();
        queue.removeFirst();

        return topVal;
    }




    /*
    Name: peek()
    Purpose: Checks the top item of the queue
    Imports: Nothing
    Exports: Object topVal
    */

    public Object peek()
    {
        Object topVal;

        if( isEmpty() == true )
        {
            throw new IllegalArgumentException( "Queue is empty!" );
        }
        else
        {
            topVal = queue.peekFirst();
        }

        return topVal;
    }

    public Iterator iterator()
    {
        return queue.iterator();
    }
}
