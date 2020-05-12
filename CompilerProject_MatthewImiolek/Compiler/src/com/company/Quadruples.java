package com.company;

import java.util.*;

/* Creates a vector which holds all of the individual quadruples, which are String arrays that hold an individual line
   of TVI code. Using this vector of String arrays, it also allows for the management/access of indivudial quads. */

public class Quadruples
{
    private Vector<String[]> quadruple;
    private int nextquad;

    // A constructor that sets up the vector for quads to be added to it, and adds a single quad with all of its fields
    // as null as the first value.
    public Quadruples()
    {
        quadruple = new Vector<String[]>();
        nextquad = 0;
        String[] dummyquadruple = new String[4];
        dummyquadruple[0] = dummyquadruple[1] = dummyquadruple[2] = dummyquadruple[3] = null;
        quadruple.add(nextquad,dummyquadruple);
        nextquad++;
    }

    // Gets a specific field of a quad (what command it does or a memory location it uses for that command).
    public String GetField(int quadindex, int field)
    {
        return quadruple.elementAt(quadindex)[field];
    }

    // Sets a specific field of a quad (what command it does or a memory location it uses for that command).
    public void SetField(int quadindex, int index, String field)
    {
        quadruple.elementAt(quadindex)[index] = field;
    }

    // Get the location of where the next quad will go.
    public int GetNextQuad()
    {
        return nextquad;
    }

    // Increments next quad.
    public void IncrementNextQuad()
    {
        nextquad++;
    }

    // Returns the string array containing all the information for a specific quadruple at the given index of the vector.
    public String[] GetQuad(int index)
    {
        return (String []) quadruple.elementAt(index);
    }

    // Add a new quadruple to the vector.
    public void AddQuad(String[] quad)
    {
        quadruple.add(nextquad, quad);
        nextquad++;
    }

    // Prints out all of the quadruples stored in the vector.
    public void print()
    {
        int quadLabel = 1;
        String separator;

        System.out.println("TVI CODE:");

        Enumeration<String[]> e = this.quadruple.elements();
        e.nextElement();

        while (e.hasMoreElements())
        {
            String[] quad = e.nextElement();
            System.out.print(quadLabel + ":  " + quad[0]);

            if (quad[1] != null)
                System.out.print(" " + quad[1]);

            if (quad[2] != null)
                System.out.print(", " + quad[2]);

            if (quad[3] != null)
                System.out.print(", " + quad[3]);

            System.out.println();
            quadLabel++;
        }
    }
}