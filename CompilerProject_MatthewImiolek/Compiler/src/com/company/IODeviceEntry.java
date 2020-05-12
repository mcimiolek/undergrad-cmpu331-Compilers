package com.company;

/* Creates a symbol table entry specifically for an input/output device, which has the information required to create
   and store an input/output device in a program. */

public class IODeviceEntry extends SymbolTableEntry
{
    // A constructor that creates a IO device entry object which sets the variables holding the information require to
    // create/store an actual IO device.
    IODeviceEntry(String name)
    {
        super(name, "IODEVICE");
    }

    // Gets the name of the IO device represented by the IO device entry.
    public String GetName()
    {
        return name;
    }

    // Sets the name of the IO device represented by the IO device entry.
    public void SetName(String name)
    {
        this.name = name;
    }

    // Returns all of the arguments for the given IO device entry, with which parameter they correspond with.
    public String toString()
    {
        return "Name: " + name;
    }

    // Returns if the IO device represented in the IO device entry is the parameter of a function.
    public Boolean GetIsParam()
    {
        return isparam;
    }

    // Sets if the IO device represented in the IO device entry is the parameter of a function.
    public void SetIsParam(Boolean val)
    {
        this.isparam = val;
    }
}
