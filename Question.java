/*
 * COMP2211 CW 2015
 * SEG GROUP 1
 * R/T Auto marker 
 * Class: Question
 */


public class Question {
	
	private int ID;
	private String expectedResponse;
	private String[] expectedWords; //TODO revision: This kind of array to index order of words or not?
	private String question;
	
	/*
	 * Constructor Notes
	 */
	public Question(int ID, String question, String expected) {
		this.ID = ID;
		this.expectedResponse = expected;
		this.question = question;
	}
	
	/*
	 * checkOrder Method Notes
	 */
	public void checkOrder() {
		//TODO revision: Return type?
	}
	
	/*
	 * checkPhraseology Method Notes
	 */
	public void checkPhraseology() {
		//TODO revision: Return type?
	}
	
	/*
	 * checkHomonyms Method Notes
	 */
	public void checkHomonyms() {
		//TODO revision: Return type?
	}
	
	//AUTO GENERATED GETTERS AND SETTERS

	public String getExpectedResponse() {
		return expectedResponse;
	}

	public String getQuestion() {
		return question;
	}
	
	protected void setWords(String words) {
		//Hopefully this splits on all punctuation
		expectedWords = words.split("\\p{Punct}|\\s");		
		//Remove the empty strings?
	}

}
