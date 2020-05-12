package com.company;

import java.util.*;

/* Creates a symbol table entry specifically for a procedure, which has the information required to create
   and store a procedure in a program. */

public class ProcedureEntry extends SymbolTableEntry
{
    // A constructor that creates a procedure entry object which sets the variables holding the information require to
    // create/store an actual procedure.
    ProcedureEntry(String name, int numofparams, LinkedList paraminfo)
    {
        super(name, "PROCEDURE");
        this.numberofparameters = numofparams;
        this.parameterinfo = paraminfo;

        SetIsProcedure(true);
    }

    //  A constructor that creates a procedure entry object which sets the variables holding the name of the procedure.
    //  The other fields are set later.
    ProcedureEntry(String name)
    {
        super(name, "PROCEDURE");

        SetIsProcedure(true);
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
        return "Name: " + name + " Number of Parameters: " + numberofparameters + " Parameter Info: " + parameterinfo;
    }

    // Returns the number of parameters the procedure the procedure entry represents needs.
    public int GetNumberOfParameters()
    {
        return numberofparameters;
    }

    // Sets the number of parameters the procedure the procedure entry represents needs.
    public void SetNumberOfParameters(int numofparams)
    {
        this.numberofparameters = numofparams;
    }

    // Returns the information for all of the parameters for the procedure the procedure entry represents.
    public LinkedList GetParameterInfo()
    {
        return parameterinfo;
    }

    // Sets the information for all of the parameters for the procedure that the procedure entry represents.
    public void SetParameterInfo(LinkedList paraminfo)
    {
        this.parameterinfo = paraminfo;
    }

    // Returns if the procedure entry is for a procedure.
    public Boolean GetIsProcedure()
    {
        return true;
    }

    // Sets if the procedure entry is for a procedure.
    public void SetIsProcedure(Boolean val)
    {
        this.isprocedure = val;
    }

    // Returns if the procedure represented in the procedure entry is the parameter of a function.
    public Boolean GetIsParam()
    {
        return isparam;
    }

    // Sets if the procedure represented in the procedure entry is the parameter of a function.
    public void SetIsParam(Boolean val)
    {
        this.isparam = val;
    }
}
