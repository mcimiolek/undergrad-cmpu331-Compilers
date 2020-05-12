package com.company;

import java.io.*;
import java.lang.*;

/* Creates and initializes a Compiler object, then passes control to that object. */

public class Main
{
    // Creates the compiler object, then runs the main function for that compiler.
    public static void main(String[] args) throws LexicalError, SymbolTableError, IOException, SemanticError, ParserError
    {
        Compiler compiler = new Compiler();
        compiler.CompilerMain();
    }
}
