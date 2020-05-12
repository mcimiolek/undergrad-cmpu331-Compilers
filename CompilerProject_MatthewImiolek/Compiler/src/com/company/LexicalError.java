package com.company;

/* This class is used to return errors caused by a mistake in the lexer. Errors may include an invalid character in the
   input, an ill-formed constant, an ill-formed comment, or an identifier/constant being too long. */

public class LexicalError extends CompilerError
{

    // Causes an error, with the message depending on the input string giving more specifics on the error.
    LexicalError(String message, int line)
    {
        System.out.println(message + " On line " + line + ".");
    }
}
