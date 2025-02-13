package com.cs463group.neural_net;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *  Network.java
 *  Created on 2/12/2025
 *  Defines a collection of neurons. Top-level neurons are inputs, middle neurons are
 *  "hidden", final layer is outputs. Can return prediction from network.
 */
public class Network {

    int epochs = 0; //1000;
    Double learnFactor = null;

    // Constructor
    public Network(int epochs){
        this.epochs = epochs;
        Logger.log(Logger.LogLevel.INFO, "Neural network with " + epochs + " epochs has been successfuly created.", true);
    }

    // Constructor
    public Network(int epochs, Double learnFactor) {
        this.epochs = epochs;
        this.learnFactor = learnFactor;

    }

    // Represent neural network as a collection of neurons placed in List
    List<Neuron> neurons = Arrays.asList(
            new Neuron(), new Neuron(), new Neuron(),                 /*  input nodes */
            new Neuron(), new Neuron(),                               /* hidden nodes */
            new Neuron()                                              /* output nodes */
    );

    // Training hardcoded epoch value of 1000 - epoch is number of cycles of training that will be performed.
    public void train(List<List<Integer>> data, List<Double> answers) {
        Logger.log(Logger.LogLevel.INFO, "Network training has begun with " + epochs + " specified training cycles.", true);
        Double bestEpochLoss = null;
        for (int epoch = 0; epoch < epochs; epoch++) {
            // adapt neuron
            Neuron epochNeuron = neurons.get(epoch % 6);
            epochNeuron.mutate(this.learnFactor);

            List<Double> predictions = new ArrayList<Double>();
            for (int i = 0; i < data.size(); i++) {
                predictions.add(i, this.predict(data.get(i).get(0), data.get(i).get(1)));
            }
            Double thisEpochLoss = Utilities.meanSquareLoss(answers, predictions);

            // LOGGING THE TRAINER
            // TODO: Implement with log system

            if (epoch % 10 == 0) Logger.log(Logger.LogLevel.INFO, String.format("Epoch: %s | bestEpochLoss: %.15f | thisEpochLoss: %.15f", epoch, bestEpochLoss, thisEpochLoss), false);

            if (bestEpochLoss == null) {
                bestEpochLoss = thisEpochLoss;
                epochNeuron.remember();
            } else {
                if (thisEpochLoss < bestEpochLoss) {
                    bestEpochLoss = thisEpochLoss;
                    epochNeuron.remember();
                } else {
                    epochNeuron.forget();
                }
            }
        }
        Logger.log(Logger.LogLevel.INFO, "Network training has finished.", true);
    }


    // TODO: find a way to programmatically do this with a network of arbitrary size (recursion?)
    // FeedForward prediction process
    public Double predict(Integer param1, Integer param2) {
        return neurons.get(5).compute(
                neurons.get(4).compute(
                        neurons.get(2).compute(param1, param2),
                        neurons.get(1).compute(param1, param2)
                ),
                neurons.get(3).compute(
                        neurons.get(1).compute(param1, param2),
                        neurons.get(0).compute(param1, param2)
                )
        );
    }

}
