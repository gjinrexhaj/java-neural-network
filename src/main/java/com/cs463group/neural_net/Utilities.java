package com.cs463group.neural_net;


/**
 *  Utilities.java
 *  Created on 2/11/2025
 *  Contains commonly used helper functions to be used in other classes
 */
public class Utilities {

    // Sigmoid activation function
    public static double sigmoid(double input) {
        return 1 / (1 + Math.exp(-input));
    }
}
