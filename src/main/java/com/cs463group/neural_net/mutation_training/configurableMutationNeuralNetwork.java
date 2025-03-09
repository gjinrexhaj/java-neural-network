package com.cs463group.neural_net.mutation_training;

import jdk.net.NetworkPermission;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Random;
import java.lang.Math;

public class configurableMutationNeuralNetwork {

    public static void main( String[] args ) {
        configurableMutationNeuralNetwork app = new configurableMutationNeuralNetwork();
        app.trainAndPredict();
    }

    public void trainAndPredict() {


        // create the network
        Network network500= new Network(500,2, 3, 2, 1);

        // create and load 2d arraylist with data, and create list containing corresponding answers
        List<List<Double>> data = new ArrayList<List<Double>>();
        data.add(Arrays.asList(115.0, 66.0));
        data.add(Arrays.asList(175.0, 78.0));
        data.add(Arrays.asList(205.0, 72.0));
        data.add(Arrays.asList(120.0, 67.0));
        List<Double> answers = Arrays.asList(1.0,0.0,0.0,1.0);

        // train the network with aforementioned data and answers
        network500.train(data, answers);

        // inputs for prediction
        List<Double> input1 = List.of(167.0, 73.0);
        List<Double> input2 = List.of(105.0, 67.0);
        List<Double> input3 = List.of(120.0, 72.0);
        List<Double> input4 = List.of(143.0, 67.0);
        List<Double> input5 = List.of(130.0, 66.0);

        // 0 is male, 1 is female
        System.out.println(input1 + " | Expected output: 0 | Actual output: " + network500.predict(input1));
        System.out.println(input2 + " | Expected output: 1 | Actual output: " + network500.predict(input2));
        System.out.println(input3 + " | Expected output: 1 | Actual output: " + network500.predict(input3));
        System.out.println(input4 + " | Expected output: 0 | Actual output: " + network500.predict(input4));
        System.out.println(input5 + " | Expected output: 0 | Actual output: " + network500.predict(input5));



        /*         // TODO: figure out how to format elements in list then print said elements with specified formatting
        System.out.println(String.format("  male, 167, 73: network500: %.10f", network500.predict(input1)));
        System.out.println(String.format("female, 105, 67: network500: %.10f", network500.predict(input2)));
        System.out.println(String.format("female, 120, 72: network500: %.10f", network500.predict(input3)));
        System.out.println(String.format("  male, 143, 67: network500: %.10f", network500.predict(input4)));
        System.out.println(String.format(" male', 130, 66: network500: %.10f", network500.predict(input5)));
        */
    }


    class Network {
        public int epochs;
        public Double learnFactor = null;

        // parametrized attributes
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


        // TODO: retrofit mutation training to allow for multiple outputs and whatnot
        // TODO: verified with 3 2 1 network configuration case
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




        // TODO: Parameterize mutation training method
        // TODO: DATA ENTRIES MUST EQUAL TO NUMBER OF INPUTS

        public void train(List<List<Double>> data, List<Double> answers){
            Double bestEpochLoss = null;
            for (int epoch = 0; epoch < epochs; epoch++){
                // pick a random neuron to adjust, mutate it with learnRate constructor parameter
                Neuron epochNeuron = network.get(epoch % network.size());
                epochNeuron.mutate(this.learnFactor);

                List<Double> predictions = new ArrayList<Double>();
                for (int i = 0; i < data.size(); i++){
                    // TODO: possibly change how data is stored (maybe not have it in a list of lists)
                    predictions.addAll(i, this.predict(data.get(i)));
                }
                Double thisEpochLoss = Util.meanSquareLoss(answers, predictions);

                if (epoch % 10 == 0) System.out.println(String.format("Epoch: %s | bestEpochLoss: %.15f | thisEpochLoss: %.15f", epoch, bestEpochLoss, thisEpochLoss));

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






    class Neuron {
        Random random = new Random();

        // new code, initialize weights and bias upon construction
        private int numOfWeights;
        public List<Double> weights = new ArrayList<>();
        // init onld and new bias with temp values to avoid null
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


        // TODO: TEST ALL CHANGES MADE BELOW, AND MAKE SURE THEY WORK AS INTENDED
        public void printAttributes() {
            System.out.printf("--- NEURON ATTRIBUTES ---" +
                    "\noldBias: %.15f | bias: %.15f " +
                    "\noldWeights: " + oldWeights.toString() +
                    "\nweights: " + weights.toString() +"\n\n", oldBias, bias);
        }

        // TODO: remove legacy comments when modular solution proves corerct in testing
        public void mutate(Double learnFactor){

            int changeWeightOrBias = random.nextInt(0,1);
            Double changeFactor = (learnFactor == null) ? random.nextDouble(-1, 1) : (learnFactor * random.nextDouble(-1, 1));
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
        // TODO: remove legacy comments when modular solution proves correct in testing
        public void forget(){
            bias = oldBias;

            for(int i = 0; i < numOfWeights; i++) {
                weights.set(i, oldWeights.get(i));
            }

            //weight1 = oldWeight1;
            //weight2 = oldWeight2;
        }
        public void remember(){
            oldBias = bias;

            for(int i = 0; i < numOfWeights; i++) {
                oldWeights.set(i, weights.get(i));
            }
            //oldWeight1 = weight1;
            //oldWeight2 = weight2;
        }

        public double compute(List<Double> inputs) {
            // iterate through all values, multiply each index by its associated weight, then add the bias at the end
            double preActivation = 0;

            for(int i = 0; i < numOfWeights; i++){
                preActivation += weights.get(i).doubleValue() * inputs.get(i);
            }
            preActivation += bias;
            double output = Util.sigmoid(preActivation);
            return output;
        }
    }





    class Util {
        public static double sigmoid(double in){
            return 1 / (1 + Math.exp(-in));
        }
        public static double sigmoidDeriv(double in){
            double sigmoid = Util.sigmoid(in);
            return sigmoid * (1 - in);
        }
        /** Assumes array args are same length */
        public static Double meanSquareLoss(List<Double> correctAnswers, List<Double> predictedAnswers){
            double sumSquare = 0;
            for (int i = 0; i < correctAnswers.size(); i++){
                double error = correctAnswers.get(i) - predictedAnswers.get(i);
                sumSquare += (error * error);
            }
            return sumSquare / (correctAnswers.size());
        }
    }
}