package com.cs463group.neural_net.mutation_training;
import com.cs463group.neural_net.utils.Functions;

import java.util.ArrayList;
import java.util.List;

/**
 *  Network.java
 *  Created on 2/12/2025
 *  Defines a collection of neurons. Top-level neurons are inputs, middle neurons are
 *  "hidden", final layer is outputs. Can return prediction from network.
 */

public class Network {
    public int epochs;
    public Double learnFactor;

    // parametrized network attributes
    public int numOfInputNeurons;
    public int numOfHiddenNeurons;
    public int numOfOutputNeurons;
    public int numOfDataInputs;
    public List<Double> outputs = new ArrayList<>();

    // linear representation of network
    List<Neuron> network = new ArrayList<>();

    // constructor
    public Network(int epochs, int numOfDataInputs, int numOfInputNeurons, int numOfHiddenNeurons, int numOfOutputNeurons) {
        this.epochs = epochs;
        this.learnFactor = null;
        this.numOfInputNeurons = numOfInputNeurons;
        this.numOfHiddenNeurons = numOfHiddenNeurons;
        this.numOfOutputNeurons = numOfOutputNeurons;
        this.numOfDataInputs = numOfDataInputs;

        // input layer
        for(int i = 0; i < numOfInputNeurons; i++) {
            Neuron neuron = new Neuron(numOfDataInputs);
            network.add(neuron);
            neuron.printAttributes();
        }
        // hidden layer
        for(int i = 0; i < numOfHiddenNeurons; i++) {
            Neuron neuron = new Neuron(numOfInputNeurons);
            network.add(neuron);
            neuron.printAttributes();
        }
        // output layer
        for(int i = 0; i < numOfOutputNeurons; i++) {
            Neuron neuron = new Neuron(numOfHiddenNeurons);
            network.add(neuron);
            neuron.printAttributes();
        }
    }

    public Network(int epochs, Double learnFactor, int numOfDataInputs, int numOfInputNeurons, int numOfHiddenNeurons, int numOfOutputNeurons) {
        this.epochs = epochs;
        this.learnFactor = learnFactor;
        this.numOfInputNeurons = numOfInputNeurons;
        this.numOfHiddenNeurons = numOfHiddenNeurons;
        this.numOfOutputNeurons = numOfOutputNeurons;
        this.numOfDataInputs = numOfDataInputs;

        // input layer
        for(int i = 0; i < numOfInputNeurons; i++) {
            Neuron neuron = new Neuron(numOfDataInputs);
            network.add(neuron);
            neuron.printAttributes();
        }
        // hidden layer
        for(int i = 0; i < numOfHiddenNeurons; i++) {
            Neuron neuron = new Neuron(numOfInputNeurons);
            network.add(neuron);
            neuron.printAttributes();
        }
        // output layer
        for(int i = 0; i < numOfOutputNeurons; i++) {
            Neuron neuron = new Neuron(numOfHiddenNeurons);
            network.add(neuron);
            neuron.printAttributes();
        }
    }

    public List<Double> predict(List<Double> inputs) {

        // param: inputs - which is the input data to be used
        // output, single value for the neuron to be compared with epoch
        List<Double> inputOutputs = new ArrayList<>();
        List<Double> hiddenOutputs = new ArrayList<>();
        List<Double> outputOutputs = new ArrayList<>();

        if (inputs.size() != numOfDataInputs) {
            System.out.println("Error: Too many or too few prediction inputs specified!");
            return null;
        }

        int i = 0;
        int j = 0;
        int k = 0;

        // iterate through all input neurons, compute their outputs and store them in "outputs" list respectively
        while (i < numOfInputNeurons) {
            double computation = network.get(i).compute(inputs);
            outputs.add(computation);
            inputOutputs.add(computation);

            i++;
        }

        // iterate through all hidden neurons, compute output using preceding neuron outputs as their input
        while (j < numOfHiddenNeurons) {
            double computation = network.get(i + j).compute(inputOutputs);
            outputs.add(computation);
            hiddenOutputs.add(computation);

            j++;
        }

        // iterate through all output neurons, compute output using preceding neuron outputs as their input
        while (k < numOfOutputNeurons) {
            double computation = network.get(i + j + k).compute(hiddenOutputs);
            outputs.add(computation);
            outputOutputs.add(computation);

            k++;
        }

        return outputOutputs;
    }

    // Train the neural-net via brute-forcing random mutations
    public void train(List<List<Double>> data, List<List<Double>> answers){
        Double bestEpochLoss = null;
        for (int epoch = 0; epoch < epochs; epoch++){
            // pick a random neuron to adjust, mutate it with learnRate constructor parameter
            int chooseNeuron = epoch % network.size();
            Neuron epochNeuron = network.get(chooseNeuron);
            epochNeuron.mutate(learnFactor);

            List<Double> predictions = new ArrayList<>();
            for (int i = 0; i < data.size(); i++){
                predictions.addAll(i, this.predict(data.get(i)));
            }
            Double thisEpochLoss = Functions.meanSquareLossREV2(answers, predictions);

            if (epoch % 10 == 0) System.out.printf("Epoch: %s | bestEpochLoss: %.15f | thisEpochLoss: %.15f%n", epoch, bestEpochLoss, thisEpochLoss);

            if (bestEpochLoss == null){
                bestEpochLoss = thisEpochLoss;
                epochNeuron.remember();
            } else {
                if (thisEpochLoss < bestEpochLoss){
                    bestEpochLoss = thisEpochLoss;
                    epochNeuron.remember();
                } else {
                    epochNeuron.forget();
                }
            }
        }
    }
}
