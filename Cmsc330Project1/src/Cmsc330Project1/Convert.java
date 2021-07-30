/* File Name: Convert.java
 * Author: Lang, Jordan
 * Project Name: Project 1 CMSC330
 * Date: 06/08/2021
*/
package Cmsc330Project1;

import java.io.*;
import javax.swing.JOptionPane;

class Convert {

	private StreamTokenizer streamTokenizer;
	private String otherGrammar = "():,;.";
	
	private Token[] Grammar = { Token.LEFT_PARENTHESIS, Token.RIGHT_PARENTHESIS, Token.COLON, Token.COMMA, Token.SEMICOLON,
			Token.PERIOD };

	// use to convert/accept the input file to lex/tokenize
	public Convert(String file) {
		try {

			streamTokenizer = new StreamTokenizer(new FileReader(file));
			// ignore the .
			streamTokenizer.ordinaryChar('.');

		} catch (FileNotFoundException e) {
			// output frame if file is not found
			JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Could not find file.");
			System.exit(0);
		}
	}

	// Get the line the error occurred on
	public int checkLineNumber() {
		return streamTokenizer.lineno();
	}

	public Token getToken() throws InvalidSyntax {

		try {
			// continue to "getTokens" until the end of the file
			while (streamTokenizer.nextToken() != StreamTokenizer.TT_EOF) {

				// check for strings
				if (streamTokenizer.ttype == '"') {

					return Token.STRING;
				}

				// check if token is a number
				else if (streamTokenizer.ttype == StreamTokenizer.TT_NUMBER) {
					return Token.NUM;
				}

				// check if the token is a word
				else if (streamTokenizer.ttype == StreamTokenizer.TT_WORD) {

					// create array and iterate through enums
					Token[] values = Token.values();
					for (Token theToken : values) {

						// if the current token word equals one of the enums
						if (theToken.name().equals(streamTokenizer.sval.toUpperCase()))
							return theToken;
					}
					// throw error if the current word token is not in the enums
					throw new InvalidSyntax("There was an issue with the GUI tokens. Please check your file.");
				} 
				
				else if ((streamTokenizer.ttype != StreamTokenizer.TT_WORD)
						| (streamTokenizer.ttype != StreamTokenizer.TT_NUMBER) | (streamTokenizer.ttype != '"')) {

					// if the token is not any of the above, loop through the "otherGrammar" to
					for (int i = 0; i < otherGrammar.length(); i++)
						if (streamTokenizer.ttype == otherGrammar.charAt(i))
							// find the enum it corresponds to
							return Grammar[i];
				}

				else {
					throw new InvalidSyntax("There was an issue with the GUI tokens. Please check your file.");
				}
			}

		} catch (InvalidSyntax | IOException e) {
			e.printStackTrace();
		}

		return Token.END;
	}

	// get the string value of the current token
	public String stringValue() {
		return streamTokenizer.sval;
	}

	// Get the number value of the current token
	public int numValue() {
		return (int) streamTokenizer.nval;
	}

}
