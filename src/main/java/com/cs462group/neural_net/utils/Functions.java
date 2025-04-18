package com.cs462group.neural_net.utils;

import java.util.List;

/**
 *  Functions.java
 *  Created on 2/11/2025
 *  Contains commonly used helper functions to be used in other classes
 */
public class Functions {

    // Sigmoid activation function
    public static double sigmoid(double input) {
        return 1 / (1 + Math.exp(-input));
    }

    // Derivative of sigmoid activation function
    public static double sigmoidDeriv(double in){
        double sigmoid = Functions.sigmoid(in);
        return sigmoid * (1 - sigmoid);
    }

    // Mean square loss function - Legacy
    // TODO: remove in favor of revised version below
    // Mean square loss function
    public static Double meanSquareLoss(List<Double> correctAnswers, List<Double> predictedAnswers){
        double sumSquare = 0;
        for (int i = 0; i < correctAnswers.size(); i++){
            double error = correctAnswers.get(i) - predictedAnswers.get(i);
            sumSquare += (error * error);
        }
        return sumSquare / (correctAnswers.size());
    }

    // Mean square loss function - Revision
    public static Double meanSquareLossREV2(List<List<Double>> correctAnswers, List<Double> predictedAnswers){
        double sumSquare = 0;
        for (int i = 0; i < correctAnswers.size(); i++){
            for (int j = 0; j < correctAnswers.get(i).size(); j++){
                double error = correctAnswers.get(i).get(j) - predictedAnswers.get(i);
                sumSquare += (error * error);
            }
            //double error = correctAnswers.get(i) - predictedAnswers.get(i);
            //sumSquare += (error * error);
        }
        return sumSquare / (correctAnswers.size());
    }

    /*
    public static Double meanSquareLoss(List<List<Double>> correctAnswers, List<List<Double>> predictedAnswers){
        double sumSquare = 0;

        // Ensure the sizes of the correctAnswers and predictedAnswers lists are the same
        if (correctAnswers.size() != predictedAnswers.size()) {
            throw new IllegalArgumentException("The number of samples in correctAnswers and predictedAnswers must match.");
        }

        // Iterate through all data points
        for (int i = 0; i < correctAnswers.size(); i++){
            List<Double> trueValues = correctAnswers.get(i);
            List<Double> predictedValues = predictedAnswers.get(i);

            // Ensure the number of outputs matches
            if (trueValues.size() != predictedValues.size()) {
                throw new IllegalArgumentException("The number of outputs in correctAnswers and predictedAnswers must match.");
            }

            // Iterate through all the outputs
            for (int j = 0; j < trueValues.size(); j++){
                double error = trueValues.get(j) - predictedValues.get(j); // Calculate error for each output
                sumSquare += (error * error); // Accumulate squared error
            }
        }

        // Return the mean squared error (average of squared errors)
        return sumSquare / (correctAnswers.size() * correctAnswers.get(0).size());
    }
    */

    //Rewrote the meanSquareLoss to avoid "same erasure" error
    public static double meanSquareLoss(double[][] correctAnswers, double[][] predictedAnswers) {
        double sumSquare = 0;
        int numSamples = correctAnswers.length;
        int numOutputs = correctAnswers[0].length;

        for (int i = 0; i < numSamples; i++) {
            for (int j = 0; j < numOutputs; j++) {
                double error = correctAnswers[i][j] - predictedAnswers[i][j];
                sumSquare += (error * error);
            }
        }
        return sumSquare / (numSamples * numOutputs);
    }

    public static double[][] convertToArray(List<List<Double>> list) {
        double[][] array = new double[list.size()][list.get(0).size()];
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.get(i).size(); j++) {
                array[i][j] = list.get(i).get(j);
            }
        }
        return array;
    }
}
