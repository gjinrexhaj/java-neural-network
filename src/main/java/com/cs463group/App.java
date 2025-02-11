package com.cs463group;

/**
 *  App.java
 *  Created on 2/10/2025
 *  Launching point of the program.
 */

public class App {

    public static void main(String[] args) {
        Logger logger = new Logger();
        logger.log(Logger.LogLevel.INFO, "Main Method Launched!");
        logger.closeLogger();
    }
}