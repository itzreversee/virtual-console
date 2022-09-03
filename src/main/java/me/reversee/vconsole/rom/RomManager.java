package me.reversee.vconsole.rom;

import me.reversee.vconsole.exceptions.NotImplementedException;
import me.reversee.vconsole.util.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class RomManager {
    public static void CreateBlankRomFile(String rom_file_name) { RomManager.WriteBlankRomFile(rom_file_name);           }
    public static void CreateBlankRomFile()                     { RomManager.WriteBlankRomFile("blank.rsc"); }

    public static void WriteBlankRomFile(String rom_file_name)  {
        try {
            File rom_file = new File(rom_file_name);
            FileWriter writer;
            if (rom_file.createNewFile()) {
                BufferedWriter outStream= new BufferedWriter(new FileWriter(rom_file, true));
                outStream.write("flg dbg" + System.lineSeparator());                       // Set flag debug
                outStream.write("mov 1, \"Hello, World!\" " + System.lineSeparator());    // move value "Hello, World!"   --> Memory address 1
                outStream.write("int 0x0A 1" + System.lineSeparator());                      // call 0x0A 1 which prints content of memory starting on byte 1
                outStream.write("hlt" + System.lineSeparator());                            // Stop execution
                outStream.flush();
                outStream.close();
                Logger.log("Blank rom source: " + rom_file.getPath());
            }
        } catch (IOException e) {
            Logger.log("An error occurred!");
            e.printStackTrace();
        }
    }

    public static RomFile fromFilePath(String cmap_file_path) throws NotImplementedException {
        RomFile rf = new RomFile();
        File f = new File(cmap_file_path);

        rf.file_path = f.getPath();
        rf.file_path_absolute = f.getAbsolutePath();
        rf.isCMap = f.getPath().endsWith(".rom_cmap");

        return rf;
    }
}
