// AUTHOR: Tyler Ziggas
// Date: February 14, 2021
// This program finds a file on the desktop named z.txt
// This program is a lexical analyzer that can check for a multitude of different tokens.
// These tokens include digits, letters, special characters, whitespace, EOF, and a restricted INT variable.
// This is a conversion of C code made into Java.

package edu.umsl;

import java.io.*;

public class LexicalAnalyzer {

    static char lexeme[] = new char[100], readFile[] = new char[100]; // One array for adding, one for storing the file characters
    static char nextChar;
    static int nextToken, charClass, counter, lexLen, readChar, resetCounter = 0;

    final static int LETTER = 0, DIGIT = 1, UNKNOWN = 99, INT_LIT = 10, IDENT = 11, ASSIGN_OP = 20, ADD_OP = 21, SUB_OP = 22,
            MULT_OP = 23, DIV_OP = 24, LEFT_PAREN = 25, RIGHT_PAREN = 26, COMMA = 27, SEMI = 28, INTEGER = 29, EOF = -1;
    // Final variables needed to figure out what the token id is for each one

    public static void main(String[] args) throws IOException{// Main method
        String userHome = System.getProperty("user.home"); // User home path
        File file = new File(userHome + "\\Desktop\\z.txt"); // Find desktop file z.txt
     if(file.exists()){ // Check if file exists
         BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
         while ((readChar = br.read()) != -1) { // Put the file into a character array to parse
             readFile[counter] = (char) readChar;
             counter++;
         }
         counter = -1; // Reset e to start at the beginning
         getChar(); // Invoke getChar
         do {
             lex(); // Go to lex and find what the character is
         } while (nextToken != EOF); // Check to see if it is EOF
        }else {
         System.out.println("File doesn't exist"); // If file doesnt exist
        }
    }

    static void getChar() {
        counter++; // Add one every time for the next char
        if (readFile[counter] != (char) 0) {// Check to see if we hit the end of the file
            nextChar = readFile[counter];
            if (Character.isDigit(nextChar)) { // If it is a digit
                charClass = DIGIT;
            } else if (Character.isAlphabetic(nextChar)) { // If it is a letter
                charClass = LETTER;
            } else { // If it is neither of those
                charClass = UNKNOWN;
            }
        } else {
            charClass = EOF;
        }
    }

    static void addChar() { // This adds to the character array for printing the lexeme
        if (lexLen <= 98) {
            lexeme[lexLen++] = nextChar;
            lexeme[lexLen] = 0;
            resetCounter++; // Keep track of how many were input
        } else {
            System.out.println("Error - Lexeme is too long");
        }
    }

    private static int lookup() {
        switch(nextChar){ // Check special character
            case '(': // Left parentheses case
                addChar();
                nextToken = LEFT_PAREN;
                break;
            case ')': // Right parentheses case
                addChar();
                nextToken = RIGHT_PAREN;
                break;
            case '+': // addition case
                addChar();
                nextToken = ADD_OP;
                break;
            case '-': // subtraction case
                addChar();
                nextToken = SUB_OP;
                break;
            case '*': // multiplication case
                addChar();
                nextToken = MULT_OP;
                break;
            case '/': // division case
                addChar();
                nextToken = DIV_OP;
                break;
            case '=': // equals case
                addChar();
                nextToken = ASSIGN_OP;
                break;
            case ',': // comma case
                addChar();
                nextToken = COMMA;
                break;
            case ';': // semicolon case
                addChar();
                nextToken = SEMI;
                break;
            default: // otherwise, EOF
                addChar();
                nextToken = EOF;
        }
        return nextToken;
    }

    private static int lex() {//Handling as in C code. Space handling done at main. Reading char is also at main.
        lexLen = 0; // Start at beginning of lexLen
        int EOFFlag = 0; // Flag to make sure EOF hasnt been triggered
        while (Character.isWhitespace(nextChar)) {
            getChar();
        }
        switch (charClass) {
            case LETTER:
                addChar(); // Adding character to array and getting the character
                getChar();
                while (charClass == LETTER || charClass == DIGIT) { // Loop if a letter or digit keeps appearing
                    addChar();
                    getChar();
                }
                nextToken = IDENT;
                if ((lexeme[0] == 'i' && lexeme[1] == 'n' && lexeme[2] == 't' && lexeme[3] == '\0') ||
                        (lexeme[0] == 'I' && lexeme[1] == 'N' && lexeme[2] == 'T' && lexeme[3] == '\0')) { // Reserved case for INT
                    nextToken = INTEGER;
                }
                break;
            case DIGIT:
                addChar(); // Adding character to array and getting the character
                getChar();
                while (charClass == DIGIT) { // Loop if a digit keeps appearing
                    addChar();
                    getChar();
                }
                nextToken = INT_LIT;
                break;
            case UNKNOWN:
                lookup(); // Lookup what character grabbed is
                getChar();
                break;
            case EOF:
                nextToken = EOF;
                EOFFlag = 1; // Set flag, EOF has been reached
        }
        if (EOFFlag == 0) { // Check if EOF flag has been touched
            System.out.println("Next Token is " + nextToken + ": Next lexeme is " + new String(lexeme)); // Print next lexeme
            while (resetCounter != 0) { // Used to clear the lexeme for the next print
                lexeme[resetCounter] = (char) 0;
                resetCounter--;
            }
        } else {
            System.out.println("Next Token is " + nextToken + ": Next lexeme is EOF"); // Print EOF
        }
        return nextToken;
    }
}