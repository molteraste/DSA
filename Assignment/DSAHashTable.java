import java.util.NoSuchElementException;

/*
File: DSAHashTable.java
Author: Tobias Jun
Unit: COMP1002
Purpose: Creates a hash table
References: 1. Modified DSAHashTable.java file from practical 7
Last Modified: 22/05/2023
*/

public class DSAHashTable
{

    /*
    Private Class: DSAHashEntry
    Purpose: Creates a hash entry
    Last Modified: 22/05/2023
    */
    private static class DSAHashEntry
    {
        private String key;
        private Object value;
        private Object value1;
        private Object value2;
        private int state;

        /*
        Name: DSAHashEntry()
        Purpose: Default Constructor method
        creates a hash entry
        Imports: Nothing
        Exports: Nothing
        */

        public DSAHashEntry()
        {
            key = "";
            value = null;
            value1 = null;
            value2 = null;
            state = 0;
        }




        /*
        Name: DSAHashEntry()
        Purpose: Parameter Constructor method
        creates a hash entry
        Imports: String inKey, Object inValue, Object inValue1,
        Object inValue2
        Exports: Nothing
        */

        public DSAHashEntry( String inKey, Object inValue, Object inValue1,
                             Object inValue2 )
        {
            key = inKey;
            value = inValue;
            value1 = inValue1;
            value2 = inValue2;
            state = 1;
        }




        /*
        Name: DSAHashEntry()
        Purpose: Parameter Constructor method
        creates a hash entry
        Imports: int state1
        Exports: Nothing
        */

        public DSAHashEntry( int state1 )
        {
            key = "";
            value = null;
            value1 = null;
            value2 = null;
            state = state1;
        }




        /*
        Name: getKey()
        Purpose: Accessor method that returns the key of
        the hash entry
        Imports: Nothing
        Exports: String key
        */

        public String getKey()
        {
            return key;
        }




        /*
        Name: getValue()
        Purpose: Accessor method that returns the first
        value of the hash entry
        Imports: Nothing
        Exports: Object value
        */

        public Object getValue()
        {
            return value;
        }




        /*
        Name: getValue1()
        Purpose: Accessor method that returns the second
        value of the hash entry
        Imports: Nothing
        Exports: Object value1
        */

        public Object getValue1()
        {
            return value1;
        }




        /*
        Name: getValue2()
        Purpose: Accessor method that returns the third
        value of the hash entry
        Imports: Nothing
        Exports: Object value2
        */

        public Object getValue2()
        {
            return value2;
        }




        /*
        Name: getState()
        Purpose: Accessor method that returns the state
        of the hash entry
        Imports: Nothing
        Exports: int state
        */

        public int getState()
        {
            return state;
        }

    }

    private DSAHashEntry[] hashArray;
    private int count;
    private final double UPPER_LF = 0.75;
    private final double LOWER_LF = 0.4;

    /*
    Name: DSAHashTable()
    Purpose: Parameter Constructor method
    creates a hash table
    Imports: int tableSize
    Exports: Nothing
    */

    public DSAHashTable( int tableSize )
    {
        int actualSize;

        actualSize = findNextPrime( tableSize );
        count = 0;
        hashArray = new DSAHashEntry[actualSize];

        for( int ii = 0; ii < actualSize; ii++ )
        {
            hashArray[ii] = new DSAHashEntry();
        }
    }




    /*
    Name: get()
    Purpose: Accessor method that returns the values of
    the hash entry that matches the given key
    Imports: String inKey, int option
    Exports: Object retValue
    */

    public Object get( String inKey, int option )
    {
        int hashIdx;
        Object retValue;
        int origIdx;
        boolean found;
        boolean giveUp;

        hashIdx = hash( inKey );
        origIdx = hashIdx;
        found = false;
        giveUp = false;

        try
        {
            while( !found && !giveUp )
            {
                if( hashArray[hashIdx].getState() == 0 )
                {
                    giveUp = true;
                }
                else if( hashArray[hashIdx].getKey().equals( inKey ) )
                {
                    found = true;
                }
                else
                {
                    hashIdx = ( hashIdx + stepHash( inKey ) ) % hashArray.length;
                    if( hashIdx == origIdx )
                    {
                        giveUp = true;
                    }
                }
            }

            if( !found )
            {
                throw new NoSuchElementException();
            }
        }
        catch( NoSuchElementException e )
        {
            System.out.println( "Key not found" );
        }

        retValue = hashArray[hashIdx].getValue().toString() + " degrees Celsius, " +
                hashArray[hashIdx].getValue1() + "%, " +
                hashArray[hashIdx].getValue2() + "km/h";

//        If only specific information is desired
        if( option == 1 )
        {
            retValue = hashArray[hashIdx].getValue() + " degrees Celsius";
        }
        else if( option == 2 )
        {
            retValue = hashArray[hashIdx].getValue1() + "%";
        }
        else if( option == 3 )
        {
            retValue = hashArray[hashIdx].getValue2() + "km/h";
        }

        return retValue;
    }




    /*
    Name: put()
    Purpose: Adds a new hash entry into the hash table
    Imports: String inKey, Object inValue, Object inValue1,
    Object inValue2
    Exports: Nothing
    */

    public void put( String inKey, Object inValue, Object inValue1, Object inValue2 )
    {
        int hashIdx;
        int origIdx;
        boolean found;
        boolean giveUp;

        found = false;
        giveUp = false;
        hashIdx = hash(inKey);
        origIdx = hashIdx;

        try
        {
            while ( !found && !giveUp )
            {
//                If the place for the new hash entry is available
                if ( hashArray[hashIdx].getState() == 0 || hashArray[hashIdx].getState() == -1 )
                {
                    giveUp = true;
                }
//                If there already is a hash entry of given key
                else if ( hashArray[hashIdx].getKey().equals( inKey ) )
                {
                    found = true;
                }
//                If the placement of the new hash entry is not available
                else
                {
                    hashIdx = ( hashIdx + stepHash( inKey ) ) % hashArray.length;
                    if ( hashIdx == origIdx )
                    {
                        giveUp = true;
                    }
                }
            }

            if( found )
            {
                throw new IllegalArgumentException();
            }

            hashArray[hashIdx] = new DSAHashEntry( inKey, inValue, inValue1, inValue2 );
            count++;
        }
        catch( IllegalArgumentException e )
        {
            System.out.println( "Key: " + inKey + " is already in table" );
        }

//        For resizing
        if( loadFactorCalc() > UPPER_LF )
        {
            resize( hashArray.length * 2 );
        }
    }




    /*
    Name: remove()
    Purpose: Removes a hash entry with the given key
    Imports: String inKey
    Exports: Object removed
    */

    public Object remove( String inKey )
    {
        int hashIdx;
        int origIdx;
        boolean found;
        boolean giveUp;
        Object removed;

        hashIdx = hash( inKey );
        origIdx = hashIdx;
        found = false;
        giveUp = false;
        removed = null;

        try
        {
            while ( !found && !giveUp )
            {
                if ( hashArray[hashIdx].getState() == 0 )
                {
                    giveUp = true;
                }
                else if ( hashArray[hashIdx].getKey().equals( inKey ) )
                {
                    found = true;
                }
                else
                {
                    hashIdx = ( hashIdx + stepHash( inKey ) ) % hashArray.length;
                    if( hashIdx == origIdx )
                    {
                        giveUp = true;
                    }
                }
            }

            if( !found )
            {
                throw new NoSuchElementException();
            }

            removed = hashArray[hashIdx].getValue();
            hashArray[hashIdx] = new DSAHashEntry( -1 );
            count--;
        }
        catch( NoSuchElementException e )
        {
            System.out.println( "Key not found" );
        }

//        For resizing
        if( loadFactorCalc() < LOWER_LF )
        {
            resize( hashArray.length / 2 );
        }

        return removed;
    }




    /*
    Name: findNextPrime()
    Purpose: Finds the next prime number to the given
    number
    Imports: int startVal
    Exports: int primeVal
    */

    public int findNextPrime( int startVal )
    {
        int primeVal;
        boolean isPrime;
        int ii;

        if( startVal % 2 == 0 )
        {
            primeVal = startVal + 1;
        }
        else
        {
            primeVal = startVal;
        }
        primeVal = primeVal - 2;
        isPrime = false;

        while( !isPrime )
        {
            primeVal = primeVal + 2;

            ii = 3;
            isPrime = true;

            while( ii * ii <= primeVal && isPrime )
            {
                if( primeVal % ii == 0 )
                {
                    isPrime = false;
                }
                else
                {
                    ii = ii + 2;
                }
            }
        }
        return primeVal;
    }




    /*
    Name: hash()
    Purpose: Provides the placement of the hash entry
    in the hash table
    Imports: String key
    Exports: int
    */

    public int hash( String key )
    {
        int hashIdx = 0;

        for( int ii = 0; ii < key.length(); ii++ )
        {
            hashIdx = ( 33 * hashIdx ) + key.charAt( ii );
        }

        return hashIdx % hashArray.length;
    }




    /*
    Name: stepHash()
    Purpose: Provides a new placement for a hash entry in
    the hash table when there already is a hash entry in the
    original placement
    Imports: String key
    Exports: int hashStep
    */

    public int stepHash( String key )
    {
        int hashStep;
        int hashIdx;

        hashIdx = hash( key );

        hashStep = 7 - ( hashIdx % 7 );

        return hashStep;
    }




    /*
    Name: loadFactorCalc()
    Purpose: Calculates the load factor of the hash table
    Imports: Nothing
    Exports: double loadFactor
    */

    public double loadFactorCalc()
    {
        double loadFactor;

        loadFactor = ( double) count / ( double ) hashArray.length;

        return loadFactor;
    }




    /*
    Name: resize()
    Purpose: Resizes the hash table
    Imports: int size
    Exports: Nothing
    */

    public void resize( int size )
    {
        DSAHashEntry[] oldHashArray;
        int newActualSize;
        int newCount;

        newActualSize = findNextPrime( size );
        newCount = 0;

        oldHashArray = hashArray;

        hashArray = new DSAHashEntry[newActualSize];

        for( int i = 0; i < newActualSize; i++ )
        {
            hashArray[i] = new DSAHashEntry();
        }

        for( int ii = 0; ii < oldHashArray.length; ii++ )
        {
            if( oldHashArray[ii].getState() == 1 )
            {
                put( oldHashArray[ii].getKey(), oldHashArray[ii].getValue(),
                        oldHashArray[ii].getValue1(), oldHashArray[ii].getValue2() );
                newCount++;
            }
        }
        count = newCount;
    }
}
