package com.company;

import java.util.*;
import java.io.*;

/* Takes a file, and breaks it up by line, with each line being added as a string to an array list. These lines
   can then be accessed to get individual characters, their length, or how many lines are in the file. */

public class FileByLine
{
    private ArrayList<String> file = new ArrayList<>();

    // Constructor which calls MakeList() to create the list of lines in the input code.
    FileByLine() throws IOException
    {
        MakeList();
    }

    // Function which takes a text file, and reads it line by line using a file reader and buffered reader. As the
    // buffered reader goes from line to line they are added to and ArrayList of Strings, which ends up holding the entire
    // file by line.
    private void MakeList() throws IOException
    {
        String line;
        BufferedReader br = new BufferedReader(new FileReader(Compiler.filebylinefile));

        while ((line = br.readLine()) != null)
        {
            file.add(line);
        }
    }

    // Gets the character of a line, using a given line number to pick which line in the array list, and a given position
    // to get the character at the position in that line.
    public char NextChar(int linenum, int pos)
    {
        return file.get(linenum).charAt(pos);
    }

    // Returns the length of the line at the given location in the array list.
    public int LineLength(int linenum)
    {
        return file.get(linenum).length();
    }

    // Returns the number of members in array list, saying how many lines are in the file.
    public int Size()
    {
        return file.size();
    }
}