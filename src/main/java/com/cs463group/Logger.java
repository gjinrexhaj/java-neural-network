package com.cs463group;

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

    // member variables, each logger has an associated File and FileWriter object
    public File file;
    public FileWriter writer;


    // Define all possible log types

    public enum LogLevel {
        DEBUG,
        INFO,
        WARNING,
        ERROR
    }

    // Constructor, create and open a .txt file to write logs to
    public Logger() {
        createFile();
        createFileWriter();
    }

    // Log method, accepts an entry parameter (message to be logged) and a type parameter
    // specifying the messages designation in accordance with the LogLevel enum
    public void log(LogLevel level, String entry) {

        try {
            // write timestamp before message
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String logMessage = "[" + LocalDateTime.now().format(formatter) + "] " +
                    levelToString(level) + ": " + entry + "\n";

            // write to logfile, print log in console
            writer.write(logMessage);
            System.out.print(logMessage);

        } catch (IOException e) {
            System.out.println("log() failed!");
            e.printStackTrace();
        }
    }

    // close logger when done
    public void closeLogger() {
        try {
            writer.close();
        } catch (IOException e) {
            System.out.println("closeLogger() failed!");
            e.printStackTrace();
        }
    }

    // append log type to log
    private String levelToString(LogLevel level)
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
    private void createFile() {
        try {
            file = new File("log_file.txt");
            if (file.createNewFile()) {
                System.out.print(file.getName() + " successfully created in ");
                System.out.println(System.getProperty("user.dir"));
            } else {
                System.out.println("Log file already exists.");
            }

        } catch (IOException e) {
            System.out.println("createFile() failed!");
            e.printStackTrace();
        }
    }

    private void createFileWriter() {
        try {
            writer = new FileWriter(file);
        } catch (IOException e) {
            System.out.println("createFileWriter() failed!");
            e.printStackTrace();
        }
    }

}
