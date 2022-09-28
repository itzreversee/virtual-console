package me.reversee.vconsole.rom;

import me.reversee.vconsole.box.*;
import me.reversee.vconsole.cpu.MemoryManager;
import me.reversee.vconsole.exceptions.NotImplementedException;
import me.reversee.vconsole.util.Logger;
import me.reversee.vconsole.util.StringTool;

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
        MemoryManager memory = new MemoryManager(64); // Main Memory
        MemoryManager lm = new MemoryManager(4); // LoopMemory used for loops
        MemoryManager cm = new MemoryManager(1); // CompareMemory used for cmp/if/else etc.
        LinkedList<LinkedHashMap<_tokenValues, Object>> loopMap = new LinkedList<LinkedHashMap<_tokenValues, Object>>();

        int loop_count = 0;
        int instruction_count = 0;
        boolean loop_enabled = false;
        boolean loop_prep = false;
        boolean loop_inf = false;

        boolean cmp_result = false;
        boolean in_do_block = false;
        boolean do_is_equal = false;

        for (HashMap<_tokenValues, Object> line : CompiledMap) {
            if (line.isEmpty()) continue;
            if (loop_enabled && loop_prep) {
                if (seekForLoopEnd(line)) {
                    loop_prep = false;
                    instruction_count = (loop_count + 1) * loopMap.size();
                    continue;
                }
                loopMap.add((LinkedHashMap<_tokenValues, Object>) line);
                continue;
            }
            if (loop_enabled) {
                while (loop_enabled) {
                    System.out.println(loop_count);
                    System.out.println(instruction_count);
                    for (HashMap<_tokenValues, Object> loopline : loopMap) {
                        if (instruction_count <= 0 && !loop_inf) {
                            loop_enabled = false;
                            loop_prep = false;
                            instruction_count = 0;
                            loop_count = 0;
                            break;
                        }
                        if (!loop_inf) instruction_count--;
                        if(seekForEndDo(line) == DO_End && in_do_block)  {
                            in_do_block = false;
                        }
                        if (in_do_block) {
                            if (do_is_equal && !cmp_result) continue;
                            if (!do_is_equal && cmp_result) continue;
                        }
                        r = executeInstruction((LinkedHashMap<_tokenValues, Object>) loopline, memory, lm, cm);
                        cmp_result = Boolean.parseBoolean(String.valueOf(cm.readByte(0)!=0));
                        switch (r) {
                            case DEQ_Start -> {
                                in_do_block = true;
                                do_is_equal = true;
                            }
                            case DNQ_Start -> {
                                in_do_block = true;
                                do_is_equal = false;
                            }
                            case Loop_Break -> {
                                loop_enabled = false;
                                loop_prep = false;
                                instruction_count = 0;
                                loop_count = 0;
                                break;
                            }
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
                        }
                    }
                }
            } else {
                System.out.println(loopMap);
                if(seekForEndDo(line) == DO_End && in_do_block)  {
                    in_do_block = false;
                }
                if (in_do_block) {
                    if (do_is_equal && !cmp_result) continue;
                    if (!do_is_equal && cmp_result) continue;
                }
                r = executeInstruction((LinkedHashMap<_tokenValues, Object>) line, memory, lm, cm);
                cmp_result = Boolean.parseBoolean(String.valueOf(cm.readByte(0)!=0));
                switch (r) {
                    case DEQ_Start -> {
                        in_do_block = true;
                        do_is_equal = true;
                    }
                    case DNQ_Start -> {
                        in_do_block = true;
                        do_is_equal = false;
                    }
                    case Loop_Start -> {
                        String ls = new String(lm.readByteArray(0));
                        loop_count = Integer.valueOf(ls.trim());
                        if (loop_count == -1)  {
                            loop_inf = true;
                        } else { loop_inf = false; }
                        loop_enabled = true;
                        loop_prep = true;
                    }
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
                }
            } // Debug Results
        }
        return true;
    }

    private static ExecuteResults seekForEndDo(HashMap<_tokenValues, Object> line) {
        Instructions currentInstructions;

        Iterator<Map.Entry<_tokenValues, Object>> it = line.entrySet().iterator();

        Map.Entry<_tokenValues, Object> next = it.next();

        Object value = next.getValue();

        if (Instructions.valueOf(String.valueOf(value)) == Instructions.ENDDO) return DO_End;

        return Instruction_Perfect;
    }

    private static boolean seekForLoopEnd(HashMap<_tokenValues, Object> line) {
        Instructions currentInstructions;

        Iterator<Map.Entry<_tokenValues, Object>> it = line.entrySet().iterator();

        Map.Entry<_tokenValues, Object> next = it.next();

        Object value = next.getValue();

        if (Instructions.valueOf(String.valueOf(value)) == Instructions.ENDLOOP) return true;

        return false;
    }

    public static ExecuteResults executeInstruction(LinkedHashMap<_tokenValues, Object> instruction, MemoryManager memory, MemoryManager lm, MemoryManager cm) {

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
                        //System.exit(0);
                        int addr = 0;
                        if (next_value.toString().startsWith("$")) {
                            String tmp = next_value.toString();
                            tmp = StringTool.removeFirstChar(tmp);
                            if (!(VirtualMachineMemory.Variables.containsKey(tmp))) {
                                return Instruction_Error;
                            }
                            addr = Integer.parseInt(String.valueOf(VirtualMachineMemory.Variables.get(tmp)));
                        } else {
                            addr = Integer.parseInt((String) next_value);
                        }
                        next = it.next();       // move iterator by 1
                        next_value  = next.getValue();  // get value
                        Object val = next_value;

                        // write to memory
                        memory.writeByteArray(addr, val.toString().getBytes());
                        return Instruction_Perfect;
                    }  // move ValueAny into Address
                    case MVA -> {
                        Object val = null;
                        int write_addr;
                        next = it.next();       // move iterator by 1
                        next_key    = next.getKey();    // get key
                        next_value  = next.getValue();  // get value
                        if (next_key == _tokenValues.ValueInteger) {
                            write_addr = Integer.parseInt(String.valueOf(next_value));
                        } else {
                            return Instruction_Error;
                        }
                        next = it.next();       // move iterator by 1
                        next_key  = next.getKey();  // get value
                        next_value  = next.getValue();  // get value

                        if (next_key == _tokenValues.Variable && VirtualMachineMemory.Variables.containsKey(next_value)) {
                            String variable_name = String.valueOf(next_value);
                            val = VirtualMachineMemory.Variables.get(variable_name);
                        } else {
                            return Instruction_Error;
                        }

                        // write to memory
                        memory.writeByteArray(write_addr, val.toString().getBytes());
                        return Instruction_Perfect;

                    } // move Variable into Address
                    case MVV -> {
                        next = it.next();       // move iterator by 1
                        next_key    = next.getKey();    // get key
                        next_value  = next.getValue();  // get value
                        String variable_name = String.valueOf(next_value);
                        if (!(next_key == _tokenValues.Variable)  // check if it is a variable
                                && !(VirtualMachineMemory.Variables.containsKey(variable_name))) { // check if variable exists
                            return Instruction_Error;
                        }
                        next = it.next(); // move iterator by 1
                        next_value  = next.getValue();  // get value
                        Object variable_value;
                        variable_value = next_value; // get value
                        VirtualMachineMemory.Variables.put(variable_name, variable_value);
                        return Instruction_Perfect;
                    } // move ValueAny into Variable
                    case INC -> {
                        next = it.next();       // move iterator by 1
                        next_key = next.getKey();    // get key
                        next_value = next.getValue();  // get value
                        if (!(next_key == _tokenValues.Variable) && VirtualMachineMemory.Variables.containsKey(next_value)) {
                            return Instruction_Error;
                        }
                        String variable_name = String.valueOf(next_value);
                        int variable_content = Integer.parseInt(String.valueOf(VirtualMachineMemory.Variables.get(variable_name)));

                        next = it.next();       // move iterator by 1
                        next_key = next.getKey();    // get key
                        next_value = next.getValue();  // get value
                        if (!(next_key == _tokenValues.ValueInteger)) {
                            return Instruction_Error;
                        }
                        // add
                        VirtualMachineMemory.Variables.put(variable_name, variable_content + Integer.parseInt(String.valueOf(next_value)));
                        return Instruction_Perfect;
                    }
                    case DEC -> {
                        next = it.next();       // move iterator by 1
                        next_key = next.getKey();    // get key
                        next_value = next.getValue();  // get value
                        if (!(next_key == _tokenValues.Variable) && VirtualMachineMemory.Variables.containsKey(next_value)) {
                            return Instruction_Error;
                        }
                        String variable_name = String.valueOf(next_value);
                        int variable_content = Integer.parseInt(String.valueOf(VirtualMachineMemory.Variables.get(variable_name)));

                        next = it.next();       // move iterator by 1
                        next_key = next.getKey();    // get key
                        next_value = next.getValue();  // get value
                        if (!(next_key == _tokenValues.ValueInteger)) {
                            return Instruction_Error;
                        }
                        // decrement
                        VirtualMachineMemory.Variables.put(variable_name, variable_content - Integer.parseInt(String.valueOf(next_value)));
                        return Instruction_Perfect;
                    }
                    case ADD -> {
                        next = it.next();       // move iterator by 1
                        next_key = next.getKey();    // get key
                        next_value = next.getValue();  // get value
                        if (!(next_key == _tokenValues.VariableTarget) && VirtualMachineMemory.Variables.containsKey(next_value)) {
                            return Instruction_Error;
                        }
                        String var1 = String.valueOf(next_value);
                        int var1c = Integer.parseInt(String.valueOf(VirtualMachineMemory.Variables.get(var1)));

                        next = it.next();       // move iterator by 1
                        next_key = next.getKey();    // get key
                        next_value = next.getValue();  // get value
                        if (!(next_key == _tokenValues.Variable)) {
                            return Instruction_Error;
                        }
                        // add
                        String var2 = String.valueOf(next_value);
                        int var2c = Integer.parseInt(String.valueOf(VirtualMachineMemory.Variables.get(var2)));
                        VirtualMachineMemory.Variables.put(var1, var1c + var2c);
                        return Instruction_Perfect;
                    }
                    case MUL -> {
                        next = it.next();       // move iterator by 1
                        next_key = next.getKey();    // get key
                        next_value = next.getValue();  // get value
                        if (!(next_key == _tokenValues.VariableTarget) && VirtualMachineMemory.Variables.containsKey(next_value)) {
                            return Instruction_Error;
                        }
                        String var1 = String.valueOf(next_value);
                        int var1c = Integer.parseInt(String.valueOf(VirtualMachineMemory.Variables.get(var1)));

                        next = it.next();       // move iterator by 1
                        next_key = next.getKey();    // get key
                        next_value = next.getValue();  // get value
                        if (!(next_key == _tokenValues.Variable)) {
                            return Instruction_Error;
                        }
                        // add
                        String var2 = String.valueOf(next_value);
                        int var2c = Integer.parseInt(String.valueOf(VirtualMachineMemory.Variables.get(var2)));
                        VirtualMachineMemory.Variables.put(var1, var1c * var2c);
                        return Instruction_Perfect;
                    }
                    case DIV -> {
                        next = it.next();       // move iterator by 1
                        next_key = next.getKey();    // get key
                        next_value = next.getValue();  // get value
                        if (!(next_key == _tokenValues.VariableTarget) && VirtualMachineMemory.Variables.containsKey(next_value)) {
                            return Instruction_Error;
                        }
                        String var1 = String.valueOf(next_value);
                        int var1c = Integer.parseInt(String.valueOf(VirtualMachineMemory.Variables.get(var1)));

                        next = it.next();       // move iterator by 1
                        next_key = next.getKey();    // get key
                        next_value = next.getValue();  // get value
                        if (!(next_key == _tokenValues.Variable)) {
                            return Instruction_Error;
                        }
                        // add
                        String var2 = String.valueOf(next_value);
                        int var2c = Integer.parseInt(String.valueOf(VirtualMachineMemory.Variables.get(var2)));
                        VirtualMachineMemory.Variables.put(var1, var1c / var2c);
                        return Instruction_Perfect;
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
                        return Instruction_Perfect;
                    }
                    case LEN -> {
                        next = it.next();       // move iterator by 1
                        next_key    = next.getKey();    // get key
                        next_value  = next.getValue();  // get value
                        String var1 = String.valueOf(next_value);

                        next = it.next();       // move iterator by 1
                        next_key    = next.getKey();    // get key
                        next_value  = next.getValue();  // get value
                        String var2 = String.valueOf(next_value);
                        int length = String.valueOf(VirtualMachineMemory.Variables.get(var2)).length();
                        VirtualMachineMemory.Variables.put(var1, length);
                        return Instruction_Perfect;
                    }
                    case LOOP -> {
                        next = it.next();       // move iterator by 1
                        next_key    = next.getKey();    // get key
                        next_value  = next.getValue();  // get value
                        if (next_key != _tokenValues.ValueInteger) {
                            return Instruction_Error;
                        }
                        int loop_times = Integer.parseInt(String.valueOf(next_value)); // how many loops to do
                        lm.writeByteArray(0, String.valueOf(loop_times).getBytes());
                        return Loop_Start;
                    }
                    case ENDLOOP -> {
                        lm.clear();
                        return Loop_End;
                    }
                    case BREAKLOOP -> {
                        return Loop_Break;
                    }
                    case CMP -> {
                        next = it.next();       // move iterator by 1
                        next_key = next.getKey();    // get key
                        next_value = next.getValue();  // get value
                        if (!(next_key == _tokenValues.VariableTarget) && VirtualMachineMemory.Variables.containsKey(next_value)) {
                            return Instruction_Error;
                        }
                        String var1 = String.valueOf(next_value);
                        Object var1c = VirtualMachineMemory.Variables.get(var1);

                        next = it.next();       // move iterator by 1
                        next_key = next.getKey();    // get key
                        next_value = next.getValue();  // get value
                        if (!(next_key == _tokenValues.Variable) && VirtualMachineMemory.Variables.containsKey(next_value)) {
                            return Instruction_Error;
                        }
                        // add
                        String var2 = String.valueOf(next_value);
                        Object var2c = VirtualMachineMemory.Variables.get(var2);
                        boolean iseq = Objects.equals(var1c, var2c);
                        if (iseq) {
                            cm.writeByte(0, (byte) 1);
                        } else {
                            cm.writeByte(0, (byte) 0);
                        }
                        return Instruction_Perfect;
                    }
                    case DEQ -> {
                        return DEQ_Start;
                    }
                    case DNQ -> {
                        return DNQ_Start;
                    }
                    case ENDDO -> {
                        return DO_End;
                    }
                    case INT -> {
                        next = it.next();       // move iterator by 1
                        next_key = next.getKey();    // get key
                        next_value = next.getValue();  // get value
                        if (!(next_key == _tokenValues.HexadecimalAddress)) {
                            return Instruction_Error;
                        }
                        Interrupts r = Interrupts.valueOf(String.valueOf(next.getValue()));
                        switch (r) {
                            case INT_0X0A -> {
                                StringBuilder sb = new StringBuilder();
                                next = it.next();
                                byte[] ba = memory.readByteArray(Integer.parseInt((String) next.getValue()));
                                for (byte b : ba) {
                                    if (b == -3)
                                        break;
                                    sb.append((char) b);
                                }
                                System.out.print(sb.toString());
                                Logger.log("PRINT: " + sb.toString(), Logger.logfile);
                                return Instruction_Perfect;
                            }
                            case INT_0X0B -> {
                                return Instruction_Warning;
                            }
                            case INT_0X1A -> {
                                Scanner scn = new Scanner(System.in);  // Create a Scanner object
                                System.out.println(" > ");
                                String input = scn.nextLine();  // Read user input
                                next = it.next();
                                next_value = next.getValue();
                                String variable_name = String.valueOf(next_value);
                                // check if it is a variable
                                if (!VirtualMachineMemory.Variables.containsKey(variable_name)) { // check if variable exists
                                    return Instruction_Error;
                                }
                                VirtualMachineMemory.Variables.put(variable_name, input);
                                return Instruction_Perfect;
                            }
                            case INT_0XFF -> {
                                memory.clear();
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
                            Logger.lldo(" Memory Table: " + Arrays.toString(memory.dump()));
                            Logger.lldo(" Compare Result: " + Arrays.toString(cm.dump()));
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
