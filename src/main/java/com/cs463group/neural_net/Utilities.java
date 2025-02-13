package com.cs463group.neural_net;


import java.util.List;

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

    // Derivative of sigmoid activation function
    public static double sigmoidDeriv(double in){
        double sigmoid = Utilities.sigmoid(in);
        return sigmoid * (1 - in);
    }

    // Mean square loss function
    public static Double meanSquareLoss(List<Double> correctAnswers, List<Double> predictedAnswers){
        double sumSquare = 0;
        for (int i = 0; i < correctAnswers.size(); i++){
            double error = correctAnswers.get(i) - predictedAnswers.get(i);
            sumSquare += (error * error);
        }
        return sumSquare / (correctAnswers.size());
    }
}
