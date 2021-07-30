/* File Name: GenerateGUI.java
 * Author: Lang, Jordan
 * Project Name: Project 1 CMSC330
 * Date: 06/08/2021
*/
package Cmsc330Project1;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.IOException;
import javax.swing.*;

public class GenerateGUI {

	private boolean insideFrame;
	private Convert converter;
	private String getText;
	private int jTextFieldlength;
	private double windowWidth;
	private double windowHeight;
	private int row;
	private int column;
	private int optionalHorizontalGap;
	private int optionalVerticalGap;
	private Token placeholderToken;
	JPanel panel1 = new JPanel();
	JFrame frame1 = new JFrame();

	// constructor to read in/choose file
	public GenerateGUI() throws IOException {
		try {

			JFileChooser fc = new JFileChooser();
			fc.setCurrentDirectory(new java.io.File(".")); // use current directory
			fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				// use Convert Object to lexically analyze the file first
				converter = new Convert(fc.getSelectedFile().getAbsolutePath());
			}
			placeholderToken = converter.getToken();

		} catch (InvalidSyntax e) {
			e.printStackTrace();
		}
	}

	// parse the GUI from the tokens
	public boolean getGUI() throws InvalidSyntax, IOException {

		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		boolean stillValid;
		// get the first placeholderToken
		if (stillValid = (placeholderToken == Token.WINDOW)) {
			if (stillValid) {
				// update so to know that you are in the frame/window at the start
				insideFrame = true;
				placeholderToken = converter.getToken();
			}

			// traverse through the file pulling each placeholderToken
			stillValid = stillValid && (placeholderToken == Token.STRING);
			if (stillValid) {
				// use the String next to WINDOW placeholderToken to label the frame
				frame1.setTitle(converter.stringValue());
				placeholderToken = converter.getToken();
			}

			stillValid = stillValid && (placeholderToken == Token.LEFT_PARENTHESIS);
			if (stillValid) {
				placeholderToken = converter.getToken();
			}

			stillValid = stillValid && (placeholderToken == Token.NUM);
			if (stillValid) {
				// use numValue() from Converter to get the window width
				windowWidth = converter.numValue();
				placeholderToken = converter.getToken();
			}

			stillValid = stillValid && (placeholderToken == Token.COMMA);
			if (stillValid) {
				placeholderToken = converter.getToken();
			}

			stillValid = stillValid && (placeholderToken == Token.NUM);
			if (stillValid) {
				// use numValue() from Converter to get the window height
				windowHeight = converter.numValue();
				placeholderToken = converter.getToken();
			}

			stillValid = stillValid && (placeholderToken == Token.RIGHT_PARENTHESIS);
			if (stillValid) {
				// cast to int to allow the entry
				frame1.setSize((int) windowWidth, (int) windowHeight);
				placeholderToken = converter.getToken();
			}

			stillValid = stillValid && (placeholderToken == Token.LAYOUT);
			if (stillValid) {
				placeholderToken = converter.getToken();
			}

			// call the layout class to see if the layout is a GRID or FLOW
			stillValid = stillValid && (layout());

			stillValid = stillValid && (placeholderToken == Token.COLON);
			if (stillValid) {
				placeholderToken = converter.getToken();
			}

			// call the widgets class to traverse through and see if there are RadioButtons,
			// Buttons, labels, textfields, or panels
			stillValid = stillValid && (widgets());

			stillValid = stillValid && (placeholderToken == Token.END);
			
			if (stillValid) {
				placeholderToken = converter.getToken();
			}

			stillValid = stillValid && (placeholderToken == Token.PERIOD);
			if (stillValid) {
				frame1.setVisible(true); // display the frame and other aspects once the file reads END.
				return true;
			}
			throw new InvalidSyntax(
					// display the line number the error occurred on
					"There was an issue with the tokens. Please check line " + converter.checkLineNumber() + ".");
		}
		return false;
	}

	boolean layout() throws InvalidSyntax {

		// again use the stillValid variable to traverse through the file
		boolean stillValid;

		// check if the current token is GRID...if so, take following inputs
		if (stillValid = (placeholderToken == Token.GRID)) {
			placeholderToken = converter.getToken();

			stillValid = stillValid && (placeholderToken == Token.LEFT_PARENTHESIS);
			if (stillValid) {
				placeholderToken = converter.getToken();
			}

			stillValid = stillValid && (placeholderToken == Token.NUM);
			if (stillValid) {
				// get the number of rows using numValue from converter
				row = converter.numValue();
				placeholderToken = converter.getToken();
			}

			stillValid = stillValid && (placeholderToken == Token.COMMA);
			if (stillValid) {
				placeholderToken = converter.getToken();
			}

			stillValid = stillValid && (placeholderToken == Token.NUM);
			if (stillValid) {
				// get the number of columns using numValue from converter
				column = converter.numValue();
				placeholderToken = converter.getToken();
			}

			stillValid = stillValid && (placeholderToken == Token.COMMA);
			if (stillValid) {
				placeholderToken = converter.getToken();

				stillValid = stillValid && (placeholderToken == Token.NUM);
				if (stillValid) {
					// if needed, get the optional vertical gap with the numValue from converter
					optionalVerticalGap = converter.numValue();
					placeholderToken = converter.getToken();
				}

				stillValid = stillValid && (placeholderToken == Token.COMMA);
				if (stillValid) {
					placeholderToken = converter.getToken();
				}

				stillValid = stillValid && (placeholderToken == Token.NUM);
				if (stillValid) {
					// if needed, get the optional horizontal gap with the numValue from converter
					optionalHorizontalGap = converter.numValue();
					placeholderToken = converter.getToken();
				}

				if (stillValid = (placeholderToken == Token.RIGHT_PARENTHESIS)) {
					// create panel with all 4 inputs (this works with 3 as well)
					panel1.setLayout(new GridLayout(row, column, optionalVerticalGap, optionalHorizontalGap));
				}
			}

			// If there are not optionalVerticalGap or optionalHorizontalGap in the file
			else if (stillValid = (placeholderToken == Token.RIGHT_PARENTHESIS)) {
				// jump here if only a row and column are input without the other option Gaps
				// create panel with only row and column
				panel1.setLayout(new GridLayout(row, column));
			}

			placeholderToken = converter.getToken();
			return true;
		}

		// check if the current token is FLOW
		if (stillValid = (placeholderToken == Token.FLOW)) {
			if (stillValid) {
				// check if inside frame
				if (insideFrame == true) {
					frame1.setLayout(new FlowLayout());
				} else {
					panel1.setLayout(new FlowLayout());
				}
			}

			placeholderToken = converter.getToken();
			return true;
		}

		return false;
	}

	boolean widget() throws InvalidSyntax {

		boolean stillValid;
		if (stillValid = (placeholderToken == Token.BUTTON)) {
			if (stillValid) {
				placeholderToken = converter.getToken();
			}

			stillValid = stillValid && (placeholderToken == Token.STRING);
			if (stillValid) {
				// get the text for the button using stringValue from converter
				getText = converter.stringValue();
				placeholderToken = converter.getToken();
			}

			stillValid = stillValid && (placeholderToken == Token.SEMICOLON);
			if (stillValid) {
				// check if inside the frame
				if (insideFrame == true) {
					frame1.add(new JButton(getText));
					// place in the panel1 if there is a panel1
				} else {
					panel1.add(new JButton(getText));
				}
			}
			placeholderToken = converter.getToken();
			return true;

		}
		// check if the current token is RADIO
		else if (stillValid = (placeholderToken == Token.RADIO)) {
			placeholderToken = converter.getToken();

			stillValid = stillValid && (placeholderToken == Token.STRING);
			if (stillValid) {
				// use the stringValue to get the string name for the RadioButton
				getText = converter.stringValue();
				placeholderToken = converter.getToken();
			}

			stillValid = stillValid && (placeholderToken == Token.SEMICOLON);
			if (stillValid) {
				// check if inside frame
				if (insideFrame == true) {
					frame1.add(new JRadioButton(getText)); // create radiobutton in frame
				} else {
					panel1.add(new JRadioButton(getText)); // create radiobutton in panel
				}
			}
			placeholderToken = converter.getToken();
			return true;
		}

		// check if current token is LABEL
		else if (stillValid = (placeholderToken == Token.LABEL)) {
			placeholderToken = converter.getToken();

			stillValid = stillValid && (placeholderToken == Token.STRING);
			if (stillValid) {
				// use stringValue to get the String name for the label
				getText = converter.stringValue();
				placeholderToken = converter.getToken();
			}

			stillValid = stillValid && (placeholderToken == Token.SEMICOLON);
			if (stillValid) {
				// check if inside frame
				if (insideFrame == true) {
					frame1.add(new JLabel(getText)); // create JLabel inside a frame
				} else {
					panel1.add(new JLabel(getText)); // creat JLabel inside a panel
				}
				placeholderToken = converter.getToken();
				return true;
			}

			// check if the current token is PANEL
		} else if (stillValid = (placeholderToken == Token.PANEL)) {
			// If inside the frame only, create a new panel1 and add it to the frame
			if (insideFrame == true) {
				frame1.add(panel1 = new JPanel());

				// if not inside frame, add to the panel
			} else {
				panel1.add(panel1 = new JPanel());
			}
			// update so that if there is another panel1, it will place it inside current
			insideFrame = false;
			placeholderToken = converter.getToken();

			stillValid = stillValid && (placeholderToken == Token.LAYOUT);
			if (stillValid) {
				placeholderToken = converter.getToken();
			}

			// call the layout class to check if the panel will be GRID or FLOW
			stillValid = stillValid && (layout());

			stillValid = stillValid && (placeholderToken == Token.COLON);
			if (stillValid) {
				placeholderToken = converter.getToken();
			}

			// call the widgets class to traverse through and see if there are RadioButtons,
			// Buttons, labels, textfields, or panels
			stillValid = stillValid && (widgets());

			stillValid = stillValid && (placeholderToken == Token.END);
			if (stillValid) {
				placeholderToken = converter.getToken();
			}

			stillValid = stillValid && (placeholderToken == Token.SEMICOLON);
			if (stillValid) {
				placeholderToken = converter.getToken();
				return true;
			}

		}

		// Check if current token is TEXTFIELD
		else if (stillValid = (placeholderToken == Token.TEXTFIELD)) {
			placeholderToken = converter.getToken();

			stillValid = stillValid && (placeholderToken == Token.NUM);
			if (stillValid) {
				//use numValue to get the text for the field
				jTextFieldlength = converter.numValue();
				placeholderToken = converter.getToken();
			}

			stillValid = stillValid && (placeholderToken == Token.SEMICOLON);
			if (stillValid) {
				// If inside the frame only, create a new panel1 and add it to the frame
				if (insideFrame == true) {
					frame1.add(new JTextField(jTextFieldlength));

					// if there is already a frame, add to the current frame
				} else {
					panel1.add(new JTextField(jTextFieldlength));
				}
				placeholderToken = converter.getToken();
				return true;
			}
		}
		return false;

	}

	// call the widget class so that it may be called inside itself
	boolean widgets() throws InvalidSyntax {
		if (widget()) {
			if (widgets()) {
				return true;
			}
			return true;
		}
		return false;
	}
}
