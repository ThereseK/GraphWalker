package org.tigris.mbt;

import java.util.Iterator;
import java.util.Vector;

/**
 * Handles the common constants for the org.tigris.mbt package. This includes
 * reserved key words, and text strings used for storing custom data as
 * UserDatum in graphs, vertices and edges.
 *
 */
public class Keywords {

	/**
	* Each graph has a start (entry) vertex, and that vertex holds this datum.<br>
	* There is only one vertex in a graph, holding this datum.<br>
	* Used by MBT when merging graphs, and when generating test sequences.
	*/
	public static final String  START_NODE = "Start";
	
	/**
	* The stop (exit) vertex denotes an exit point in a graph.<br>
	* There can only be one vertex in a graph, holding this datum.<br>
	* Only sub-graphs can have a stop vertex.<br>
	* Used by MBT only when merging graphs. 
	*/
	public static final String  STOP_NODE = "Stop";
	
	/**
	* The id taken from the graphml file.
	* TODO Investigate if this datum is deprecated
	*/
	public static final String  ID_KEY = "id";
	
	/**
	* The graph editor yEd, can use images to depict vertices, which normally gets
	* lost during merging. So, when writing merged graphs back to file,
	* this datum holds the path and file of that image.
	*/
	public static final String  IMAGE_KEY = "image";
	
	/**
	* The graph editor yEd, can use images to depict vertices, which normally gets
	* lost during merging. So, when writing merged graphs back to file,
	* this datum holds the width of that image.
	*/
	public static final String  WIDTH_KEY = "width";
	
	/**
	* The graph editor yEd, can use images to depict vertices, which normally gets
	* lost during merging. So, when writing merged graphs back to file,
	* this datum holds the height of that image.
	*/
	public static final String  HEIGHT_KEY = "height";
	
	/**
	* When merging graphs, the source file of each graph is noted, so that in the event
	* of an error, the correct graph file can be used in meaningful error messages to
	* the end user.
	*/
	public static final String  FILE_KEY = "file";
	
	/**
	* The name of the vertex or edge, that will result in a method or function call in
	* the executing test tool.<br>
	* * The label of an edge can be empty (or null).<br>
	* * A vertex must always have a label.<br>
	* * The label is always defined at the first line in a label.<br>
	*/
	public static final String  LABEL_KEY = "label";
	
	/**
	* This datum contain the complete text hold by a label.
	*/
	public static final String  FULL_LABEL_KEY = "full_label";
	
	/**
	* This datum contains a counter for each vertex and edge.
	* Used by MBT during a online test. Every time a vertex or an edge is traversed
	* it is visited, thus incremented once.
	*/
	public static final String  VISITED_KEY = "visited";
	
	/**
	* Used by MBT during random walks during test sequence generation. It holds a
	* real value between 0 and 1, and represents the probability that a specific
	* edge should be chosen. A value of 0.05, would mean a 5% chance of that edge
	* to be selected during a run.<br>
	* * Only edges uses this datum.
	*/
	public static final String  WEIGHT_KEY = "weight";
	
	/**
	* The datum provides the edge or vertex a unique integer number, uniquely
	* identifying the object.<br>
	* Generated by MBT when reading and merging graphs. Also used to provide
	* better info during logging at:<br>
	* * Parsing graphml files<br>
    * * Merging<br>
	* * Generating offline and online test sequences
	*/
	public static final String  INDEX_KEY = "index";
	
	/**
	* This datum is used by MBT when merging graphs. It tells MBT that the vertex
	* containing this key word, should be merged with the first occurence of a vertex
	* with the same label.
	*/
	public static final String  MERGE = "merge";
	
	/**
	* This datum is used by MBT when merging graphs. It tells MBT that the vertex
	* containing this key word, should not merge this vertex with any subgraph.
	*/
	public static final String  NO_MERGE = "no merge";
	
	/**
	* Used internally by MBT. When graphs are merged, MBT keeps track of which vertices
	* have been merged by MBT.
	*/
	public static final String  MERGED_BY_MBT = "merged by mbt";
	
	/**
	* Used internally  by MBT. A vertex in a graph with the label START, and a
	* single empty out edge is defined as the main graph (or Mother Graph)
	*/
	public static final String  MOTHER_GRAPH_START_VERTEX = "mother graph start vertex";
	
	/**
	* Used internally  by MBT. A vertex in a graph with the label START, and a
	* single non-empty out edge is defined as a subgraph.
	*/
	public static final String  SUBGRAPH_START_VERTEX = "subgraph start vertex";
	
	/**
	* A vertex or an edge with the key word BLOCKED, will not participate in the
	* resulting (merged) graph, end thus be excluded.
	*/
	public static final String  BLOCKED	= "BLOCKED";
	
	/**
	* An edge with the key word BACKTRACK is used to enable a simple logic that enables
	* a end user to backtrack in the graph to the previous vertex.<br>
	* This is used only during a online run.
	*/
	public static final String  BACKTRACK = "BACKTRACK";

	/**
	* This datum contains the label parameter used by an edge in EFSM models.
	*/
	public static final String PARAMETER_KEY = "parameter";

	/**
	* This datum contains the label guard used by an edge in EFSM models.
	*/
	public static final String GUARD_KEY = "guard";

	/**
	* This datum contains the actions used by an edge in EFSM models.
	* The datum is the java.Util.Vector object, and each action is stored as seperate string
	* in the vector.
	*/
	public static final String ACTIONS_KEY = "action";

	/**
	* This datum contains the dijkstra object used to find the shortest path in models.
	*/
	public static final String DIJKSTRA = "dijkstra";


	/**
	* Holds the pre-defined key words
	*/
	private static Vector reservedKeyWords = new Vector();
	
	/**
	* Defines the key words
	*/
	static
	{
		reservedKeyWords.add( "BLOCKED" );
		reservedKeyWords.add( "BACKTRACK" );
		reservedKeyWords.add( "MERGE" );
		reservedKeyWords.add( "NO_MERGE" );		
	}
	
	/**
	* Returns true if the wordToCheck is a pre-defined key word.
	*/
	public static boolean isKeyWord( String wordToCheck )
	{
        for ( Iterator iter = reservedKeyWords.iterator(); iter.hasNext(); )
        {
            String keyWord = (String) iter.next();
            if ( keyWord.equals( wordToCheck ) )
            {
            	return true;
            }
        }
        return false;
	}
}
