/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.prytesting;

/**
 *
 * @author Aprendiz
 */
public class ClassTest {
    
    public int sumar (int a, int b)
    {
        return a+b;
    }
    
    
    public boolean esPositivo(int n)
    {
        return n>0;
    }
    
    
    public int [] array1()
    {
        return new int[] {1,7,3,4,8};
    }
    
    public int [] array2()
    {
        return new int[] {1,2,3,4,5};
    }
    
    public double excDivision (int a, int b)
    {
        if (b==0)
        {
            throw new IllegalArgumentException ("No es posible dividir entre 0");
        }
        return(double)(a/b);
    }
    
    
    
}
