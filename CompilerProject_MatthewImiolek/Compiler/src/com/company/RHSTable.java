package com.company;

/* Holds a RHS table, containing all of the right-hand side productions for all the non-terminals in vascal. The actual
   RHS table is an array of string arrays. Each position in the inner array corresponds with a right hand side
   production. The outer array corresponds with a production number from the parse table. */

public class RHSTable
{
    private String[][] rhstable =
                   {{},                                                                                                                                                                             //0
                    {"PROGRAM", "IDENTIFIER", "#13", "LPAREN", "<identifier-list>", "RPAREN", "#9", "SEMICOLON", "<declarations>", "<sub-declarations>", "#56", "<compound-statement>", "#55"},     //1
                    {"IDENTIFIER", "#13", "<identifier-list-tail>"},                                                                                                                                //2
                    {"COMMA", "IDENTIFIER", "#13", "<identifier-list-tail>"},                                                                                                                       //3
                    {},                                                                                                                                                                             //4
                    {"VAR", "#1", "<declaration-list>", "#2"},                                                                                                                                      //5
                    {},                                                                                                                                                                             //6
                    {"<identifier-list>", "COLON", "<type>", "#3", "SEMICOLON", "<declaration-list-tail>"},                                                                                         //7
                    {"<identifier-list>", "COLON", "<type>", "#3", "SEMICOLON", "<declaration-list-tail>"},                                                                                         //8
                    {},                                                                                                                                                                             //9
                    {"<standard-type>"},                                                                                                                                                            //10
                    {"<array-type>"},                                                                                                                                                               //11
                    {"INTEGER", "#4"},                                                                                                                                                              //12
                    {"REAL", "#4"},                                                                                                                                                                 //13
                    {"#6", "ARRAY", "LBRACKET", "INTCONSTANT", "#7", "DOUBLEDOT", "INTCONSTANT", "#7", "RBRACKET", "OF", "<standard-type>"},                                                        //14
                    {"<subprogram-declaration>", "<sub-declarations>"},                                                                                                                             //15
                    {},                                                                                                                                                                             //16
                    {"#1", "<subprogram-head>", "<declarations>", "#5", "<compound-statement>", "#11"},                                                                                             //17
                    {"FUNCTION", "IDENTIFIER", "#15", "<arguments>", "COLON", "RESULT", "<standard-type>", "SEMICOLON", "#16"},                                                                     //18
                    {"PROCEDURE", "IDENTIFIER", "#17", "<arguments>", "SEMICOLON"},                                                                                                                 //19
                    {"LPAREN", "#19", "<parameter-list>", "RPAREN", "#20"},                                                                                                                         //20
                    {},                                                                                                                                                                             //21
                    {"<identifier-list>", "COLON", "<type>", "#21", "<parameter-list-tail>"},                                                                                                       //22
                    {"SEMICOLON", "<identifier-list>", "COLON", "<type>", "#21", "<parameter-list-tail>"},                                                                                          //23
                    {},                                                                                                                                                                             //24
                    {"BEGIN", "<statement-list>", "END"},                                                                                                                                           //25
                    {"<statement>", "<statement-list-tail>"},                                                                                                                                       //26
                    {"SEMICOLON", "<statement>", "<statement-list-tail>"},                                                                                                                          //27
                    {},                                                                                                                                                                             //28
                    {"<elementary-statement>"},                                                                                                                                                     //29
                    {"IF", "<expression>", "#22", "THEN", "<statement>", "<else-clause>"},                                                                                                          //30
                    {"WHILE", "#24", "<expression>", "#25", "DO", "<statement>", "#26"},                                                                                                            //31
                    {"ELSE", "#27", "<statement>", "#28"},                                                                                                                                          //32
                    {"#29"},                                                                                                                                                                        //33
                    {"IDENTIFIER", "#30", "<es-tail>"},                                                                                                                                             //34
                    {"<compound-statement>"},                                                                                                                                                       //35
                    {"#53", "<subscript>", "ASSIGNOP", "<expression>", "#31"},                                                                                                                      //36
                    {"#54", "<parameters>"},                                                                                                                                                        //37
                    {"#32", "LBRACKET", "<expression>", "RBRACKET", "#33"},                                                                                                                         //38
                    {"#34"},                                                                                                                                                                        //39
                    {"#35", "LPAREN", "<expression-list>", "RPAREN", "#51"},                                                                                                                        //40
                    {"#36"},                                                                                                                                                                        //41
                    {"<expression>", "#37", "<expression-list-tail>"},                                                                                                                              //42
                    {"COMMA", "<expression>", "#37", "<expression-list-tail>"},                                                                                                                     //43
                    {},                                                                                                                                                                             //44
                    {"<simple-expression>", "<expression-tail>"},                                                                                                                                   //45
                    {"RELOP", "#38", "<simple-expression>", "#39"},                                                                                                                                 //46
                    {},                                                                                                                                                                             //47
                    {"<term>", "<simple-expression-tail>"},                                                                                                                                         //48
                    {"<sign>", "#40", "<term>", "#41", "<simple-expression-tail>"},                                                                                                                 //49
                    {"ADDOP", "#42", "<term>", "#43", "<simple-expression-tail>"},                                                                                                                  //50
                    {},                                                                                                                                                                             //51
                    {"<factor>", "<term-tail>"},                                                                                                                                                    //52
                    {"MULOP", "#44", "<factor>", "#45", "<term-tail>"},                                                                                                                             //53
                    {},                                                                                                                                                                             //54
                    {"IDENTIFIER", "#46", "<factor-tail>"},                                                                                                                                         //55
                    {"<constant>", "#46"},                                                                                                                                                          //56
                    {"LPAREN", "<expression>", "RPAREN"},                                                                                                                                           //57
                    {"NOT", "<factor>", "#47"},                                                                                                                                                     //58
                    {"<actual-parameters>"},                                                                                                                                                        //59
                    {"<subscript>", "#48"},                                                                                                                                                         //60
                    {"#49", "LPAREN", "<expression-list>", "RPAREN", "#50"},                                                                                                                        //61
                    {"#52"},                                                                                                                                                                        //62
                    {"UNARYPLUS"},                                                                                                                                                                  //63
                    {"UNARYMINUS"},                                                                                                                                                                 //64
                    {"<program>", "ENDMARKER"},                                                                                                                                                     //65
                    {"INTCONSTANT"},                                                                                                                                                                //66
                    {"REALCONSTANT"}};                                                                                                                                                               //67

    // Gets a specific part of a right hand side production.
    public String GetValue(int x, int y)
    {
        return rhstable[x][y];
    }

    // Gets a complete right hand side production.
    public String[] GetValues(int x)
    {
        return rhstable[x];
    }
}
