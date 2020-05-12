package com.company;

import java.util.*;
import java.lang.*;

/* Holds information about specific objects in a program the compiler is compiling so they can be looked at and used as
   necessary. It does this by holding all the symbol table entries in a hashtable. */

public class SymbolTable
{
    private Hashtable<String, SymbolTableEntry> values;

    // Sets up the hashtable to the given size.
    SymbolTable(int size)
    {
        values = new Hashtable<>(size);
    }

    // Inserts a new symbol table entry into the symbol table.
    public void Insert(SymbolTableEntry newent) throws SymbolTableError
    {
        if (values.containsKey(newent.GetName()))
        {
            throw new SymbolTableError(newent);
        }
        else
        {
            values.put(newent.GetName(), newent);
        }
    }

    // Returns a specific entry in the symbol table.
    public SymbolTableEntry Lookup(String name)
    {
        return values.get(name);
    }

    // Returns the number of entries in the symbol table.
    public int Size()
    {
        return values.size();
    }

    // Prints out all the values in the symbol table.
    public void DumpTable()
    {
        Set<String> keys = values.keySet();

        for(String key: keys)
        {
            System.out.print(String.format("%-25s|", "Name: " + key));
            System.out.println(" Value: <"+ values.get(key) + ">");
        }

    }
}
