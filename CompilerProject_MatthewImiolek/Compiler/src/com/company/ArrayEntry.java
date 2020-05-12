package com.company;

   /* Creates a symbol table entry specifically for an array, which has the information required to create and store an
      array in a program. It includes functions in order to get and set these fields. */

public class ArrayEntry extends SymbolTableEntry
{
    private int address;
    private int upperbound;
    private int lowerbound;

    // Creates an array entry object which sets the variables holding the information require to create/store an
    // actual array.
    ArrayEntry(String name, int address, String type, int upperbound, int lowerbound)
    {
        super(name, type);
        this.address = address;
        this.upperbound = upperbound;
        this.lowerbound = lowerbound;
    }

    // Gets the name of the array represented by the array entry.
    public String GetName()
    {
        return name;
    }

    // Sets the name of the array represented by the array entry.
    public void SetName(String name)
    {
        this.name = name;
    }

    // Returns all of the arguments for the given array entry, with which parameter they correspond with.
    public String toString()
    {
        return "Array Entry <Name: " + name + " Address: " + address + " Type: " + type + " Upper Bound: " + upperbound + " Lower Bound: " + lowerbound + ">";
    }

    // Gets the memory address of the array represented by the array entry, which is the last location in the memory
    // range the array takes.
    public int GetAddress()
    {
        return address;
    }

    // Set the memory address of the array represented by the array entry.
    public void SetAddress(int address)
    {
        this.address = address;
    }

    // Get the type of the array represented by the array entry, if it's a real or integer.
    public String GetType()
    {
        return type;
    }

    // Sets the type of the array represented by the array entry to either real or integer.
    public void SetType(String type)
    {
        this.type = type;
    }

    // Gets the upper bound of the memory range the array represented by the array entry takes.
    public int GetUpperBound()
    {
        return upperbound;
    }

    // Sets the upper bound of the memory range the array represented by the array entry takes.
    public void SetUpperBound(int upper)
    {
        this.upperbound = upper;
    }

    // Gets the lower bound of the memory range the array represented by the array entry takes.
    public int GetLowerBound()
    {
        return lowerbound;
    }

    // Sets the lower bound of the memory range the array represented by the array entry takes.
    public void SetLowerBound(int lower)
    {
        this.lowerbound = lower;
    }

    // Returns the array entry represents an array.
    public Boolean GetIsArray()
    {
        return true;
    }

    // Get if the array represented by the array entry is a parameter for a function.
    public Boolean GetIsParam()
    {
        return isparam;
    }

    // Sets if the array represented by the array entry is a parameter for a function.
    public void SetIsParam(Boolean val)
    {
        this.isparam = val;
    }
}
