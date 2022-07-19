package me.reversee.vconsole.rom;

import me.reversee.vconsole.box.Instructions;
import me.reversee.vconsole.box._tokenValues;

import java.util.*;

import static me.reversee.vconsole.rom.RomFile.instructions;

public class Tokenizer {

    /* eg. Turns " mov PBA, "Hello, World" "
     * into ->  "mov",          "pba",       "Hello, World!"
     *          "Instruction",  "Address",   "Value-string"
     */

    // breaks string into pieces
    public ArrayList<String> getTokenizedString(String str) {
        return (ArrayList<String>) Collections.list(new StringTokenizer(str, " ")).stream()
                .map(token -> (String) token).toList();
    }

    public HashMap<_tokenValues, Object> getCompiledTokenizedString(ArrayList<String> tokenized_string) {

        HashMap<_tokenValues, Object> compiledTokenizedString = new HashMap<>();

        ListIterator<String> tokens = tokenized_string.listIterator();
        Instructions ci;
        ArrayList<_tokenValues> compileSet = new ArrayList<>();
        Object output;
        Integer i;

        while (tokens.hasNext()) {
            output = tokens.next();
            i = tokens.nextIndex();

            if (i == 0) {
                compiledTokenizedString.put(_tokenValues.Instruction, output);
                ci = Instructions.valueOf(String.valueOf(output));
                compileSet = getInstructionCompileSet(ci);
            } else if (i > 0) {
                if (isTokenValueValid(output, compileSet.get(i))) {
                    compiledTokenizedString.put(compileSet.get(i), output);
                }
            }

        }

        return compiledTokenizedString;
    }

    public ArrayList<_tokenValues> getInstructionCompileSet(Instructions instruction) {
        ArrayList<_tokenValues> compiledTokenizedString = new ArrayList<>();

        switch (instruction) {
            case MOV -> {
                compiledTokenizedString.add(_tokenValues.Address);
                compiledTokenizedString.add(_tokenValues.ValueAny);
            }
            case ADD, INC, DEC -> {
                compiledTokenizedString.add(_tokenValues.Address);
                compiledTokenizedString.add(_tokenValues.ValueInteger);
            }
            case FLG, INT -> compiledTokenizedString.add(_tokenValues.HexadecimalAddress);
        }

        return compiledTokenizedString;
    }

    public boolean isTokenValueValid(Object value, _tokenValues target) {
        if      (value instanceof String    && target == _tokenValues.ValueString)  { return true; }
        else if (value instanceof Integer   && target == _tokenValues.ValueInteger) { return true; }
        else if (value instanceof Boolean   && target == _tokenValues.ValueBoolean) { return true; }
        else if (value instanceof List      && target == _tokenValues.ValueList)    { return true; }
        
        return false;
    }

}

