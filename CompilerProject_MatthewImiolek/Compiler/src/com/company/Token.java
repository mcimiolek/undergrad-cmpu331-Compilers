package com.company;

/* Creates a token object, which is a pair of strings the first of which is it's type, called name, which is based off
   of the analysis of the current lexeme. Depending on what the type of the token is it may also store a value, which
   may be the actual original string of the lexeme, or a string representing a specific case of the token type. */

public class Token
{
    private String name;
    private String value;

    // Creates a token, using the given name for the tokens name/type and the value for its value.
    public Token(String name, String value)
    {
        this.name = name;
        this.value = value;
    }

    // Returns the name, or type, of the token.
    public String GetName()
    {
        return name;
    }

    // Returns the value of the token.
    public String GetValue()
    {
        return value;
    }

    // Returns a string with both the tokens name and value.
    public String toString()
    {
        return ("<Name: " + name + " Value: " + value + ">");
    }

    // Converts the token to an integer.
    public int TokenToInt()
    {
        return Integer.parseInt(value);
    }
}
