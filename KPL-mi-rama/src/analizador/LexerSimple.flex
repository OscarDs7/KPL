%%

%class LexerSimple
%public
%unicode
%standalone
%function next_token
%cup

%{
  import java_cup.runtime.Symbol;
%}

VAR     = [a-zA-Z_][a-zA-Z0-9_]*
NUM     = [0-9]+
CADENA  = \"([^\"\n])*\"    
ESPACIO = [ \t\r\f\n]+

%%

"mover"         { return new Symbol(sym.MOVER); }
"decir"         { return new Symbol(sym.DECIR); }
"repetir"       { return new Symbol(sym.REPETIR); }
"if"            { return new Symbol(sym.IF); }
"else"          { return new Symbol(sym.ELSE); }
"=="            { return new Symbol(sym.IGUAL); }
"!="            { return new Symbol(sym.DIFERENTE); }
"="             { return new Symbol(sym.ASIGNAR); }
"<"             { return new Symbol(sym.MENOR); }
">"             { return new Symbol(sym.MAYOR); }
"+"             { return new Symbol(sym.SUMA); }
"-"             { return new Symbol(sym.RESTA); }
"*"             { return new Symbol(sym.MULT); }
"/"             { return new Symbol(sym.DIV); }
"{"             { return new Symbol(sym.LLAVE_ABRE); }
"}"             { return new Symbol(sym.LLAVE_CIERRA); }
"("             { return new Symbol(sym.PAREN_ABRE); }
")"             { return new Symbol(sym.PAREN_CIERRA); }

{NUM}           { return new Symbol(sym.NUM, Integer.valueOf(yytext())); }
{VAR}           { return new Symbol(sym.VAR, yytext()); }
{CADENA}        { return new Symbol(sym.CADENA, yytext().substring(1, yytext().length() - 1)); }

{ESPACIO}       { /* ignorar espacios y saltos */ }

<<EOF>>         { return new Symbol(sym.EOF); }

.               { throw new Error("Carácter no reconocido: " + yytext()); }
