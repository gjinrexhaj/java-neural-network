package com.cs462group.neural_net.gradient_descent;

import com.cs462group.neural_net.utils.Functions;

import java.util.Random;

public class DifferentialNeuron {

    Random random = new Random();
    // declare neuron attributes, initially randomize all weights and biases
    private Double bias = random.nextGaussian();
    private Double weight1 = random.nextGaussian();
    private Double weight2 = random.nextGaussian();
    private Double preActivation = null;
    private Double input1;
    private Double input2;

    // compute method
    public double compute(double input1, double input2) {
        return Functions.sigmoid(this.getSum(input1, input2));
    }

    // return attributes in form of string
    public String toString() {
        return "w1: " + this.weight1 + " w2: " + this.weight2 + " b: " + this.bias;
    }

    // getter methods
    public Double getWeight1() {
        return weight1;
    }
    public Double getWeight2() {
        return weight2;
    }
    public Double getBias() {
        return bias;
    }

    // setter methods
    public void setWeight1(Double w) {
        this.weight1 = w;
    }
    public void setWeight2(Double w) {
        this.weight2 = w;
    }
    public void setBias(Double b) {
        this.bias = b;
    }

    // computational methods
    public Double getSum(double input1, double input2) {
        return (this.weight1 * input1) + (this.weight2 * input2) + this.bias;
    }
    public Double getDerivedOutput(double input1, double input2) {
        return Functions.sigmoidDeriv(this.getSum(input1, input2));
    }
    public void adjust(Double w1, Double w2, Double b){
        this.weight1 -= w1;
        this.weight2 -= w2;
        this.bias -= b;
    }
}
