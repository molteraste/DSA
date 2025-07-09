import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

/*
File: DSALinkedList.java
Author: Tobias Jun
Unit: COMP1002
Purpose: Creates a Linked List
Comment: Uses an iterator for the list
Reference: 1. Modified DSAGraph.java file from practical 4
Last Modified: 22/05/2023
*/

public class DSALinkedList implements Iterable, Serializable
{
//    Class fields
    private DSAListNode head;
    private DSAListNode tail;
    private int count;




    /*
    Name: DSALinkedList()
    Purpose: Default constructor creates a linked list
    Imports: Nothing
    Exports: Nothing
    */

    public DSALinkedList()
    {
        head = null;
        tail = null;
        count = 0;
    }

    public int getCount()
    {
        return count;
    }


    /*
    Name: insertFirst()
    Purpose: Inserts an item to the top of the list
    Imports: Object newValue
    Exports: Nothing
    */

    public void insertFirst( Object newValue )
    {
        DSAListNode newNd;

        newNd = new DSAListNode( newValue );

        if( isEmpty() )
        {
            head = newNd;
            tail = newNd;
        }
        else
        {
//            The previous variable of head becomes the new variable
            head.setPrev( newNd );
//            The rest of the list is added next to the new variable
            newNd.setNext( head );
//            The new variable becomes the new head
            head = newNd;
        }
        count++;
    }




    /*
    Name: insertLast()
    Purpose: Inserts an item to the end of the list
    Imports: Object newValue
    Exports: Nothing
    */

    public void insertLast( Object newValue )
    {
        DSAListNode newNd;

        newNd = new DSAListNode( newValue );

        if( isEmpty() )
        {
            head = newNd;
            tail = newNd;
        }
        else
        {
//            The previous variable of the new variable becomes the tail
            newNd.setPrev( tail );
//            The next variable of tail becomes the new variable
            tail.setNext( newNd );
//            The tail becomes the new variable
            tail = newNd;
        }
        count++;
    }




    /*
    Name: isEmpty()
    Purpose: Checks if the list is empty
    Imports: Nothing
    Exports: boolean empty
    */

    public boolean isEmpty()
    {
        boolean empty = false;

        if( head == null && tail == null )
        {
            empty = true;
        }

        return empty;
    }




    /*
    Name: peekFirst()
    Purpose: Returns the first value of the list .ie the head
    Imports: Nothing
    Exports: Object nodeValue
    */

    public Object peekFirst()
    {
        Object nodeValue;

        if( isEmpty() )
        {
            throw new IllegalArgumentException( "List is empty!" );
        }
        else
        {
            nodeValue = head.getValue();
        }

        return nodeValue;
    }




    /*
    Name: peekLast()
    Purpose: Returns the last value of the list .ie the tail
    Imports: Nothing
    Exports: Object nodeValue
    */

    public Object peekLast()
    {
        Object nodeValue;

        if( isEmpty() )
        {
            throw new IllegalArgumentException( "List is empty!" );
        }
        else
        {
            nodeValue = tail.getValue();
        }
        return nodeValue;
    }




    /*
    Name: removeFirst()
    Purpose: Removes the first value of the list and returns this value
    Imports: Nothing
    Exports: Object nodeValue
    */

    public Object removeFirst()
    {
        Object nodeValue;

        if( isEmpty() )
        {
            throw new IllegalArgumentException( "List is Empty!" );
        }
        else if( head.getNext() == null )
        {
            nodeValue = head.getValue();
            tail = null;
            head = null;
            count--;
        }
        else
        {
            nodeValue = head.getValue();
            head = head.getNext();
            count--;
        }

        return nodeValue;
    }




    /*
    Name: removeLast()
    Purpose: Removes the last value of the list and returns this value
    Imports: Nothing
    Exports: Object nodeValue
    */

    public Object removeLast()
    {
        Object nodeValue;

        if( isEmpty() )
        {
            throw new IllegalArgumentException( "List is Empty!" );
        }
        else if( tail.getPrev() == null )
        {
            nodeValue = tail.getValue();
            head = null;
            tail = null;
            count--;
        }
        else
        {
            nodeValue = tail.getValue();
            tail.getPrev().setNext( null );
            tail = tail.getPrev();
            count--;
        }

        return nodeValue;
    }




    /*
    Name: remove()
    Purpose: Removes the node with the given label from the list
    Imports: String label
    Exports: Object nodeValue
    */

    public Object remove( String label )
    {
        Object nodeValue;
        DSAListNode currNode;
        DSAListNode prevNd;

        nodeValue = "";

        try
        {
            if( isEmpty() )
            {
                throw new IllegalArgumentException();
            }
            else if( head.getValue().toString().equals( label ) )
            {
                nodeValue = head.getValue();
                removeFirst();
            }
            else if( tail.getValue().toString().equals( label ) )
            {
                nodeValue = tail.getValue();
                removeLast();
            }
            else
            {
                currNode = head;

                while( currNode.getNext() != null )
                {
                    prevNd = currNode;
                    currNode = currNode.getNext();

                    if( currNode.getValue().toString().equals( label ) )
                    {

//                        Removes the node desired
                        prevNd.setNext( currNode.getNext() );
                        nodeValue = currNode.getValue();
                        count--;
                    }
                }
            }
        }
        catch( IllegalArgumentException e )
        {
            System.out.println( "List is empty" );
        }
        catch( NoSuchElementException e1 )
        {
            System.out.println( "No such element found." );
        }

        return nodeValue;
    }




    /*
    Class Name: DSAListNode()
    Purpose: Acts as a place-holder for a value in a linked list
    Comments: Able to be serialised
    */

    protected class DSAListNode implements Serializable
    {
//        Class fields
        private Object m_value;
        private DSAListNode m_next;
        private DSAListNode m_prev;




        /*
        Name: DSAListNode()
        Purpose: Default Constructor for DSAListNode
        Imports: Object inValue
        Exports: Nothing
        */

        public DSAListNode( Object inValue )
        {
            m_value = inValue;
            m_next = null;
            m_prev = null;
        }




        /*
        Name: getValue()
        Purpose: Accessor for the value of the node
        Imports: Nothing
        Exports: Object m_value
        */

        public Object getValue()
        {
            return m_value;
        }




        /*
        Name: getNext()
        Purpose: Accessor for the value next to the node
        Imports: Nothing
        Exports: DSAListNode m_next
        */

        public DSAListNode getNext()
        {
            return m_next;
        }




        /*
        Name: getPrev()
        Purpose: Accessor for the value previous to the node
        Imports: Nothing
        Exports: DSAListNode m_prev
        */

        public DSAListNode getPrev()
        {
            return m_prev;
        }




        /*
        Name: setNext()
        Purpose: Mutator for the value next to the node
        Imports: DSAListNode inValue
        Exports: Nothing
        */

        public void setNext( DSAListNode inValue )
        {
            m_next = inValue;
        }




        /*
        Name: getNext()
        Purpose: Mutator for the value previous to the node
        Imports: DSAListNode m_prev
        Exports: Nothing
        */

        public void setPrev( DSAListNode inValue )
        {
            m_prev = inValue;
        }

    }




    /*
    Class Name: DSALinkedListIterator()
    Purpose: Iterator class for DSALinkedList
    */

    private class DSALinkedListIterator implements Iterator
    {
        private DSAListNode iterNext;




        /*
        Name: DSALinkedListIterator()
        Purpose: Default Constructor for DSALinkedListIterator
        Imports: DSALinkedList theList
        Exports: Nothing
        */

        public DSALinkedListIterator( DSALinkedList theList )
        {
            iterNext = theList.head;
        }




        /*
        Name: hasNext()
        Purpose: Accessor for iterator
        Imports: Nothing
        Exports: boolean iterNext
        */

        public boolean hasNext()
        {
            return ( iterNext != null );
        }




        /*
        Name: next()
        Purpose: Goes through the list
        Imports: Nothing
        Exports: Object value
        */

        public Object next()
        {
            Object value;
            if( iterNext == null )
            {
                value = null;
            }
            else
            {
                value = iterNext.getValue();
                iterNext = iterNext.getNext();
            }
            return value;
        }

        public void remove()
        {
            throw new UnsupportedOperationException( "Not Supported" );
        }
    }




    /*
    Class Name: iterator()
    Purpose: Returns a new Iterator of internal type DSALinkedListIterator
    */

    public Iterator iterator()
    {
        return new DSALinkedListIterator( this );
    }
}
