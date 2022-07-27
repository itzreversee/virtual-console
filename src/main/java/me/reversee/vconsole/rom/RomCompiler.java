package me.reversee.vconsole.rom;

import me.reversee.vconsole.box._tokenValues;
import me.reversee.vconsole.util.FileUtil;
import me.reversee.vconsole.util.Logger;
import me.reversee.vconsole.util.StringTool;

import java.io.*;
import java.util.*;

import java.nio.file.Files;
import java.nio.file.Path;

import static me.reversee.vconsole.rom.CompilerOptions.*;

public class RomCompiler {
    public static LinkedHashMap<CompilerOptions, Object> opt = new LinkedHashMap<>();
    public static void compileFromFile(String filename) {

        // Get source code into memory.
        String content = null;

        Logger.log("Loading source code into memory...", Logger.logfile, true);
        try {
            Path filepath = Path.of(filename);
            content = Files.readString(filepath);
        } catch (OutOfMemoryError e) {
            Logger.log("Compiler ran out of memory!", Logger.logfile, true);
            e.printStackTrace();
        } catch (IOException e) {
            Logger.log("Caught IO Exception, is the path valid? Do you have read permissions?", Logger.logfile, true);
            e.printStackTrace();
        }

        // Loop through lines
        Logger.log("Tokenizing lines...", Logger.logfile, true);
        assert content != null;
        BufferedReader buffer = new BufferedReader(new StringReader(content));
        ArrayList<ArrayList<String>> TokenizedSource = new ArrayList<ArrayList<String>>(); // depth lol

        String line = null;
        int lc = 0;

        try {
            while ((line = buffer.readLine()) != null) {
                if (line.startsWith(";")) { continue; } // check for comment

                ArrayList<String> TokenizedLine = Tokenizer.getTokenizedArray(line);
                TokenizedSource.add(TokenizedLine);
                Logger.log("Tokenized line at index " + lc + ": " + TokenizedLine.toString(), Logger.logfile, true );
                lc++;
            }
        } catch (OutOfMemoryError e) {
            Logger.log("Compiler ran out of memory!", Logger.logfile, true);
            e.printStackTrace();
        } catch (IOException e) {
            Logger.log("Caught IO Exception!", Logger.logfile, true);
            e.printStackTrace();
        }

        String dot_rtsc_filename = StringTool.removeLastCharsUntil(filename, ".") + "rtsc";
        if (!(opt.containsKey(SkipRsc) && Boolean.valueOf(opt.get(SkipRsc).toString()) == true)) {
            FileUtil.writeToFile(TokenizedSource.toString(), dot_rtsc_filename, true);
        }

        // get compiled map
        Logger.log("Creating Compiled Map from Tokenized Source", Logger.logfile, true);

        // Yet another loop
        ArrayList<HashMap<_tokenValues, Object>> CompiledMap = new ArrayList<HashMap<_tokenValues, Object>>();
        try {
            for (int i = 0; i < TokenizedSource.size(); i++) {
                ArrayList<String> TokenizedLine = TokenizedSource.get(i);
                HashMap<_tokenValues, Object> CompiledMapLine = Tokenizer.getCompiledMap(TokenizedLine);
                CompiledMap.add(CompiledMapLine);
            }

        } catch (OutOfMemoryError e) {
            Logger.log("Compiler ran out of memory!", Logger.logfile, true);
            e.printStackTrace();
        }

        Logger.log("Writing Compiled Map from Tokenized Source to file", Logger.logfile, true);

        String dot_rom_cmap_filename = StringTool.removeLastCharsUntil(filename, ".") + "rom_cmap";
        if (opt.containsKey(OutputFile)) {
            dot_rom_cmap_filename = opt.get(OutputFile).toString();
        }
        try{
            FileOutputStream fos = new FileOutputStream(dot_rom_cmap_filename);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(CompiledMap);
            os.close();
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

}
