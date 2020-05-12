package com.company;

/* This class is used to return errors caused by a mistake in the semantic actions. Errors may include an undeclared
   variable, a type mismatch (I.E. getting a real when you expect an integer), a bad parameter (I.E. getting a real when
   doing MOD), an equation type mismatch, not an array, illegal procedure, wrong number of parameters, a bad parameter,
   not a function error, or a not a procedure error. */

public class SemanticError extends CompilerError
{
    // Causes an error, with the message depending on the input string giving more specifics on the error.
    SemanticError(String message, int line)
    {
        System.out.println(message + " on line: " + line);
    }
}
