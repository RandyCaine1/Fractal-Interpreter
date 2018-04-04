/* Specification for Fractal tokens */

// user customisations

package fractal.syntax;
import java_cup.runtime.*;
import fractal.sys.FractalException;
import fractal.sys.FractalLexerException;

// JFlex directives

%%

%cup
%public

%class FractalLexer
%throws FractalException

%type java_cup.runtime.Symbol

%eofval{
	return new Symbol(sym.EOF);
%eofval}

%eofclose false

%char
%column
%line

%{
    private Symbol mkSymbol(int id) {
        return new Symbol(id, yyline, yycolumn);
    }

    private Symbol mkSymbol(int id, Object val) {
        return new Symbol(id, yyline, yycolumn, val);
    }

    public int getChar() {
	return yychar + 1;
    }

    public int getColumn() {
    	return yycolumn + 1;
    }

    public int getLine() {
	return yyline + 1;
    }

    public String getText() {
	return yytext();
    }
%}

nl = [\n\r]

cc = ([\b\f]|{nl})

ws = {cc}|[\t ]

digit = [0-9]


alpha = [_a-zA-Z?]

alphnum = {digit}|{alpha}

%%

<YYINITIAL>	{nl}	{
			 //skip newline
			}
<YYINITIAL>	{ws}	{
			 // skip whitespace
			}

<YYINITIAL>	"//".*	{
			 // skip line comments
			}

<YYINITIAL> {
    "+"			{return mkSymbol(sym.PLUS);}
    "-"			{return mkSymbol(sym.MINUS);}
    "*"			{return mkSymbol(sym.MUL);}
    "/"			{return mkSymbol(sym.DIV);}
    "%"			{return mkSymbol(sym.MOD);}

//Code that I added
    "="   {return mkSymbol(sym.EQ);}
    "<"   {return mkSymbol(sym.LT);}
    ">"   {return mkSymbol(sym.GT);}
    "and" {return mkSymbol(sym.AND);}
    "or"  {return mkSymbol(sym.OR);}
    "not" {return mkSymbol(sym.NOT);}

    ","     {return mkSymbol(sym.COMMA);}

    "("			{return mkSymbol(sym.LPAREN);}
    ")"			{return mkSymbol(sym.RPAREN);}
    "["     {return mkSymbol(sym.LBRACE);}
    "]"     {return mkSymbol(sym.RBRACE);}
		":"			{return mkSymbol(sym.COLON);}

    "render"    {return mkSymbol(sym.RENDER);}
    "clear"     {return mkSymbol(sym.CLEAR);}
    "restore"   {return mkSymbol(sym.RESTORE);}
    "save"      {return mkSymbol(sym.SAVE);}
    "home"      {return mkSymbol(sym.HOME);}
    "fractal"   {return mkSymbol(sym.FRACTAL);}
    "end"       {return mkSymbol(sym.END);}
    "forward"|"fd"  {return mkSymbol(sym.FORWARD);}
    "back"|"bk"     {return mkSymbol(sym.BACK);}
    "left"|"lt"     {return mkSymbol(sym.LEFT);}
    "right"|"rt"    {return mkSymbol(sym.RIGHT);}
    "penup"|"pu"    {return mkSymbol(sym.PENUP);}
    "pendown"|"pd"  {return mkSymbol(sym.PENDOWN);}

    "def"   					{return mkSymbol(sym.DEF);}
    "rep"| "repeat"   {return mkSymbol(sym.REPEAT);}
		"!"               {return mkSymbol(sym.SEQUENCE);}
		"@"								{return mkSymbol(sym.COMPOSE);}


    {digit}+ 		{
			 // INTEGER
	       		 return mkSymbol(sym.INT,
			 	         new Integer(yytext()));
	       		}
						//{rdigit}{
							//DOUBLE
							//return mkSymbol(sym.REAL, new Double(yytext()));
						//}

    {alpha}{alphnum}*   {
    		      	 // IDENTIFIERS
	       		 return mkSymbol(sym.ID, yytext());
	       		}

    .			{ // Unknown token (leave this in the last position)
    			  throw new FractalLexerException(yytext(), getLine(),
							  getColumn());
    			}
}
