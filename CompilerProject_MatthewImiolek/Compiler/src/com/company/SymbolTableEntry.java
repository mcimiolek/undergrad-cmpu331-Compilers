package com.company;

import java.util.LinkedList;

/* Sets up the basic, parent class for all symbol table entries. Each symbol table entry has two main parts: the name of
   a specific entry, and any information that goes with the given name. More specific types of symbol table entries may
   require more information to be valid, as that information is necessary to properly create/store the object that
   entry represents. */

public class SymbolTableEntry
{
    public Boolean isvariable = false;
    public Boolean isprocedure = false;
    public Boolean isfunction = false;
    public  Boolean isparam = false;
    public  Boolean isconstant = false;
    private  Boolean isreserved = false;
    private  Boolean isresult = false;
    public  String name;
    public  String type;
    public int numberofparameters;
    public LinkedList parameterinfo = new LinkedList();
    private VariableEntry result;
    private int upperbound;
    private int lowerbound;

    // Creates the most basic type of symbol table entry, only requiring a name and type (real or integer) to create.
    SymbolTableEntry(String name, String type)
    {
        this.name = name;
        this.type = type;
    }

    // Gets the name of the object represented by the symbol table entry.
    public String GetName()
    {
        return name;
    }

    // Sets the name of the object represented by the symbol table entry.
    public void SetName(String name)
    {
        this.name = name;
    }

    // Checks if the object represented by the symbol table entry is a variable.
    public Boolean GetIsVariable()
    {
        return isvariable;
    }

    // Sets if the object represented by the symbol table entry is a variable.
    public void SetIsVariable(Boolean val)
    {
        isvariable = val;
    }

    // Checks if the object represented by the symbol table entry is an array.
    public Boolean GetIsArray()
    {
        return false;
    }

    // Checks if the object represented by the symbol table entry is a procedure.
    public Boolean GetIsProcedure()
    {
        return isprocedure;
    }

    // Sets if the object represented by the symbol table entry is a procedure.
    public void SetIsProcedure(Boolean val)
    {
        isprocedure = val;
    }

    // Checks if the object represented by the symbol table entry is a function.
    public Boolean GetIsFunction()
    {
        return isfunction;
    }

    // Sets if the object represented by the symbol table entry is a function.
    public void SetIsFunction(Boolean val)
    {
        isfunction = val;
    }

    // Checks if the object represented by the symbol table entry is reserved.
    public Boolean GetIsReserved()
    {
        return isreserved;
    }

    // Sets if the object represented by the symbol table entry is reserved.
    public void SetIsReserved(Boolean val)
    {
        isreserved = val;
    }

    // Checks if the object represented by the symbol table entry is a constant.
    public Boolean GetIsConstant()
    {
        return isconstant;
    }

    // Sets if the object represented by the symbol table entry is a constant.
    public void SetIsConstant(Boolean val)
    {
        isconstant = val;
    }

    // Checks if the object represented by the symbol table entry is the parameter of a function.
    public Boolean GetIsParam()
    {
        return isparam;
    }

    // Checks if the object represented by the symbol table entry is the parameter of a function.
    public void SetIsParam(Boolean val)
    {
        isparam = val;
    }

    // Gets the type (integer or real) of the object represented by the symbol table.
    public String GetType()
    {
        return type;
    }

    // Sets the type (integer or real) of the object represented by the symbol table.
    public void SetType(String type)
    {
        this.type = type;
    }

    // Checks if the object represented by the symbol table entry is the result of a function.
    public Boolean GetIsResult()
    {
        return isresult;
    }

    // Sets if the object represented by the symbol table entry is the result of a function.
    public void SetIsResult(Boolean val)
    {
        isresult = val;
    }

    // Gets the number of parameters the object represented by the symbol table needs.
    public int GetNumberOfParameters()
    {
        return numberofparameters;
    }

    // Sets the number of parameters the object represented by the symbol table needs.
    public void SetNumberOfParameters(int numOfParams)
    {
        this.numberofparameters = numOfParams;
    }

    // Adds a parameter to the parameters for the object represented by the symbol table.
    public void AddParam(SymbolTableEntry newParam)
    {
        parameterinfo.add(newParam);
    }

    // Gets the result of the object represented by the symbol table.
    public VariableEntry GetResult()
    {
        return result;
    }

    // Sets the result of the object represented by the symbol table.
    public void SetResult(VariableEntry result)
    {
        this.result = result;
    }

    // Gets the upper bound of the memory range the object represented by the symbol table entry takes.
    public int GetUpperBound()
    {
        return upperbound;
    }

    // Sets the upper bound of the memory range the object represented by the symbol table entry takes.
    public void SetUpperBound(int upper)
    {
        this.upperbound = upper;
    }

    // Gets the lower bound of the memory range the object represented by the symbol table entry takes.
    public int GetLowerBound()
    {
        return lowerbound;
    }

    // Sets the lower bound of the memory range the object represented by the symbol table entry takes.
    public void SetLowerBound(int lower)
    {
        this.lowerbound = lower;
    }
}