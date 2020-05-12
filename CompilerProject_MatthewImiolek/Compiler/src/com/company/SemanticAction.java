package com.company;

import java.util.*;

/* Used to create a semantic action object, which using all of the semantic actions generates the intermediate code
   the vassar interpreter can use (TVI). It uses a combination of tokens to provide new information, and symbol tables
   storing information, in order to generate the correct code. */

public class SemanticAction
{
    private Boolean seesemstack = false;
    private Boolean dumpglobal = false;
    private Boolean dumploc = false;
    private Boolean dumpconst = false;
    private Boolean printtvi = false;
    private Boolean insert = true;
    private Boolean global = true;
    private Boolean array = false;
    private int globmem = 0;
    private int locmem = 0;
    private int tablesize = 128;
    private int globalstore = 0;
    private int tempcounter = 0;
    private int errorline;
    private int localstore;
    private int nextparam;
    private Stack<Object> sstack = new Stack<>();
    private Stack<Integer> paramcount;
    private Stack<LinkedList> paramstack = new Stack<>();
    private SymbolTable globaltable = new SymbolTable(tablesize);
    private SymbolTable localtable = new SymbolTable(tablesize);
    private SymbolTable constanttable = new SymbolTable(tablesize);
    private SymbolTableEntry currentfunction;
    Quadruples alltvi = new Quadruples();

    // Creates the semantic actions object, and puts the initial entries in the global table.
    SemanticAction() throws SymbolTableError
    {
        globaltable.Insert(new ProcedureEntry("MAIN", 0, null));
        globaltable.Insert(new ProcedureEntry("read", 0, null));
        globaltable.Insert(new ProcedureEntry("write", 0, null));
    }

    // Executes the correct semantic action, and dumps the stack when needed.
    public void Execute(String action, Token prevtok, int errorline) throws SymbolTableError, SemanticError
    {
        this.errorline = errorline;

        switch (action)
        {
            case "#1":
                Act1();
                break;
            case "#2":
                Act2();
                break;
            case "#3":
                Act3();
                break;
            case "#4":
                Act4(prevtok);
                break;
            case "#5":
                Act5();
                break;
            case "#6":
                Act6();
                break;
            case "#7":
                Act7(prevtok);
                break;
            case "#9":
                Act9();
                break;
            case "#11":
                Act11();
                break;
            case "#13":
                Act13(prevtok);
                break;
            case "#15":
                Act15(prevtok);
                break;
            case "#16":
                Act16();
                break;
            case "#17":
                Act17(prevtok);
                break;
            case "#19":
                Act19();
                break;
            case "#20":
                Act20();
                break;
            case "#21":
                Act21();
                break;
            case "#22":
                Act22();
                break;
            case "#24":
                Act24();
                break;
            case "#25":
                Act25();
                break;
            case "#26":
                Act26();
                break;
            case "#27":
                Act27();
                break;
            case "#28":
                Act28();
                break;
            case "#29":
                Act29();
                break;
            case "#30":
                Act30(prevtok);
                break;
            case "#31":
                Act31();
                break;
            case "#32":
                Act32();
                break;
            case "#33":
                Act33();
                break;
            case "#34":
                Act34(prevtok);
                break;
            case "#35":
                Act35();
                break;
            case "#36":
                Act36();
                break;
            case "#37":
                Act37();
                break;
            case "#38":
                Act38(prevtok);
                break;
            case "#39":
                Act39();
                break;
            case "#40":
                Act40(prevtok);
                break;
            case "#41":
                Act41();
                break;
            case "#42":
                Act42(prevtok);
                break;
            case "#43":
                Act43();
                break;
            case "#44":
                Act44(prevtok);
                break;
            case "#45":
                Act45();
                break;
            case "#46":
                Act46(prevtok);
                break;
            case "#47":
                Act47();
                break;
            case "#48":
                Act48(prevtok);
                break;
            case "#49":
                Act49();
                break;
            case "#50":
                Act50();
                break;
            case "#51":
                Act51(prevtok);
                break;
            case "#51WRITE":
                Act51Write();
                break;
            case "#51READ":
                Act51Read();
                break;
            case "#52":
                Act52();
                break;
            case "#53":
                Act53();
                break;
            case "#54":
                Act54();
                break;
            case "#55":
                Act55();
                break;
            case "#56":
                Act56();
                break;
            default:
                Finale(action);
                break;
        }

        if (seesemstack)
        {
            DumpStack();
        }
    }

    // Sets the actions to insertion mode for the symbol tables.
    private void Act1()
    {
        insert = true;
    }

    // Sets the actions to search mode for the symbol tables.
    private void Act2()
    {
        insert = false;
    }

    // Creates either an array entry or a variable entry based off of the value of boolean array using tokens which have
    // been pushed on the stack, and inserts them into the global or local symbol table based on the value of global.
    private void Act3() throws SymbolTableError
    {
        Token typetoken = (Token) sstack.pop();
        String type = typetoken.GetName();

        //If array is true, creates an array

        if (array)
        {
            int upperbound = TokenToInt((Token) sstack.pop());
            int lowerbound = TokenToInt((Token) sstack.pop());
            int memorysize = (upperbound - lowerbound) + 1;

            while (!(sstack.empty()) && (sstack.peek() instanceof Token) && ((Token) sstack.peek()).GetName().equals("IDENTIFIER"))
            {
                Token tok = (Token) sstack.pop();
                String name = tok.GetValue();
                int address;

                //If global is true, adds the array to the global symbol table and updates the memory usage of global

                if (global)
                {
                    address = globmem;
                    ArrayEntry id = new ArrayEntry(name, address, type, upperbound, lowerbound);
                    globaltable.Insert(id);
                    globmem += memorysize;
                }

                //Otherwise adds the array to the local symbol table and updates the memory usage of local

                else
                {
                    address = locmem;
                    ArrayEntry id = new ArrayEntry(name, address, type, upperbound, lowerbound);
                    localtable.Insert(id);
                    locmem += memorysize;
                }

            }
        }

        //Otherwise creates a variable

        else
        {
            while (!(sstack.empty()) && (sstack.peek() instanceof Token) && ((Token) sstack.peek()).GetName().equals("IDENTIFIER"))
            {
                Token tok = (Token) sstack.pop();
                String name = tok.GetValue();
                int address;

                //If global is true, adds the variable to the global symbol table and updates the memory usage of global

                if (global)
                {
                    address = globmem;
                    VariableEntry id = new VariableEntry(name, address, type);
                    globaltable.Insert(id);
                    globmem++;
                }

                //Otherwise, adds the variable to the local symbol table and updates the memory usage of local

                else
                {
                    address = locmem;
                    VariableEntry id = new VariableEntry(name, address, type);
                    localtable.Insert(id);
                    locmem++;
                }
            }
        }

        array = false;
    }

    // Pushes a token, which should be a type (integer or real), on the stack.
    private void Act4(Token prevtok)
    {
        sstack.push(prevtok);
    }

    // Sets up the beginning of the TVI code for a function or procedure that is not main. Creates the PROCBEGIN line
    // and the alloc line, while also keeping track of the location of the alloc statement so it can be backpatched.
    private void Act5()
    {
        insert = false;
        SymbolTableEntry id = (SymbolTableEntry) sstack.pop();
        Generate("PROCBEGIN", id.GetName());

        localstore = alltvi.GetNextQuad();
        Generate("alloc", "_");
    }

    // Sets array to true, so the next variable should be treated as an array.
    private void Act6()
    {
        array = true;
    }

    // Pushes an integer identifier onto the stack, which represents the upper and lower bounds of an array.
    private void Act7(Token prevtok)
    {
        sstack.push(prevtok);
    }

    // Sets up the very beginning of the TVI code, adding the programs input and output identifiers as IODevices to the
    // global table, and the program's name identifier as a procedure to the global table. It also generates the first
    // two lines of TVI code.
    private void Act9() throws SymbolTableError
    {
        Token id1 = (Token) sstack.pop();
        Token id2 = (Token) sstack.pop();
        Token id3 =  (Token) sstack.pop();

        globaltable.Insert(new IODeviceEntry(id1.GetValue()));
        globaltable.Insert(new IODeviceEntry(id2.GetValue()));
        globaltable.Insert(new ProcedureEntry(id3.GetValue(), 0, null));

        insert = false;

        Generate("call", "main", "0");
        Generate("exit");
    }

    // Ends the TVI code for a function or procedure that is not main. Begins preparing for a new function or procedure,
    // and generates the free and PROCEND TVI code.
    private void Act11()
    {
        global = true;
        localtable = new SymbolTable(tablesize);
        currentfunction = null;
        Backpatch(localstore, locmem);
        Generate("free", locmem);
        Generate("PROCEND");
    }

    // Pushes an identifier token onto the stack.
    private void Act13(Token prevtok)
    {
        sstack.push(prevtok);
    }

    // Creates a new function entry, which is given a new variable entry of type integer as its result. It also sets
    // the semantic actions to work in local memory.
    private void Act15(Token prevtok) throws SymbolTableError
    {
        VariableEntry result = Create(prevtok.GetValue() + "_RESULT", "INTEGER");
        result.SetIsResult(true);

        FunctionEntry id = new FunctionEntry(prevtok.GetValue(), result);

        globaltable.Insert(id);
        global = false;
        locmem = 0;
        currentfunction = id;
        sstack.push(id);
    }

    // Sets the type of a function and its result.
    private void Act16()
    {
        Token type = (Token) sstack.pop();
        FunctionEntry id = (FunctionEntry) sstack.peek();
        id.SetType(type.GetName());
        id.SetResultType(type.GetName());
        id.GetResult().SetType(type.GetName());
        currentfunction = id;
    }

    // Creates a new procedure entry with the name of the token from the parser. It also sets the semantic actions to
    // work in local memory.
    private void Act17(Token prevtok) throws SymbolTableError
    {
        ProcedureEntry id = new ProcedureEntry(prevtok.GetValue());
        globaltable.Insert(id);
        global = false;
        locmem = 0;
        currentfunction = id;
        sstack.push(id);
    }

    // Initializes paramcount, and begins counting parameters.
    private void Act19()
    {
        paramcount = new Stack<>();
        paramcount.push(0);
    }

    // Sets the number of parameters of a function or parameter entry to paramcount.
    private void Act20()
    {
        SymbolTableEntry id = (SymbolTableEntry) sstack.peek();
        int numparams = paramcount.pop();
        id.SetNumberOfParameters(numparams);
    }

    // Adds the parameters to a given function or procedure entry.
    private void Act21() throws SymbolTableError
    {
        Token type = (Token) sstack.pop();
        int upperbound = -1;
        int lowerbound = -1;

        // If it is an array, pop the upper and lower bounds

        if (array)
        {
            upperbound = TokenToInt((Token) sstack.pop());
            lowerbound = TokenToInt((Token) sstack.pop());
        }

        Stack<Token> parameters = new Stack<>();

        // As the identifiers are popped of the stack, push them onto a new stack in reverse order, so they can be added
        // popped from that stack and added as parameters in the right order

        while (!(sstack.empty()) && (sstack.peek() instanceof Token) && ((Token) sstack.peek()).GetName().equals("IDENTIFIER"))
        {
            Token temp = (Token) sstack.pop();
            parameters.push(temp);
        }

        // Add the parameters

        while (!parameters.empty())
        {
            Token param = parameters.pop();

            if (array)
            {
                ArrayEntry var = new ArrayEntry(param.GetValue(), locmem, type.GetName(), upperbound, lowerbound);
                var.SetIsParam(true);
                localtable.Insert(var);
                currentfunction.AddParam(var);
                locmem++;
                paramcount.push(paramcount.pop() + 1);
            }
            else
            {
                VariableEntry var = new VariableEntry(param.GetValue(), locmem, type.GetName());
                var.SetIsParam(true);
                localtable.Insert(var);
                currentfunction.AddParam(var);
                locmem++;
                paramcount.push(paramcount.pop() + 1);
            }
        }
        array = false;
    }

    // Checks that the expression for an if statement is relational, and backpatches where to go if the expression is
    // true.
    private void Act22() throws SemanticError
    {
        EType etype = (EType) sstack.pop();

        if (etype != EType.RELATIONAL)
        {
            throw new SemanticError("EType mismatch error", errorline);
        }

        List<Integer> efalse = (List) sstack.pop();
        List<Integer> etrue = (List) sstack.pop();
        Backpatch(etrue, alltvi.GetNextQuad());
        sstack.push(etrue);
        sstack.push(efalse);
    }

    // Pushes where a loop starts onto the stack.
    private void Act24()
    {
        int beginloop = alltvi.GetNextQuad();
        sstack.push(beginloop);
    }

    // Checks that the expression for a loop is relational, and backpatches where to go if the expression is true.
    private void Act25() throws SemanticError
    {
        EType etype = (EType) sstack.pop();

        if (etype != EType.RELATIONAL)
        {
            throw new SemanticError("EType mismatch error", errorline);
        }

        List<Integer> efalse = (List<Integer>) sstack.pop();
        List<Integer> etrue = (List<Integer>) sstack.pop();
        Backpatch(etrue, alltvi.GetNextQuad());
        sstack.push(etrue);
        sstack.push(efalse);
    }

    // Creates the goto that re-starts the loop, and backpatches where to go if the loop expression is false.
    private void Act26()
    {
        List<Integer> efalse = (List<Integer>) sstack.pop();
        List<Integer> etrue = (List<Integer>) sstack.pop();

        int beginLoop = (int) sstack.pop();
        Generate("goto", beginLoop);
        Backpatch(efalse, alltvi.GetNextQuad());
    }

    // Generates the code to skip the else of an else statement, and backpatches where to go if the if expression is
    // false.
    private void Act27()
    {
        List<Integer> skipelse = MakeList(alltvi.GetNextQuad());
        Generate("goto", "_");
        List<Integer> efalse = (List<Integer>) sstack.pop();
        List<Integer> etrue = (List<Integer>) sstack.pop();
        Backpatch(efalse, alltvi.GetNextQuad());
        sstack.push(skipelse);
        sstack.push(etrue);
        sstack.push(efalse);
    }

    // Backpatches where to go if the else statement is skipped.
    private void Act28()
    {
        List<Integer> efalse = (List<Integer>) sstack.pop();
        List<Integer> etrue = (List<Integer>) sstack.pop();
        List<Integer> skipelse = (List<Integer>) sstack.pop();
        Backpatch(skipelse, alltvi.GetNextQuad());
    }

    // Backpatches where to go if there is no else, but the if is false.
    private void Act29()
    {
        List<Integer> efalse = (List<Integer>) sstack.pop();
        List<Integer> etrue = (List<Integer>) sstack.pop();
        Backpatch(efalse, alltvi.GetNextQuad());
    }

    // Checks if an identifier has been declared prior to use in a statement.
    private void Act30(Token prevtok) throws SemanticError
    {
        SymbolTableEntry id = LookupID(prevtok);

        if (id == null)
        {
            throw new SemanticError("Undeclared Variable Error", errorline);
        }
        sstack.push(id);
        sstack.push(EType.ARITHMETIC);
    }

    // Generates the TVI code for the results of arithmetic expressions.
    private void Act31() throws SymbolTableError, SemanticError
    {
        EType etype = (EType) sstack.pop();

        // Throws an error for a non-arithmetic expression

        if (etype != EType.ARITHMETIC)
        {
            throw new SemanticError("EType mismatch error", errorline);
        }
        SymbolTableEntry id2 = (SymbolTableEntry) sstack.pop();
        SymbolTableEntry offset  = (SymbolTableEntry) sstack.pop();
        SymbolTableEntry id1 = (SymbolTableEntry) sstack.pop();

        // Throws an error if the operands are ordered integer then real

        if (TypeCheck(id1, id2) == 3)
        {
            throw new SemanticError("Type Mismatch Error", errorline);
        }

        // Converts the integer into a real if the order of operands is real then integer

        if (TypeCheck(id1, id2) == 2)
        {
            VariableEntry temp = Create("temp" + tempcounter, "REAL");
            tempcounter++;
            Generate("ltof", id2, temp);

            if (offset == null)
            {
                Generate("move", temp, id1);
            }
            else
            {
                Generate("stor", temp, offset, id1);
            }
        }
        else
        {
            if (offset == null)
            {
                Generate("move", id2, id1);
            }
            else
            {
                Generate("stor", id2, offset, id1);
            }
        }
    }

    // Checks that a subscript is only used for an array, which is used in an arithmetic expression.
    private void Act32() throws SemanticError
    {
        EType etype = (EType) sstack.pop();
        SymbolTableEntry id = (SymbolTableEntry) sstack.peek();

        if (etype != EType.ARITHMETIC)
        {
            throw new SemanticError("Etype mismatch error", errorline);
        }
        if (!(id.GetIsArray()))
        {
            throw new SemanticError("ID is not array", errorline);
        }
    }

    // Checks that the result of an arithmetic expression in the subscript is an integer, and generates the TVI code
    // finding the value at that location.
    private void Act33() throws SemanticError, SymbolTableError
    {
        EType etype = (EType) sstack.pop();

        if (etype != EType.ARITHMETIC)
        {
            throw new SemanticError("Etype mismatch error", errorline);
        }

        SymbolTableEntry id = (SymbolTableEntry) sstack.pop();

        if (!(id.GetType().equals("INTEGER")))
        {
            throw new SemanticError("Type mismatch", errorline);
        }

        ArrayEntry array = (ArrayEntry) sstack.peek();
        VariableEntry temp1 = Create("temp" + tempcounter, "INTEGER");
        tempcounter++;
        VariableEntry temp2 = Create("temp" + tempcounter, "INTEGER");
        tempcounter++;
        Generate("move", array.GetLowerBound(), temp1);
        Generate("sub", id, temp1, temp2);
        sstack.push(temp2);
    }

    // Checks if a symbol table entry is a function, and if it is executes action 52.
    private void Act34(Token prevtok) throws SemanticError, SymbolTableError
    {
        EType etype = (EType) sstack.pop();
        SymbolTableEntry id = (SymbolTableEntry) sstack.peek();

        if (id.GetIsFunction())
        {
            sstack.push(etype);
            Execute("#52", prevtok, errorline);
        }
        else
        {
            sstack.push(null);
        }
    }

    // Pushes the parameter info of a procedure entry on the paramstack.
    private void Act35()
    {
        EType etype = (EType) sstack.pop();
        ProcedureEntry id = (ProcedureEntry) sstack.peek();
        sstack.push(etype);
        paramcount.push(0);
        paramstack.push(id.GetParameterInfo());
    }

    // Generates the call code for a procedure with 0 parameters.
    private void Act36() throws SemanticError
    {
        EType etype = (EType) sstack.pop();
        ProcedureEntry id = (ProcedureEntry) sstack.pop();

        if (id.GetNumberOfParameters() != 0)
        {
            throw new SemanticError("Wrong number of parameters", errorline);
        }
        Generate("call", id.GetName(), 0);
    }

    // Checks that a symbol table entry is a valid parameter for a function or procedure.
    private void Act37() throws SemanticError
    {
        EType etype = (EType) sstack.pop();

        // The equation type must be arithmetic, all parameters must be numerical

        if (etype != EType.ARITHMETIC)
        {
            throw new SemanticError("EType mismatch", errorline);
        }

        SymbolTableEntry id = (SymbolTableEntry) sstack.peek();

        // Checks that the parameter is of a form a function or procedure can use

        if (!(id.GetIsArray() || id.GetIsConstant() || id.GetIsResult() || id.GetIsVariable()))
        {
            throw new SemanticError("Bad parameter", errorline);
        }

        paramcount.push(paramcount.pop() + 1);
        Stack parameters = new Stack();

        // Finds the actual function or procedure to make sure the parameter meets expectations

        while (!((sstack.peek() instanceof ProcedureEntry) || (sstack.peek() instanceof FunctionEntry)))
        {
            parameters.push(sstack.pop());
        }

        SymbolTableEntry funcid = (SymbolTableEntry) sstack.peek();

        // Puts all other parameters back on the stack

        while (!parameters.empty())
        {
            sstack.push(parameters.pop());
        }

        if (!(funcid.GetName().equals("read") || funcid.GetName().equals("write")))
        {

            // Cannot cause the function to exceed the number of parameters it requires

            if (paramcount.peek() > funcid.GetNumberOfParameters())
            {
                throw new SemanticError("Wrong number of parameters", errorline);
            }

            SymbolTableEntry param = (SymbolTableEntry) paramstack.peek().get(nextparam);

            // Makes sure the given parameter has the same type as the expected next parameter

            if (!(id.GetType().equals(param.GetType())))
            {
                throw new SemanticError("Bad parameter", errorline);
            }

            // Checks that if the next parameter should be an array, the given parameter meets those array's needs

            if (param.GetIsArray())
            {
                if ((id.GetLowerBound() != param.GetLowerBound()) || (id.GetUpperBound() != param.GetUpperBound()))
                {
                    throw new SemanticError("Bad parameter", errorline);
                }
            }
            nextparam++;
        }
    }

    // Pushes a token of an operator onto the stack.
    private void Act38(Token prevtok) throws SemanticError
    {
        EType etype = (EType) sstack.pop();

        if (etype != EType.ARITHMETIC)
        {
            throw new SemanticError("EType mismatch error", errorline);
        }
        sstack.push(prevtok);
    }

    // Creates the TVI code for a relational expression.
    private void Act39() throws SemanticError, SymbolTableError
    {
        EType etype = (EType) sstack.pop();

        if (etype != EType.ARITHMETIC)
        {
            throw new SemanticError("EType mismatch error", errorline);
        }

        SymbolTableEntry id2 = (SymbolTableEntry) sstack.pop();
        Token operator = (Token) sstack.pop();
        String opcode = operator.GetValue();
        SymbolTableEntry id1 = (SymbolTableEntry) sstack.pop();

        // Takes the number representing the exact type of relational operator and converts it into the TVI code word
        // that represents that operator

        if (opcode.equals("1"))
        {
            opcode = "beq";
        }
        else if (opcode.equals("2"))
        {
            opcode = "bne";
        }
        else if (opcode.equals("3"))
        {
            opcode = "blt";
        }
        else if (opcode.equals("4"))
        {
            opcode = "bgt";
        }
        else if (opcode.equals("5"))
        {
            opcode = "ble";
        }
        else
        {
            opcode = "bge";
        }

        // If necessary, convert a integer in the expression into a real so the comparison can be done, and then generate
        // the TVI code for the relational expression

        if (TypeCheck(id1, id2) == 2)
        {
            VariableEntry temp = Create("temp" + tempcounter, "REAL");
            tempcounter++;
            Generate("ltof", id2, temp);
            Generate(opcode, id1, temp, "_");
        }
        else if (TypeCheck(id1, id2) == 3)
        {
            VariableEntry temp = Create("temp" + tempcounter, "REAL");
            tempcounter++;
            Generate("ltof", id1, temp);
            Generate(opcode, temp, id2, "_");
        }

        // If not, just generate the TVI code for the relational expression

        else
        {
            Generate(opcode, id1, id2, "_");
        }

        Generate("goto", "_");

        List<Integer> etrue = MakeList(alltvi.GetNextQuad() - 2);
        List<Integer> efalse = MakeList(alltvi.GetNextQuad() - 1);

        sstack.push(etrue);
        sstack.push(efalse);
        sstack.push(EType.RELATIONAL);
    }

    // Pushes either a unary minus or plus token onto the stack.
    private void Act40(Token prevtok)
    {
        sstack.push(prevtok);
    }

    // Generates the TVI code for a unary minus.
    private void Act41() throws SymbolTableError, SemanticError
    {
        EType etype = (EType) sstack.pop();

        if (etype != EType.ARITHMETIC)
        {
            throw new SemanticError("EType mismatch error", errorline);
        }

        SymbolTableEntry id = (SymbolTableEntry) sstack.pop();
        Token sign = (Token) sstack.pop();

        System.out.println(sign);

        // Makes sure the sign is a unaryminus, and creates the TVI code if it is

        if(sign.GetName().equals("UNARYMINUS"))
        {
            VariableEntry temp = Create("temp" + tempcounter, id.GetType());
            tempcounter++;

            // Generates the code using uminus if the identifier it is before is an integer, and fuminus if it a real

            if (id.GetType().equals("INTEGER"))
            {
                Generate("uminus", id, temp) ;
            }
            else
            {
                Generate("fuminus", id, temp);
            }

            sstack.push(temp);
        }

        // Otherwise just pushes id back on the stack

        else
        {
            sstack.push(id);
        }
        sstack.push(EType.ARITHMETIC);
    }

    // Checks that in the case of an addop, if the addop is an or the expression is relational, and if it is not the
    // expression is arithmetic.
    private void Act42(Token prevtok) throws SemanticError
    {
        EType etype = (EType) sstack.pop();

        // Checks if there is an addop with a value of 3, which is the equivalent of an or

        if (prevtok.GetName().equals("ADDOP") && prevtok.GetValue().equals("3"))
        {

            // Checks if the expression is a relational expression

            if (etype != EType.RELATIONAL)
            {
                throw new SemanticError("EType mismatch error", errorline);
            }

            List<Integer> efalse = (List<Integer>) sstack.peek();
            Backpatch(efalse, alltvi.GetNextQuad());
        }

        // Otherwise checks if the expression is an arithmetic expression

        else
        {
            if (etype != EType.ARITHMETIC)
            {
                throw new SemanticError("EType mismatch error", errorline);
            }
        }
        sstack.push(prevtok);
    }

    // Creates the TVI code for an addition expression.
    private void Act43() throws SymbolTableError
    {
        EType etype = (EType) sstack.pop();

        // In the case the expression is a relational expression, the operator must be an or, and can therefore combine
        // what makes each operand of the expression true as if any of that is true the whole statement is true, and
        // only use the false of the second operand as if that is false, it implies the first operand was not true
        // without explicitly checking if it was false

        if (etype == EType.RELATIONAL)
        {
            List<Integer> e2false = (List<Integer>) sstack.pop();
            List<Integer> e2true = (List<Integer>) sstack.pop();
            Token operator = (Token) sstack.pop();
            List<Integer> e1false = (List<Integer>) sstack.pop();
            List<Integer> e1true = (List<Integer>) sstack.pop();

            List<Integer> etrue = Merge(e1true, e2true);
            List<Integer> efalse = e2false;
            sstack.push(etrue);
            sstack.push(efalse);
            sstack.push(EType.RELATIONAL);
        }
        else
        {
            SymbolTableEntry id2 = (SymbolTableEntry) sstack.pop();
            Token operator = (Token) sstack.pop();
            String opcode = operator.GetValue();
            SymbolTableEntry id1 = (SymbolTableEntry) sstack.pop();

            // Takes the number representing the exact type of addition operator and converts it into the TVI code word
            // that represents that operator

            if (opcode.equals("1"))
            {
                opcode = "add";
            }
            else if (opcode.equals("2"))
            {
                opcode = "sub";
            }
            else
            {
                opcode = "or";
            }

            // Generates the correct TVI code, depending on what type each of the operands was, and therefore what
            // integer to real conversion may have been necessary.

            if (TypeCheck(id1, id2) == 0)
            {
                VariableEntry temp = Create("temp" + tempcounter, "INTEGER");
                tempcounter++;
                Generate(opcode, id1, id2, temp);
                sstack.push(temp);
            }
            else if (TypeCheck(id1, id2) == 1)
            {
                VariableEntry temp = Create("temp" + tempcounter, "REAL");
                tempcounter++;
                Generate("f" + opcode, id1, id2, temp);
                sstack.push(temp);
            }
            else if (TypeCheck(id1, id2) == 2)
            {
                VariableEntry temp1 = Create("temp" + tempcounter, "REAL");
                tempcounter++;
                VariableEntry temp2 = Create("temp" + tempcounter, "REAL");
                tempcounter++;
                Generate("ltof", id2, temp1);
                Generate("f" + opcode, id1, temp1, temp2);
                sstack.push(temp2);
            }
            else if (TypeCheck(id1, id2) == 3)
            {
                VariableEntry temp1 = Create("temp" + tempcounter, "REAL");
                tempcounter++;
                VariableEntry temp2 = Create("temp" + tempcounter, "REAL");
                tempcounter++;
                Generate("ltof", id1, temp1);
                Generate("f" + opcode, temp1, id2, temp2);
                sstack.push(temp2);
            }

            sstack.push(EType.ARITHMETIC);
        }
    }

    // Checks that in the case of a mulop, if the mulop is an and the expression is relational, and if it is not the
    // expression is arithmetic.
    private void Act44(Token prevtok)
    {
        EType etype = (EType) sstack.pop();

        // Checks if the expression is a relational expression

        if (etype == EType.RELATIONAL)
        {
            List<Integer> efalse = (List<Integer>) sstack.pop();
            List<Integer> etrue = (List<Integer>) sstack.pop();

            // Checks if there is an mulop with a value of 5, which is the equivalent of an and

            if (prevtok.GetName().equals("MULOP") && prevtok.GetValue().equals("5"))
            {
                Backpatch(etrue, alltvi.GetNextQuad());
            }
            sstack.push(etrue);
            sstack.push(efalse);
        }

        sstack.push(prevtok);
    }

    // Creates the TVI code for an mulop expression.
    private void Act45() throws SymbolTableError, SemanticError
    {
        EType etype = (EType) sstack.pop();

        // In the case the expression is a relational expression, the operator must be an and, and can therefore combine
        // what makes each operand of the expression false as if any of that is true the whole statement is false, and
        // only use the true of the second operand as if that is true, it implies the first operand was true
        // without explicitly checking if it was true

        if (etype == EType.RELATIONAL)
        {
            List<Integer> e2false = (List<Integer>) sstack.pop();
            List<Integer> e2true = (List<Integer>) sstack.pop();
            Token operator = (Token) sstack.pop();

            if (operator.GetName().equals("MULOP") && operator.GetValue().equals("5"))
            {
                List<Integer> e1false = (List<Integer>) sstack.pop();
                List<Integer> e1true = (List<Integer>) sstack.pop();

                List<Integer> etrue = e2true;
                List<Integer> efalse = Merge(e1false, e2false);
                sstack.push(etrue);
                sstack.push(efalse);
                sstack.push(EType.RELATIONAL);
            }
        }
        else
        {
            SymbolTableEntry id2 = (SymbolTableEntry) sstack.pop();
            Token operator = (Token) sstack.pop();
            String opcode = operator.GetValue();
            SymbolTableEntry id1 = (SymbolTableEntry) sstack.pop();

            // Cannot use DIV and MOD without having two integers

            if (TypeCheck(id1, id2) != 0 && (operator.GetName().equals("MULOP") && (operator.GetValue().equals("3") || operator.GetValue().equals("4"))))
            {
                throw new SemanticError("Bad Parameter Error", errorline);
            }

            // Generates the correct TVI code, depending on what type each of the operands was, and therefore what
            // integer to real conversions may have been necessary, and if multiple, more basic operations are required
            // to get the correct result

            if (TypeCheck(id1, id2) == 0)
            {
                if (operator.GetValue().equals("4"))
                {
                    VariableEntry temp1 = Create("temp" + tempcounter, "INTEGER");
                    tempcounter++;
                    VariableEntry temp2 = Create("temp" + tempcounter, "INTEGER");
                    tempcounter++;
                    VariableEntry temp3 = Create("temp" + tempcounter, "INTEGER");
                    tempcounter++;

                    Generate("div", id1, id2, temp1);
                    Generate("mul", id2, temp1, temp2);
                    Generate("sub", id1, temp2, temp3);

                    sstack.push(temp3);
                }
                else if (operator.GetValue().equals("2"))
                {
                    VariableEntry temp1 = Create("temp" + tempcounter, "REAL");
                    tempcounter++;
                    VariableEntry temp2 = Create("temp" + tempcounter, "REAL");
                    tempcounter++;
                    VariableEntry temp3 = Create("temp" + tempcounter, "REAL");
                    tempcounter++;
                    Generate("ltof", id1, temp1);
                    Generate("ltof", id2, temp2);
                    Generate("fdiv", temp1, temp2, temp3);
                    sstack.push(temp3);
                }
                else
                {
                    VariableEntry temp = Create("temp" + tempcounter, "INTEGER");
                    tempcounter++;

                    opcode = GetMulopWord(opcode);

                    Generate(opcode, id1, id2, temp);

                    sstack.push(temp);
                }
            }
            else if (TypeCheck(id1, id2) == 1)
            {
                opcode = GetMulopWord(opcode);
                VariableEntry temp = Create("temp" + tempcounter, "REAL");
                tempcounter++;
                Generate("f" + opcode, id2, id1, temp);
                sstack.push(temp);
            }
            else if (TypeCheck(id1, id2) == 2)
            {
                opcode = GetMulopWord(opcode);
                VariableEntry temp1 = Create("temp" + tempcounter, "REAL");
                tempcounter++;
                VariableEntry temp2 = Create("temp" + tempcounter, "REAL");
                tempcounter++;
                Generate("ltof", id2, temp1);
                Generate("f" + opcode, id1, temp1, temp2);
                sstack.push(temp2);
            }
            else if (TypeCheck(id1, id2) == 3)
            {
                opcode = GetMulopWord(opcode);
                VariableEntry temp1 = Create("temp" + tempcounter, "REAL");
                tempcounter++;
                VariableEntry temp2 = Create("temp" + tempcounter, "REAL");
                tempcounter++;
                Generate("ltof", id1, temp1);
                Generate("f" + opcode, temp1, id2, temp2);
                sstack.push(temp2);
            }

            sstack.push(EType.ARITHMETIC);
        }
    }

    // Checks if identifier tokens have been declared earlier and can be used in an expression, or if a constant is
    // input, adds that constant to the constant table
    private void Act46(Token prevtok) throws SymbolTableError, SemanticError
    {

        // Checks if a token is an identifier, and if it is checks if it has been declared

        if (prevtok.GetName().equals("IDENTIFIER"))
        {

            // Checks of the identifier has been declared

            SymbolTableEntry id = LookupID(prevtok);

            // If it has not throws an error

            if (id == null)
            {
                throw new SemanticError("Undeclared Variable Error", errorline);
            }

            sstack.push(id);
        }

        // Checks if a token is a constant (either integer or real), and if it is checks if it already exists in the
        // constant table

        else if (prevtok.GetName().equals("INTCONSTANT") || prevtok.GetName().equals("REALCONSTANT"))
        {
            SymbolTableEntry id = LookupConstant(prevtok);

            // If the constant has not been added to the constant table, creates a new constant entry to add to the table

            if (id == null)
            {
                if (prevtok.GetName().equals("INTCONSTANT"))
                {
                    id = new ConstantEntry(prevtok.GetValue(), "INTEGER");
                }
                else if (prevtok.GetName().equals("REALCONSTANT"))
                {
                    id = new ConstantEntry(prevtok.GetValue(), "REAL");
                }
                constanttable.Insert(id);
            }
            sstack.push(id);
        }
        sstack.push(EType.ARITHMETIC);
    }

    // Checks an expression is relational and then switches the order of what to do if it's true and if it's false on
    // the stack.
    private void Act47() throws SemanticError
    {
        EType etype = (EType) sstack.pop();

        if (etype != EType.RELATIONAL)
        {
            throw new SemanticError("EType mismatch error", errorline);
        }

        List<Integer> efalse = (List<Integer>) sstack.pop();
        List<Integer> etrue = (List<Integer>) sstack.pop();

        sstack.push(efalse);
        sstack.push(etrue);
        sstack.push(EType.RELATIONAL);
    }

    // If offset is a function, executes action 52, otherwise gets the memory address of a member of an array
    private void Act48(Token prevtok) throws SymbolTableError, SemanticError
    {
        SymbolTableEntry offset = (SymbolTableEntry) sstack.pop();

        if (offset != null)
        {

            // If offset is a function executes action 52

            if (offset.GetIsFunction())
            {
                Execute("#52", prevtok, errorline);
            }

            // Otherwise finds the original memory address of an array, adds the offset to it to get the memory address
            // of a member of the array, and returns a load of that memory address

            else
            {
                SymbolTableEntry id = (SymbolTableEntry) sstack.pop();
                VariableEntry temp = Create("temp" + tempcounter, id.type);
                tempcounter++;
                Generate("load", id, offset, temp);
                sstack.push(temp);
            }
        }
        sstack.push(EType.ARITHMETIC);
    }

    // Checks for a valid, arithmetic function and pushes its parameters on the stack
    private void Act49() throws SemanticError
    {
        EType etype = (EType) sstack.pop();
        SymbolTableEntry id = (SymbolTableEntry) sstack.peek();
        sstack.push(etype);

        if (etype != EType.ARITHMETIC)
        {
            throw new SemanticError("EType mismatch error", errorline);
        }

        if (!(id.GetIsFunction()))
        {
            throw new SemanticError("Id is not a function", errorline);
        }
        paramcount.push(0);
        paramstack.push(((FunctionEntry)id).GetParameterInfo());
    }

    // generates the TVI code for each argument of a function used as part of an expression, and the call to that
    // function.
    private void Act50() throws SymbolTableError, SemanticError
    {
        Stack<SymbolTableEntry> parameters = new Stack<>();

        // Puts all of the parameters on a new stack in reverse order

        while ((sstack.peek() instanceof ArrayEntry) || (sstack.peek() instanceof ConstantEntry) || (sstack.peek() instanceof VariableEntry))
        {
            parameters.push((SymbolTableEntry) sstack.pop());
        }

        // Generates the TVI code for each argument

        while (!parameters.empty())
        {
            String prefix = GetParamPrefix(parameters.peek());
            Generate("param", parameters.pop(),prefix);
            locmem++;
        }

        EType etype = (EType) sstack.pop();
        FunctionEntry id = (FunctionEntry) sstack.pop();
        int numparams = paramcount.pop();

        // Makes sure a function has the right number of parameters

        if (numparams > id.GetNumberOfParameters())
        {
            throw new SemanticError("Wrong number of parameters", errorline);
        }

        // Generates the TVI code for the call

        Generate("call", id.GetName(), numparams);
        paramstack.pop();
        nextparam = 0;

        VariableEntry temp = Create("temp" + tempcounter, id.GetResult().GetType());
        tempcounter++;
        Generate("move", id.GetResult(), temp);
        sstack.push(temp);
        sstack.push(EType.ARITHMETIC);
    }

    // Checks if a procedure is read or write, and executes 51Read or 51Write respectively if it is, otherwise generates
    // the TVI code for each argument of a procedure and the call to that procedure.
    private void Act51(Token prevtok) throws SemanticError,SymbolTableError
    {
        Stack<SymbolTableEntry> parameters = new Stack<>();

        // Puts all of the parameters on a new stack in reverse order

        while ((sstack.peek() instanceof ArrayEntry) || (sstack.peek() instanceof ConstantEntry) || (sstack.peek() instanceof VariableEntry))
        {
            parameters.push((SymbolTableEntry) sstack.pop());
        }

        EType etype = (EType) sstack.pop();
        ProcedureEntry id = (ProcedureEntry) sstack.pop();

        // Calls read or write if that is the function

        if (id.GetName().equals("read") || id.GetName().equals("write"))
        {
            sstack.push(id);
            sstack.push(etype);

            // Replace everything on the stack

            while (! parameters.empty())
            {
                sstack.push(parameters.pop());
            }

            // Call read

            if (id.GetName().equals("read"))
            {
                Execute("#51READ", prevtok, errorline);
            }

            // Call write

            else
            {
                Execute("#51WRITE", prevtok, errorline);
            }
        }

        // Otherwise generates the TVI code for each argument on the parameter stack, and a call for the procedure they
        // are part of

        else
        {
            int numparams = paramcount.pop();

            if (numparams != id.GetNumberOfParameters())
            {
                throw new SemanticError("Wrong number of parameters", errorline);
            }

            while (! parameters.empty())
            {
                String prefix = GetParamPrefix(parameters.peek());
                Generate("param", parameters.pop(),prefix);
                locmem++;
            }
            Generate("call", id.GetName(), numparams);
            paramstack.pop();
            nextparam = 0;
        }
    }

    // Generates the TVI code for a read in the given code to compile, what would take input.
    private void Act51Read() throws SymbolTableError
    {
        Stack<SymbolTableEntry> parameters = new Stack<>();

        // Puts all variable entries, which are parameters, on another stack in reverse order

        while (sstack.peek() instanceof VariableEntry)
        {
            parameters.push((SymbolTableEntry) sstack.pop());
        }

        // Generates the input TVI code

        while (! parameters.empty())
        {
            SymbolTableEntry id = parameters.pop();

            if (id.GetType().equals("REAL"))
            {
                Generate("finp", id);
            }
            else
            {
                Generate("inp", id);
            }
        }
        EType etype = (EType) sstack.pop();
        SymbolTableEntry id = (SymbolTableEntry) sstack.pop();
        paramcount.pop();
    }

    // Generates the TVI code for a write in the given code to compile, what would create output.
    private void Act51Write() throws SymbolTableError
    {
        Stack<SymbolTableEntry> parameters = new Stack<>();

        // Puts any variables or constants on the stack on a new stack in reverse order

        while ((sstack.peek() instanceof ConstantEntry) || (sstack.peek() instanceof VariableEntry))
        {
            parameters.push((SymbolTableEntry) sstack.pop());
        }

        // For each argument in the write, generates the corresponding TVI code

        while (! parameters.empty())
        {
            SymbolTableEntry id = parameters.pop();

            if (id.GetIsConstant())
            {
                if (id.GetType().equals("REAL"))
                {
                    Generate("foutp", id.GetName());
                }
                else
                {
                    Generate("outp", id.GetName());
                }
            }
            else
            {
                Generate("print", "\"" + id.GetName() + " = \"");

                if (id.GetType().equals("REAL"))
                {
                    Generate("foutp", id);
                }
                else
                {
                    Generate("outp", id);
                }
            }
            Generate("newl");
        }
        EType etype = (EType) sstack.pop();
        SymbolTableEntry id = (SymbolTableEntry) sstack.pop();
        paramcount.pop();
    }

    // Produces the call and move TVI code for a function that has no parameters.
    private void Act52() throws SemanticError, SymbolTableError
    {
        EType etype = (EType) sstack.pop();
        SymbolTableEntry id = (SymbolTableEntry) sstack.pop();

        if (! (id.GetIsFunction()))
        {
            throw new SemanticError("Id is not a function", errorline);
        }

        if (id.GetNumberOfParameters() > 0)
        {
            throw new SemanticError("Wrong number of parameters", errorline);
        }
        Generate("call", id.GetName(), 0);
        VariableEntry temp = Create("temp", id.GetType());
        Generate("move", id.GetResult(), temp);
        sstack.push(temp);
        sstack.push(null);
    }

    // Checks that inside the body of a function in the code to compile, the only function assigned a value is that
    // function.
    private void Act53() throws SemanticError
    {
        EType etype = (EType) sstack.pop();
        SymbolTableEntry id = (SymbolTableEntry) sstack.pop();

        if (id.GetIsFunction())
        {
            if (id != currentfunction)
            {
                throw new SemanticError("Illegal procedure error", errorline);
            }
            sstack.push(id.GetResult());
            sstack.push(EType.ARITHMETIC);
        }
        else
        {
            sstack.push(id);
            sstack.push(etype);
        }
    }

    // Checks that after an etype on the stack, there is a procedure on the stack.
    private void Act54() throws SemanticError
    {
        EType etype = (EType) sstack.pop();
        SymbolTableEntry id = (SymbolTableEntry) sstack.peek();

        if (! (id.GetIsProcedure()))
        {
            throw new SemanticError("Id is not a procedure", errorline);
        }
        sstack.push(etype);
    }

    // Generates the free and PROCEND line of TVI code for the main function, and backpatches the alloc statement.
    private void Act55()
    {
        Backpatch(globalstore, globmem);
        Generate("free", globmem);
        Generate("PROCEND");
    }

    // Generates the PROCBEGIN and alloc lines of TVI code for the main function of the given file.
    private void Act56()
    {
        Generate("PROCBEGIN", "main");
        globalstore = alltvi.GetNextQuad();
        Generate("alloc", "_");
    }

    // Prints out a line saying there is an un-implemented semantic action that has been attempted to be used.
    private void Finale(String action)
    {
        System.out.println("There is an un-implemented semantic action, but the show will go on. Missing: " + action);
        System.out.println();
    }

    // Prints out the semantic stack.
    private void DumpStack()
    {
        Stack<Object> tempstack = new Stack<>();
        Object tempval;

        System.out.println("Semantic Actions Stack:");

        while (! (sstack.empty()))
        {
            tempval = sstack.pop();
            tempstack.push(tempval);
        }

        while (! (tempstack.empty()))
        {
            sstack.push(tempstack.pop());
        }

        System.out.println();

        // Prints out the TVI code up to that point, if wanted

        if (printtvi)
        {
            alltvi.print();
        }

        System.out.println();

        // Prints the global symbol table if wanted

        if (dumpglobal)
        {
            globaltable.DumpTable();
            System.out.println();
        }

        // Prints the local symbol table if wanted

        if (dumploc)
        {
            localtable.DumpTable();
            System.out.println();
        }

        // Prints the constant symbol table if wanted
        if (dumpconst)
        {
            constanttable.DumpTable();
            System.out.println();
        }
    }

    // Generates the appropriate TVI code and stores it in the quadruples alltvi.
    private void Generate(String op1)
    {
        alltvi.AddQuad(new String[] {op1, null, null, null});
    }

    // Generates the appropriate TVI code and stores it in the quadruples alltvi.
    private void Generate(String op1, String op2)
    {
        alltvi.AddQuad(new String[] {op1, op2, null, null});
    }

    // Generates the appropriate TVI code and stores it in the quadruples alltvi.
    private void Generate(String op1, int op2)
    {
        alltvi.AddQuad(new String[] {op1, Integer.toString(op2), null, null});
    }

    // Generates the appropriate TVI code and stores it in the quadruples alltvi.
    private void Generate(String op1, SymbolTableEntry op2) throws SymbolTableError
    {
        String part2 = GetSTEPrefix(op2) + GetSTEAddress(op2);
        alltvi.AddQuad(new String[] {op1, part2, null, null});
    }

    // Generates the appropriate TVI code and stores it in the quadruples alltvi.
    private void Generate(String op1, SymbolTableEntry op2, String prefix) throws SymbolTableError
    {
        String part2 = prefix + GetSTEAddress(op2);
        alltvi.AddQuad(new String[] {op1, part2, null, null});
    }

    // Generates the appropriate TVI code and stores it in the quadruples alltvi.
    private void Generate(String op1, int op2, SymbolTableEntry op3) throws SymbolTableError
    {
        String part3 = GetSTEPrefix(op3) + GetSTEAddress(op3);
        alltvi.AddQuad(new String[] {op1, Integer.toString(op2), part3, null});
    }

    // Generates the appropriate TVI code and stores it in the quadruples alltvi.
    private void Generate(String op1, String op2, int op3)
    {
        alltvi.AddQuad(new String[] {op1, op2, Integer.toString(op3), null});
    }

    // Generates the appropriate TVI code and stores it in the quadruples alltvi.
    private void Generate(String op1, String op2, String op3)
    {
        alltvi.AddQuad(new String[] {op1, op2, op3, null});
    }

    // Generates the appropriate TVI code and stores it in the quadruples alltvi.
    private void Generate(String op1, String op2, SymbolTableEntry op3) throws SymbolTableError
    {
        String part3 = GetSTEPrefix(op3) + GetSTEAddress(op3);
        alltvi.AddQuad(new String[] {op1, op2, part3, null});
    }

    // Generates the appropriate TVI code and stores it in the quadruples alltvi.
    private void Generate(String op1, SymbolTableEntry op2, SymbolTableEntry op3) throws SymbolTableError
    {
        String part2 = GetSTEPrefix(op2) + GetSTEAddress(op2);
        String part3 = GetSTEPrefix(op3) + GetSTEAddress(op3);
        alltvi.AddQuad(new String[] {op1, part2, part3, null});
    }

    // Generates the appropriate TVI code and stores it in the quadruples alltvi.
    private void Generate(String op1, SymbolTableEntry op2, SymbolTableEntry op3, SymbolTableEntry op4) throws SymbolTableError
    {
        String part2 = GetSTEPrefix(op2) + GetSTEAddress(op2);
        String part3 = GetSTEPrefix(op3) + GetSTEAddress(op3);
        String part4 = GetSTEPrefix(op4) + GetSTEAddress(op4);
        alltvi.AddQuad(new String[] {op1, part2, part3, part4});
    }

    // Generates the appropriate TVI code and stores it in the quadruples alltvi.
    private void Generate(String op1, SymbolTableEntry op2, SymbolTableEntry op3, String op4) throws SymbolTableError
    {
        String part2 = GetSTEPrefix(op2) + GetSTEAddress(op2);
        String part3 = GetSTEPrefix(op3) + GetSTEAddress(op3);
        alltvi.AddQuad(new String[] {op1, part2, part3, op4});
    }

    // Checks the type of two IDS, and returns an integer which represents which possible combination the two IDS makes
    // (int-int, real-real, real-int, or int-real).
    private int TypeCheck(SymbolTableEntry entry1, SymbolTableEntry entry2)
    {
        if (entry1 == null || entry2 == null)
        {
            return 2;
        }

        else
        {
            if (entry1.GetType().equals("INTEGER") && entry2.GetType().equals("INTEGER"))
            {
                return 0;
            }
            else if (entry1.GetType().equals("REAL") && entry2.GetType().equals("REAL"))
            {
                return 1;
            }
            else if (entry1.GetType().equals("REAL") && entry2.GetType().equals("INTEGER"))
            {
                return 2;
            }
            else
            {
                return 3;
            }
        }
    }

    // Inserts a new variable entry into the appropriate symbol table and associates it with a valid memory address.
    private VariableEntry Create(String name, String type) throws SymbolTableError
    {
        VariableEntry ve;
        if (global)
        {
            int address = globmem;

            globmem++;

            ve = new VariableEntry(name, address, type);
            globaltable.Insert(ve);
        }
        else
        {
            int address = locmem;
            locmem++;
            ve = new VariableEntry(name, address,type);
            localtable.Insert(ve);
        }

        return ve;
    }

    // Updates a line of TVI code, filling out a field with a value that couldn't be calculated at generation.
    private void Backpatch(int quadindex, int field)
    {
        Integer fieldchange = field;
        alltvi.SetField(quadindex, 1, fieldchange.toString());
    }

    // Updates a line of TVI code, filling out a field with a value that couldn't be calculated at generation.
    private void Backpatch(List<Integer> list, int field)
    {
        for (Integer quadindex : list)
        {
            String temp = alltvi.GetField(quadindex,0);
            if (temp.equals("goto"))
            {
                alltvi.SetField(quadindex,1, Integer.toString(field));
            }
            else
            {
                alltvi.SetField(quadindex,3, Integer.toString(field));
            }
        }
    }

    // Looks up a specific token, and returns the symbol table entry for that token if it is found in the global or
    // local table.
    private SymbolTableEntry LookupID(Token id)
    {
        SymbolTableEntry ste = localtable.Lookup(id.GetValue());
        if (ste == null)
        {
            ste = globaltable.Lookup(id.GetValue());
        }

        return ste;
    }

    // Looks up a token to see if it is in the constant table, and if it is returns the symbol table entry for it.
    private SymbolTableEntry LookupConstant(Token id)
    {
        SymbolTableEntry ste = constanttable.Lookup(id.GetValue());

        return ste;
    }

    // Responsible for determining the memory address for a symbol table entry.
    private int GetSTEAddress(SymbolTableEntry ste) throws SymbolTableError
    {
        if (ste.GetIsVariable())
        {
            VariableEntry temp = (VariableEntry) ste;
            return temp.GetAddress();
        }

        if (ste.GetIsArray())
        {
                ArrayEntry temp = (ArrayEntry) ste;
                return temp.GetAddress();
        }

        // Constants do not have an address, and a temporary variable must be created to store its address

        else if (ste.GetIsConstant())
        {
            VariableEntry temp = Create("temp" + tempcounter, ste.GetName());
            tempcounter++;
            Generate("move", ste.GetName(), temp);
            return temp.GetAddress();
        }
        return 0;
    }

    // Converts the value of a token from a string to an integer.
    private int TokenToInt(Token tok)
    {
        return Integer.parseInt(tok.GetValue());
    }

    // Creates an integer list with one element.
    private List<Integer> MakeList(int i)
    {
        LinkedList<Integer> temp = new LinkedList<Integer>();
        temp.add(i);

        return temp;
    }

    // Merges two lists of integers.
    private List<Integer> Merge(List<Integer> lst1, List<Integer> lst2)
    {
        List<Integer> temp = new LinkedList<Integer>();
        temp.addAll(lst1);
        temp.addAll(lst2);

        return temp;
    }

    // Takes a number representing a specific mulop, and converts it into the full word version of that mulop.
    private String GetMulopWord(String opcode)
    {
        String wordopcode;

        if (opcode.equals("1"))
        {
            wordopcode = "mul";
        }
        else if (opcode.equals("3") || opcode.equals("2"))
        {
            wordopcode = "div";
        }
        else if (opcode.equals("4"))
        {
            wordopcode = "mod";
        }
        else
        {
            wordopcode = "and";
        }

        return wordopcode;
    }

    // Responsible for determining the prefix of the address for a symbol table entry that is specifically being used
    // when generating the opcode "param". This gives greater detail on where/what that entry is.
    private String GetParamPrefix(SymbolTableEntry param)
    {
        if (global)
        {
            if ((constanttable.Lookup(param.GetName()) == null))
            {
                if (param.GetIsParam())
                {
                    return "%";
                }
                else
                {
                    return "@%";
                }
            }
            return "@_";
        }
        else
        {
            if (param.GetIsParam())
            {
                return "%";
            }
            else
            {
                return "@%";
            }
        }
    }

    // Responsible for determining the prefix of the address for a symbol table entry, which gives greater detail on
    // where/what that symbol table entry is.
    private String GetSTEPrefix(SymbolTableEntry ste)
    {
        if (global)
        {
            return "_";
        }
        else
        {
            SymbolTableEntry entry = localtable.Lookup(ste.GetName());

            // Also lookup if it is in the constant table, as there can be local constants, and if it is not searched
            // if will default constants to global too

            SymbolTableEntry entry2 = constanttable.Lookup(ste.GetName());

            // If both are null, it must be a global variable

            if (entry == null && entry2 == null)
            {
                return "_";
            }
            else
            {
                if (ste.GetIsParam())
                {
                    return "^%";
                }
                else
                {
                    return "%";
                }
            }
        }
    }
}
