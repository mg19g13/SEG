/*
 * COMP2211 CW 2015
 * SEG GROUP 1
 * R/T Auto marker 
 * Class: Activity
 */


public class Activity {
	
	private int activityID;
	private Question question;
	private String response;
	private int recordingID;
	private double mark = 0.0;
	
	/*
	 * Constructor Notes
	 */
	public Activity(int rID, int aID, String response) {
		this.setRecordingID(rID);
		this.activityID = aID;
		this.response = response;
		this.question = getQuestion(activityID);
	}
	
	//Retrieve correct question to go with response
	private Question getQuestion(int aID) {
		//Need null checks etc...
		return Main.questions.get(aID);
	}

	/*
	 * calculateMark Method Notes
	 */
	public void calculateMark() {
		//TODO revision: Return Type?
		
	}

	//AUTO GENERATED GETTERS AND SETTERS
	
	public double getMark() {
		return mark;
	}

	public void setMark(double mark) {
		this.mark = mark;
	}
	
	public int getActivityID() {
		return this.activityID;
	}
	
	public String getResponse() {
		return this.response;
	}

	public int getRecordingID() {
		return recordingID;
	}

	public void setRecordingID(int recordingID) {
		this.recordingID = recordingID;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

}
