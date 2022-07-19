package me.reversee.vconsole;

import me.reversee.vconsole.exceptions.NotImplementedException;
import me.reversee.vconsole.args.ArgumentHandler;
import me.reversee.vconsole.args.ArgumentParser;
import me.reversee.vconsole.util.Logger;
import me.reversee.vconsole.util.Workspace;

import java.util.HashMap;

public class Main {

    public static void main(String[] args) throws NotImplementedException {

        HashMap<String, Object> arg_map = ArgumentParser.getArgumentHashMap(args);

        // Debug: skip this
        if (!ArgumentParser.argumentMeetRequirements(arg_map)) {
            Logger.log("Invalid usage!");
            System.exit(1);
        }

        // Launch here!
        Logger.logfile = Logger.init(); // INIT LOG

        // Prepare workspace
        Workspace.prepare();

        // Handle events
        ArgumentHandler._handleEvents(arg_map);

        System.exit(0);
    }

}
