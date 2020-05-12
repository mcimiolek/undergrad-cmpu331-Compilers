package com.company;

/* This class is used to return errors caused by a mistake in the parser. Errors may include attempting to access in an
   invalid line in the parse table or RHS table, an error in the syntax, or an attempt to use an error parse number. */

public class ParserError  extends CompilerError
{
    // Causes an error, with the message depending on the input string giving more specifics on the error.
    ParserError(String message, int line)
    {
        System.out.println(message + " on line: " + line);
    }
}
