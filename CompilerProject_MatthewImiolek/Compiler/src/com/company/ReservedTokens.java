package com.company;

import java.util.*;

/* Creates a hashmap of all the tokens for reserved keywords in vascal that cannot be used as identifiers. It also lets
   this map to be searched to see if a token is one of these reserved keywords. */

public class ReservedTokens
{
    private HashMap<String, String> keywords = new HashMap<>();

    // Creates a ReservedTokens object and sets up the reserved tokens.
    ReservedTokens()
    {
        AddTokens();
    }

    // Adds all the reserved keyword tokens to the hashmap.
    private void AddTokens()
    {
        keywords.put("PROGRAM", null);
        keywords.put("BEGIN", null);
        keywords.put("END", null);
        keywords.put("VAR", null);
        keywords.put("FUNCTION", null);
        keywords.put("PROCEDURE", null);
        keywords.put("RESULT", null);
        keywords.put("INTEGER", null);
        keywords.put("REAL", null);
        keywords.put("ARRAY", null);
        keywords.put("OF", null);
        keywords.put("NOT", null);
        keywords.put("IF", null);
        keywords.put("THEN", null);
        keywords.put("ELSE", null);
        keywords.put("WHILE", null);
        keywords.put("DO", null);
    }

    // Checks if a given string is a key in the hashmap, and this indicates that, the string the token came from is for a
    // reserved keyword.
    public Boolean IsReserved(String reserved)
    {
        if (keywords.containsKey(reserved.toUpperCase()))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
