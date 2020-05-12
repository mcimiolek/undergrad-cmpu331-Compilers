package com.company;

/* This class is used to return errors caused by a mistake in the Symbol Tables. There is only one error possible, which
   is an entry already exists in the Symbol Table */

public class SymbolTableError extends CompilerError
{
    // Causes an error in the case an already existing entry is attempted to be added a second time.
    SymbolTableError(SymbolTableEntry prevEntry)
    {
        System.out.println("Cannot add entry: '" + prevEntry.GetName() + "'. Already exists in this symbol table.");
    }
}
