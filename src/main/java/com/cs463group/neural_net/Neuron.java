package com.cs463group.neural_net;

import java.util.Random;

/**
 *  Neuron.java
 *  Created on 2/11/2025
 *  Defines a neuron object which contains bias and weights, the value
 *  of the neuron is initialized with some random double between -1 and 1
 */
public class Neuron {
    // randomize initial bias and weights
    Random random = new Random();
    private Double bias = random.nextDouble(-1, 1);
    private Double weight1 = random.nextDouble(-1, 1);
    private Double weight2 = random.nextDouble(-1, 1);

    // compute preactivation, return it's sigmoid function output
    public double compute(double param1, double param2) {
        double preActivation = (this.weight1 * param1) + (this.weight2 * param2) + this.bias;
        double output = Utilities.sigmoid(preActivation);
        return output;
    }
}
