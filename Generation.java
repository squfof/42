// File:        Generation.java
// Author:      Jason Gower
// Created:     March 21, 2018
// Purpose:     Implement a collection of ArithExpress.

import edu.princeton.cs.algs4.*;
import java.util.Random;
import java.util.Arrays;

public class Generation {
    
    // Instance variables
    private static Random rand = new Random();      // Used to create random data
    private static int genNum = 0;                  // keep track of Generation number
    private ArithExpress[] gen;                     // Contains a list of ArithExpress
    private int len;                                // Length of each ArithExpress
    private double mutateProb;                      // Probability of mutation
    
    
    // Constructor requires number of ArithExpress, and length of each ArithExpress
    public Generation(int genSize, int l, double p) {
        if (genSize <= 0)
            throw new RuntimeException();
        
        genNum++;
        gen = new ArithExpress[genSize];
        len = l;
        mutateProb = p;
        for (int i = 0; i < genSize; i++)
            gen[i] = new ArithExpress(len);
    }
    
    
    // Generate a random Generation
    public void randGen() {
        for (int i = 0; i < this.gen.length; i++) {
            gen[i] = new ArithExpress(len);
            gen[i].randAE();
        }
        Arrays.sort(gen);
    }
    
    
    // Generate the next Generation
    public void nextGen() {
        int l = this.gen.length;
        ArithExpress[] aeList = new ArithExpress[l * (l - 1)];
        int idx = 0;
        for (int i = 0; i < l; i++) {
            for (int j = i + 1; j < l; j++) {
                int s = rand.nextInt(len - 1);
                aeList[idx] = this.gen[i].splice(this.gen[j], s).mutate(mutateProb);
                idx++;
                aeList[idx] = this.gen[j].splice(this.gen[i], s).mutate(mutateProb);
                idx++;
            }
        }
        Arrays.sort(aeList);
        for (int k = 0; k < l; k++) {
            this.gen[k] = aeList[k];
        }
        genNum++;
    }
    
    
    // Determine whether or not we have found an ArithExpress that evaluates to the TARGET_NUMBER
    public boolean targetFound() {
        return gen[0].score() == 0;
    }
    
    
    // Used to test code.
	public static void main(String[] args) {
        // test code...
        /*
        Generation g = new Generation(1, 3, 0.0);
        g.randGen();
        while (!g.targetFound()) {
            g = new Generation(1, 3, 0.0);
            g.randGen();
        }
         */
        
        Generation g = new Generation(10, 5, 0.10);
        g.randGen();
         /*
        for (int i = 0; i < g.gen.length; i++)
            StdOut.println(g.gen[i] + " = " + g.gen[i].evalExpress());
        g.nextGen();
        for (int i = 0; i < g.gen.length; i++)
            StdOut.println(g.gen[i] + " = " + g.gen[i].evalExpress());
         */
        
        int count = 0;
        while (!g.targetFound() && count < 5000) {
            g.nextGen();
            count++;
        }
        
        StdOut.println("* " + g.targetFound() + " after " + genNum + " generations *");
        for (int i = 0; i < 1; i++)
            StdOut.println(g.gen[i] + " = " + g.gen[i].evalExpress());
        
	}
}
