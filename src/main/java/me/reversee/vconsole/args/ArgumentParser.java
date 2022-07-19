package me.reversee.vconsole.args;

import java.util.HashMap;

public class ArgumentParser {
    public static HashMap<String, Object> getArgumentHashMap(String[] args) {

        HashMap<String, Object> finalMap = new HashMap<>();

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
                    case "clear-cache" -> finalMap.put("clear-cache", true);
                    case "clear-logs" ->  finalMap.put("clear-logs", true);
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

    public static boolean argumentMeetRequirements(HashMap<String, Object> arg_map) {
        boolean containsRequiredKeys = false;

        if (arg_map.containsKey("rom_file")) {
            containsRequiredKeys = true;
        } else if (arg_map.containsKey("make_blank_rom_file")) {
            containsRequiredKeys = true;
        }

        return containsRequiredKeys;
    }
}
