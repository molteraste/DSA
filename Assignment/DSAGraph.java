import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.NoSuchElementException;

/*
File: DSAGraph.java
Author: Tobias Jun
Unit: COMP1002
Purpose: Creates a graph
References: 1. https://stackoverflow.com/questions/49734206/finding-the-shortest-path-between-two-pointsbfs
2. Modified DSAGraph.java file from practical 6
Last Modified: 22/05/2023
*/
public class DSAGraph
{
    private DSALinkedList vertices;
    private DSALinkedList edges;

    /*
    Name: DSAGraph()
    Purpose: Default Constructor method
    creates a graph
    Imports: Nothing
    Exports: Nothing
    */

    public DSAGraph()
    {
        vertices = new DSALinkedList();
        edges = new DSALinkedList();
    }




    /*
    Name: deleteEdge()
    Purpose: deletes an edge
    Imports: String label, DSALinkedList list
    Exports: Nothing
    */

    public void deleteEdge( String label, DSALinkedList list )
    {
        DSAGraphVertex vertex;
        DSAGraphEdge edge;

        for( Object o : list )
        {
            edge = ( DSAGraphEdge ) o;
            vertex = edge.to;

            if( vertex.getLabel().equals( label ) )
            {
                list.remove( label );
            }
        }
    }




    /*
    Name: deleteVertex()
    Purpose: deletes a vertex
    Imports: String label
    Exports: Object deleted
    */

    public Object deleteVertex( String label )
    {
        Object deleted;
        DSAGraphVertex vertex;

        deleted = null;
        try
        {
            if( !hasVertex( label ) )
            {
                throw new IllegalArgumentException();
            }
            else
            {

//                Edges with the deleting vertex need to also be deleted
                deleteEdge( label, edges );
                for( Object o : vertices )
                {
                    vertex = ( DSAGraphVertex ) o;

                    if( vertex.getLabel().equals( label ) )
                    {
                        deleted = vertices.remove( label );
                    }
                }
            }
        }
        catch( IllegalArgumentException e )
        {
            System.out.println( "Vertex doesn't exist." );
        }

        return deleted;
    }




    /*
    Name: addVertex()
    Purpose: adds a vertex
    Imports: String label
    Exports: Nothing
    */

    public void addVertex( String label )
    {
        try
        {
            if( hasVertex( label ) )
            {
                throw new IllegalArgumentException();
            }
            else
            {
                vertices.insertLast( new DSAGraphVertex( label ) );
            }
        }
        catch( IllegalArgumentException e )
        {
            System.out.println( "Vertex already exists." );
        }
    }




    /*
    Name: addEdge1()
    Purpose: adds an edge
    Imports: String label1, String label2, double weight
    Exports: Nothing
    */

    public void addEdge1( String label1, String label2, double weight )
    {
        DSAGraphVertex label1Vert;
        DSAGraphVertex label2Vert;

        label1Vert = getVertex( label1 );
        label2Vert = getVertex( label2 );

        try
        {
            if( isAdjacent( label1Vert.getLabel(), label2Vert.getLabel() ) )
            {
                throw new IllegalArgumentException();
            }
            else
            {
                label1Vert.addEdge( label2Vert );


                edges.insertLast( new DSAGraphEdge( label1Vert, label2Vert, weight ) );
            }
        }
        catch( IllegalArgumentException e )
        {
            System.out.println( "Edge (" + label1Vert + ", " + label2Vert + ") already exists" );
        }
    }




    /*
    Name: hasVertex()
    Purpose: Checks whether a given vertex is in the graph
    Imports: String label
    Exports: boolean found
    */

    public boolean hasVertex( String label )
    {
        boolean found = false;

        for( Object o : vertices )
        {
            // Search through every vertex to find matching label
            if( ( ( DSAGraphVertex )o ).getLabel().equals( label ) )
            {
                found = true;
            }
        }

        return found;
    }




    /*
    Name: getVertex()
    Purpose: Finds the vertex with the given label
    Imports: String label
    Exports: DSAGraphVertex vertex
    */

    public DSAGraphVertex getVertex( String label )
    {
        DSAGraphVertex vertex = null;

        try
        {
            if( !hasVertex( label ) )
            {
                throw new NoSuchElementException();
            }
            else
            {
                for( Object o : vertices )
                {
                    if( ( ( DSAGraphVertex ) o ).getLabel().equals( label ) )
                    {
                        vertex = ( DSAGraphVertex ) o;
                    }
                }
            }
        }
        catch( NoSuchElementException e )
        {
            System.out.println( "There is no " + label + " in graph" );
        }

        return vertex;
    }




    /*
    Name: getEdge()
    Purpose: Finds the edge with the given labels
    Imports: String label1, String label2
    Exports: DSAGraphEdge edge
    */

    public DSAGraphEdge getEdge( String label1, String label2 )
    {
        DSAGraphEdge edge = null;

        for( Object o : edges )
        {
            if( ( ( DSAGraphEdge ) o ).getFrom().toString().equals( label1 )
            && ( ( DSAGraphEdge ) o ).getTo().toString().equals( label2 ) )
            {
                edge = ( DSAGraphEdge ) o;
            }
        }

        return edge;
    }




    /*
    Name: isAdjacent()
    Purpose: Checks whether the given vertexes are adjacent
    Imports: String label1, String label2
    Exports: boolean is
    */

    public boolean isAdjacent( String label1, String label2 )
    {
        DSAGraphVertex inVertex;
        DSAGraphVertex inVertex1;
        boolean is;

        inVertex = getVertex( label1 );
        inVertex1 = getVertex( label2 );
        is = false;

        for( Object o : ( inVertex.getAdjacent() ) )
        {
            if( ( ( DSAGraphVertex )o ).equals( inVertex1 ) )
            {
                is = true;
            }
        }

        return is;
    }




    /*
    Name: depthFirstSearch()
    Purpose: Wrapper class for depthRec()
    Imports: Nothing
    Exports: String list
    */

    public String depthFirstSearch()
    {
        String list;

        try
        {
            clear();

            if( vertices.isEmpty() )
            {
                throw new IllegalArgumentException();
            }
        }
        catch( IllegalArgumentException e )
        {
            System.out.println( "Graph is empty" );
        }

        list = depthRec( ( DSAGraphVertex ) vertices.peekFirst());

        return list;
    }




    /*
    Name: depthRec()
    Purpose: Uses depth first search through a recursive method
    and adds it to a list
    Imports: DSAGraphVertex inVertex
    Exports: String list
    */

    private String depthRec( DSAGraphVertex inVertex )
    {
        DSAGraphVertex vertex;
        String list;

        list = "";

        inVertex.setVisited();

        for( Object o : inVertex.getAdjacent() )
        {
            vertex = ( DSAGraphVertex ) o;

            if( !vertex.getVisited() )
            {
                list += ( "(" + inVertex + ", " + vertex + ") " + depthRec( vertex ) );
            }
        }
        return list;
    }




    /*
    Name: breadthFirstSearch()
    Purpose: Wrapper class for breadthRec() and gives only the searches
    between the given vertexes
    Imports: String start, String end
    Exports: String list
    */

    public String breadthFirstSearch( String start, String end )
    {
        DSAQueue queue = new DSAQueue();
        String[] realList;
        String list;

        try
        {
            clear();

            queue.enqueue( getVertex( start ) );
            getVertex( start ).setVisited();

            if( vertices.isEmpty() )
            {
                throw new IllegalArgumentException();
            }
        }
        catch( IllegalArgumentException e )
        {
            System.out.println( "Graph is empty" );
        }

        list = breadthRec( queue, getVertex( end ) );

//        Ends the breadth first search at the desired ending vertex
        realList = list.split( "End" );
        list = realList[0];

        return list;
    }




    /*
    Name: breadthRec()
    Purpose: Uses breadth first search through a recursive method
    and adds it to a list
    Imports: DSAQueue queue, DSAGraphVertex endVertex
    Exports: String list
    */

    private String breadthRec( DSAQueue queue, DSAGraphVertex endVertex )
    {
        DSAGraphVertex inVertex;
        DSAGraphVertex vertex;
        String list = "";

        inVertex = ( DSAGraphVertex ) queue.dequeue();

//        Adds an indicator of when the search should end
        if( inVertex == endVertex )
        {
            list += "End";
        }
        else
        {
            for( Object o : inVertex.getAdjacent() )
            {
                vertex = ( DSAGraphVertex ) o;

                if( !vertex.getVisited() )
                {
                    queue.enqueue( vertex );
                    vertex.setVisited();

                    list += ( "(" + inVertex + ", " + vertex + ")  " );
                }
            }
            if( !queue.isEmpty() )
            {
                list += breadthRec( queue, endVertex );
            }
        }
        return list;
    }




    /*
    Name: shortestPathWithWeight()
    Purpose: Wrapper class for shortPathRec() that prints all possible
    paths made in the shortPathRec() method and the shortest path
    according to the weight of the edges
    Imports: String start, String end
    Exports: String shortPath
    */

    public String shortestPathWithWeight( String start, String end )
    {
        String shortPath;
        String paths;
        DSALinkedList path;
        DSAHeap priorityQueue;
        String[] splitLine;
        double distance;
        NumberFormat format = new DecimalFormat( "#0.0" );

        path = new DSALinkedList();
        priorityQueue = new DSAHeap( 20 );

        shortPathRec( getVertex( start ), getVertex( end ), getVertex( start ),
                start + "", path );

        System.out.println( "Possible paths: ");
        for( Object o : path )
        {
            distance = 0;

            paths = o.toString().replace( " ", ", ");

            splitLine = paths.split( ", " );

            System.out.println( paths );

            for( int i = 0; i < splitLine.length - 1; i++ )
            {

//              Subtraction is needed as the code uses a max heap
                distance -= getEdge( splitLine[i], splitLine[i + 1] ).getWeight();
            }

            priorityQueue.add( distance, paths + ", " +
                    -Double.parseDouble( format.format( distance ) ) );
        }

        shortPath = priorityQueue.remove().toString();

        return shortPath;
    }




    /*
    Name: shortPathRec()
    Purpose: Uses a method similar to a depth first search through a recursive
    method to find the paths between two given vertexes
    Imports: DSAGraphVertex start, DSAGraphVertex end, DSAGraphVertex current,
    String answer, DSALinkedList path
    Exports: String list
    Comments: This method has adapted code from "Dezigo" in Stack Overflow (refer
    to reference 1)
    */

    public void shortPathRec( DSAGraphVertex start, DSAGraphVertex end,
                            DSAGraphVertex current, String answer, DSALinkedList path )
    {
        DSAGraphVertex nextVertex;

        clear();

        start.setVisited();
        current.setVisited();

//        If the ending vertex is found the path will be added to the list
        if( current == end )
        {
            path.insertLast( answer );
        }

//        For smaller test data
        else if( current == start )
        {
            for ( Object o : current.getAdjacent() )
            {
                nextVertex = (DSAGraphVertex) o;
                if( nextVertex == end )
                {
                    path.insertLast( answer + " " + nextVertex );
                }
                else if( !nextVertex.getVisited() )
                {
                    nextVertex.setVisited();
                    shortPathRec( start, end, nextVertex, answer + " " + nextVertex, path );
                }

            }
        }
        else
        {
            for ( Object o : current.getAdjacent() )
            {
                nextVertex = (DSAGraphVertex) o;
                if ( !nextVertex.getVisited() )
                {
                    nextVertex.setVisited();
                    shortPathRec( start, end, nextVertex, answer + " " + nextVertex, path );
                }
            }
        }
    }




    /*
    Name: hasPath()
    Purpose: Checks whether if there is a path between two given vertexes
    Imports: String start, String end
    Exports: boolean has
    */

    public boolean hasPath( String start, String end )
    {
        DSALinkedList path;
        boolean has;

        path = new DSALinkedList();
        has = false;

        shortPathRec( getVertex( start ), getVertex( end ), getVertex( start ), start + "", path );

        if( !path.isEmpty() )
        {
            has = true;
        }
        return has;
    }




    /*
    Name: hasMultiPath()
    Purpose: Checks whether if there is a path between multiple given vertexes
    Imports: DSALinkedList locations
    Exports: boolean has
    */

    public boolean hasMultiPath( DSALinkedList locations )
    {
        DSALinkedList path;
        DSALinkedList locationsChange;
        DSAGraphVertex[] locations1;
        String start;
        boolean has;
        int size;
        int ii;
        ii = 1;

        path = new DSALinkedList();
        locationsChange = new DSALinkedList();
        start = locations.peekFirst().toString();
        size = locations.getCount();
        has = false;

        for( Object o : locations )
        {
            locationsChange.insertLast( o );
        }

        locations1 = new DSAGraphVertex[size];

        for( int i = 0; i < size; i++ )
        {
            locations1[i] = new DSAGraphVertex( locationsChange.removeFirst().toString() );
        }

        multiPathRec( locations1, getVertex( start ), start + "", path, ii );

        if( !path.isEmpty() )
        {
            has = true;
        }

        return has;
    }




    /*
    Name: shortestPathMulti()
    Purpose: Wrapper class for multiPathRec() that prints all possible
    paths made in the multiPathRec() method and the shortest path
    according to the weight of the edges
    Imports: DSALinkedList locations
    Exports: String shortPath
    */

    public String shortestPathMulti( DSALinkedList locations )
    {
        NumberFormat format = new DecimalFormat( "#0.0" );

        String shortPath;
        String paths;
        DSALinkedList path;
        DSALinkedList locationsChange;
        DSAHeap priorityQueue;
        String[] splitLine;
        DSAGraphVertex[] locations1;
        double distance;
        String start;
        int size;
        int ii;

        ii = 1;
        path = new DSALinkedList();
        locationsChange = new DSALinkedList();
        priorityQueue = new DSAHeap( 20 );
        start = locations.peekFirst().toString();
        size = locations.getCount();

//        Replacing a copy constructor for DSALinkedList
        for( Object o : locations )
        {
            locationsChange.insertLast( o );
        }

        locations1 = new DSAGraphVertex[size];

        for( int i = 0; i < size; i++ )
        {
            locations1[i] = new DSAGraphVertex( locationsChange.removeFirst().toString() );
        }

        multiPathRec( locations1, getVertex( start ), start + "", path, ii );

        System.out.println( "Possible paths: ");
        for( Object o : path )
        {
            distance = 0;

            paths = o.toString().replace( " ", ", ");

            splitLine = paths.split( ", " );

            System.out.println( paths );

            for( int i = 0; i < splitLine.length - 1; i++ )
            {
                distance -= getEdge( splitLine[i], splitLine[i + 1] ).getWeight();
            }
            priorityQueue.add( distance, paths + ", " +
                    ( -Double.parseDouble( format.format( distance ) ) ) );
        }

        shortPath = priorityQueue.remove().toString();

        return shortPath;
    }




    /*
    Name: multiPathRec()
    Purpose: Uses a method similar to a depth first search through a recursive
    method to find the paths between multiple given vertexes
    Imports: DSALinkedList locations, DSAGraphVertex current,
    String answer, DSALinkedList path, int ii
    Exports: Nothing
    Comments: This method has adapted code from "Dezigo" in Stack Overflow (refer
    to reference 1)
    */

    public void multiPathRec( DSAGraphVertex[] locations1, DSAGraphVertex current, String answer,
                                 DSALinkedList path, int ii )
    {
        DSAGraphVertex nextVertex;
        int testNum;

        clear();

        testNum = 0;

        locations1[0].setVisited();
        current.setVisited();

        for( int i = 0; i < locations1.length; i++ )
        {
            if( answer.contains( locations1[i].toString() ) )
            {
                testNum++;
            }
        }

//        If current vertex is equal to the ending vertex
        if( current.equals( locations1[locations1.length - 1] ) &&
                testNum == locations1.length )
        {
            path.insertLast( answer );
        }

//        If the current vertex is equal to the next location in the list
        else if( current.equals( locations1[ii] ) && testNum == locations1.length )
        {
            path.insertLast( answer );
            ii++;
            for ( Object o : current.getAdjacent() )
            {
                nextVertex = (DSAGraphVertex) o;
                if ( !nextVertex.getVisited() )
                {
                    nextVertex.setVisited();
                    multiPathRec( locations1, nextVertex, answer + " " + nextVertex, path, ii );
                }
            }
        }
        else
        {
            for ( Object o : current.getAdjacent() )
            {
                nextVertex = (DSAGraphVertex) o;
                if ( !nextVertex.getVisited() )
                {
                    nextVertex.setVisited();
                    multiPathRec( locations1, nextVertex, answer + " " + nextVertex, path, ii );
                }
            }
        }
    }




    /*
    Name: clear()
    Purpose: Makes all vertices not visited
    Imports: Nothing
    Exports: Nothing
    */

    private void clear()
    {
        for( Object o : vertices )
        {
            ( ( DSAGraphVertex ) o ).clearVisited();
        }
    }




    /*
    Name: displayAsList()
    Purpose: Displays the graph as an adjacency list
    Imports: Nothing
    Exports: Nothing
    */

    public void displayAsList()
    {
        DSAGraphVertex vertex;
        DSAGraphVertex vertex1;
        DSAGraphEdge edge;

        for( Object o : vertices )
        {
            vertex = ( DSAGraphVertex ) o;
            System.out.print( vertex.getLabel() + " |" );
            for( Object o1 : edges )
            {
                edge = ( DSAGraphEdge ) o1;
                if( vertex.getLabel().equals( edge.from.getLabel() ) )
                {
                    vertex1 = edge.to;
                    System.out.print( " " + vertex1.getLabel() );
                }
            }
            System.out.println();
        }


    }




    /*
    Private Class: DSAGraphEdge
    Purpose: Creates an edge
    Last Modified: 22/05/2023
    */
    private class DSAGraphEdge
    {
        private String label;
        private DSAGraphVertex from;
        private DSAGraphVertex to;
        private double weight;

        /*
        Name: DSAGraphEdge()
        Purpose: Default Constructor method
        creates an edge
        Imports: Nothing
        Exports: Nothing
        */

        public DSAGraphEdge()
        {
            from = null;
            to = null;
            weight = 0.0;
            label = null;
        }




        /*
        Name: DSAGraphEdge()
        Purpose: Parameter Constructor method
        creates an edge
        Imports: DSAGraphVertex inFrom, DSAGraphVertex inTo,
        double inWeight
        Exports: Nothing
        */

        public DSAGraphEdge( DSAGraphVertex inFrom, DSAGraphVertex inTo,
                             double inWeight )
        {
            from = inFrom;
            to = inTo;
            weight = inWeight;
            label = inTo.getLabel();
        }




        /*
        Name: getWeight()
        Purpose: Accessor method that returns the weight of
        the edge
        Imports: Nothing
        Exports: double weight
        */

        public double getWeight()
        {
            return weight;
        }




        /*
        Name: setWeight()
        Purpose: Mutator method that sets the weight of
        the edge
        Imports: double inWeight
        Exports: Nothing
        */

        public void setWeight( double inWeight )
        {
            weight = inWeight;
        }




        /*
        Name: getFrom()
        Purpose: Accessor method that returns the starting vertex
        of the edge
        Imports: Nothing
        Exports: DSAGraphVertex from
        */

        public DSAGraphVertex getFrom()
        {
            return from;
        }




        /*
        Name: getTo()
        Purpose: Accessor method that returns the ending vertex
        of the edge
        Imports: Nothing
        Exports: DSAGraphVertex to
        */

        public DSAGraphVertex getTo()
        {
            return to;
        }




        /*
        Name: toString()
        Purpose: An object to string method
        Imports: Nothing
        Exports: String label
        */

        public String toString()
        {
            return label;
        }
    }




    /*
    Private Class: DSAGraphEdge
    Purpose: Creates an edge
    Last Modified: 22/05/2023
    */
    private class DSAGraphVertex
    {
        private String labels;
        private DSALinkedList links;
        private boolean visited;

        /*
        Name: DSAGraphVertex()
        Purpose: Parameter Constructor method
        creates a vertex
        Imports: String inLabel
        Exports: Nothing
        */

        public DSAGraphVertex( String inLabel )
        {
            labels = inLabel;
            links = new DSALinkedList();
            visited = false;
        }




        /*
        Name: getLabel()
        Purpose: Accessor method that returns the label
        of the vertex
        Imports: Nothing
        Exports: String labels
        */

        public String getLabel()
        {
            return labels;
        }




        /*
        Name: getAdjacent()
        Purpose: Accessor method that returns the list of
        adjacent vertexes of the current vertex
        Imports: Nothing
        Exports: DSALinkedList links
        */

        public DSALinkedList getAdjacent()
        {
            return links;
        }




        /*
        Name: addEdge()
        Purpose: Adds a vertex to its list of adjacent vertexes
        Imports: DSAGraphVertex inVertex
        Exports: Nothing
        */

        public void addEdge( DSAGraphVertex inVertex )
        {
            links.insertLast( inVertex );
        }




        /*
        Name: setVisited()
        Purpose: Mutator method that sets the vertex as being
        visited
        Imports: Nothing
        Exports: Nothing
        */

        public void setVisited()
        {
            visited = true;
        }




        /*
        Name: clearVisited()
        Purpose: Mutator method that sets the vertex as being
        not visited
        Imports: Nothing
        Exports: Nothing
        */

        public void clearVisited()
        {
            visited = false;
        }




        /*
        Name: getVisited()
        Purpose: Accessor method that returns whether
        the vertex has been visited or not
        Imports: Nothing
        Exports: boolean visited
        */

        public boolean getVisited()
        {
            return visited;
        }




        /*
        Name: toString()
        Purpose: An object to string method
        Imports: Nothing
        Exports: String labels
        */

        public String toString()
        {
            return labels;
        }




        /*
        Name: equals()
        Purpose: Determines whether the given vertex equals
        this vertex
        Imports: DSAGraphVertex inVertex
        Exports: boolean export from equals( String ) method
        */

        public boolean equals( DSAGraphVertex inVertex )
        {
            return ( labels.equals( inVertex.getLabel() ) );
        }
    }
}
