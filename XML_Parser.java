import java.io.File;
import java.util.ArrayList;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XML_Parser {
	
	//Error token in strings
	private static final String fail = "error";
	
	//Temporary Data stores for error checking, only accessible by this class
	private static int asID, cID, rID, acID;
	private static String xmls, date, fName, sName, comp, aPort, trans, qText, eResp;
	private static NodeList temp;
	private static ArrayList<Node> assessments, details, candidate, recordings, activities, activity;
	private static ArrayList<Recording> transcripts;
	
	//Only visible to parser, inner class to hold recording details
	private static class Recording {
		int ID1; int ID2; String transcript;
		Recording(int id1, int id2, String response) {
			ID1 = id1; ID2 = id2; transcript = response;
		}
	}
	
	private static void initialiseAssessmentVars() {
		//Should never stay as err so use for error checking
		xmls = fail; date = fail; fName = fail; sName = fail;
		comp = fail; aPort = fail; trans = fail; 
	}
	
	private static void initialiseActivityVars() {
		//Should never stay as err so use for error checking
		qText = fail; eResp = fail;
		//Will re-use existing acID
	}
	
	//The actual assessment.xml parser
	//Remember to check fields against fail later
	protected static boolean parseXML(File xml_file, File schema_file) {
		
		//Check the file passed to this method exists and can be read
		if( !xml_file.exists() || !xml_file.canRead() ) {
			System.err.println("File does not exist or cannot be read!");
			return false;
		}
		
		//Check Schema supplied
		if ( !schema_file.exists() || !schema_file.canRead() ) {
			System.err.println("No valid Schema supplied.");
			return false;
		}
		
		//Validate against Schema
		try {
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = factory.newSchema(schema_file);
			Validator validator = schema.newValidator();
			validator.validate(new StreamSource(xml_file));
		} catch ( Exception e ) {
            System.out.println("Schema Validation Failed: " + e.getMessage());
            return false;
		}
		
		//Mandatory Setup
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		Document dom = null;
		
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			dom = db.parse(xml_file);
		} catch (Exception e) {
			System.err.println("Unable to parse XML file.");
		}

		//Get root of document and test for correct input
		Element root;
		try {
			root = dom.getDocumentElement();
		} catch ( NullPointerException npe ) {
			System.err.println("Incorrect tag matching, please revise XML file.");
			return false;
		}
		
		if ( !root.getNodeName().equals("assessment") ) {
			if ( root.getNodeName().equals("activities") ) {
				//Parse the activities XML instead
				return parseActivities(root);
			} else {
				System.err.println("Incorrect file, please use file with root" +
			                       "element 'assessment' or 'activities'.");
				return false;
			}
		} 

		//If correct document, initialise variables
		initialiseAssessmentVars();
		
		//Get assessmentTaken blocks and check for no assessments
		temp = root.getChildNodes();			
		assessments = removeTextNodes(temp);
		if ( assessments.size() == 0 ) {
			System.err.println("No assessments found!");
			return false;
		}
		
		//Actual data retrieval
		for ( Node n : assessments ) {
			
			//Instantiate transcripts array for each assessment
			transcripts = new ArrayList<Recording>();
			
			try {
				//Get assessment details
				temp = n.getChildNodes();
				details = removeTextNodes(temp);
				//Get candidate details
				temp = details.get(1).getChildNodes();
				candidate = removeTextNodes(temp);
				//Get recordings
				temp = details.get(2).getChildNodes();
				recordings = removeTextNodes(temp);
				
				//Error checking for unexpected nodes
				checkNode(n, "assessmentTaken");				
				checkNode(details.get(0), "dateTaken");
				checkNode(details.get(1), "candidate");
				checkNode(details.get(2), "recordings");
				checkNode(candidate.get(0), "forename");
				checkNode(candidate.get(1), "surname");
				checkNode(candidate.get(2), "company");
				checkNode(candidate.get(3), "airport");
				
				for ( Node recNode : recordings ) {
					checkNode(recNode, "recording");
				}
				
			} catch ( Exception e ) {
				System.err.println("This assessment does not conform to the \n" +
			                       "assessment.xml structure. Please revise.");
				return false;
			}
							
			
			//Get tricky data
			String errNode = "";
			try {
				//Integer ID fields 
				errNode = "assessmentTaken id";	asID = Integer.parseInt(extractAttribute(n, "id"));
				errNode = "cadidate id";		cID = Integer.parseInt(extractAttribute(details.get(1), "id"));
				//Recordings
				for ( Node recNode : recordings ) {
					errNode = "recording id";	rID = Integer.parseInt(extractAttribute(recNode, "id"));
					errNode = "activity id";	acID = Integer.parseInt(extractAttribute(recNode, "activityID"));
					errNode = "transcript";		 
					temp = recNode.getChildNodes();
					Node transNode = extractTranscript(temp);
					trans = transNode.getTextContent();
					addRecording(rID, acID, trans, asID);
				}
			} catch ( NumberFormatException nfe ) {
				System.err.println("Unexpected format of " + errNode +
						                     "\nPlease revise the xml document");
				return false;
			} catch ( NullPointerException npe ) {
				System.err.println("Null pointer for: " + errNode);
				return false;
			}
							
			//get the rest!
			xmls  = extractAttribute(n, "xmls");				
			date  = details.get(0).getTextContent();
			fName = candidate.get(0).getTextContent();
			sName = candidate.get(1).getTextContent();
			comp  = candidate.get(2).getTextContent();
			aPort = candidate.get(3).getTextContent();
			
			//Check for errors in strings
			if ( xmls.equals(fail)  ||
				 date.equals(fail)  || date.matches("\\s*")  ||
				 trans.equals(fail) || trans.matches("\\s*") ||
				 fName.equals(fail) || fName.matches("\\s*") ||
				 sName.equals(fail) || sName.matches("\\s*") ||
				 comp.equals(fail)  || comp.matches("\\s*")  ||
				 aPort.equals(fail) || aPort.matches("\\s*")) {
				System.err.println("Data Missing..."); 
				return false;
			}
			
			//If all is well, Create objects
			Applicant applicant = new Applicant(cID, fName, sName);
			Application application = new Application(applicant, date, comp, aPort, asID, xmls);
			for ( Recording r : transcripts ) {
				application.addActivity(new Activity(r.ID1, r.ID2, r.transcript));
			}
			//Store in mains static hashmap so applications can be recalled by candidate ID
			Main.applications.put(cID, application);
			
		}	
		//Success
		return true;
	}
	
	private static boolean parseActivities(Element root) {
		
		temp = root.getChildNodes();
		activities = removeTextNodes(temp);
		if ( activities.size() == 0 ) {
			System.err.println("No activities found!");
			return false;
		}
		
		initialiseActivityVars();
		
		//Extract xmls
		xmls = extractAttribute(root, "xmls");
		
		for ( Node n : activities ) {
			
			//Check structure
			try {
				
				temp = n.getChildNodes();
				activity = removeTextNodes(temp);
				
				checkNode(n, "activity");
				checkNode(activity.get(0), "questionText");
				checkNode(activity.get(1), "expectedResponse");
				
			} catch ( Exception e ) {
				System.err.println("These activities does not conform to the \n" +
	                       "activities.xml structure. Please revise.");
				return false;
			}
			
			//Extract ID
			String errNode = "";
			try {
				//Integer ID fields 
				errNode = "activity id";	acID = Integer.parseInt(extractAttribute(n, "id"));				
			} catch ( NumberFormatException nfe ) {
				System.err.println("Unexpected format of " + errNode +
						                     "\nPlease revise the xml document");
				return false;
			} catch ( NullPointerException npe ) {
				System.err.println("Null pointer for: " + errNode);
				return false;
			}
			
			qText = activity.get(0).getTextContent();
			eResp = activity.get(1).getTextContent();
			
			//Check for errors in strings
			if ( xmls.equals(fail)   ||
				 qText.equals(fail)  || qText.matches("\\s*") ||
				 eResp.equals(fail)  || eResp.matches("\\s*")) {
				System.err.println("Data Missing...");
				return false; 
			}
			
			//Everything is ok, create objects
			Main.questions.put(acID, new Question(acID, qText, eResp));
			
		}
		
		return true;
	}

	/*
	 * This method creates a temporary Recording object that can be used to build part 
	 * of the activity class later. There is an error message here if the transcript
	 * is empty. 
	 */
	private static void addRecording(int rID, int acID, String trans, int asID) {
		if( !trans.matches("\\s*") ) {
			transcripts.add(new Recording(rID, acID, trans));
		} else {
			System.err.println("No transcript for recording: " + rID +
					           "\n" + "In assessment: " + asID);
		}
	}

	/*
	 * Special version of removing text nodes which applies to transcripts. This method
	 * just gets the transcript Node from NodeList provided. 
	 */
	private static Node extractTranscript(NodeList nl) {
		for ( int n = 0; n < nl.getLength(); n++ ) {
			if ( nl.item(n).getNodeName().equals("transcript"))
				return nl.item(n);
		}
		return null;
	}

	/*
	 * When first exploring the document, the structure is checked against the expected
	 * structure, if nodes are incorrectly positioned or missing errors will be caught.
	 */
	private static void checkNode(Node n, String expected) throws Exception {
		if ( !n.getNodeName().equals(expected) ) {
			System.err.println("Unexpected node name: " + 
					"saw: " + n.getNodeName() +
					" expected: " + expected + "\n");
			throw new Exception();
		}
	}

	/*
	 * Given a Node and attribute name, this method retrieves the nodes attributes and
	 * returns the one which matches the attrib string. If the attribute does not exist
	 * and error message is printed. If the field exists but the contents is empty, 
	 * return fail string. 
	 */
	private static String extractAttribute(Node n, String attrib) {
		if ( n.hasAttributes() )
			try {
				String foundAttrib = n.getAttributes().getNamedItem(attrib).getTextContent();
				//Regex match to catch empty attributes
				if ( !foundAttrib.matches("\\s*") ) 
					return foundAttrib;
			} catch (NullPointerException npe ) {
				System.err.println(n.getNodeName() + 
						           " Does not have the attribute: " + attrib);
			}
		return fail;
	}

	/*
	 * When getChildNodes() is called on assessment.xml, every other Node is a TEXT_NODE
	 * which we have no interest in. Also NodeLists are not itterable but ArrayLists are
	 * so going through a NodeList, only extracting the useful nodes and storing them in
	 * an ArrayList is much more efficient.
	 */
	private static ArrayList<Node> removeTextNodes(NodeList nl) {
		ArrayList<Node> usefull = new ArrayList<Node>();
		for ( int n = 0; n < nl.getLength(); n++ ) {
			if ( nl.item(n).getNodeType() != Node.TEXT_NODE )
				usefull.add(nl.item(n));
		}
		return usefull;
	}
}
