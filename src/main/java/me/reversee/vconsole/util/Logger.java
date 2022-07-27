package me.reversee.vconsole.util;

import java.io.*;

public class Logger {
    public static String logfile = "";
    public static boolean lldo = false; // low level debug output
    public static String init() {

        String file_prefix = DateControl.getCurrentDate("full");
        String logfile_path;

        try {
            File logfile = new File(file_prefix + "-log.log");
            logfile_path = logfile.getAbsolutePath();
            if (logfile.createNewFile()) {
                Logger.log("Log started!", logfile_path, false);
            } else {
                Logger.log("Bad date or filename!");
            }
        } catch (IOException e) {
            Logger.log("An error occurred!");
            e.printStackTrace();
            return null;
        }

        return logfile_path;
    }

    public static void log(String s, String logfile_path, boolean broadcast) { // to logfile with broadcast option

        // get invoker class

        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        String callerMethod = ste.getMethodName();
        String callerClass;

        try {
            callerClass = Class.forName(ste.getClassName()).getSimpleName();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        String source = callerClass + "/" + callerMethod; // if no values provided, use default
        String log_prefix = DateControl.getCurrentDate("hour"); // get special formatted date ( HOUR:MINUTE:SECOND )
        String exact_string = " [" + log_prefix + "] " + "[" + source + "]: "+ s; // build string

        try {
            BufferedWriter outStream= new BufferedWriter(new FileWriter(logfile_path, true));
            outStream.write(exact_string + System.lineSeparator()); outStream.flush(); outStream.close();
        } catch (IOException e) {
            Logger.log("Couldn't write to logfile!");
        }

        if (broadcast) { Logger.log(s, ste); } // also broadcast in console ( ste pass through )
    }

    public static void log(String s, String logfile_path) { // to logfile

        // get invoker class

        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        String callerMethod = ste.getMethodName();
        String callerClass;

        try {
            callerClass = Class.forName(ste.getClassName()).getSimpleName();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        String source = callerClass + "/" + callerMethod; // if no values provided, use default
        String log_prefix = DateControl.getCurrentDate("hour"); // get special formatted date ( HOUR:MINUTE:SECOND )
        String exact_string = " [" + log_prefix + "] " + "[" + source + "]: "+ s; // build string

        try {
            BufferedWriter outStream= new BufferedWriter(new FileWriter(logfile_path, true));
            outStream.write(exact_string + System.lineSeparator()); outStream.flush(); outStream.close();
        } catch (IOException e) {
            Logger.log("Couldn't write to logfile!");
        }
    }

    public static void log(String s, StackTraceElement ste) { // to console with forced stack trace

        // get invoker class

        String callerMethod = ste.getMethodName();
        String callerClass;

        try {
            callerClass = Class.forName(ste.getClassName()).getSimpleName();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        String log_prefix = DateControl.getCurrentDate("hour");
        System.out.println("[" + log_prefix + "] " + "[" + callerClass + "/" + callerMethod + "]: "+ s);
    }
    public static void log(String s) { // to console

        // get invoker class

        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        String callerMethod = ste.getMethodName();
        String callerClass;

        try {
            callerClass = Class.forName(ste.getClassName()).getSimpleName();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        String log_prefix = DateControl.getCurrentDate("hour");
        System.out.println("[" + log_prefix + "] " + "[" + callerClass + "/" + callerMethod + "]: "+ s);
    }

    public static void newline() { System.out.println(); }
    public static void newline(boolean broadcast) {

        if (!broadcast) {
            return;
        }

        System.out.println();

        try {
            BufferedWriter outStream= new BufferedWriter(new FileWriter(Logger.logfile, true));
            outStream.write(System.lineSeparator()); outStream.flush(); outStream.close();
        } catch (IOException e) {
            Logger.log("Couldn't write to logfile!");
        }

    }

    public static void lldo(String s) {

        String log_prefix = DateControl.getCurrentDate("hour"); // get special formatted date ( HOUR:MINUTE:SECOND )
        String exact_string = " [" + log_prefix + "] " + "{LLDO}: "+ s; // build string

        try {
            BufferedWriter outStream= new BufferedWriter(new FileWriter(Logger.logfile, true));
            outStream.write(exact_string + System.lineSeparator()); outStream.flush(); outStream.close();
        } catch (IOException e) {
            Logger.log("Couldn't write to logfile!");
        }

        if (Logger.lldo) {
            System.out.println(exact_string);
        }

    }

}
