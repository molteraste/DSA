/*
File: DSAHeap.java
Author: Tobias Jun
Unit: COMP1002
Purpose: Creates a heap
References: 1. Modified DSAHeap.java file from practical 8
Last Modified: 22/05/2023
*/

public class DSAHeap
{

    /*
    Private Class: DSAHeapEntry
    Purpose: Creates a heap entry
    Last Modified: 22/05/2023
    */
    private class DSAHeapEntry
    {
        private double priority;
        private Object value;

        /*
        Name: DSAHeapEntry()
        Purpose: Default Constructor method
        creates a heap entry
        Imports: Nothing
        Exports: Nothing
        */

        public DSAHeapEntry()
        {
            priority = 0;
            value = null;
        }




        /*
        Name: DSAHeapEntry()
        Purpose: Parameter Constructor method
        creates a heap entry
        Imports: double inPriority, Object inValue
        Exports: Nothing
        */

        public DSAHeapEntry( double inPriority, Object inValue )
        {
            priority = inPriority;
            value = inValue;
        }




        /*
        Name: getPriority()
        Purpose: Accessor method that returns the priority of
        the heap entry
        Imports: Nothing
        Exports: double priority
        */

        public double getPriority()
        {
            return priority;
        }




        /*
        Name: getValue()
        Purpose: Accessor method that returns the value of
        the heap entry
        Imports: Nothing
        Exports: Object value
        */

        public Object getValue()
        {
            return value;
        }
    }

    private DSAHeapEntry[] heap;
    private int count;

    /*
    Name: DSAHeap()
    Purpose: Parameter Constructor method
    creates a heap
    Imports: int size
    Exports: Nothing
    */

    public DSAHeap( int size )
    {
        count = 0;
        heap = new DSAHeapEntry[size];

        for( int i = 0; i < count; i++ )
        {
            heap[i] = new DSAHeapEntry();
        }
    }




    /*
    Name: DSAHashTable()
    Purpose: Copy Constructor method
    creates a hash table
    Imports: DSAHeap inHeap
    Exports: Nothing
    */

    public DSAHeap( DSAHeap inHeap )
    {
        count = inHeap.getCount();
        heap = new DSAHeapEntry[inHeap.heap.length];

        for( int i = 0; i < count; i++ )
        {
            heap[i] = inHeap.heap[i];
        }
    }




    /*
    Name: getCount()
    Purpose: Accessor method that returns the count of
    the heap
    Imports: Nothing
    Exports: int count
    */

    public int getCount()
    {
        return count;
    }




    /*
    Name: isEmpty()
    Purpose: Determines whether the heap is empty
    Imports: Nothing
    Exports: boolean is
    */

    public boolean isEmpty()
    {
        boolean is;

        is = false;

        if( count == 0 )
        {
            is = true;
        }

        return is;
    }




    /*
    Name: add()
    Purpose: Adds a heap entry to the heap and reorganises the
    heap
    Imports: double priority, Object value
    Exports: Nothing
    */

    public void add( double priority, Object value )
    {
        try
        {
            if( count == heap.length )
            {
                throw new IllegalArgumentException();
            }
            else
            {
                count += 1;
                heap[count - 1] = new DSAHeapEntry( priority, value );
                trickleUpRec( heap, count - 1 );
            }
        }
        catch( IllegalArgumentException e )
        {
            System.out.println( "Heap is full!" );
        }

    }




    /*
    Name: remove()
    Purpose: Removes the top heap entry and reorganises the
    heap
    Imports: Nothing
    Exports: Object
    */

    public Object remove()
    {
        DSAHeapEntry removed = new DSAHeapEntry();

        try
        {
            if( count != 0 )
            {
                removed = heap[0];
                heap[0] = heap[count - 1];
                count -= 1;
                trickleDownRec( heap, 0, count - 1 );
            }
            else if( count == 0 )
            {
                throw new IllegalArgumentException();
            }
        }
        catch( IllegalArgumentException e )
        {
            System.out.println( "Heap is empty!" );
        }

        return removed.getValue();
    }




    /*
    Name: remove()
    Purpose: Removes a heap entry with the given value and
    reorganises the heap
    Imports: String value
    Exports: Object
    */

    public Object remove( String value )
    {
        DSAHeapEntry removed = new DSAHeapEntry();
        int idx;

        idx = 0;

        try
        {
            if( count != 0 )
            {
                for( int i = 0; i < count; i++ )
                {
                    if( heap[i].getValue().equals( value ) )
                    {
                        removed = heap[i];
                        idx = i;
                    }
                }
                count -= 1;
                trickleDownRec( heap, idx, count - 1 );
            }
            else if( count == 0 )
            {
                throw new IllegalArgumentException();
            }
        }
        catch( IllegalArgumentException e )
        {
            System.out.println( "Heap is empty!" );
        }

        return removed.getValue();
    }




    /*
    Name: display()
    Purpose: Displays the heap
    Imports: Nothing
    Exports: Nothing
    */

    public void display()
    {
        for( int i = 0; i < count; i++ )
        {
            System.out.println( "Priority: " + heap[i].getPriority() +
                    ", Value: " + heap[i].getValue() );
        }
    }




    /*
    Name: trickleUpRec()
    Purpose: Moves the heap entries in a trickle up
    process
    Imports: DSAHeapEntry[] heapArray, int curIdx
    Exports: DSAHeapEntry[] heapArray
    */

    public DSAHeapEntry[] trickleUpRec( DSAHeapEntry[] heapArray, int curIdx )
    {
        int parentIdx;
        Object temp;

        parentIdx = ( curIdx - 1 ) / 2;

        if( curIdx > 0 )
        {
            if( heapArray[curIdx].getPriority() >
                    heapArray[parentIdx].getPriority() )
            {
                temp = heapArray[parentIdx];
                heapArray[parentIdx] = heapArray[curIdx];
                heapArray[curIdx] = ( DSAHeapEntry ) temp;
                trickleUpRec( heapArray, parentIdx );
            }
        }

        return heapArray;
    }




    /*
    Name: trickleDownRec()
    Purpose: Moves the heap entries in a trickle down
    process
    Imports: DSAHeapEntry[] heapArray, int curIdx, int numItems
    Exports: DSAHeapEntry[] heapArray
    */

    public DSAHeapEntry[] trickleDownRec( DSAHeapEntry[] heapArray,
                                          int curIdx, int numItems )
    {
        int lChildIdx;
        int rChildIdx;
        int largeIdx;
        Object temp;

        lChildIdx = curIdx * 2 + 1;
        rChildIdx = lChildIdx + 1;

        if( lChildIdx < numItems )
        {
            largeIdx = lChildIdx;

            if( rChildIdx < numItems )
            {
                if( heapArray[lChildIdx].getPriority() < heapArray[rChildIdx].getPriority() )
                {
                    largeIdx = rChildIdx;
                }
            }

            if( heapArray[largeIdx].getPriority() > heapArray[curIdx].getPriority() )
            {
                temp = heapArray[largeIdx];
                heapArray[largeIdx] = heapArray[curIdx];
                heapArray[curIdx] = (DSAHeapEntry) temp;
                trickleDownRec( heapArray, largeIdx, numItems );
            }
        }

        return heapArray;
    }
}
