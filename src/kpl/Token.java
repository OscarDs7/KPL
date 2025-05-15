/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kpl;

/**
 *
 * @author Oscar
 */
// === Token.java ===
// Clase para representar tokens
public class Token {
    public String tipo;
    public Object valor;

    public Token(String tipo, Object valor) 
    {
        this.tipo = tipo;
        this.valor = valor;
    }
    @Override
    public String toString() 
    {
        return tipo + ": " + valor;
    }
}

