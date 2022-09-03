package me.reversee.vconsole.args;

import java.util.LinkedHashMap;
public class ArgumentParser {
    public static LinkedHashMap<String, Object> getArgumentHashMap(String[] args) {

        LinkedHashMap<String, Object> finalMap = new LinkedHashMap<>();

        boolean insertNextOption = false;
        String optionBefore = "";

        for (Object entry : args) {
            if (insertNextOption) {
                finalMap.put(optionBefore, entry);
                insertNextOption = false;
            }
            if (entry.toString().startsWith("--")) {
                StringBuilder sb = new StringBuilder();
                sb.append(entry.toString().toLowerCase());     // convert to lower case
                sb.deleteCharAt(0);           // delete the '-' symbol 1/2
                sb.deleteCharAt(0);           // delete the '-' symbol 2/2
                String option_type = sb.toString();

                switch (option_type) { // check what option is it
                    case "skip-rtsc" -> finalMap.put("skip_rtsc", true);
                    case "clear-cache" -> finalMap.put("clear_cache", true);
                    case "clear-logs" ->  finalMap.put("clear_logs", true);
                    case "source-output" -> {
                        optionBefore = "output";
                        insertNextOption = true;
                    }
                    case "compile-rom-source" -> {
                        optionBefore = "compile_rom_source";
                        insertNextOption = true;
                    }
                    case "rom" -> {
                        optionBefore = "rom_file";
                        insertNextOption = true;
                    }
                    case "debug" -> finalMap.put("debug_mode", true);
                    case "make-blank" -> finalMap.put("make_blank_rom_file", true);
                }
            }
        }
        return finalMap;
    }

    public static boolean argumentMeetRequirements(LinkedHashMap<String, Object> arg_map) {
        boolean containsRequiredKeys = false;

        if (arg_map.containsKey("rom_file")) {
            containsRequiredKeys = true;
        } else if (arg_map.containsKey("make_blank_rom_file")) {
            containsRequiredKeys = true;
        } else if (arg_map.containsKey("compile_rom_source")) {
            containsRequiredKeys = true;
        } else if (arg_map.containsKey("clear_logs") || arg_map.containsKey("clear_cache")) {
            containsRequiredKeys = true;
        }

        return containsRequiredKeys;
    }
}
