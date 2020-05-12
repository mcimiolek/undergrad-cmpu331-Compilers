package com.company;

import java.util.*;

/* Creates a symbol table entry specifically for a function, which has the information required to create
   and store a function in a program. */

public class FunctionEntry extends SymbolTableEntry
{
    private VariableEntry result;
    private String resulttype;

    // A constructor that creates a function entry object which sets the variables holding the information require to
    // create/store an actual function.
    FunctionEntry(String name, int numofparams, LinkedList paraminfo, VariableEntry result)
    {
        super(name, "FUNCTION");
        this.numberofparameters = numofparams;
        this.parameterinfo = paraminfo;
        this.result = result;

        SetIsFunction(true);
    }

    // A constructor that creates a function entry object which sets only the values for the variables holding the
    // functions name and result. The other fields are set later in this case.
    FunctionEntry(String name, VariableEntry result)
    {
        super(name, "FUNCTION");
        this.result = result;
    }

    // Gets the name of the function represented by the function entry.
    public String GetName()
    {
        return name;
    }

    // Sets the name of the function represented by the function entry.
    public void SetName(String name)
    {
        this.name = name;
    }

    // Returns all of the arguments for the given function entry, with which parameter they correspond with.
    public String toString()
    {
        return "Function Entry <Name: " + name + " Number of Parameters: " + numberofparameters + " Parameter Info: " + parameterinfo + " Result: " + result + ">";
    }

    // Returns the number of parameters the function the function entry represents needs.
    public int GetNumberOfParameters()
    {
        return numberofparameters;
    }

    // Sets the number of parameters the function the function entry represents needs.
    public void SetNumberOfParameters(int numofparams)
    {
        this.numberofparameters = numofparams;
    }

    // Returns the information for all of the parameters for the function the function entry represents.
    public LinkedList GetParameterInfo()
    {
        return parameterinfo;
    }

    // Sets the information for all of the parameters for the function that the function entry represents.
    public void SetParameterInfo(LinkedList paraminfo)
    {
        this.parameterinfo = paraminfo;
    }

    // Get the result of the function represented by the function entry.
    public VariableEntry GetResult()
    {
        return result;
    }

    // Set the result of the function represented by the function entry.
    public void SetResult(VariableEntry result)
    {
        this.result = result;
    }

    // Returns if the function entry is for a function.
    public Boolean GetIsFunction()
    {
        return true;
    }

    // Sets if the function entry represents a function.
    public void SetIsFunction(Boolean val)
    {
        isfunction = val;
    }

    // Returns if the function represented in the function entry is the parameter of a function.
    public Boolean GetIsParam()
    {
        return isparam;
    }

    // Sets if the function represented in the function entry is the parameter of a function.
    public void SetIsParam(Boolean val)
    {
        this.isparam = val;
    }

    // Gets what kind of result, an integer or real, the result of the function represented by the function result is.
    public String GetResultType()
    {
        return resulttype;
    }

    // Sets what kind of result, an integer or real, the result of the function represented by the function result is.
    public void SetResultType(String val)
    {
        this.resulttype = val;
    }
}
