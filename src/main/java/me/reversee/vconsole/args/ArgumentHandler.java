package me.reversee.vconsole.args;

import me.reversee.vconsole.DoNothing;
import me.reversee.vconsole.exceptions.NotImplementedException;
import me.reversee.vconsole.rom.RomCompiler;
import me.reversee.vconsole.rom.RomExecutor;
import me.reversee.vconsole.rom.RomFile;
import me.reversee.vconsole.rom.RomManager;
import me.reversee.vconsole.util.Logger;

import java.util.HashMap;
import java.util.Map;

public class ArgumentHandler {
    public static void _handleEvents(HashMap<String, Object> arg_map) throws NotImplementedException {
        for (Map.Entry<String, Object> entry : arg_map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            Logger.log(key + " : " + value.toString(), Logger.logfile, true);
            DoNothing.invoke();

            switch (key) {
                case "debug_mode" -> {
                    Logger.log("Enabled debug mode!", Logger.logfile, true);
                }
                case "make_blank_rom_file" -> {
                    Logger.log("Creating blank.rom file!", Logger.logfile, true);
                    RomManager.CreateBlankRomFile();
                }
                case "compile_rom_source" -> {
                    Logger.log("Compiling rom from source", Logger.logfile, true);
                    RomCompiler.compileFromFile(String.valueOf(value));              }
                case "rom_file" -> {
                    Logger.log("Loading rom file", Logger.logfile, true);
                    RomFile rf = RomManager.fromFilePath(value.toString());
                    if (rf.isCMap) {
                        RomExecutor.executeRomCMapFile(rf);
                    } else {
                        RomExecutor.executeRomFile(rf);
                    }
                }
            }
        }
    }
}
