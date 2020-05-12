package com.company;

/* Creates a symbol table entry specifically for an variable, which has the information required to create and store a
   variable in a program. It includes functions in order to get and set these fields. */

public class VariableEntry extends SymbolTableEntry {
    int address;
    Boolean isResult = false;

    // Creates a variable entry object which sets the variables holding the information require to create/store an actual
    // variable.
    VariableEntry(String name, int address, String type) {
        super(name, type);
        this.address = address;

        SetIsVariable(true);
    }

    // Gets the name of the variable represented by the variable entry.
    public String GetName() {
        return name;
    }

    // Sets the name of the variable represented by the variable entry.
    public void SetName(String name) {
        this.name = name;
    }

    // Returns all of the arguments for the given variable entry, with which parameter they correspond with.
    public String toString() {
        return "Name: " + name + " Address: " + address + " Type: " + type;
    }

    // Gets the memory address of the variable represented by the variable entry.
    public int GetAddress() {
        return address;
    }

    // Sets the memory address of the variable represented by the variable entry.
    public void SetAddress(int address) {
        this.address = address;
    }

    // Gets the type of the variable represented by the variable entry.
    public String GetType() {
        return type;
    }

    // Sets the type of the variable represented by the variable entry.
    public void SetType(String type) {
        this.type = type;
    }

    // Gets if the variable represented by the variable entry is a variable.
    public Boolean GetIsVariable() {
        return isvariable;
    }

    // Sets if the variable represented by the variable entry is a variable.
    public void SetIsVariable(Boolean val) {
        this.isvariable = val;
    }

    // Gets if the variable represented by the variable entry is a parameter of a function.
    public Boolean GetIsParam() {
        return isparam;
    }

    // Sets if the variable represented by the variable entry is a parameter of a function.
    public void SetIsParam(Boolean val) {
        this.isparam = val;
    }

    // Gets if the variable represented by the variable entry is the result of a function.
    public Boolean GetIsResult() {
        return isResult;
    }

    // Sets if the variable represented by the variable entry is the result of a function.
    public void SetIsResult(Boolean val) {
        isResult = val;
    }
}
