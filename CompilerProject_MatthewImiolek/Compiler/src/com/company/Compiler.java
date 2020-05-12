package com.company;

import java.io.*;

    /* Creates a complete compiler, with all of the key parts of the compiler (the lexer, the parser, and the semantic
       actions), so that they can all be used as a whole. It also manages these components so they are all created and
       initialized in the same place. */

public class Compiler
{
    private LexicalAnalyser lexer;
    private Parser parser;
    private SemanticAction semact;
    public static File filebylinefile = new File("C:\\Users\\mcimi\\Desktop\\cmpu-331\\CompilerProject_MatthewImiolek\\test.txt");

    // Creates a new compiler object, which consists of making a new lexer, parser, and set of semantic actions to be
    // used in the main compiler.
    Compiler() throws SymbolTableError, IOException
    {
        lexer = new LexicalAnalyser();
        parser = new Parser();
        semact = new SemanticAction();
    }

    // The main compiler class, which actually runs the compiler by running the parser, which uses the semantic actions
    // and lexer to provide the final compiler output. It then prints the TVI code the parser has generated.
    public void CompilerMain() throws LexicalError, SymbolTableError, SemanticError, ParserError
    {
        parser.MainParser(semact, lexer);
        parser.semact.alltvi.print();
    }
}
