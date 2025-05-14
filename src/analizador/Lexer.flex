package analizador;

%class Lexer
%implements java_cup.runtime.Scanner
%unicode
%cup
%line
%column
%public

%{

  import java_cup.runtime.Symbol;
  import java_cup.runtime.ComplexSymbolFactory;
  import java_cup.runtime.ComplexSymbolFactory.Location;
  import analizador.sym;

  private ComplexSymbolFactory sf;

  public Lexer(java.io.Reader in, ComplexSymbolFactory sf) {
    this.yyreset(in);
    this.sf = sf;
  }

%}

NUM     = [0-9]+
CADENA  = \"([^\"\n])*\"     
ESPACIO = [ \t\r\f\n]+

%%

"mover"         { return sf.newSymbol("MOVER", sym.MOVER); }
"decir"         { return sf.newSymbol("DECIR", sym.DECIR); }
"repetir"       { return sf.newSymbol("REPETIR", sym.REPETIR); }
"{"             { return sf.newSymbol("LLAVE_ABRE", sym.LLAVE_ABRE); }
"}"             { return sf.newSymbol("LLAVE_CIERRA", sym.LLAVE_CIERRA); }

{NUM}           { return sf.newSymbol("NUM", sym.NUM, Integer.parseInt(yytext())); }
{CADENA}        { return sf.newSymbol("CADENA", sym.CADENA, yytext().substring(1, yytext().length() - 1)); }

{ESPACIO}       { /* ignorar */ }

<<EOF>>         { return sf.newSymbol("EOF", sym.EOF); }

.               { System.err.println("Carácter no reconocido: " + yytext()); return sf.newSymbol("error", sym.error); }
