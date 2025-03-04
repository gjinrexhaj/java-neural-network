package com.cs463group.neural_net.mutation_training;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Random;
import java.lang.Math;

public class configurableMutationNeuralNetwork {

    int numOfInputNeurons;
    int numOfHiddenNeurons;
    int numOfOutputNeurons;

    public static void main( String[] args ) {
        configurableMutationNeuralNetwork app = new configurableMutationNeuralNetwork();
        app.trainAndPredict();
    }

    public void trainAndPredict() {
        List<List<Integer>> data = new ArrayList<List<Integer>>();

        Network network500= new Network(500, 1.0,2, 3, 2, 1);
        //Network network1000= new Network(1000, 1.0, 3, 2, 1);

        // prototpye training data // legacy
        data.add(Arrays.asList(115, 66));
        data.add(Arrays.asList(175, 78));
        data.add(Arrays.asList(205, 72));
        data.add(Arrays.asList(120, 67));
        List<Double> answers = Arrays.asList(1.0,0.0,0.0,1.0);

        //network500.train(data, answers);
        //network1000.train(data, answers);


        List<Double> input1 = List.of(167.0, 73.0);
        List<Double> input2 = List.of(105.0, 67.0);
        List<Double> input3 = List.of(120.0, 72.0);
        List<Double> input4 = List.of(143.0, 67.0);
        List<Double> input5 = List.of(130.0, 66.0);


        List<Double> testPrediction1 = network500.predict(input1);


        // TODO: figure out how to format elements in list then print said elements with specified formatting
        System.out.println(String.format("  male, 167, 73: network500: %.10f", network500.predict(input1)));
        System.out.println(String.format("female, 105, 67: network500: %.10f", network500.predict(input2)));
        System.out.println(String.format("female, 120, 72: network500: %.10f", network500.predict(input3)));
        System.out.println(String.format("  male, 143, 67: network500: %.10f", network500.predict(input4)));
        System.out.println(String.format(" male', 130, 66: network500: %.10f", network500.predict(input5)));



        // UNCOMMENT WHEN TEST
        /*
        System.out.println(String.format("  male, 167, 73: network500: %.10f | network1000: %.10f", network500.predict(167, 73), network1000.predict(167, 73)));
        System.out.println(String.format("female, 105, 67: network500: %.10f | network1000: %.10f", network500.predict(105, 67), network1000.predict(105, 67)));
        System.out.println(String.format("female, 120, 72: network500: %.10f | network1000: %.10f", network500.predict(120, 72), network1000.predict(120, 72)));
        System.out.println(String.format("  male, 143, 67: network500: %.10f | network1000: %.10f", network500.predict(143, 67), network1000.predict(120, 72)));
        System.out.println(String.format(" male', 130, 66: network500: %.10f | network1000: %.10f", network500.predict(130, 66), network1000.predict(130, 66)));
        */
/*
    Network network500learn1 = new Network(500, 2.0);
    network500learn1.train(data, answers);

    Network network1000learn1 = new Network(1000, 2.0);
    network1000learn1.train(data, answers);

    System.out.println("");
    System.out.println(String.format("  male, 167, 73: network500learn1: %.10f | network1000learn1: %.10f", network500learn1.predict(167, 73), network1000learn1.predict(167, 73)));
    System.out.println(String.format("female, 105, 67: network500learn1: %.10f | network1000learn1: %.10f", network500learn1.predict(105, 67), network1000learn1.predict(105, 67)));
    System.out.println(String.format("female, 120, 72: network500learn1: %.10f | network1000learn1: %.10f", network500learn1.predict(120, 72), network1000learn1.predict(120, 72)));
    System.out.println(String.format("  male, 143, 67: network500learn1: %.10f | network1000learn1: %.10f", network500learn1.predict(143, 67), network1000learn1.predict(120, 72)));
    System.out.println(String.format(" male', 130, 66: network500learn1: %.10f | network1000learn1: %.10f", network500learn1.predict(130, 66), network1000learn1.predict(130, 66)));
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

        // TODO: decide if this data field even needs to exist (might need it for gradient descent)
        // parameterize output attribute, keeps track of all
        public List<Double> outputs = new ArrayList<>();

        // linear representation of network
        List<Neuron> network = new ArrayList<>();

        public Network(int epochs, int numOfDataInputs, int numOfInputNeurons, int numOfHiddenNeurons, int numOfOutputNeurons) {
            this.epochs = epochs;
            this.numOfInputNeurons = numOfInputNeurons;
            this.numOfHiddenNeurons = numOfHiddenNeurons;
            this.numOfOutputNeurons = numOfOutputNeurons;
            this.numOfDataInputs = numOfDataInputs;

            // set prediction input data array equal to numOfDataInputs - 1

            // input layer
            for(int i = 0; i < numOfInputNeurons; i++) {
                Neuron neuron = new Neuron(0);
                network.add(neuron);
                System.out.println(network.get(i));
            }
            // hidden layer
            for(int i = numOfInputNeurons; i < numOfHiddenNeurons; i++) {
                Neuron neuron = new Neuron(numOfInputNeurons);
                network.add(neuron);
                System.out.println(network.get(i));
            }
            // output layer
            for(int i = numOfHiddenNeurons; i < numOfOutputNeurons; i++) {
                Neuron neuron = new Neuron(numOfOutputNeurons);
                network.add(neuron);
                System.out.println(network.get(i));
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


        // TODO: PARAMETRIZE predict ------- DO THIS AFTER NEURONS ARE FULLY PARAMETRIZED --- implement using a list
        // TODO: make is so predict can return multiple output depending on num of output neurons specified,
        // TODO: retrofit mutation training to allow for multiple outputs and whatnot
        // TODO: verified with 3 2 1 network configuration case
        public List<Double> predict(List<Double> inputs) {

            // param: inputs - which is the input data to be used
            // output, single value for the neuron to be compared with epoch
            List<Double> inputOutputs = new ArrayList<>();
            List<Double> hiddenOutputs = new ArrayList<>();
            List<Double> outputOutputs = new ArrayList<>();

            if(inputs.size() != numOfDataInputs) {
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
                double computation = network.get(i+j).compute(inputOutputs);
                outputs.add(computation);
                hiddenOutputs.add(computation);

                j++;
            }

            // iterate through all output neurons, compute output using preceding neuron outputs as their input
            while (k < numOfOutputNeurons) {
                double computation = network.get(i+j+k).compute(hiddenOutputs);
                outputs.add(computation);
                outputOutputs.add(computation);

                k++;
            }


            // TODO: make it possible so more than one output can be attained from compute, right now it just returns the first element of outputlist
            //Double tempoutput = outputOutputs.get(0);
            return outputOutputs;

            // idea- start with numOfDataEntries inputs, and iterate neurons 0 - numOfInputNeurons,
            // where your predicting each, then go from there progressively computing stuff using recursion possibly
            // can also do this by specifying limits for recursive case, base case is ofc when it reaches end of list

            // LEGACY CODE - UPDATE LATER using recursion and list parametrization
            /*
            return network.get(5).compute(
                    network.get(4).compute(
                            network.get(2).compute(inputs.get(0), inputs.get(1)),
                            network.get(1).compute(inputs.get(0), inputs.get(1))
                    ),
                    network.get(3).compute(
                            network.get(1).compute(inputs.get(0), inputs.get(1)),
                            network.get(0).compute(inputs.get(0), inputs.get(1))
                    )
            );
             */

        }

        /*LEGACY PREDICTION CODE
        public Double predict(Integer input1, Integer input2){

            // maybe recursively traverse and return neural net values
            return network.get(5).compute(
                    network.get(4).compute(
                            network.get(2).compute(input1, input2),
                            network.get(1).compute(input1, input2)
                    ),
                    network.get(3).compute(
                            network.get(1).compute(input1, input2),
                            network.get(0).compute(input1, input2)
                    )
            );
        }
         */

        // ERROR PART
        // TODO: LAST ACTUAL PART - PARAMETERIZE TRAIN FOR MUTATION TRAINING
        // TODO: PARAMETERIZE TRAIN ------- DO THIS AFTER NEURONS ARE FULLY PARAMETERIZED
        // TODO: DATA ENTRIES MUST EQUAL TO NUMBER OF INPUTS
        /*
        public void train(List<List<Integer>> data, List<Double> answers){
            Double bestEpochLoss = null;
            for (int epoch = 0; epoch < epochs; epoch++){
                // adapt neuron
                Neuron epochNeuron = network.get(epoch % 6);
                epochNeuron.mutate(this.learnFactor);

                List<Double> predictions = new ArrayList<Double>();
                for (int i = 0; i < data.size(); i++){
                    predictions.add(i, this.predict(data.get(i).get(0), data.get(i).get(1)));
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
        }*/
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

        /*private Double oldBias = random.nextDouble(-1, 1), bias = random.nextDouble(-1, 1);
        public Double oldWeight1 = random.nextDouble(-1, 1), weight1 = random.nextDouble(-1, 1);
        private Double oldWeight2 = random.nextDouble(-1, 1), weight2 = random.nextDouble(-1, 1);
        */

        // Iterate through all the neurons given weights

        // TODO: REMOVE LEGACY CODE AFTER TESTING
        public void printAttributes() {
            System.out.printf("--- NEURON ATTRIBUTES ---" +
                    "\noldBias: %.15f | bias: %.15f " +
                    "\noldWeights: " + oldWeights.toString() +
                    "\nweights: " + weights.toString() +"\n\n", oldBias, bias);
        }

        /*
        public String toString(){

            // maybe iterate through all weights and other stuff, concatenate them to a string, and print resulting string
            return String.format("\nNEURON ATTRIBUTES:" +
                    "\noldBias: %.15f | bias: %.15f " +
                    "\noldWeights: " + oldWeights.toString() +
                    "\nweights: " + weights.toString(), oldBias, bias);
            //return String.format("oldBias: %.15f | bias: %.15f | oldWeight1: %.15f | weight1: %.15f | oldWeight2: %.15f | weight2: %.15f", this.oldBias, this.bias, this.oldWeight1, this.weight1, this.oldWeight2, this.weight2);
        }
         */

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
            /*
            int propertyToChange = random.nextInt(0, 3);
            Double changeFactor = (learnFactor == null) ? random.nextDouble(-1, 1) : (learnFactor * random.nextDouble(-1, 1));
            if (propertyToChange == 0){
                this.bias += changeFactor;
            } else if (propertyToChange == 1){
                this.weight1 += changeFactor;
            } else {
                this.weight2 += changeFactor;
            };

             */
        }
        // TODO: remove legacy comments when modular solution proves corerct in testing
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

        // TODO: TODO TODO
        // TODO: parameterize compute (must also make parameter fields (num of inputs) a list, take inputs as param)
        //      TODO: could also make this function vlid and simply return the computation of each and every neuron using
        //      TOOD: their internal List<Double> weights list
        //  --- implement inputs using a list
        // TODO: TEST CURRENT SOLTUION - RETROFIT predict() TO WORK WITH THESE CHANGES

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

        /* LEGACY CODE
        public double compute(double input1, double input2){
            // FAILED ATTEMPT
            //double preActivation = bias;
            //for(int i = 0; i < numOfWeights; i++){
            //    preActivation += weights.get(i).doubleValue() * input1;
            //}
            //double output = Util.sigmoid(preActivation);
            //return output;

            // NEW ATTEMPT
            //double preActivation = 0.0;
            //for(int i = 0; i < numOfWeights; i++) {
            //    preActivation += weights.get(i)*;
            //}

            // LEGACY CODE
            double preActivation = (weights.get(0) * input1) + (weights.get(1) * input2) + bias;
            double output = Util.sigmoid(preActivation);
            return output;
        }
        */

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