// File:        ArithExpress.java
// Author:      Jason Gower
// Created:     March 16, 2018
// Purpose:     Implement an arithmetic expression using random integers 1-9, +, -, *, /
//              using left-to-right reverse-Polish notation.

import edu.princeton.cs.algs4.*;
import java.util.Random;

public class ArithExpress implements Comparable<ArithExpress> {
    
    // Instance variables
    private static int TARGET_NUMBER = 42;          // Required so we can "score" each ArithExpress
    private static Random rand = new Random();      // Used to create random data
    private int[] symb;     // Contains integers from 1-13, where 10 = addition,
                            // 11 = subtracton, 12 = multiplication, 13 = division
    
    
    // Constructor requires a positive odd number,
    // otherwise no chance that the arithmetic expression is valid
    public ArithExpress(int numOfSymb) {
        if (numOfSymb <= 0 || numOfSymb % 2 == 0)
            throw new RuntimeException();
        
        symb = new int[numOfSymb];
    }
    
    
    public boolean validExpress() {
        // Recall that we are using left-to-right reverse-Polish notation. This means that a
        // string of digits and binary operations will be valid if an only if: we initialize a
        // counter to zero, then scan left-to-right through the symbols, incrementing by one
        // if we find a digit, and decrementing by two then incrementing by one if we find a binary
        // operation; if the counter never goes negative (after being decremented) and if the counter
        // does ends with value one, then the expression is valid.
        int count = 0;
        for (int i = 0; i < symb.length; i++) {
            if (symb[i] < 10)
                count++;
            else {
                count -= 2;
                if (count < 0)
                    return false;
                count++;
            }
        }
        return count == 1;
    }
    
    
    // Generate random symbols for this ArithExpress.
    public void randAE() {
        while (!this.validExpress()) {
            for (int i = 0; i < this.symb.length; i++)
                symb[i] = 1 + rand.nextInt(12);
        }
    }
    
    
    public int evalExpress() {
        // If the expression isn't valid, we can't evaluate it so return the largest possible
        // integer value, which presumably isn't a possible result of evaluating a valid expression.
        if (!this.validExpress())
            return Integer.MAX_VALUE;
        
        // The array 'aux' will act like a stack. As we move left-to-right through the symbols,
        // push digits onto the stack, otherwise pop two digits from the stack, perform the
        // operation, then push result onto the statck. The final result will be in aux[0].
        int[] aux = new int[symb.length];
        int idx = 0;
        for (int i = 0; i < symb.length; i++) {
            int symbi = symb[i];
            if (symbi < 10)
                aux[idx++] = symbi;
            else {
                if (symbi == 10)
                    aux[idx - 2] += aux[idx - 1];
                else if (symbi == 11)
                    aux[idx - 2] -= aux[idx - 1];
                else if (symbi == 12)
                    aux[idx - 2] *= aux[idx - 1];
                else {
                    if (aux[idx] == 0)
                        // Can't divide by zero, so return largest possible integer value.
                        return Integer.MAX_VALUE;
                    else
                        aux[idx - 2] /= aux[idx - 1];
                }
                idx--;
            }
        }
        return aux[0];
    }
    
    
    // Returns the "score" of the ArithExpress, which is how far away its value is from
    // the TARGET_NUMBER.
    public int score() {
        return Math.abs(TARGET_NUMBER - this.evalExpress());
    }
    
    
    // Required in order to be Comparable (so we can sort)
    @Override
    public int compareTo(ArithExpress ae) {
        if (this.score() < ae.score())
            return -1;
        else if (this.score() == ae.score())
            return 0;
        else
            return 1;
    }
    
    
    // Returns a mutated version of this expression using the given probability, a double
    // in the interval (0,1), that a symbol will be mutated.
    public ArithExpress mutate(double prob) {
        ArithExpress ae = new ArithExpress(this.symb.length);
        for (int i = 0; i < ae.symb.length; i++) {
            double p = rand.nextDouble();
            if (p < prob)
                ae.symb[i] = 1 + rand.nextInt(12);
            else
                ae.symb[i] = this.symb[i];
        }
        return ae;
    }
    
    
    // Returns a combined version of this expression using the given ArithExpress and the
    // given splice-point. First part is from this ArithExpress, second part is from the
    // other ArithExpress.
    public ArithExpress splice(ArithExpress otherAE, int s) {
        ArithExpress ae = new ArithExpress(this.symb.length);
        for (int i = 0; i <= s; i++)
            ae.symb[i] = this.symb[i];
        for (int i = s+1; i < ae.symb.length; i++)
            ae.symb[i] = otherAE.symb[i];
        return ae;
    }
    
    
    // Return the expression as a String in left-to-right reverse-Polish notation.
    public String toString() {
        String s = "";
        for (int i = 0 ; i < symb.length; i++) {
            int symbi = symb[i];
            if (symbi < 10)
                s += symbi + " ";
            else
                s += intToOp(symbi) + " ";
        }
        return s.trim();
    }
    
    
    // Used in toString to convert integers 10-13 into binary operations.
    private static String intToOp(int i) {
        // recall: 10 = addition, 11 = subtraction, 12 = multiplication, 13 = division.
        if (i == 10)
            return "+";
        else if (i == 11)
            return "-";
        else if (i == 12)
            return "*";
        else
            return "-";
    }
    
    
    // Used to test code.
	public static void main(String[] args) {
        // test code...
        ArithExpress ae = new ArithExpress(5);
        ae.randAE();
        StdOut.println(ae + " is valid: " + ae.validExpress());
        StdOut.println(ae + " = " + ae.evalExpress() + "\n");
        
        ArithExpress aeMutate = ae.mutate(0.50);
        StdOut.println(aeMutate + " is valid: " + aeMutate.validExpress());
        StdOut.println(aeMutate + " = " + aeMutate.evalExpress() + "\n");
        
        ArithExpress aeSpliced1 = ae.splice(aeMutate, 2);
        StdOut.println(aeSpliced1 + " is valid: " + aeSpliced1.validExpress());
        StdOut.println(aeSpliced1 + " = " + aeSpliced1.evalExpress() + "\n");
        
        ArithExpress aeSpliced2 = aeMutate.splice(ae, 2);
        StdOut.println(aeSpliced2 + " is valid: " + aeSpliced2.validExpress());
        StdOut.println(aeSpliced2 + " = " + aeSpliced2.evalExpress() + "\n");
        
        StdOut.println(ae.score() + " " + aeMutate.score() + " " + ae.compareTo(aeMutate));
	}
}
