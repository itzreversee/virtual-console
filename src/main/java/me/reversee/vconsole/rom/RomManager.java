package me.reversee.vconsole.rom;

import me.reversee.vconsole.exceptions.NotImplementedException;
import me.reversee.vconsole.util.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class RomManager {
    public static void CreateBlankRomFile(String rom_file_name) { RomManager.WriteBlankRomFile(rom_file_name);           }
    public static void CreateBlankRomFile()                     { RomManager.WriteBlankRomFile("blank.rom"); }

    public static void WriteBlankRomFile(String rom_file_name)  {
        try {
            File rom_file = new File(rom_file_name);
            FileWriter writer;
            if (rom_file.createNewFile()) {
                BufferedWriter outStream= new BufferedWriter(new FileWriter(rom_file, true));
                outStream.write("flg 0x01" + System.lineSeparator());                       // Set flag debug
                outStream.write("mov rma, \"Blank rom\" " + System.lineSeparator());        // move value "Blank rom"       --> rma ( RomManifestA ) which contains info about rom
                outStream.write("mov pba, \"Hello, World!\" " + System.lineSeparator());    // move value "Hello, World!"   --> pba ( PrintBufferA ) which contains values that will be printed on next 0x0A call
                outStream.write("int 0x0A " + System.lineSeparator());                      // call 0x0A which spits PrintBufferA-F content
                outStream.write("hlt" + System.lineSeparator());                            // Stop execution

                outStream.flush();
                outStream.close();
                Logger.log("Blank rom: " + rom_file.getPath());
            }
        } catch (IOException e) {
            Logger.log("An error occurred!");
            e.printStackTrace();
        }
    }

    public static RomFile fromFilePath(String rom_file_path) throws NotImplementedException {
        throw new NotImplementedException();
    }
}
