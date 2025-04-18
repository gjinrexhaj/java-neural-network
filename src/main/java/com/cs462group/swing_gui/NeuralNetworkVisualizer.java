/**
 *  NeuralNetVisualizer.java
 *  Created on 2/24/2025
 *  Widget that displays neural network as a graph of nodes and links
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


package com.cs462group.swing_gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class NeuralNetworkVisualizer extends JPanel {

    private List<Layer> layers;
    private int neuronSize = 30;
    private int lineWeight = 2;
    private Color networkColor = new Color(0, 122, 255);

    public NeuralNetworkVisualizer(List<Layer> layers) {
        this.layers = layers;
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(networkColor);
        int layerSpacing = getWidth() / (layers.size() + 1);
        // iterate over each layer
        for (int i = 0; i < layers.size(); i++) {
            Layer layer = layers.get(i);
            // programatically determine vertical spacing
            int neuronSpacing = getHeight() / (layer.neurons.size() + 1);
            // for each neuron that exists ona  given layer, draw them such that they all fit
            for (int j = 0; j < layer.neurons.size(); j++) {
                int x = (i + 1) * layerSpacing;
                int y = (j + 1) * neuronSpacing;
                drawNeuron(g, x, y);

                // draw lines connecting neurons between adjacent layers
                if (i > 0) {
                    Layer previousLayer = layers.get(i - 1);
                    int prevNeuronSpacing = getHeight() / (previousLayer.neurons.size() + 1);
                    for(int k = 0; k < previousLayer.neurons.size(); k++) {
                        int prevX = i * layerSpacing;
                        int prevY = (k + 1) * prevNeuronSpacing;
                        drawConnection(g, prevX, prevY, x, y);
                    }
                }
            }
        }
    }

    // Neuron node represented by circle
    private void drawNeuron(Graphics g, int x, int y) {
        g.fillOval(x - neuronSize/2, y - neuronSize/2, neuronSize, neuronSize);
    }

    // Connection link represented by a line
    private void drawConnection(Graphics g, int x1, int y1, int x2, int y2) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(lineWeight));
        g2.drawLine(x1, y1, x2, y2);
    }
}

class Neuron {
}

class Layer {
    List<Neuron> neurons;

    public Layer(int neuronCount) {
        neurons = new ArrayList<>();
        for (int i = 0; i < neuronCount; i++) {
            neurons.add(new Neuron());
        }
    }
}
