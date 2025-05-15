%%

%class LexerSimple
%public
%unicode
%standalone
%function next_token
%cup


%{
  // Importaciones necesarias
  import java_cup.runtime.SymbolFactory;
  import java_cup.runtime.Symbol;
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

{NUM}           { return new Symbol(sym.NUM, Integer.valueOf(yytext())); }
{CADENA}        { return new Symbol(sym.CADENA, yytext().substring(1, yytext().length() - 1)); }

{ESPACIO}       { /* ignorar espacios */ }

<<EOF>>         { return new Symbol(sym.EOF); }

.               { throw new Error("Carácter no reconocido: " + yytext()); }
