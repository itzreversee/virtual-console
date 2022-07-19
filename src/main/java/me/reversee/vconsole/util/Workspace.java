package me.reversee.vconsole.util;

import java.io.File;

public class Workspace {
    public static String location = "";
    public static String rom_cache = "";
    public static String rom_saves = "";
    public static String installed_apps = "";
    public static String installed_updates = "";

    public static void prepare() {
        Workspace.location = System.getProperty("user.dir");
        Workspace.rom_cache = Workspace.location + "/rom_cache";
        Workspace.rom_saves = Workspace.location + "/rom_saves";
        Workspace.installed_apps = Workspace.location + "/install/app";
        Workspace.installed_updates = Workspace.location + "/install/update";

        Logger.log("Preparing workspace on: " + Workspace.location, Logger.logfile, true);

        File dir;

        dir = new File(Workspace.rom_cache);            if (!dir.exists()) {dir.mkdirs();}
        dir = new File(Workspace.rom_saves);            if (!dir.exists()) {dir.mkdirs();}
        dir = new File(Workspace.installed_apps);       if (!dir.exists()) {dir.mkdirs();}
        dir = new File(Workspace.installed_updates);    if (!dir.exists()) {dir.mkdirs();}

        Logger.log("Workspace ready!: ", Logger.logfile, true);
    }
    
    public static void cleanup() {}
}
