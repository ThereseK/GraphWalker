package org.tigris.mbt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.SimpleLayout;
import org.apache.log4j.WriterAppender;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.tigris.mbt.conditions.AlternativeCondition;
import org.tigris.mbt.conditions.CombinationalCondition;
import org.tigris.mbt.conditions.EdgeCoverage;
import org.tigris.mbt.conditions.NeverCondition;
import org.tigris.mbt.conditions.ReachedEdge;
import org.tigris.mbt.conditions.ReachedRequirement;
import org.tigris.mbt.conditions.ReachedState;
import org.tigris.mbt.conditions.RequirementCoverage;
import org.tigris.mbt.conditions.StateCoverage;
import org.tigris.mbt.conditions.StopCondition;
import org.tigris.mbt.conditions.TestCaseLength;
import org.tigris.mbt.conditions.TimeDuration;
import org.tigris.mbt.generators.CodeGenerator;
import org.tigris.mbt.generators.CombinedPathGenerator;
import org.tigris.mbt.generators.ListGenerator;
import org.tigris.mbt.generators.PathGenerator;
import org.tigris.mbt.generators.RandomPathGenerator;
import org.tigris.mbt.generators.RequirementsGenerator;
import org.tigris.mbt.generators.ShortestPathGenerator;

import edu.uci.ics.jung.graph.impl.DirectedSparseEdge;
import edu.uci.ics.jung.graph.impl.DirectedSparseVertex;
import edu.uci.ics.jung.graph.impl.SparseGraph;
import edu.uci.ics.jung.utils.UserData;

/**
 * This class has some utility functionality used by org.tigris.mbt
 * The functionality is:<br>
 * * Getting names with extra info for vertices and edges<br>
 * * Setting up the logger for classes<br>
 */
public class Util {
    /**
     * Retries information regarding an edge, and returns it as a String.
     * This method is for logging purposes.
     * 
     * @param edge The edge about which information shall be retrieved.
     * @return Returns a String with information regarding the edge, including the source and
     *  destination vertices. The format is:<br>
     *  <pre>'&lt;EDGE LABEL&gt;', INDEX=x ('&lt;SOURCE VERTEX LABEL&gt;', INDEX=y -&gt; '&lt;DEST VERTEX LABEL&gt;', INDEX=z)</pre>
     *  Where x, y and n are the unique indexes for the edge, the source vertex and the destination vertex.<br>
     *  Please note that the label of an edge can be either null, or empty ("");
     */
    public static String getCompleteEdgeName( DirectedSparseEdge edge )
    {
            String str = "'" + (String)edge.getUserDatum( Keywords.LABEL_KEY ) + 
                         "', INDEX=" + edge.getUserDatum( Keywords.INDEX_KEY ) + 
                         " ('" + (String)edge.getSource().getUserDatum( Keywords.LABEL_KEY ) + 
                         "', INDEX=" + edge.getSource().getUserDatum( Keywords.INDEX_KEY ) + 
                         " -> '" + (String)edge.getDest().getUserDatum( Keywords.LABEL_KEY ) + 
                         "', INDEX=" + edge.getDest().getUserDatum( Keywords.INDEX_KEY ) +  ")";
            return str;
    }

    /**
     * Retries information regarding a vertex, and returns it as a String.
     * This method is for logging purposes.
     *
     * @param vertex The vertex about which information shall be retrieved.
     * @return Returns a String with information regarding the vertex. The format is:<br>
     *  <pre>'&lt;VERTEX LABEL&gt;', INDEX=n</pre>
     *  Where is the unique index for the vertex.
     */
    public static String getCompleteVertexName( DirectedSparseVertex vertex )
    {
            String str = "'" + (String)vertex.getUserDatum( Keywords.LABEL_KEY ) + 
                         "', INDEX=" + vertex.getUserDatum( Keywords.INDEX_KEY );
            return str;
    }

	public static void AbortIf(boolean bool, String message)
	{
		if(bool)
		{
			throw new RuntimeException( message );
		}
	}

	public static Logger setupLogger( Class classParam ) 
	{
		Logger logger = Logger.getLogger( classParam  );
		if ( new File( "mbt.properties" ).exists() )
		{
			PropertyConfigurator.configure("mbt.properties");
		}
		else
		{
	 		try
	 		{
	 			WriterAppender writerAppender = new WriterAppender( 
	 					new SimpleLayout(), 
	 					new FileOutputStream( "mbt.log" ) );
		 		logger.addAppender( writerAppender );
		 		logger.setLevel( (Level)Level.ERROR );
	 		} 
	 		catch ( Exception e )
	 		{
				throw new RuntimeException(e.getMessage());
	 		}
	 
		}
		return logger;
	}

	public static DirectedSparseVertex addVertexToGraph(
			SparseGraph graph, 
			String strLabel)
	{
		DirectedSparseVertex retur = new DirectedSparseVertex();
		retur.setUserDatum(Keywords.INDEX_KEY, new Integer(graph.numEdges()+graph.numVertices()+1), UserData.SHARED);
		if(strLabel != null) retur.setUserDatum(Keywords.LABEL_KEY, strLabel, UserData.SHARED);
		return (DirectedSparseVertex) graph.addVertex(retur);
	}
	
	public static DirectedSparseEdge addEdgeToGraph(
			SparseGraph graph, 
			DirectedSparseVertex vertexFrom, 
			DirectedSparseVertex vertexTo, 
			String strLabel,
			String strParameter,
			String strGuard,
			String strAction)
	{
		DirectedSparseEdge retur = new DirectedSparseEdge(vertexFrom, vertexTo);
		retur.setUserDatum(Keywords.INDEX_KEY, new Integer(graph.numEdges()+graph.numVertices()+1), UserData.SHARED);
		if(strLabel != null) retur.setUserDatum(Keywords.LABEL_KEY, strLabel, UserData.SHARED);
		if(strParameter != null) retur.setUserDatum(Keywords.PARAMETER_KEY, strParameter, UserData.SHARED);
		if(strGuard != null) retur.setUserDatum(Keywords.GUARD_KEY, strGuard, UserData.SHARED);
		if(strAction != null) retur.setUserDatum(Keywords.ACTIONS_KEY, strAction, UserData.SHARED);
		return (DirectedSparseEdge) graph.addEdge(retur);
	}
	
	/**
	 * @deprecated use execute from {@link ModelBasedTesting} instead.
	 */
	public static boolean RunTest( Object ref, Logger log, String method ) 
	{
		Class cls = null;
		cls = ref.getClass();
		try
		{
			Method meth = cls.getMethod( method, null );
			meth.invoke( ref, null  );
		}
		catch( Exception e )
		{
			StringWriter sw = new StringWriter();
		    PrintWriter pw = new PrintWriter( sw );
		    e.printStackTrace( pw );
		    pw.close();	    		    
			log.error( sw.toString() );
			if ( e.getCause().getMessage() != null )
			{
				System.err.println( e.getCause().getMessage() );
			}
			if ( e.getMessage() != null )
			{
				System.err.println( e.getMessage() );
			}
			return false;
		}
		return true;
	}
	
	public static StopCondition getCondition(int conditionType, String conditionValue)
	{
		StopCondition condition = null;
		switch (conditionType) 
		{
			case Keywords.CONDITION_EDGE_COVERAGE:
				condition = new EdgeCoverage(Double.parseDouble(conditionValue)/100);
				break;
			case Keywords.CONDITION_REACHED_EDGE:
				condition = new ReachedEdge(conditionValue);
				break;
			case Keywords.CONDITION_REACHED_STATE:
				condition = new ReachedState(conditionValue);
				break;
			case Keywords.CONDITION_STATE_COVERAGE:
				condition = new StateCoverage(Double.parseDouble(conditionValue)/100);
				break;
			case Keywords.CONDITION_TEST_DURATION:
				condition = new TimeDuration(Long.parseLong(conditionValue));
				break;
			case Keywords.CONDITION_TEST_LENGTH:
				condition = new TestCaseLength(Integer.parseInt(conditionValue));
				break;
			case Keywords.CONDITION_NEVER:
				condition = new NeverCondition();
				break;
			case Keywords.CONDITION_REQUIREMENT_COVERAGE:
				condition = new RequirementCoverage(Double.parseDouble(conditionValue)/100);
				break;
			case Keywords.CONDITION_REACHED_REQUIREMENT:
				condition = new ReachedRequirement(conditionValue);
				break;
			default:
				throw new RuntimeException("Unsupported stop condition selected: "+ conditionType);
		}
		return condition ;
	}
	
	public static PathGenerator getGenerator(int generatorType)
	{
		PathGenerator generator = null;
		
		switch (generatorType) 
		{
			case Keywords.GENERATOR_RANDOM:
				generator = new RandomPathGenerator();
				break;

			case Keywords.GENERATOR_SHORTEST:
				generator = new ShortestPathGenerator();
				break;
			
			case Keywords.GENERATOR_STUB:
				generator = new CodeGenerator();
				break;
				
			case Keywords.GENERATOR_LIST:
				generator = new ListGenerator();
				break;
				
			case Keywords.GENERATOR_REQUIREMENTS:
				generator = new RequirementsGenerator();
				break;
			
			default:
				throw new RuntimeException("Generator not implemented yet!");
		}

		return generator;
	}
	
	/**
	 * @param fileName The XML settings file
	 */
	public static ModelBasedTesting loadMbtFromXml( String fileName )
	{
		ModelBasedTesting mbt = new ModelBasedTesting();
		
		SAXBuilder parser = new SAXBuilder( "org.apache.crimson.parser.XMLReaderImpl", false );		
				
		Document doc;
		try {
			doc = parser.build( fileName );
		} catch (JDOMException e) {
			throw new RuntimeException("Error parsing XML file.",e);
		} catch (IOException e) {
			throw new RuntimeException("Error reading XML file.",e);
		}
		Element root = doc.getRootElement();
		List models = root.getChildren("MODEL");
		
		if(models.size()==0)
			throw new RuntimeException("Model is missing from XML");
			
		for(Iterator i=models.iterator();i.hasNext();)
		{
			mbt.readGraph(((Element) i.next()).getAttributeValue("PATH"));
		}
		
		if(root.getAttributeValue("EXTENDED").equalsIgnoreCase("true"))
		{
			mbt.enableExtended(true);
			mbt.setStartupScript(getScriptContent(root.getChildren("SCRIPT")));
		}
		 
		List generators = root.getChildren("GENERATOR");

		if(generators.size()==0)
			throw new RuntimeException("Generator is missing from XML");

		PathGenerator generator;
		if(generators.size() > 1)
		{
			generator = new CombinedPathGenerator();
			for(Iterator i=generators.iterator();i.hasNext();)
			{
				((CombinedPathGenerator)generator).addPathGenerator(getGenerator((Element) i.next()));
			}
		} else {
			generator = getGenerator((Element) generators.get(0));
		}
		if(generator == null)
			throw new RuntimeException("Failed to set generator");
		mbt.setGenerator(generator);
		return mbt;
	}

	private static String getScriptContent(List scripts) {
		String retur = "";
		for(Iterator i=scripts.iterator();i.hasNext();)
		{
			Element script = (Element) i.next();
			String internal = script.getTextTrim();
			if(internal != null && !internal.equals(""))
				retur += internal + "\n";
			String external = script.getAttributeValue("PATH");
			if(external != null && !external.equals(""))
				retur += readFile(external) + "\n";
		}
		return retur;
	}

	private static PathGenerator getGenerator(Element generator) {
		int generatorType = Keywords.getGenerator(generator.getAttributeValue("TYPE"));
		PathGenerator generatorObject = getGenerator(generatorType);
		if(generatorObject instanceof CodeGenerator)
		{
			String template = "";
			String templateFile = generator.getAttributeValue("VALUE");
			if(templateFile != null)
			{
				template = readFile(templateFile.trim());
			} else {
				Element templateElement = generator.getChild("TEMPLATE");
				if(templateElement != null)
				{
					template = templateElement.getTextTrim();
				} else {
					throw new RuntimeException("No Template is specified for the stub generator.");
				}
			} 
			((CodeGenerator)generatorObject).setTemplate(template);
		} else {
			StopCondition stopCondition = getCondition(generator.getChildren());
			if(stopCondition != null )
			{
				generatorObject.setStopCondition( stopCondition );
			}
		}
		return generatorObject;
	}

	private static StopCondition getCondition(List conditions)
	{
		StopCondition condition = null;
		if(conditions.size()>1)
		{
			condition = new CombinationalCondition();
			for(Iterator i = conditions.iterator(); i.hasNext();)
				((CombinationalCondition)condition).add(getCondition((Element) i.next()));
		} else if(conditions.size() == 1){
			condition = getCondition((Element) conditions.get(0));
		}
		return condition;
	}

	private static StopCondition getCondition(Element condition)
	{
		StopCondition stopCondition = null;
		if(condition.getName().equalsIgnoreCase("AND")) {
			stopCondition = new CombinationalCondition();
			for(Iterator i = condition.getChildren().iterator(); i.hasNext();)
				((CombinationalCondition)stopCondition).add(getCondition((Element) i.next()));
		} else if(condition.getName().equalsIgnoreCase("OR")) {
			stopCondition = new AlternativeCondition();
			for(Iterator i = condition.getChildren().iterator(); i.hasNext();)
				((AlternativeCondition)stopCondition).add(getCondition((Element) i.next()));
		} else if(condition.getName().equalsIgnoreCase("CONDITION")){
			int type = Keywords.getStopCondition(condition.getAttributeValue("TYPE"));
			String value = condition.getAttributeValue("VALUE");
			stopCondition = getCondition(type, value);
		}
		return stopCondition;
	}
	
	public static String readFile(String fileName)
	{
		String retur = "";
		try {
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			while(in.ready())
				retur+=in.readLine() + "\n";
		} catch (IOException e) {
			throw new RuntimeException("Problem reading file '"+ fileName + "'",e);
		}
		return retur;
	}

}
