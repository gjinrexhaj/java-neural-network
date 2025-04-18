package com.cs462group.neural_net.gradient_descent;

import com.cs462group.neural_net.utils.Functions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class cDifferentialNeuron {
    private     List<Double>        dlWeights;
    private     double              dBias;
    private     Random              rRandom = new Random();

    public cDifferentialNeuron(int inputSize) {
        dlWeights = new ArrayList<>();
        for (int i = 0; i < inputSize; i++) {
            dlWeights.add(rRandom.nextGaussian());
        }
        dBias = rRandom.nextGaussian();
    }

    public double compute(List<Double> inputs) {
        double sum = dBias;
        for (int i = 0; i < inputs.size(); i++) {
            sum += dlWeights.get(i) * inputs.get(i);
        }
        return Functions.sigmoid(sum);
    }

    public double getDerivedOutput(List<Double> inputs) {
        double sum = dBias;
        for (int i = 0; i < inputs.size(); i++) {
            sum += dlWeights.get(i) * inputs.get(i);
        }
        return Functions.sigmoidDeriv(sum);
    }

    public double getWeight(int index) {
        return dlWeights.get(index);
    }

    public void adjust(double loss, List<Double> inputs, double learnRate) {
        for (int i = 0; i < dlWeights.size(); i++) {
            dlWeights.set(i, dlWeights.get(i) - learnRate * loss * inputs.get(i));
        }
        dBias -= learnRate * loss;
    }
}