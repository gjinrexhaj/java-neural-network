package com.cs463group.neural_net.mutation_training;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.lang.Math;

public class configurableMutationNeuralNetwork {

    public static void main( String[] args ) {
        configurableMutationNeuralNetwork app = new configurableMutationNeuralNetwork();
        app.trainAndPredict();
    }

    public void trainAndPredict() {
        // create the network
        Network testnet = new Network(1000,2, 3, 2, 1);

        // create and load 2d arraylist with data and separate 1d arraylist to hold answers
        List<List<Double>> data = new ArrayList<>();
        List<Double> answers = new ArrayList<>();

        // TESTER CODE: manually load test data into data list and each entry's corresponding answer in answer list
        data.add(Arrays.asList(115.0, 66.0));
        data.add(Arrays.asList(175.0, 78.0));
        data.add(Arrays.asList(205.0, 72.0));
        data.add(Arrays.asList(120.0, 67.0));

        answers.add(1.0);
        answers.add(0.0);
        answers.add(0.0);
        answers.add(1.0);

        // CAN ALSO LOAD DATA IN PROGRAMMATICALLY FROM TXT FILE LIKE SO
        // TODO: Implement a fileLoader function in Utils that loads data in a parametrized fashion
        // for this use case, the loaded dataset below is too large
        /*
        Scanner scan;
        File file = new File("test-data/weight-height-gender/Source-Data.txt");
        try {
            scan = new Scanner(file);
            scan.useDelimiter("[,\\n]");
            int i = 0;

            while (scan.hasNext()) {
                i++;
                String token = scan.next();
                if (i == 1) {
                    // data add 1 and next
                    String nextToken = scan.next();
                    data.add(Arrays.asList(Double.parseDouble(token), Double.parseDouble(nextToken)));
                    System.out.print(i + ": ");
                    System.out.println(token + ", " + nextToken);
                } else if (i == 2) {
                    // do nothing
                    System.out.print(i + ": ");
                    answers.add(Double.parseDouble(token));
                    System.out.println(token);
                    i = 0;
                }
            }

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
         */

        // train the network with aforementioned data and answers
        testnet.train(data, answers);

        // manually load inputs for prediction
        List<Double> input1 = List.of(167.0, 73.0);
        List<Double> input2 = List.of(105.0, 67.0);
        List<Double> input3 = List.of(120.0, 72.0);
        List<Double> input4 = List.of(143.0, 67.0);
        List<Double> input5 = List.of(130.0, 66.0);

        // log output of neural net against expected outputs: for this data, 0 is male and 1 is female
        System.out.println(input1 + " | Expected output: 0 | Actual output: " + testnet.predict(input1));
        System.out.println(input2 + " | Expected output: 1 | Actual output: " + testnet.predict(input2));
        System.out.println(input3 + " | Expected output: 1 | Actual output: " + testnet.predict(input3));
        System.out.println(input4 + " | Expected output: 0 | Actual output: " + testnet.predict(input4));
        System.out.println(input5 + " | Expected output: 0 | Actual output: " + testnet.predict(input5));
    }


    // Network class: Represents a collection of neurons and their interconnectedness
    class Network {
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

        // Train the neural-net via brute-forcing random mutations
        public void train(List<List<Double>> data, List<Double> answers){
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
                Double thisEpochLoss = Util.meanSquareLoss(answers, predictions);

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

    // Neuron class: defines the attributes and behaviors of a single neuron
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
            double output = Util.sigmoid(preActivation);
            return output;
        }
    }

    // Util class: Contains a bunch of helper methods to aid functions carried out by neural network
    static class Util {
        public static double sigmoid(double in){
            return 1 / (1 + Math.exp(-in));
        }
        public static double sigmoidDeriv(double in){
            double sigmoid = Util.sigmoid(in);
            return sigmoid * (1 - in);
        }

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