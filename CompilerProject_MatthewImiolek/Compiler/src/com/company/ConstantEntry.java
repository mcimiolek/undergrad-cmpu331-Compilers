package com.company;

/* Creates a symbol table entry specifically for a constant, which has the information required to create
   and store a constant in a program. */

public class ConstantEntry extends SymbolTableEntry
{
    // Creates a constant entry object which sets the variables holding the information require to create/store an
    // actual constant.
    ConstantEntry(String name, String type)
    {
        super(name, type);

        SetIsConstant(true);
    }

    // Get the name of the constant represented by the constant entry.
    public String GetName()
    {
        return name;
    }

    // Sets the name of the constant represented by the constant entry.
    public void SetName(String name)
    {
        this.name = name;
    }

    // Returns all of the arguments for the given constant entry, with which parameter they correspond with.
    public String toString()
    {
        return "Constant Entry <Name: " + name +  " Type: " + type + ">";
    }

    // Gets the type of constant represented by the constant entry, if it is an integer or real.
    public String GetType()
    {
        return type;
    }

    // Sets the type of the constant represented by the constant entry, if it is an integer or real.
    public void SetType(String type)
    {
        this.type = type;
    }

    // Returns the constant represents a constant.
    public Boolean GetIsConstant()
    {
        return true;
    }

    // Sets if the constant represents a constant.
    public void SetIsConstant(Boolean val)
    {
        this.isconstant = val;
    }

    // Get if the constant represented by the constant entry is a parameter for a function.
    public Boolean GetIsParam()
    {
        return isparam;
    }

    // Sets if the constant represented by the constant entry is a parameter for a function.
    public void SetIsParam(Boolean val)
    {
        this.isparam = val;
    }
}
