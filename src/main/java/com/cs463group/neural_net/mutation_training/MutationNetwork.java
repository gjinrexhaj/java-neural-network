package com.cs463group.neural_net.mutation_training;

import com.cs463group.neural_net.utils.Logger;
import com.cs463group.neural_net.utils.Functions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *  Network.java
 *  Created on 2/12/2025
 *  Defines a collection of neurons. Top-level neurons are inputs, middle neurons are
 *  "hidden", final layer is outputs. Can return prediction from network.
 */
// TODO: programmatically make is so networks are dynamically configurable
public class MutationNetwork {

    int epochs = 0; //1000;
    Double learnFactor = null;

    // Constructor
    public MutationNetwork(int epochs){
        this.epochs = epochs;
        Logger.log(Logger.LogLevel.INFO, "Mutation neural network with " + epochs + " epochs has been successfuly created.", true, true);
    }

    // Constructor
    public MutationNetwork(int epochs, Double learnFactor) {
        this.epochs = epochs;
        this.learnFactor = learnFactor;
        Logger.log(Logger.LogLevel.INFO, "Mutation neural network with " + epochs + " epochs and learnFactor: " + learnFactor + " has been successfuly created.", true, true);

    }

    // Represent neural network as a collection of neurons placed in List
    List<MutationNeuron> neurons = Arrays.asList(
            new MutationNeuron(), new MutationNeuron(), new MutationNeuron(),                 /*  input nodes */
            new MutationNeuron(), new MutationNeuron(),                               /* hidden nodes */
            new MutationNeuron()                                              /* output nodes */
    );

    // Training via mutation
    public void train(List<List<Double>> data, List<Double> answers) {
        Logger.log(Logger.LogLevel.INFO, "Mutation training has begun with " + epochs + " specified training cycles.", true, true);
        Double bestEpochLoss = null;
        for (int epoch = 0; epoch < epochs; epoch++) {
            // adapt neuron
            MutationNeuron epochNeuron = neurons.get(epoch % 6);
            epochNeuron.mutate(this.learnFactor);

            List<Double> predictions = new ArrayList<Double>();
            for (int i = 0; i < data.size(); i++) {
                predictions.add(i, this.predict(data.get(i).get(0), data.get(i).get(1)));
            }
            Double thisEpochLoss = Functions.meanSquareLoss(answers, predictions);

            // LOGGING THE TRAINER
            if (epoch % 10 == 0) Logger.log(Logger.LogLevel.INFO, String.format("Epoch: %s | bestEpochLoss: %.15f | thisEpochLoss: %.15f", epoch, bestEpochLoss, thisEpochLoss), false, true);

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
        Logger.log(Logger.LogLevel.INFO, "Mutation network training has finished.", true, true);
    }


    // TODO: find a way to programmatically do this with a network of arbitrary size (recursion?)
    // FeedForward prediction process
    public Double predict(Double param1, Double param2) {
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
