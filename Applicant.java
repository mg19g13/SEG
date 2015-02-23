/*
 * COMP2211 CW 2015
 * SEG GROUP 1
 * R/T Auto marker 
 * Class: Applicant
 */


public class Applicant {
	
	//Fields applicable to individual applicants
	private int candidateID;
	private String forename;
	private String surname;
		
	/*
	 * Constructor Notes...
	 */
	public Applicant(int ID, String forename, String surname) {
		this.candidateID = ID;
		this.forename = forename;
		this.surname = surname;
	}
	
	//AUTO GENERATED GETTERS AND SETTERS

	public int getCandidateID() {
		//Authenticate request first
		return candidateID;
	}

	public String getForename() {
		//Authenticate request first
		return forename;
	}

	public String getSurname() {
		//Authenticate request first
		return surname;
	}

}