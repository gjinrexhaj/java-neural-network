package com.cs463group.neural_net;


/**
 *  App.java
 *  Created on 2/10/2025
 *  Launching point of the program. Commandline variant
 */

public class App {

    public static void main(String[] args) {
        Logger logger = new Logger();
        logger.log(Logger.LogLevel.INFO, "This is an information message");
        logger.log(Logger.LogLevel.DEBUG, "This is a debug message");
        logger.log(Logger.LogLevel.WARNING, "This is a warning message");
        logger.log(Logger.LogLevel.ERROR, "This is an error message");


        Network network = new Network();
        Double prediction = network.predict(115, 66);

        logger.log(Logger.LogLevel.INFO, "prediction -> " + prediction);


        logger.closeLogger();
    }
}