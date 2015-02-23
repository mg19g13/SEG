/*
 * COMP2211 CW 2015
 * SEG GROUP 1
 * R/T Auto marker 
 * Class: Application
 */

import java.util.ArrayList;
import java.util.Calendar;

public class Application {
	
	//Fields applicable to individual applicants
	private Applicant applicant;
	private Calendar dateTime;
	
	//Fields applicable to airports
	private String company;
	private String airport; //Could even make this an enumeration 
	
	//Fields applicable to assessment
	private int assessmentID;
	private String xmls;
	private ArrayList<Activity> activities;
	private double finalMark = 0.0;
	
	/*
	 * Constructor Notes...
	 */
	public Application(Applicant app, String date, String company, String airport, int aID, String xmls) {
		activities = new ArrayList<Activity>();
		this.applicant = app;
		this.dateTime = parseDT(date);
		this.company = company;
		this.airport = airport;
		this.assessmentID = aID;
		this.xmls = xmls;
	}
	
	//AUTO GENERATED GETTERS AND SETTERS

	private Calendar parseDT(String date) {
		// TODO Parse date time string into calendar object :)
		return null;
	}

	protected String getCompany() {
		return company;
	}

	protected String getAirport() {
		return airport;
	}

	protected Calendar getDateTime() {
		return dateTime;
	}

	protected int getAssessmentID() {
		return assessmentID;
	}

	protected ArrayList<Activity> getActivities() {
		return activities;
	}
	
	protected void addActivity(Activity a) {
		activities.add(a);
	}

	protected double getFinalMark() {
		return finalMark;
	}

	protected void setFinalMark(double finalMark) {
		this.finalMark = finalMark;
	}

	protected Applicant getApplicant() {
		//Authority now or later?
		return applicant;
	}

	protected String getXmls() {
		return xmls;
	}
	

}
