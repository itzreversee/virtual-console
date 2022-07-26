package me.reversee.vconsole.rom;

import me.reversee.vconsole.box.ExecuteResults;
import me.reversee.vconsole.box.Instructions;
import me.reversee.vconsole.box.Registers;
import me.reversee.vconsole.box._tokenValues;
import me.reversee.vconsole.exceptions.NotImplementedException;
import me.reversee.vconsole.util.Logger;

import java.io.*;
import java.util.*;

import static me.reversee.vconsole.box.ExecuteResults.*;
import static me.reversee.vconsole.box.Instructions.*;
import static me.reversee.vconsole.box.Registers.*;

public class RomExecutor {

    public static void executeRomCMapFile(RomFile rom)  {
        ArrayList<HashMap<_tokenValues, Object>> CompiledMap = getCompiledMap(rom);
        if (CompiledMap == null) {
            Logger.log("Could not get Compiled Map from rom file", Logger.logfile, true);
            Thread.dumpStack();
            System.exit(1);
        }

        Logger.log("Launching Virtual Console");
        Logger.newline(true);

        boolean result;

        result = parseCMapInstructions(CompiledMap);

    }

    public static void executeRomFile(RomFile rom) throws NotImplementedException {
        throw new NotImplementedException();    // bytecode too hard, but maybe compression with metadata and encryption would be good
    }

    public static boolean parseCMapInstructions(ArrayList<HashMap<_tokenValues, Object>> CompiledMap)  {

        ExecuteResults r;

        for (HashMap<_tokenValues, Object> line : CompiledMap) {
            r = executeInstruction((LinkedHashMap<_tokenValues, Object>) line);
            switch (r) {
                case Instruction_Perfect -> {
                    if (Logger.lldo) {
                        Logger.lldo("Instruction " + line + " executed perfectly.");
                    }
                }
                case Instruction_Warning -> {
                    if (Logger.lldo) {
                        Logger.lldo("Instruction " + line + " executed with warnings.");
                    }
                }
                case Instruction_Error -> {
                    if (Logger.lldo) {
                        Logger.lldo("Instruction " + line + " did not execute correctly.");
                    }

                    Logger.log("An error occurred, and function had been destroyed.", Logger.logfile, true);
                    return false;

                }
                case Instruction_PerformanceFailure -> {
                    if (Logger.lldo) {
                        Logger.lldo("Instruction " + line + " executed with worse performance.");
                    }
                }
                case ToggleFlag_Debug -> {
                    Logger.lldo = true;
                    Logger.lldo("Toggling debug flag.");
                }
                case ToggleFlag_Retail -> {
                    if (Logger.lldo) {
                        Logger.lldo("Toggling retail flag.");
                    }
                    Logger.lldo = false;
                }
            } // Debug Results
        }

        return true;
    }

    public static ExecuteResults executeInstruction(LinkedHashMap<_tokenValues, Object> instruction) {

        Instructions currentInstructions;

        Iterator<Map.Entry<_tokenValues, Object>> it = instruction.entrySet().iterator();

        // fun
            Map.Entry<_tokenValues, Object> next = it.next();

            _tokenValues key = next.getKey();
            Object value = next.getValue();

            _tokenValues next_key;
            Object next_value;

            if (key == _tokenValues.Instruction) {
                currentInstructions = Instructions.valueOf(String.valueOf(value));
                switch (currentInstructions) {
                    case FLG -> {
                        if (Objects.equals(String.valueOf(next.getValue()), "0x0A")) {
                            return ToggleFlag_Debug;
                        } // toggle debug on
                        if (Objects.equals(String.valueOf(next.getValue()), "0x0B")) {
                            return ToggleFlag_Retail;
                        } // toggle debug off
                    }
                    case MOV -> {
                        next = it.next();       // move iterator by 1
                        next_key    = next.getKey();    // get key
                        next_value  = next.getValue();  // get value
                        if (!(next_key == _tokenValues.Address)) {
                            return Instruction_Error;
                        }
                        Registers reg = Registers.valueOf(String.valueOf(next_value));
                        next = it.next();       // move iterator by 1
                        next_key    = next.getKey();    // get key
                        next_value  = next.getValue();  // get value
                        if (!(next_key == _tokenValues.ValueAny)) {
                            return Instruction_Error;
                        }
                        switch (reg) {
                            case RMA -> {
                                VirtualMachineMemory.REGISTER_RMA = next_value;
                            }
                            case RMB -> {
                                VirtualMachineMemory.REGISTER_RMB = next_value;
                            }
                            case RMC -> {
                                VirtualMachineMemory.REGISTER_RMC = next_value;
                            }
                            case RMD -> {
                                VirtualMachineMemory.REGISTER_RMD = next_value;
                            }

                            case PBA -> {
                                VirtualMachineMemory.REGISTER_PBA = next_value;
                            }
                            case PBB -> {
                                VirtualMachineMemory.REGISTER_PBB = next_value;
                            }
                            case PBC -> {
                                VirtualMachineMemory.REGISTER_PBC = next_value;
                            }
                            case PBD -> {
                                VirtualMachineMemory.REGISTER_PBD = next_value;
                            }
                            case PBE -> {
                                VirtualMachineMemory.REGISTER_PBE = next_value;
                            }
                            case PBF -> {
                                VirtualMachineMemory.REGISTER_PBF = next_value;
                            }
                        }

                    }
                    case ADD -> {
                        next = it.next();       // move iterator by 1
                        next_key    = next.getKey();    // get key
                        next_value  = next.getValue();  // get value
                        if (!(next_key == _tokenValues.Address)) {
                            return Instruction_Error;
                        }
                        Registers reg = Registers.valueOf(String.valueOf(next.getValue()));
                        next = it.next();       // move iterator by 1
                        next_key    = next.getKey();    // get key
                        next_value  = next.getValue();  // get value
                        if (!(next_key == _tokenValues.ValueInteger)) {
                            return Instruction_Error;
                        }
                    }
                    case INC -> {

                    }
                    case DEC -> {

                    }
                    case VAR -> {

                    }
                    case INT -> {

                    }
                    case HLT -> {

                    }

                    case DMP -> {

                    }
                }
            }

        return Instruction_Warning;
    }

    public static List<Object> getMetadata(ArrayList<HashMap<_tokenValues, Object>> cmap) throws NotImplementedException {
        throw new NotImplementedException();
    }

    public static ArrayList<HashMap<_tokenValues, Object>> getCompiledMap(RomFile rom) {

        ArrayList<HashMap<_tokenValues, Object>> CompiledMap = new ArrayList<HashMap<_tokenValues, Object>>();
        String fpa = rom.file_path_absolute;

        try {
            FileInputStream fis = new FileInputStream(fpa);
            ObjectInputStream is = new ObjectInputStream(fis);
            try {
                CompiledMap = (ArrayList<HashMap<_tokenValues, Object>>) is.readObject();
            } catch (Exception e) {
                e.printStackTrace();
                Logger.log("That does not look like a valid rom file!",                             Logger.logfile, true);
                Logger.log("Try checking if it is compatible with this virtual console version",    Logger.logfile, true);
                Logger.log("    or if it isn't corrupted! ",                                        Logger.logfile, true);
                return null;
            }
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return CompiledMap;
    }

}
