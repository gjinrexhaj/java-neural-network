package com.cs463group.neural_net;

import java.util.Arrays;
import java.util.List;

/**
 *  Network.java
 *  Created on 2/12/2025
 *  Defines a collection of neurons. Top-level neurons are inputs, middle neurons are
 *  "hidden", final layer is outputs. Can return prediction from network.
 */
public class Network {

    // Represent neural network as a collection of neurons placed in List
    List<Neuron> neurons = Arrays.asList(
            new Neuron(), new Neuron(), new Neuron(),                 /*  input nodes */
            new Neuron(), new Neuron(),                               /* hidden nodes */
            new Neuron()                                              /* output nodes */
    );


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
