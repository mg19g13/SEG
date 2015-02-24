/*
 * COMP2211 CW 2015
 * SEG GROUP 1
 * R/T Auto marker
 * Class: GUI
 */

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class GUI extends JPanel implements ActionListener {
//I AM A COMMENT
	
	// NEW ADDITION - Integer is the unique candidate ID.
	static HashMap<Integer, Application> applications = new HashMap<Integer, Application>();
	// NEW ADDITION - Integer is the activity ID.
	static HashMap<Integer, Question> questions = new HashMap<Integer, Question>();

	static private final String newline = "\n";
	JButton uploadSchema, uploadAssessment, upLoadActivities, loadFiles;
	JTextArea log;
	JFileChooser chooser;
	File schemaFile, assessmentFile, activitiesFile;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// Turn off metal's use of bold fonts
				UIManager.put("swing.boldMetal", Boolean.FALSE);
				showGUI();
			}
		});
	}

	public GUI() {
		super(new BorderLayout());
		JPanel buttonPanel = new JPanel();
		log = new JTextArea(10, 25);
		log.setMargin(new Insets(5, 5, 5, 5));
		log.setEditable(false);
		JScrollPane logScrollPane = new JScrollPane(log);

		uploadSchema = new JButton("Upload a XML schema");
		uploadSchema.addActionListener(this);
		
		uploadAssessment = new JButton("Upload an Assessment File");
		uploadAssessment.addActionListener(this);

		upLoadActivities = new JButton("Upload an Activities File");
		upLoadActivities.addActionListener(this);

		loadFiles = new JButton("Load in XML files");
		loadFiles.addActionListener(this);

		buttonPanel.add(uploadSchema);
		buttonPanel.add(uploadAssessment);
		buttonPanel.add(upLoadActivities);
		buttonPanel.add(loadFiles);

		add(buttonPanel, BorderLayout.PAGE_START);
		add(logScrollPane, BorderLayout.CENTER);

		// Create the file chooser
		chooser = new JFileChooser();
		FileNameExtensionFilter xmlFilter = new FileNameExtensionFilter(
				"xml files (*.xml)", "xml");
		chooser.setFileFilter(xmlFilter);
		chooser.setDialogTitle("Open schedule file");
		chooser.setFileFilter(xmlFilter);

	}

	public void actionPerformed(ActionEvent e) {

		// Takes the schema
		if (e.getSource() == uploadSchema) {
			int returnVal = chooser.showOpenDialog(GUI.this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				schemaFile = chooser.getSelectedFile();
				log.append("You chose to upload this XML Schema: "
						+ chooser.getSelectedFile().getName() + "." + newline);
			} else {
				log.append("Upload cancelled by user." + newline);
			}
			log.setCaretPosition(log.getDocument().getLength());
		}
		
		// THIS TAKES THE assessment file
		if (e.getSource() == uploadAssessment) {
			int returnVal = chooser.showOpenDialog(GUI.this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				assessmentFile = chooser.getSelectedFile();
				log.append("You chose to upload this XML assessment file: "
						+ chooser.getSelectedFile().getName() + "." + newline);
			} else {
				log.append("Upload cancelled by user." + newline);
			}
			log.setCaretPosition(log.getDocument().getLength());
		}

		// THIS TAKES THE activities file
		if (e.getSource() == upLoadActivities) {
			int returnVal = chooser.showOpenDialog(GUI.this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				activitiesFile = chooser.getSelectedFile();
				log.append("You chose to upload this XML activities file: "
						+ chooser.getSelectedFile().getName() + "." + newline);
			} else {
				log.append("Upload cancelled by user." + newline);
			}
			log.setCaretPosition(log.getDocument().getLength());
		}
		
		if (e.getSource() == loadFiles) {
			// TODO If check that both files have been uploaded
			System.out.println("Successful parse: "
					+ XML_Parser.parseXML(getActivitiesFile(), schemaFile));
			System.out.println("Successful parse: "
					+ XML_Parser.parseXML(getAssessmentFile(), schemaFile));
		}
	}

	public static void showGUI() {
		JFrame frame = new JFrame("Prototype User Interface");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new GUI());
		frame.pack();
		frame.setVisible(true);
	}

	public File getAssessmentFile() {
		return assessmentFile;
	}

	public File getActivitiesFile() {
		return activitiesFile;
	}

}