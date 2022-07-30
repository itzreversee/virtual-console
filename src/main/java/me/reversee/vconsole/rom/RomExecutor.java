package me.reversee.vconsole.rom;

import me.reversee.vconsole.box.*;
import me.reversee.vconsole.exceptions.NotImplementedException;
import me.reversee.vconsole.util.Logger;

import java.io.*;
import java.util.*;

import static me.reversee.vconsole.box.ExecuteResults.*;

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
                    Logger.lldo("Instruction " + line + " executed perfectly.");
                }
                case Instruction_Warning -> {
                    Logger.lldo("Instruction " + line + " executed with warnings.");
                }
                case Instruction_Error -> {
                    Logger.lldo("Instruction " + line + " did not execute correctly.");
                    Logger.log("An error occurred, and function had been destroyed.", Logger.logfile, true);
                    return false;
                }
                case Instruction_PerformanceFailure -> {
                    Logger.lldo("Instruction " + line + " executed with worse performance.");
                }
                case ToggleFlag_Debug -> {
                    Logger.lldo = true;
                    Logger.lldo("Toggling debug flag.");
                }
                case ToggleFlag_Retail -> {
                    Logger.lldo("Toggling retail flag.");
                    Logger.lldo = false;
                }
                case Halt -> {
                    Logger.lldo("Function halted");
                    return true;
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
                        next = it.next();       // move iterator by 1
                        next_value  = next.getValue();  // get value
                        if (String.valueOf(next_value).equalsIgnoreCase("dbg")) {
                            return ToggleFlag_Debug;
                        } // toggle debug on
                        if (String.valueOf(next_value).equalsIgnoreCase("no-dbg")) {
                            return ToggleFlag_Retail;
                        } // toggle debug off
                        return Instruction_Warning;
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
                        next_value  = next.getValue();  // get value
                        Object val = next_value;
                        switch (reg) {
                            case RMA -> {
                                VirtualMachineMemory.REGISTER_RMA = val;
                                return Instruction_Perfect;
                            }
                            case RMB -> {
                                VirtualMachineMemory.REGISTER_RMB = val;
                                return Instruction_Perfect;
                            }
                            case RMC -> {
                                VirtualMachineMemory.REGISTER_RMC = val;
                                return Instruction_Perfect;
                            }
                            case RMD -> {
                                VirtualMachineMemory.REGISTER_RMD = val;
                                return Instruction_Perfect;
                            }
                            case PBA -> {
                                VirtualMachineMemory.REGISTER_PBA = val;
                                return Instruction_Perfect;
                            }
                            case PBB -> {
                                VirtualMachineMemory.REGISTER_PBB = val;
                                return Instruction_Perfect;
                            }
                            case PBC -> {
                                VirtualMachineMemory.REGISTER_PBC = val;
                                return Instruction_Perfect;
                            }
                            case PBD -> {
                                VirtualMachineMemory.REGISTER_PBD = val;
                                return Instruction_Perfect;
                            }
                            case PBE -> {
                                VirtualMachineMemory.REGISTER_PBE = val;
                                return Instruction_Perfect;
                            }
                            case PBF -> {
                                VirtualMachineMemory.REGISTER_PBF = val;
                                return Instruction_Perfect;
                            }
                            default -> {
                                return Instruction_Warning;
                            }
                        }
                    }
                    case MVA -> {
                        next = it.next();       // move iterator by 1
                        next_key    = next.getKey();    // get key
                        next_value  = next.getValue();  // get value
                        if (!(next_key == _tokenValues.Address)) {
                            return Instruction_Error;
                        }
                        Registers reg = Registers.valueOf(String.valueOf(next_value));
                        next = it.next();       // move iterator by 1
                        next_key  = next.getKey();  // get value
                        next_value  = next.getValue();  // get value
                        if (!(next_key == _tokenValues.AddressB)) {
                            return Instruction_Error;
                        }
                        Registers move_reg = Registers.valueOf(String.valueOf(next_value));
                        Object val = null;
                        switch (move_reg) { // get source
                            case RMA -> {
                                val = VirtualMachineMemory.REGISTER_RMA;
                            }
                            case RMB -> {
                                val =VirtualMachineMemory.REGISTER_RMB;
                            }
                            case RMC -> {
                                val = VirtualMachineMemory.REGISTER_RMC;
                            }
                            case RMD -> {
                                val = VirtualMachineMemory.REGISTER_RMD;
                            }
                            case PBA -> {
                                val = VirtualMachineMemory.REGISTER_PBA;
                            }
                            case PBB -> {
                                val = VirtualMachineMemory.REGISTER_PBB;
                            }
                            case PBC -> {
                                val = VirtualMachineMemory.REGISTER_PBC;
                            }
                            case PBD -> {
                                val = VirtualMachineMemory.REGISTER_PBD;
                            }
                            case PBE -> {
                                val = VirtualMachineMemory.REGISTER_PBE;
                            }
                            case PBF -> {
                                val = VirtualMachineMemory.REGISTER_PBF;
                            }
                            default -> {
                                return Instruction_Warning;
                            }
                        }
                        switch (reg) {  // write
                            case RMA -> {
                                VirtualMachineMemory.REGISTER_RMA = val;
                                return Instruction_Perfect;
                            }
                            case RMB -> {
                                VirtualMachineMemory.REGISTER_RMB = val;
                                return Instruction_Perfect;
                            }
                            case RMC -> {
                                VirtualMachineMemory.REGISTER_RMC = val;
                                return Instruction_Perfect;
                            }
                            case RMD -> {
                                VirtualMachineMemory.REGISTER_RMD = val;
                                return Instruction_Perfect;
                            }
                            case PBA -> {
                                VirtualMachineMemory.REGISTER_PBA = val;
                                return Instruction_Perfect;
                            }
                            case PBB -> {
                                VirtualMachineMemory.REGISTER_PBB = val;
                                return Instruction_Perfect;
                            }
                            case PBC -> {
                                VirtualMachineMemory.REGISTER_PBC = val;
                                return Instruction_Perfect;
                            }
                            case PBD -> {
                                VirtualMachineMemory.REGISTER_PBD = val;
                                return Instruction_Perfect;
                            }
                            case PBE -> {
                                VirtualMachineMemory.REGISTER_PBE = val;
                                return Instruction_Perfect;
                            }
                            case PBF -> {
                                VirtualMachineMemory.REGISTER_PBF = val;
                                return Instruction_Perfect;
                            }
                            default -> {
                                return Instruction_Warning;
                            }
                        }
                    }
                    case ADD, INC -> {
                        next = it.next();       // move iterator by 1
                        next_key = next.getKey();    // get key
                        next_value = next.getValue();  // get value
                        if (!(next_key == _tokenValues.Address)) {
                            return Instruction_Error;
                        }
                        Registers reg = Registers.valueOf(String.valueOf(next.getValue()));
                        next = it.next();       // move iterator by 1
                        next_key = next.getKey();    // get key
                        next_value = next.getValue();  // get value
                        if (!(next_key == _tokenValues.ValueInteger)) {
                            return Instruction_Error;
                        }
                        // add
                        switch (reg) {
                            case PBA -> {
                                VirtualMachineMemory.REGISTER_PBA = (Integer) next_value + (Integer) VirtualMachineMemory.REGISTER_PBA;
                                return Instruction_Perfect;
                            }
                            case PBB -> {
                                VirtualMachineMemory.REGISTER_PBB = (Integer) next_value + (Integer) VirtualMachineMemory.REGISTER_PBB;
                                return Instruction_Perfect;
                            }
                            case PBC -> {
                                VirtualMachineMemory.REGISTER_PBC = (Integer) next_value + (Integer) VirtualMachineMemory.REGISTER_PBC;
                                return Instruction_Perfect;
                            }
                            case PBD -> {
                                VirtualMachineMemory.REGISTER_PBD = (Integer) next_value + (Integer) VirtualMachineMemory.REGISTER_PBD;
                                return Instruction_Perfect;
                            }
                            case PBE -> {
                                VirtualMachineMemory.REGISTER_PBE = (Integer) next_value + (Integer) VirtualMachineMemory.REGISTER_PBE;
                                return Instruction_Perfect;
                            }
                            case PBF -> {
                                VirtualMachineMemory.REGISTER_PBF = (Integer) next_value + (Integer) VirtualMachineMemory.REGISTER_PBF;
                                return Instruction_Perfect;
                            }
                        }
                        return Instruction_Warning;
                    }
                    case DEC -> {
                        next = it.next();       // move iterator by 1
                        next_key = next.getKey();    // get key
                        next_value = next.getValue();  // get value
                        if (!(next_key == _tokenValues.Address)) {
                            return Instruction_Error;
                        }
                        Registers reg = Registers.valueOf(String.valueOf(next.getValue()));
                        next = it.next();       // move iterator by 1
                        next_key = next.getKey();    // get key
                        next_value = next.getValue();  // get value
                        if (!(next_key == _tokenValues.ValueInteger)) {
                            return Instruction_Error;
                        }
                        // add
                        switch (reg) {
                            case PBA -> {
                                VirtualMachineMemory.REGISTER_PBA = (Integer) next_value - (Integer) VirtualMachineMemory.REGISTER_PBA;
                                return Instruction_Perfect;
                            }
                            case PBB -> {
                                VirtualMachineMemory.REGISTER_PBB = (Integer) next_value - (Integer) VirtualMachineMemory.REGISTER_PBB;
                                return Instruction_Perfect;
                            }
                            case PBC -> {
                                VirtualMachineMemory.REGISTER_PBC = (Integer) next_value - (Integer) VirtualMachineMemory.REGISTER_PBC;
                                return Instruction_Perfect;
                            }
                            case PBD -> {
                                VirtualMachineMemory.REGISTER_PBD = (Integer) next_value - (Integer) VirtualMachineMemory.REGISTER_PBD;
                                return Instruction_Perfect;
                            }
                            case PBE -> {
                                VirtualMachineMemory.REGISTER_PBE = (Integer) next_value - (Integer) VirtualMachineMemory.REGISTER_PBE;
                                return Instruction_Perfect;
                            }
                            case PBF -> {
                                VirtualMachineMemory.REGISTER_PBF = (Integer) next_value - (Integer) VirtualMachineMemory.REGISTER_PBF;
                                return Instruction_Perfect;
                            }
                        }
                        return Instruction_Warning;
                    }
                    case VAR -> {
                        next = it.next();       // move iterator by 1
                        next_key = next.getKey();    // get key
                        next_value = next.getValue();  // get value
                        if (!(next_key == _tokenValues.ValueString)) {
                            return Instruction_Error;
                        }
                        String variable_name = String.valueOf(next_value);
                        VirtualMachineMemory.Variables.put(variable_name, null);
                    }
                    case INT -> {
                        next = it.next();       // move iterator by 1
                        next_key = next.getKey();    // get key
                        next_value = next.getValue();  // get value
                        if (!(next_key == _tokenValues.HexadecimalAddress)) {
                            return Instruction_Error;
                        }
                        Interrupts r = Interrupts.valueOf(String.valueOf(next.getValue()));
                        // add
                        switch (r) {
                            case INT_0X0A -> {
                                LinkedList<Object> l = new LinkedList<>();
                                l.add(VirtualMachineMemory.REGISTER_PBA);
                                l.add(VirtualMachineMemory.REGISTER_PBB);
                                l.add(VirtualMachineMemory.REGISTER_PBC);
                                l.add(VirtualMachineMemory.REGISTER_PBD);
                                l.add(VirtualMachineMemory.REGISTER_PBE);
                                l.add(VirtualMachineMemory.REGISTER_PBF);

                                for (Object o : l) {
                                    if (o == null)
                                        continue;
                                    System.out.print(o.toString());
                                }
                                return Instruction_Perfect;
                            }
                            case INT_0X0B -> {
                                VirtualMachineMemory.REGISTER_PBA = null;
                                VirtualMachineMemory.REGISTER_PBB = null;
                                VirtualMachineMemory.REGISTER_PBC = null;
                                VirtualMachineMemory.REGISTER_PBD = null;
                                VirtualMachineMemory.REGISTER_PBE = null;
                                VirtualMachineMemory.REGISTER_PBF = null;
                                return Instruction_Perfect;
                            }
                        }
                        return Instruction_Warning;
                    }
                    case HLT -> {
                        return Halt;
                    }
                    case DMP -> {
                        next = it.next();       // move iterator by 1
                        next_key = next.getKey();    // get key
                        next_value = next.getValue();  // get value
                        if (!(next_key == _tokenValues.ValueDebugString)) {
                            return Instruction_Error;
                        }
                        String d = next_value.toString();
                        if (Objects.equals(d, "Memory")) {
                            Logger.lldo("Memory Dump:");
                            Logger.lldo(" REGISTER_RMA: " + VirtualMachineMemory.REGISTER_RMA);
                            Logger.lldo(" REGISTER_RMB: " + VirtualMachineMemory.REGISTER_RMB);
                            Logger.lldo(" REGISTER_RMC: " + VirtualMachineMemory.REGISTER_RMC);
                            Logger.lldo(" REGISTER_RMD: " + VirtualMachineMemory.REGISTER_RMD);
                            Logger.lldo(" REGISTER_PBA: " + VirtualMachineMemory.REGISTER_PBA);
                            Logger.lldo(" REGISTER_PBB: " + VirtualMachineMemory.REGISTER_PBB);
                            Logger.lldo(" REGISTER_PBC: " + VirtualMachineMemory.REGISTER_PBC);
                            Logger.lldo(" REGISTER_PBD: " + VirtualMachineMemory.REGISTER_PBD);
                            Logger.lldo(" REGISTER_PBE: " + VirtualMachineMemory.REGISTER_PBE);
                            Logger.lldo(" REGISTER_PBF: " + VirtualMachineMemory.REGISTER_PBF);
                            Logger.lldo(" Variable Table: " + VirtualMachineMemory.Variables);
                            Logger.lldo("End dump");
                            return Instruction_Perfect;
                        }
                        return Instruction_Warning;
                    }
                    default -> {
                        return Instruction_Warning;
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
