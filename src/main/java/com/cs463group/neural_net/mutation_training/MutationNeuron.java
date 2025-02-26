package com.cs463group.neural_net.mutation_training;

import com.cs463group.neural_net.utils.Functions;

import java.util.Random;

/**
 *  Neuron.java
 *  Created on 2/11/2025
 *  Defines a neuron object which contains bias and weights, the value
 *  of the neuron is initialized with some random double between -1 and 1.
 *  Contains mutate, remember, and forget functions and stores old weights and biases.
 */

// TODO: programmatically make is so networks are dynamically configurable
public class MutationNeuron {

    // randomize initial bias and weights - used in mutation training
    Random random = new Random();
    private Double oldBias = random.nextDouble(-1, 1), bias = random.nextDouble(-1, 1);
    private Double oldWeight1 = random.nextDouble(-1, 1), weight1 = random.nextDouble(-1, 1);
    private Double oldWeight2 = random.nextDouble(-1, 1), weight2 = random.nextDouble(-1, 1);

    // Training type: mutate, randomly adjust weight and bias and compare to previous
    public void mutate(Double learnFactor) {
        int propertyToChange = random.nextInt(0,3);
        Double changeFactor = random.nextDouble(-1, 1);
        switch(propertyToChange) {
            case 0:
                this.bias += changeFactor;
                break;
            case 1:
                this.weight1 += changeFactor;
                break;
            default:
                this.weight2 += changeFactor;
                break;
        }
    }


    // Forget method
    public void forget() {
        bias = oldBias;
        weight1 = oldWeight1;
        weight2 = oldWeight2;
    }

    // Remember method
    public void remember() {
        oldBias = bias;
        oldWeight1 = weight1;
        oldWeight2 = weight2;
    }

    // compute preactivation, return it's sigmoid function output
    public double compute(double param1, double param2) {
        double preActivation = (this.weight1 * param1) + (this.weight2 * param2) + this.bias;
        double output = Functions.sigmoid(preActivation);
        return output;
    }
}
