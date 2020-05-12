package com.company;

import java.util.*;

/* Used to create parser object, which creates a lexer and semantic actions. Using the tokens from the lexer in
   conjunction with the parse table and RHS table, the parser manipulates the parse stack, making sure that the order of
   the tokens meets the syntactic rules of vascal. It also calls the semantic actions when the marker for a semantic
   action appears on the parse stack. */

public class Parser
{
    private Boolean seeparsestack = false;
    private Boolean horizontaldump = true;
    private Stack<String> pstack = new Stack<>();
    private Token curtoken;
    private Token prevtoken;
    private LexicalAnalyser lexan;
    private HashMap<String, Integer> rhslocations = new HashMap<>();
    private HashMap<String, Integer> tokenlocs = new HashMap<>();
    private RHSTable rhstable = new RHSTable();
    private ParseTable parsetable = new ParseTable();
    public SemanticAction semact;
    private String result = "nan";
    private int x;
    private int y;
    private int prodruleloc;

    // Sets up the lexer, semantic actions, parse stack, and the table locations.
    private void Initialize(SemanticAction semacts, LexicalAnalyser lexer)
    {
        pstack.push("ENDOFFILE");
        pstack.push("<Goal>");
        lexan = lexer;
        semact = semacts;

        InitRHSLoc();
        InitTokenLocs();
    }

    // Sets up the hashmap of which line the given non-terminal expands to in the table of the right-hand side productions
    // of the grammar.
    private void InitRHSLoc()
    {
        rhslocations.put("<program>",0);
        rhslocations.put("<identifier-list>",1);
        rhslocations.put("<declarations>",2);
        rhslocations.put("<sub-declarations>",3);
        rhslocations.put("<compound-statement>",4);
        rhslocations.put("<identifier-list-tail>",5);
        rhslocations.put("<declaration-list>",6);
        rhslocations.put("<type>",7);
        rhslocations.put("<declaration-list-tail>",8);
        rhslocations.put("<standard-type>",9);
        rhslocations.put("<array-type>",10);
        rhslocations.put("<subprogram-declaration>",11);
        rhslocations.put("<subprogram-head>",12);
        rhslocations.put("<arguments>",13);
        rhslocations.put("<parameter-list>",14);
        rhslocations.put("<parameter-list-tail>",15);
        rhslocations.put("<statement-list>",16);
        rhslocations.put("<statement>",17);
        rhslocations.put("<statement-list-tail>",18);
        rhslocations.put("<elementary-statement>",19);
        rhslocations.put("<expression>",20);
        rhslocations.put("<else-clause>",21);
        rhslocations.put("<es-tail>",22);
        rhslocations.put("<subscript>",23);
        rhslocations.put("<parameters>",24);
        rhslocations.put("<expression-list>",25);
        rhslocations.put("<expression-list-tail>",26);
        rhslocations.put("<simple-expression>",27);
        rhslocations.put("<expression-tail>",28);
        rhslocations.put("<term>",29);
        rhslocations.put("<simple-expression-tail>",30);
        rhslocations.put("<sign>",31);
        rhslocations.put("<factor>",32);
        rhslocations.put("<term-tail>",33);
        rhslocations.put("<factor-tail>",34);
        rhslocations.put("<actual-parameters>",35);
        rhslocations.put("<Goal>",36);
        rhslocations.put("<constant>",37);
    }

    // Sets up the hashmap of which line in the parse table each terminal must be looked at with the current stack symbol
    // in order to find the production number, an accept, or an error.
    private void InitTokenLocs()
    {
        tokenlocs.put("PROGRAM",0);
        tokenlocs.put("BEGIN",1);
        tokenlocs.put("END",2);
        tokenlocs.put("VAR",3);
        tokenlocs.put("FUNCTION",4);
        tokenlocs.put("PROCEDURE",5);
        tokenlocs.put("RESULT",6);
        tokenlocs.put("INTEGER",7);
        tokenlocs.put("REAL",8);
        tokenlocs.put("ARRAY",9);
        tokenlocs.put("OF",10);
        tokenlocs.put("IF",11);
        tokenlocs.put("THEN",12);
        tokenlocs.put("ELSE",13);
        tokenlocs.put("WHILE",14);
        tokenlocs.put("DO",15);
        tokenlocs.put("NOT",16);
        tokenlocs.put("IDENTIFIER",17);
        tokenlocs.put("INTCONSTANT",18);
        tokenlocs.put("REALCONSTANT",19);
        tokenlocs.put("RELOP",20);
        tokenlocs.put("MULOP",21);
        tokenlocs.put("ADDOP",22);
        tokenlocs.put("ASSIGNOP",23);
        tokenlocs.put("COMMA",24);
        tokenlocs.put("SEMICOLON",25);
        tokenlocs.put("COLON",26);
        tokenlocs.put("LPAREN",27);
        tokenlocs.put("RPAREN",28);
        tokenlocs.put("LBRACKET",29);
        tokenlocs.put("RBRACKET",30);
        tokenlocs.put("UNARYMINUS",31);
        tokenlocs.put("UNARYPLUS",32);
        tokenlocs.put("DOUBLEDOT",33);
        tokenlocs.put("ENDMARKER",34);
    }

    // Runs the parser. It initializes everything used in the parser, and until the end of the file goes through and
    // checks what should be done next (end of file, terminal, action, or not terminal), depending on the current token
    // and the top of parse stack.
    public void MainParser(SemanticAction semacts, LexicalAnalyser lexer) throws ParserError, LexicalError, SymbolTableError, SemanticError
    {
        Initialize(semacts, lexer);
        curtoken = lexan.GetNextToken();
        prevtoken = curtoken;

        while (result.equals("nan"))
        {
            if (seeparsestack)
            {
                DumpStack();
            }

            x = -1;
            y = -1;

            // is end of file

            if (CheckEndOfFile())
            {
                IsEndOfFile();
            }
            // is terminal

            else if (CheckTerminal())
            {
                IsTerminal();
            }
            // is action
            else if (CheckAction())
            {
                IsAction();
            }
            // is not terminal
            else
            {
                IsNotTerminal();
            }
        }
    }

    // Prints out the contents of the parse stack.
    private void DumpStack()
    {
        Stack<String> tempstack = new Stack<>();
        String tempval;

        System.out.println("Parser Stack:");

        // Print a horizontal parse stack

        if (horizontaldump)
        {
            tempval = pstack.pop();
            System.out.print(tempval);
            tempstack.push(tempval);

            while (!(pstack.empty()))
            {
                tempval = pstack.pop();
                System.out.print(", " + tempval);
                tempstack.push(tempval);
            }

            System.out.println();
        }

        // Otherwise print it vertically

        else
        {
            while (!(pstack.empty())) {
                tempval = pstack.pop();
                System.out.println(tempval);
                tempstack.push(tempval);
            }
        }

        while (!(tempstack.empty()))
        {
            pstack.push(tempstack.pop());
        }

        System.out.println();
    }

    // Checks if the end of the file has been reached on the stack.
    private Boolean CheckEndOfFile()
    {
        return pstack.peek().equals("ENDMARKER") && pstack.peek().equals(curtoken.GetName());
    }

    // Returns that the parser worked, and sets the result of the parser to done.
    private void IsEndOfFile()
    {
        pstack.pop();

        // Dumps the final parse stack

        if (seeparsestack)
        {
            DumpStack();
        }

        System.out.println("Parser worked");
        System.out.println();
        result = "done";
    }

    // Checks if a terminal has been reached on the stack.
    private Boolean CheckTerminal()
    {
        return pstack.peek().equals(curtoken.GetName());
    }

    // Pops the terminal off of the stack, updates the previous token to be the current token, and then gets a new token
    // as the current token.
    private void IsTerminal() throws LexicalError
    {
        pstack.pop();
        prevtoken = curtoken;

        if (!(prevtoken.equals("ENDMARKER")))
        {
            curtoken = lexan.GetNextToken();
        }
    }

    // Checks if a semantic action has been reached on the stack.
    private Boolean CheckAction()
    {
        return pstack.peek().charAt(0) == '#';
    }

    // Runs the semantic action if a semantic action on the stack has been reached.
    private void IsAction() throws SymbolTableError, SemanticError
    {
        String index = pstack.pop();
        semact.Execute(index, prevtoken, lexan.linenum);
    }

    // Replaces a non-terminal with the RHS-table table production for the current top of the stack and the current token.
    private void IsNotTerminal() throws ParserError
    {
        y = rhslocations.get(pstack.peek());
        x = tokenlocs.get(curtoken.GetName());
        prodruleloc = Math.abs(parsetable.GetValue(x, y));

        // If either the RHS table or the parse table do not have current top of the stack and current token respectively,
        // throws an error

        if (x == -1 || y == -1)
        {
            throw new ParserError("Invalid RHS location or parse table location", lexan.linenum);
        }

        // If the parse table returns 999, returns an error

        else if (prodruleloc == 999)
        {
            throw new ParserError("Invalid parse table combination of token and top of parse stack", lexan.linenum);
        }
        else
        {
            pstack.pop();

            if (rhstable.GetValues(prodruleloc).length > 0)
            {
                for (int i = rhstable.GetValues(prodruleloc).length - 1;  i >= 0;  i--)
                {
                    pstack.push(rhstable.GetValues(prodruleloc)[i]);
                }
            }
        }
    }
}
