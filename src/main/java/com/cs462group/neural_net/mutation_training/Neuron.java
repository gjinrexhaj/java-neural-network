/**
 *  Network.java
 *  Created on 2/11/2025
 *  Defines the behaviors and attributes for neurons to be used with mutation training
 *
 *  Copyright (C) 2025  Gjin Rexhaj
 *
 *     This program is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 */

package com.cs462group.neural_net.mutation_training;

import com.cs462group.neural_net.utils.Functions;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


class Neuron {
    Random random = new Random();

    // new code, initialize weights and bias upon construction
    private int numOfWeights;
    public List<Double> weights = new ArrayList<>();
    // init old and new bias with temp values to avoid null
    public Double bias = 0.0;

    public List<Double> oldWeights = new ArrayList<>();
    public Double oldBias = 0.0;

    // Constructor for hidden and output neurons
    public Neuron(int numberOfWeights) {
        // keep track of number of weights associated with neuron
        numOfWeights = numberOfWeights;

        // populate weights and bias with random values
        for(int i = 0; i < numOfWeights; i++) {
            oldWeights.add(random.nextDouble(-1, 1));
            weights.add(random.nextDouble(-1, 1));
            bias = random.nextDouble(-1,1);
        }
    }

    public void printAttributes() {
        System.out.printf("--- NEURON ATTRIBUTES ---" +
                "\noldBias: %.15f | bias: %.15f " +
                "\noldWeights: " + oldWeights.toString() +
                "\nweights: " + weights.toString() +"\n\n", oldBias, bias);
    }

    public void mutate(Double learnFactor){
        int changeWeightOrBias = random.nextInt(0,2);
        double changeFactor = (learnFactor == null) ? random.nextDouble(-1, 1) : (learnFactor * random.nextDouble(-1, 1));
        switch (changeWeightOrBias) {
            case 0:
                //change bias
                bias += changeFactor;
                break;
            case 1:
                //change random weight
                int weight = random.nextInt(numOfWeights);
                Double update = weights.get(weight);
                update += changeFactor;
                weights.set(weight, update);
                break;
        }
    }

    public void forget(){
        bias = oldBias;

        for(int i = 0; i < numOfWeights; i++) {
            weights.set(i, oldWeights.get(i));
        }
    }
    public void remember(){
        oldBias = bias;

        for(int i = 0; i < numOfWeights; i++) {
            oldWeights.set(i, weights.get(i));
        }
    }

    public double compute(List<Double> inputs) {
        // iterate through all values, multiply each index by its associated weight, then add the bias at the end
        double preActivation = 0;
        for(int i = 0; i < numOfWeights; i++){
            preActivation += weights.get(i) * inputs.get(i);
        }
        preActivation += bias;
        double output = Functions.sigmoid(preActivation);
        return output;
    }
}