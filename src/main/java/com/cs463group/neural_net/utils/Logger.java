package com.cs463group.neural_net.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 *  Logger.java
 *  Created on 2/10/2025
 *  Defines a logger object which prints console outputs for warnings, errors, and debugging, as well as writing
 *  them to a newly created "log_file.txt" in the working directory. Logger automatically prepends timestamp to entry.
 *  USAGE:
 *      Logger myLogger = new Logger();
 *      myLogger.log(Logger.LogLevel.INFO, "Type whichever message you'd like to be logged here");
 *      myLogger.closeLogger();
 */

public class Logger {

    // member variables
    public static File file;
    public static FileWriter writer;
    public static boolean fileIsCreated;

    // Define all possible log types
    public enum LogLevel {
        DEBUG,
        INFO,
        WARNING,
        ERROR
    }

    // Log method, accepts an entry parameter (message to be logged) and a type parameter
    // specifying the messages designation in accordance with the LogLevel enum
    public static void log(LogLevel level, String entry, boolean showTimeStamp, boolean showColor) {
        if(!fileIsCreated) {
            createFile();
            createFileWriter();
            fileIsCreated = true;
        }

        try {
            // write timestamp before message
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String color;
            String logMessage;

            // change color depending on log level
            if(showColor) {
                switch (level) {
                    case LogLevel.ERROR -> color = "\u001B[31m";
                    case LogLevel.WARNING -> color = "\u001B[33m";
                    default -> color = "\u001B[0m";
                }
            } else {
                color = "";
            }

            // write to logfile, print log in console, toggle timestamp
            if (showTimeStamp) {
                 logMessage = "[" + LocalDateTime.now().format(formatter) + "] " +
                        levelToString(level) + ": " + entry + "\n";
            } else {
                logMessage = entry + "\n";
            }

            writer.write(logMessage);
            System.out.print(color + logMessage);


        } catch (IOException e) {
            System.out.println("log() failed!");
            e.printStackTrace();
        }
    }

    // Close logger when executed
    public static void closeLogger() {
        try {
            writer.close();
        } catch (IOException e) {
            System.out.println("closeLogger() failed!");
            e.printStackTrace();
        }
    }

    // Append log type to log, take input from enum and return appropriate string
    private static String levelToString(LogLevel level)
    {
        switch (level) {
            case DEBUG:
                return "DEBUG";
            case INFO:
                return "INFO";
            case WARNING:
                return "WARNING";
            case ERROR:
                return "ERROR";
            default:
                return "UNKNOWN";
        }
    }

    // Called by constructor, creates a new file in working directory if one doesn't already exist
    private static void createFile() {
        try {
            file = new File("log_file.txt");
            if (file.createNewFile()) {
                System.out.println(file.getName() + " successfully created in " + System.getProperty("user.dir"));
            } else {
                System.out.println(file.getName() + " already exists. File has been cleared and is now being written to");
            }

        } catch (IOException e) {
            System.out.println("createFile() failed!");
            e.printStackTrace();
        }
    }

    // Called by constructor, create file's associated file writer
    private static void createFileWriter() {
        try {
            writer = new FileWriter(file);
        } catch (IOException e) {
            System.out.println("createFileWriter() failed!");
            e.printStackTrace();
        }
    }
}
