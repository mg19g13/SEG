import java.io.File;
import java.util.HashMap;


/*
 * COMP2211 CW 2015
 * SEG GROUP 1
 * R/T Auto marker 
 * Class: Main
 */


public class Main {

	//NEW ADDITION - Integer is the unique candidate ID.
	static HashMap<Integer, Application> applications = new HashMap<Integer, Application>();
	//NEW ADDITION - Integer is the activity ID.
	static HashMap<Integer, Question> questions = new HashMap<Integer, Question>();

	public static void main(String[] args) {	
				
	
		//Obviously change to accept input from file chooser
		System.out.println("Successful parse: " +
		XML_Parser.parseXML(new File("C:/Users/Tom/Desktop/XML/assessment.xml")));
		System.out.println("Successful parse: " +
		XML_Parser.parseXML(new File("C:/Users/Tom/Desktop/XML/activities.xml")));

	}

}
