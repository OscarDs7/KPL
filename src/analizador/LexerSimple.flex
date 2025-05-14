package analizador;

%%

%class LexerSimple
%implements java_cup.runtime.Scanner
%function next_token
%unicode
%cup
%line
%column

%{
    // Importaciones necesarias
    import java_cup.runtime.Symbol;
    import java_cup.runtime.SymbolFactory;
%}

NUM     = [0-9]+
CADENA  = \"([^\"\n])*\"     // Cadenas entre comillas dobles
ESPACIO = [ \t\r\f\n]+

%%

"mover"         { return new Symbol(sym.MOVER); }
"decir"         { return new Symbol(sym.DECIR); }
"repetir"       { return new Symbol(sym.REPETIR); }
"{"             { return new Symbol(sym.LLAVE_ABRE); }
"}"             { return new Symbol(sym.LLAVE_CIERRA); }

{NUM}           { return new Symbol(sym.NUM, new Integer(yytext())); }
{CADENA}        { return new Symbol(sym.CADENA, yytext().substring(1, yytext().length() - 1)); }

{ESPACIO}       { /* ignorar espacios y saltos de línea */ }

<<EOF>>         { return new Symbol(sym.EOF); }

.               { System.err.println("Carácter no reconocido: " + yytext()); return new Symbol(sym.error); }
