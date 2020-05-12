package com.company;
import java.io.*;
import java.lang.*;

/* This class creates a lexer object. It takes a file by line, and goes through that file character by character, finding
   lexemes for the vascal language, which it then converts into tokens to be passed to the parser. */

public class LexicalAnalyser
{
    private Token token;
    private Token lasttoken;
    private String buffer;
    public static int linenum = 0;
    private boolean endoftoken;
    private static int position = 0;
    private ReservedTokens restokens = new ReservedTokens();
    private FileByLine fbl = new FileByLine();
    private String validchars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890.,;:<>/*[]+-=()}{\t ";

    // Creates a new token based of a found lexeme, which has the type (name) of that lexeme, and a value if more
    // detailed information on what that lexeme was is necessary.
    public Token GetNextToken()throws LexicalError
    {
        buffer = "";
        endoftoken = false;

        // If there are no more lines in file by line list, return the end of file token

        if (linenum >= fbl.Size())
        {
            return new Token("ENDOFFILE", null);
        }

        char c = GetChar();

        // While the next character is one indicating white space, loop until one that is not white space is found

        while (c == ' ' || c == '\t' || c == '\n' || (c == '{'))
        {
            if (c == '{')
            {
                ReadWhiteSpace(c);
                c = GetChar();
            }
            else
            {
                c = GetChar();
            }
        }

        // Throws an error if there is an exra }, indicating to many } in a comment

        if (c == '}')
        {
            throw new LexicalError("Extra }.", linenum);
        }

        // Throws an invalid character error if the next character, c, isn't allowed in vascal

        if(validchars.indexOf(c) == -1)
        {
            throw new LexicalError("Invalid character received",linenum);
        }

        endoftoken = false;

        // If a character is a letter check it its part of an identifier, if it s a number check if its part of a
        // number, and otherwise check if the character is part of a symbol

        if (Character.isLetter(c))
        {
            token = ReadIdentifier(c);
        }
        else if (Character.isDigit(c))
        {
            token = ReadNumber(c);
        }
        else
        {
            token = ReadSymbol(c);
        }
        lasttoken = token;

        return token;
    }

    // Adds a character to the buffer of characters for the current lexeme
    private void AddCharBuffer(char newbuffchar)
    {
        buffer = buffer + newbuffchar;
    }

    // Finds a lexeme starting with a letter, and returns a token with IDENTIFIER as its name, and the buffer as it's
    // value if all requirements to be a identifier, and not another token type, are met.
    private Token ReadIdentifier(char nextchar) throws LexicalError
    {
        while ((Character.isDigit(nextchar) || Character.isLetter(nextchar)) && !(endoftoken))
        {
            AddCharBuffer(nextchar);
            nextchar = GetChar();
        }

        //Due to how lookahead works, adds one final character to get the full lexeme in buffer

        if (endoftoken && (Character.isDigit(nextchar) || Character.isLetter(nextchar)))
        {
            AddCharBuffer(nextchar);
        }

        /*Checks if the string in buffer represents a non-identifier, all letter token type, and if so, returns the
          the correct token type and value for the string in buffer, and otherwise returns an identifier token with the
          buffer as its value. */

        if (restokens.IsReserved(buffer))
        {
            token = new Token(buffer.toUpperCase(), null);
        }
        else if (buffer.toUpperCase().equals("DIV"))
        {
            token = new Token("MULOP", "3");
        }
        else if (buffer.toUpperCase().equals("MOD"))
        {
            token = new Token("MULOP", "4");
        }
        else if (buffer.toUpperCase().equals("AND"))
        {
            token = new Token("MULOP", "5");
        }
        else if (buffer.toUpperCase().equals("OR"))
        {
            token = new Token("ADDOP", "3");
        }
        else
        {
            token = new Token("IDENTIFIER", buffer.toLowerCase());
        }

        if (!(endoftoken))
        {
            Pushback();
        }

        //If the identifier is longer than 32 characters throws a too long error

        if  (buffer.length() > 31)
        {
            throw new LexicalError("Identifier is too long.", linenum + 1);
        }

        return token;
    }

    // Finds a lexeme starting with a number, and returns either an integer constant or real constant as its name, and
    // the buffer as it's value if all requirements to be one of these, and not another token type, are met.
    private Token ReadNumber(char firstchar) throws LexicalError
    {
        char nextchar = firstchar;

        // Finds the part of the lexeme up until any non-numerical character

        while (Character.isDigit(nextchar))
        {
            AddCharBuffer(nextchar);
            nextchar = GetChar();
        }

        // Checks for valid dot in number

        if (nextchar == '.')
        {
            char nextnextchar = GetChar();

            // Checks for doubledot, if found returns an intconstant pushed back to before the doubledot

            if (nextnextchar == '.')
            {
                AddCharBuffer(nextchar);
                Pushback();

                token = new Token("INTCONSTANT", buffer);

                Pushback();
            }

            // Checks for a digit

            else if (Character.isDigit(nextnextchar))
            {
                AddCharBuffer(nextchar);

                while (Character.isDigit(nextnextchar))
                {
                    AddCharBuffer(nextnextchar);
                    nextnextchar = GetChar();
                }

                // Checks for a potential exponent, if found checks it it is a valid exponent, otherwise does pushback
                // and returns a realconstant

                if ((Character.toUpperCase(nextnextchar)) == 'E')
                {
                    AddCharBuffer(nextnextchar);
                    nextchar = GetChar();

                    // Checks if the exponent has a unary minus or plus

                    if (nextchar == '+' || nextchar == '-')
                    {
                        AddCharBuffer(nextchar);
                        nextchar = GetChar();

                        // Checks that the exponent has a digit in it, if it does returns a realconstant, otherwise
                        // does pushback to drop the exponent and then returns a realconstant

                        if (Character.isDigit(nextchar))
                        {
                            while (Character.isDigit(nextchar))
                            {
                                AddCharBuffer(nextchar);
                                nextchar = GetChar();
                            }

                            token = new Token("REALCONSTANT", buffer);

                            Pushback();
                        }
                        else
                        {
                            AddCharBuffer(nextchar);
                            Pushback();
                            Pushback();

                            token = new Token("REALCONSTANT", buffer);
                            Pushback();
                        }
                    }

                    // Checks that the exponent has a digit in it, if it does returns a realconstant, otherwise does
                    // pushback to drop the exponent and then returns a realconstant

                    else if (Character.isDigit(nextchar))
                    {
                        while (Character.isDigit(nextchar))
                        {
                            AddCharBuffer(nextchar);
                            nextchar = GetChar();
                        }

                        token = new Token ("REALCONSTANT", buffer);

                        Pushback();
                    }
                    else
                    {
                        AddCharBuffer(nextchar);
                        Pushback();
                        token = new Token("REALCONSTANT", buffer);
                        Pushback();
                    }
                }
                else
                {
                    token = new Token("REALCONSTANT", buffer);

                    Pushback();
                }
            }

            // In any other case, it cannot be a valid use of a period to create a realconstant, so does pushback and
            // returns and intconstant

            else
            {
                token = new Token("INTCONSTANT", buffer);

                Pushback();
            }
        }

        // Checks for a potential exponent, if found checks it it is a valid exponent, otherwise does pushback
        // and returns a intconstant (if a valid exponent is found, instead returns a realconstant because we do
        // not do the computations required to check if it is an intconstant)

        else if ((Character.toUpperCase(nextchar)) == 'E')
        {
            AddCharBuffer(nextchar);
            nextchar = GetChar();

            // Checks if the exponent has a unary minus or plus

            if (nextchar == '+' || nextchar == '-')
            {
                AddCharBuffer(nextchar);
                nextchar = GetChar();

                // Checks that the exponent has a digit in it, if it does returns a realconstant, otherwise
                // does pushback to drop the exponent and then returns an intconstant

                if (Character.isDigit(nextchar))
                {
                    while (Character.isDigit(nextchar))
                    {
                        AddCharBuffer(nextchar);
                        nextchar = GetChar();
                    }

                    token = new Token("REALCONSTANT", buffer);

                    Pushback();
                }
                else
                {
                    Pushback();
                    Pushback();

                    token = new Token("INTCONSTANT", buffer);

                    Pushback();
                }
            }

            // Checks that the exponent has a digit in it, if it does returns a realconstant, otherwise does
            // pushback to drop the exponent and then returns an intconstant

            else if (Character.isDigit(nextchar))
            {
                while (Character.isDigit(nextchar))
                {
                    AddCharBuffer(nextchar);
                    nextchar = GetChar();
                }

                token = new Token ("REALCONSTANT", buffer);

                Pushback();
            }
            else
            {
                Pushback();

                token = new Token("INTCONSTANT", buffer);

                Pushback();
            }
        }
        else
        {
            token = new Token("INTCONSTANT", buffer);

            Pushback();
        }

        // Checks that the constant does not exceed the limit of 32 characters in length

        if (buffer.length() > 31)
        {
            throw new LexicalError("Constant is too long.", linenum + 1);
        }

        return token;
    }

    // Finds a lexeme starting without either a letter or a number, and returns the correct token depending on what
    // combination of characters is found afterwards.
    private Token ReadSymbol(char nextchar) throws LexicalError
    {
        if (nextchar == ',')
        {
            AddCharBuffer(nextchar);

            token = new Token("COMMA", null);
        }
        else if (nextchar == ';')
        {
            AddCharBuffer(nextchar);

            token = new Token("SEMICOLON", null);
        }
        else if (nextchar == ':')
        {
            AddCharBuffer(nextchar);
            nextchar = GetChar();

            if (nextchar == '=')
            {
                token = new Token("ASSIGNOP", null);
            }
            else
            {
                Pushback();

                token = new Token("COLON", null);
            }
        }
        else if (nextchar == '(')
        {
            AddCharBuffer(nextchar);

            token = new Token("LPAREN", null);
        }
        else if (nextchar == ')')
        {
            AddCharBuffer(nextchar);

            token = new Token("RPAREN", null);
        }
        else if (nextchar == '[')
        {
            AddCharBuffer(nextchar);

            token = new Token("LBRACKET", null);
        }
        else if (nextchar == ']')
        {
            AddCharBuffer(nextchar);

            token = new Token("RBRACKET", null);
        }
        else if (nextchar == '=')
        {
            AddCharBuffer(nextchar);

            token = new Token("RELOP", "1");
        }
        else if (nextchar == '<')
        {
            AddCharBuffer(nextchar);
            nextchar = GetChar();

            if ((nextchar == '>') && (position != 0))
            {
                AddCharBuffer(nextchar);
                token = new Token("RELOP", "2");
            }
            else if ((nextchar == '=') && (position != 0))
            {
                AddCharBuffer(nextchar);

                token = new Token("RELOP", "5");
            }
            else
            {
                Pushback();
                token = new Token("RELOP", "3");
            }
        }
        else if (nextchar == '>')
        {
            AddCharBuffer(nextchar);
            nextchar = GetChar();

            if ((nextchar == '=')  && (position != 0))
            {
                AddCharBuffer(nextchar);

                token = new Token("RELOP", "6");
            }
            else
            {
                Pushback();
                token = new Token("RELOP", "4");
            }
        }
        else if (nextchar == '*')
        {
            AddCharBuffer(nextchar);

            token = new Token("MULOP", "1");
        }
        else if (nextchar == '/')
        {
            AddCharBuffer(nextchar);

            token = new Token("MULOP", "2");
        }
        else if (nextchar == '.')
        {
            AddCharBuffer(nextchar);
            nextchar = GetChar();

            if ((nextchar == '.') && !(endoftoken))
            {
                AddCharBuffer(nextchar);

                token = new Token("DOUBLEDOT", null);
            }
            else
            {
                token = new Token("ENDMARKER", null);
            }
        }

        // Checks if + indicates an addition operation, is a unary plus as part of a number, or if it is just a unary plus

        else if (nextchar == '+')
        {
            AddCharBuffer(nextchar);
            nextchar = GetChar();

            if ((lasttoken.GetName().equals("RBRACKET")) || (lasttoken.GetName().equals("RPAREN")) || (lasttoken.GetName().equals("IDENTIFIER")) || (lasttoken.GetName().equals("INTCONSTANT")) || (lasttoken.GetName().equals("REALCONSTANT")))
            {
                Pushback();
                token = new Token("ADDOP", "1");
            }
            else if ((Character.isDigit(nextchar)) && !(endoftoken))
            {
                token = ReadNumber(nextchar);
            }
            else
            {
                Pushback();
                token = new Token("UNARYPLUS", null);
            }
        }

        // Checks if - indicates a subtraction operation, is a unary minus as part of a number, or if it is just a unary
        // minus

        else if (nextchar == '-') {
            AddCharBuffer(nextchar);
            nextchar = GetChar();

            if ((lasttoken.GetName().equals("RBRACKET")) || (lasttoken.GetName().equals("RPAREN")) || (lasttoken.GetName().equals("IDENTIFIER")) || (lasttoken.GetName().equals("INTCONSTANT")) || (lasttoken.GetName().equals("REALCONSTANT")))
            {
                Pushback();
                token = new Token("ADDOP", "2");
            }
            else if ((Character.isDigit(nextchar)) && !(endoftoken))
            {
                token = ReadNumber(nextchar);
            }
            else
            {
                Pushback();
                System.out.println("made");

                token = new Token("UNARYMINUS", null);
            }
        }
        return token;
    }

    // Read a comment as white space, ignoring all characters until a } is found.
    private void ReadWhiteSpace(char nextchar)
    {
        while (nextchar != '}')
        {
            nextchar = GetChar();
        }
    }

    // Gets the next character for the lexer to use.
    private char GetChar()
    {
        char next = ' ';

        //In the case getting the next character would require going to the next line of code, ends the token and
        //increments the line number, if it does not, gets the next character from the file by line list

        if (position >= fbl.LineLength(linenum))
        {
            ++linenum;
            position = 0;
            endoftoken = true;
        }
        else
        {
            next = fbl.NextChar(linenum, position);
            ++position;
        }
        return next;
    }

    // Pushes what character is being looked at back in the case that when looking ahead the next character is not part
    // of the current lexeme, so the current lexeme is submitted correctly, and the incorrect character is used properly
    // in the next lexeme.
    private void Pushback()
    {
        if ((buffer.length() > 0) && (position > 0))
        {
            --position;
            buffer = buffer.substring(0, buffer.length()-1);
        }
        else
        {
            buffer = buffer.substring(0,0);
        }
    }

    // Creates a new lexer object.
    public LexicalAnalyser() throws IOException
    {
    }
}