package me.reversee.vconsole.args;

import me.reversee.vconsole.DoNothing;
import me.reversee.vconsole.box._tokenValues;
import me.reversee.vconsole.exceptions.NotImplementedException;
import me.reversee.vconsole.rom.RomCompiler;
import me.reversee.vconsole.rom.RomExecutor;
import me.reversee.vconsole.rom.RomFile;
import me.reversee.vconsole.rom.RomManager;
import me.reversee.vconsole.util.Logger;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static me.reversee.vconsole.rom.CompilerOptions.OutputFile;
import static me.reversee.vconsole.rom.CompilerOptions.SkipRtsc;

public class ArgumentHandler {
    public static void _handleEvents(LinkedHashMap<String, Object> arg_map) throws NotImplementedException {

        Set entry_set = arg_map.entrySet();

        Iterator<Map.Entry<String, Object>> it = entry_set.iterator();
        Map.Entry<String, Object> next;

        while (it.hasNext()) {
            next = it.next();
            String key = next.getKey();
            Object value = next.getValue();
            Logger.log(key + " : " + value.toString(), Logger.logfile, true);

            switch (key) {
                case "debug_mode" -> {
                    Logger.log("Enabled debug mode!", Logger.logfile, true);
                }
                case "make_blank_rom_file" -> {
                    Logger.log("Creating blank.rom file!", Logger.logfile, true);
                    RomManager.CreateBlankRomFile();
                }
                case "skip_rtsc" -> {
                    RomCompiler.opt.put(SkipRtsc, true);
                }
                case "source_output" -> {
                    RomCompiler.opt.put(OutputFile, value.toString());
                }
                case "compile_rom_source" -> {
                    Logger.log("Compiling rom from source", Logger.logfile, true);
                    RomCompiler.compileFromFile(String.valueOf(value));
                }
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
