import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

/*
File: Main.java
Author: Tobias Jun
Unit: COMP1002
Purpose: Main program
Last Modified: 22/05/2023
*/

public class Main
{



    /*
    Name: Main()
    Purpose: Executes the program with a menu
    and initialises all necessities
    Imports: String[] args
    Exports: Nothing
    */

    public static void main( String[] args )
    {
        int choice;

        DSAGraph graph;
        DSAHeap totalHeap;
        DSAHeap prepHeap;
        DSAHeap tempHeap;
        DSAHeap humidHeap;
        DSAHeap windHeap;
        DSAHashTable table;
        String dataOption;
        boolean condition;

        Scanner scan = new Scanner( System.in );
        dataOption = "";
        condition = true;

        while( condition )
        {
//            Choice in choosing which test data
            System.out.println( "Please enter which test data you would like to work with" +
                    "\n1. Normal" +
                    "\n2. Smaller" +
                    "\n3. Larger" );

            choice = scan.nextInt();

            switch( choice )
            {
                case 1:
                    condition = false;
                    break;
                case 2:
                    dataOption = "Smaller";
                    condition = false;
                    break;
                case 3:
                    dataOption = "Larger";
                    condition = false;
                    break;
                default:
                    break;
            }
        }

//        Initialising the data structures
        graph = readLocationFile( "location" + dataOption + ".txt" );
        table = readDataFile( "UAVdata" + dataOption + ".txt" );
        totalHeap = readDataFileHeap( "UAVdata" + dataOption + ".txt", "All" );
        prepHeap = readDataFileHeap( "UAVdata" + dataOption + ".txt", "prep" );
        tempHeap = readDataFileHeap( "UAVdata" + dataOption + ".txt", "T" );
        humidHeap = readDataFileHeap( "UAVdata" + dataOption + ".txt", "H" );
        windHeap = readDataFileHeap( "UAVdata" + dataOption + ".txt", "W" );

        while( true )
        {
            menu();
            choice = scan.nextInt();

            switch( choice )
            {
                case 1:
                    graph.displayAsList();
                    break;
                case 2:
                    option2( graph, table );
                    break;
                case 3:
                    option3( graph );
                    break;
                case 4:
                    option4N5N6( graph, table, totalHeap,
                            tempHeap, humidHeap, windHeap,
                            prepHeap, 0 );
                    break;
                case 5:
                    option4N5N6( graph, table, totalHeap,
                            tempHeap, humidHeap, windHeap,
                            prepHeap, 1 );
                    break;
                case 6:
                    option4N5N6( graph, table, totalHeap,
                            tempHeap, humidHeap, windHeap,
                            prepHeap, 2 );
                    break;
                case 7:
                    option7( table );
                    break;
                case 8:
                    option8( totalHeap, tempHeap, humidHeap, windHeap );
                    break;
                case 9:
                    option9( graph, prepHeap );
                    break;
                default:
                    System.out.println( "Please enter a valid option" );
                    break;
            }
        }
    }




    /*
    Name: menu()
    Purpose: Shows the main menu of the program
    Imports: Nothing
    Exports: Nothing
    */

    public static void menu()
    {
        System.out.println();
        System.out.println( "Welcome to the Bushfire Monitoring Program." +
                "\n Please choose an option below." +
                "\n 1. Display graph of location" +
                "\n 2. Show the shortest path between two locations" +
                "\n 3. Show the full graph" +
                "\n 4. Insert location to graph" +
                "\n 5. Remove location from graph" +
                "\n 6. Add edge to graph" +
                "\n 7. Search for location" +
                "\n 8. Show risk data" +
                "\n 9. Create Itinerary" );
    }




    /*
    Name: option9()
    Purpose: Executes option 9 from the menu
    and prints the itinerary for the UAVs
    Imports: DSAGraph graph, DSAHeap heap
    Exports: Nothing
    */

    public static void option9( DSAGraph graph, DSAHeap heap )
    {
        DSALinkedList uavTrips;

        uavTrips = itinerary( graph, heap );

        System.out.println();
        for( Object o : uavTrips )
        {
            System.out.println( o );
        }
    }




    /*
    Name: itinerary()
    Purpose: Creates the itinerary for the UAVs
    Imports: DSAGraph graph, DSAHeap heap
    Exports: DSALinkedList uav
    */

    public static DSALinkedList itinerary( DSAGraph graph, DSAHeap heap )
    {
        Object topOfHeap;
        String[] splitLine;
        String trip;
        DSALinkedList prev;
        DSALinkedList locations;
        DSALinkedList uav;
        DSAHeap directedHeap;
        DSAHeap copyHeap;

        trip = "";
        uav = new DSALinkedList();
        locations = new DSALinkedList();
        prev = new DSALinkedList();
        directedHeap = new DSAHeap( 20 );
        copyHeap = new DSAHeap( heap );

        while( !copyHeap.isEmpty() )
        {
            topOfHeap = copyHeap.remove();
            splitLine = topOfHeap.toString().split( " " );
//            If at least one of the risk factors is high then added
//            to heap in alphabetical order.
            if( splitLine[0].equals( "3" ) || splitLine[1].equals( "3" ) ||
            splitLine[2].equals( "3" ) )
            {
                directedHeap.add( -( ( int ) splitLine[3].charAt( 0 ) ), splitLine[3] );
            }
        }

        locations.insertLast( directedHeap.remove() );

        while( !directedHeap.isEmpty() )
        {
            topOfHeap = directedHeap.remove();
            locations.insertLast( topOfHeap );

            if ( !graph.hasMultiPath( locations ) )
            {
//                If a path can't be made it adds the path that could
//                to the linked list uav
                uav.insertLast( "UAV journey: " + trip + "km journey" );
//                The prev linked list acts as a list of locations that the
//                previous uav has gone through and deletes these locations
//                from the locations list if they cannot make a path to the
//                current location
                while ( !graph.hasMultiPath( locations ) )
                {
                    locations.remove( prev.removeLast().toString() );
                }
            }
//            If the locations in the list can make a path
//            trip becomes this path
            trip = graph.shortestPathMulti( locations );

            prev.insertLast( topOfHeap );
        }
        uav.insertLast( "UAV journey: " + trip + "km journey" );

        return uav;
    }




    /*
    Name: option8()
    Purpose: Executes option 8 from the menu
    and prints the risk data
    Imports: DSAHeap heap, DSAHeap heap1, DSAHeap heap2, DSAHeap heap3
    Exports: Nothing
    */

    public static void option8( DSAHeap heap, DSAHeap heap1, DSAHeap heap2, DSAHeap heap3 )
    {
        System.out.println( "Showing the total level of " +
                "risk in each location with the first" +
                " location having the highest risk level." );
        heap.display();

        System.out.println( "Showing the level of" +
                " temperature risk");
        heap1.display();

        System.out.println( "Showing the level of" +
                " humidity risk");
        heap2.display();

        System.out.println( "Showing the level of" +
                " wind-speed risk");
        heap3.display();
    }




    /*
    Name: option7()
    Purpose: Executes option 7 from the menu
    and prints the information of a given location
    Imports: DSAHashTable table
    Exports: Nothing
    */

    public static void option7( DSAHashTable table )
    {
        Scanner scan = new Scanner( System.in );

        System.out.println( "Please enter the name of the location" +
                " you would like to search for" );
        System.out.println( table.get( scan.next(), 0 ) );
    }




    /*
    Name: option4N5N6()
    Purpose: Executes either option 4, 5 or 6 from the menu.
    Option 4 - Adds a new location
    Option 5 - Deletes a location
    Option 6 - Adds a new edge
    Imports: DSAGraph graph, DSAHashTable table, DSAHeap heap,
    DSAHeap heap1, DSAHeap heap2, DSAHeap heap3, DSAHeap heap4,
    int change
    Exports: Nothing
    */

    public static void option4N5N6( DSAGraph graph, DSAHashTable table,
                                    DSAHeap heap, DSAHeap heap1,
                                    DSAHeap heap2, DSAHeap heap3,
                                    DSAHeap heap4, int change )
    {
        Scanner scan = new Scanner( System.in );
        String key;
        int temp;
        int humidity;
        int windSpeed;
        int tempThresh;
        int humidThresh;
        int windThresh;

        if( change == 0 )
        {
            System.out.println( "Please enter the name of the" +
                    " location you would like to insert" );
            key = scan.next();
            graph.addVertex( key );

            System.out.println( "Please enter the temperature, humidity, " +
                    "and the wind speed of the location" );
            temp = scan.nextInt();
            humidity = scan.nextInt();
            windSpeed = scan.nextInt();
            table.put( key, temp, humidity, windSpeed );

//            Initialising thresh-hold variables to their respective
//            risk levels
            tempThresh = threshHolds( "T", temp );
            humidThresh = threshHolds( "H", humidity );
            windThresh = threshHolds( "W", windSpeed );

//            If the given values for the temperature, humidity or wind speed
//            are valid then this code occurs
            if( tempThresh != 0 && humidThresh != 0
            && windThresh != 0 )
            {
                heap.add( tempThresh + humidThresh +
                        windThresh, key );
                heap1.add( tempThresh, key );
                heap2.add( humidThresh, key );
                heap3.add( windThresh, key );
                heap4.add( tempThresh + humidThresh +
                        windThresh, tempThresh + " " +
                        humidThresh + " " + windThresh + " " +
                        key );
            }
            else
            {
//                If invalid values the added items have to be removed
                graph.deleteVertex( key );
                table.remove( key );
            }
        }
        else if( change == 1 )
        {
            System.out.println( "Please enter a location to delete" );
            key = scan.next();

            System.out.println( "Deleted: " +
                    graph.deleteVertex( key ) );

            table.remove( key );
            heap.remove( key );
            heap1.remove( key );
            heap2.remove( key );
            heap3.remove( key );
        }
        else if( change == 2 )
        {
            System.out.println( "Please enter the starting location," +
                    "the ending location and the distance between them" );
            graph.addEdge1( scan.next(), scan.next(), scan.nextDouble() );
        }
    }




    /*
    Name: option3()
    Purpose: Executes option 3 from the menu
    and shows the full graph through depth first search
    Imports: DSAGraph graph
    Exports: Nothing
    */

    public static void option3( DSAGraph graph )
    {
        String list;

        list = graph.depthFirstSearch();
        System.out.println( list );
    }




    /*
    Name: option2()
    Purpose: Executes option 2 from the menu
    and shows the breadth first search between two locations
    and the shortest path accounting for weight
    Imports: DSAGraph graph, DSAHashTable table
    Exports: Nothing
    */

    public static void option2( DSAGraph graph, DSAHashTable table )
    {
        String list;
        String loc1;
        String loc2;
        String[] splitLine;
        Scanner scan = new Scanner( System.in );

        System.out.println( "Please enter" +
                " the two locations.");

        loc1 = scan.next();
        loc2 = scan.next();

        try
        {
//            If there is an existing path between the locations the
//            following code executes
            if( graph.hasPath( loc1, loc2 ) )
            {
                System.out.println( "Showing the breadth first search " +
                        "between locations");
                System.out.println( graph.breadthFirstSearch( loc1, loc2 ) );

                System.out.println( "Now showing the shortest path" +
                        " between locations accounting for the distances" );

                list = graph.shortestPathWithWeight( loc1, loc2 );
                splitLine = list.split( ", " );

                System.out.println( "The shortest path is: " );
                for ( int ii = 0; ii < splitLine.length - 1; ii++ )
                {
                    System.out.print( splitLine[ii] + ", " );
                }

                System.out.println();

//                Shows the information of each location
                for ( int i = 0; i < splitLine.length - 1; i++ )
                {
                    System.out.println( splitLine[i] + ": " +
                            table.get( splitLine[i], 0 ) );
                }

                System.out.println( "The path distance is: " +
                        splitLine[splitLine.length - 1] + "km" );
            }
            else
            {
                throw new IllegalArgumentException();
            }
        }
        catch ( IllegalArgumentException e )
        {
            System.out.println( "There is no path from " + loc1 + " to " + loc2 );
        }
    }




    /*
    Name: threshHolds()
    Purpose: Determines the risk level according to the
    thresh-holds of each risk factor
    Imports: String type, int data
    Exports: int riskLevel
    */

    private static int threshHolds( String type, int data )
    {
        int riskLevel;

        riskLevel = 0;

        try
        {
            if( type.equals( "T" ) )
            {
                if( data >= 25 && data <= 32 )
                {
                    riskLevel = 1;
                }
                else if( data >= 33 && data <= 40 )
                {
                    riskLevel = 2;
                }
                else if( data > 40 && data <= 48 )
                {
                    riskLevel = 3;
                }
                else
                {
                    System.out.println( "Temperature" );
                    throw new IllegalArgumentException();
                }
            }
            else if( type.equals( "H" ) )
            {
                if( data > 50 && data <= 60 )
                {
                    riskLevel = 1;
                }
                else if( data >= 31 && data <= 50 )
                {
                    riskLevel = 2;
                }
                else if( data < 31 && data >= 15 )
                {
                    riskLevel = 3;
                }
                else
                {
                    System.out.println( "Humidity" );
                    throw new IllegalArgumentException();
                }
            }
            else if( type.equals( "W" ) )
            {
                if( data < 41 && data >= 30 )
                {
                    riskLevel = 1;
                }
                else if( data >= 41 && data <= 55 )
                {
                    riskLevel = 2;
                }
                else if( data > 55 && data <= 100 )
                {
                    riskLevel = 3;
                }
                else
                {
                    System.out.println( "Wind speed" );
                    throw new IllegalArgumentException();
                }
            }
        }
        catch( IllegalArgumentException e )
        {
            System.out.println( " is out of scope" );
        }
        return riskLevel;
    }




    /*
    Name: readDataFileHeap()
    Purpose: Reads the given file and exports the information
    onto a heap
    Imports: String fileName, String type
    Exports: DSAHeap inHeap
    */

    private static DSAHeap readDataFileHeap( String fileName, String type )
    {
        FileInputStream fileStream = null;
        InputStreamReader reader;
        BufferedReader br;
        String line;
        String[] splitLine;
        DSAHeap inHeap;

        inHeap = new DSAHeap( 100 );

        try
        {
            fileStream = new FileInputStream( fileName );
            reader = new InputStreamReader( fileStream );
            br = new BufferedReader( reader );

            line = br.readLine();
            while( line != null )
            {
                splitLine = line.split( " " );

//                Heap for all risk factors
                if( type.equals( "All" ) )
                {
                    inHeap.add( ( threshHolds( "T", Integer.parseInt( splitLine[1] ) ) +
                            threshHolds( "H", Integer.parseInt( splitLine[2] ) ) +
                            threshHolds( "W", Integer.parseInt( splitLine[3] ) ) ),
                            splitLine[0] );
                }

//              Heap for UAV itinerary
                else if( type.equals( "prep" ) )
                {
                    inHeap.add( ( threshHolds( "T", Integer.parseInt( splitLine[1] ) ) +
                                    threshHolds( "H", Integer.parseInt( splitLine[2] ) ) +
                                    threshHolds( "W", Integer.parseInt( splitLine[3] ) ) ),
                            ( threshHolds( "T", Integer.parseInt( splitLine[1] ) ) + " "
                                    + threshHolds( "H", Integer.parseInt( splitLine[2] ) ) +
                                    " " + threshHolds( "W", Integer.parseInt( splitLine[3] ) ) +
                                    " " + splitLine[0] ) );
                }

//                Heap for temperature risk
                else if( type.equals( "T" ) )
                {
                    inHeap.add( threshHolds( "T", Integer.parseInt( splitLine[1] ) ),
                            splitLine[0] );
                }

//                Heap for humidity risk
                else if( type.equals( "H" ) )
                {
                    inHeap.add( threshHolds( "H", Integer.parseInt( splitLine[2] ) ),
                            splitLine[0] );
                }

//                Heap for wind speed risk
                else if( type.equals( "W" ) )
                {
                    inHeap.add( threshHolds( "W", Integer.parseInt( splitLine[3] ) ),
                            splitLine[0] );
                }
                line = br.readLine();
            }
            fileStream.close();
        }
        catch( IOException errorDetails )
        {
            if( fileStream != null )
            {
                try
                {
                    fileStream.close();
                }
                catch(IOException ex2)
                { }
            }
            System.out.println( "Error in fileProcessing: " +
                    errorDetails.getMessage() );
        }
        return inHeap;
    }




    /*
    Name: readDataFile()
    Purpose: Reads the given file and exports the information
    onto a hash table
    Imports: String fileName
    Exports: DSAHashTable inHashTable
    */

    private static DSAHashTable readDataFile( String fileName )
    {
        FileInputStream fileStream = null;
        InputStreamReader reader;
        BufferedReader br;
        String line;
        String[] splitLine;
        DSAHashTable inHashTable;

        inHashTable = new DSAHashTable( 11 );
        try
        {
            fileStream = new FileInputStream( fileName );
            reader = new InputStreamReader( fileStream );
            br = new BufferedReader( reader );

            line = br.readLine();

            while( line != null )
            {
                splitLine = line.split( " " );
                inHashTable.put( splitLine[0], splitLine[1], splitLine[2], splitLine[3] );
                line = br.readLine();
            }
            fileStream.close();
        }

        catch( IOException errorDetails )
        {
            if( fileStream != null )
            {
                try
                {
                    fileStream.close();
                }
                catch(IOException ex2)
                { }
            }
            System.out.println( "Error in fileProcessing: " +
                    errorDetails.getMessage() );
        }
        return inHashTable;
    }




    /*
    Name: readDataLocationFile()
    Purpose: Reads the given file and exports the information
    onto a graph
    Imports: String fileName
    Exports: DSAGraph inGraph
    */

    private static DSAGraph readLocationFile( String fileName )
    {
        FileInputStream fileStream = null;
        InputStreamReader reader;
        BufferedReader br;
        String line;
        String[] splitLine;
        DSAGraph inGraph;

        inGraph = new DSAGraph();
        try
        {
            fileStream = new FileInputStream( fileName );
            reader = new InputStreamReader( fileStream );
            br = new BufferedReader( reader );

            br.readLine();
            line = br.readLine();

            while( line != null )
            {
                splitLine = line.split( " " );
                if( !inGraph.hasVertex( splitLine[0] ) )
                {
                    inGraph.addVertex( splitLine[0] );
                }
                if( !inGraph.hasVertex( splitLine[1] ) )
                {
                    inGraph.addVertex( splitLine[1] );
                }
                inGraph.addEdge1( splitLine [0], splitLine[1], Double.parseDouble( splitLine[2] ) );
                line = br.readLine();
            }
            fileStream.close();
        }
        catch( IOException errorDetails )
        {
            if( fileStream != null )
            {
                try
                {
                    fileStream.close();
                }
                catch(IOException ex2)
                { }
            }
            System.out.println( "Error in fileProcessing: " +
                    errorDetails.getMessage() );
        }
        return inGraph;
    }
}
