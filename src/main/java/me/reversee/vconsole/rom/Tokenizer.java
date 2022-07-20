package me.reversee.vconsole.rom;

import me.reversee.vconsole.DoNothing;
import me.reversee.vconsole.box.Instructions;
import me.reversee.vconsole.box.Registers;
import me.reversee.vconsole.box._tokenValues;
import me.reversee.vconsole.util.Logger;
import me.reversee.vconsole.util.StringTool;

import java.util.*;

import static me.reversee.vconsole.rom.RomFile.instructions;

public class Tokenizer {

    /* eg. Turns " mov PBA, "Hello, World" "
     * into ->  "mov",          "pba",       "Hello, World!"
     *          "Instruction",  "Address",   "Value-string"
     */

    // breaks string into pieces
    public static ArrayList<String> getTokenizedArray(String str) {

        StringTokenizer st = new StringTokenizer(str);
        ArrayList<String> TokenizedArray = new ArrayList<String>();

        String TokenBuffer = null;
        int skip = 0;

        while (st.hasMoreTokens()) {
            String nextToken = st.nextToken();
            if (skip != 0) {
                skip -= 1;
                continue;
            }
            TokenBuffer = nextToken;

            if (TokenBuffer.startsWith("\"")) {
                // get quoted string
                TokenBuffer = StringTool.getQuote(str);

                // check how many tokens were skipped
                skip = getTokenCount(TokenBuffer);
            }

            if (TokenBuffer.endsWith(",")) { TokenBuffer = StringTool.removeLastChar(TokenBuffer); }

            TokenizedArray.add(TokenBuffer);

        }

        return TokenizedArray;

    }

    public static Integer getTokenCount(String str) {
        StringTokenizer st = new StringTokenizer(str);
        return st.countTokens();
    }

    public static LinkedHashMap<_tokenValues, Object> getCompiledMap(ArrayList<String> tokenized_string) {

        LinkedHashMap<_tokenValues, Object> compiledTokenizedString = new LinkedHashMap<>();

        ListIterator<String> tokens = tokenized_string.listIterator();
        Instructions ci;
        Registers ri;
        ArrayList<_tokenValues> compileSet = new ArrayList<>();
        Object output;
        int i;

        while (tokens.hasNext()) {
            output = tokens.next();
            i = tokens.nextIndex();

            if (i == 1) {
                Logger.log("Check instruction " + output, Logger.logfile, true);
                compiledTokenizedString.put(_tokenValues.Instruction, output);
                ci = Instructions.valueOf((output.toString().toUpperCase()));
                compileSet = getInstructionCompileSet(ci);
            } else if (i > 1) {
                Logger.log("Check parameters for : " + output.toString() + ", should be : " + (i - 2), Logger.logfile, true );
                try {
                    Registers.valueOf((output.toString().toUpperCase()));
                    Logger.log("Valid!", Logger.logfile, true);
                    compiledTokenizedString.put(compileSet.get(i - 2), output);
                    continue;
                } catch (Exception e) {
                    DoNothing.invoke();
                }

                if (isTokenValueValid(output, compileSet.get(i - 2))) {
                    Logger.log("Valid!", Logger.logfile, true);
                    compiledTokenizedString.put(compileSet.get(i - 2), output);
                } else {
                    System.out.println("Compiler error! Instruction needs " + compileSet.get(i));
                }
            }

        }

        return compiledTokenizedString;
    }

    public static ArrayList<_tokenValues> getInstructionCompileSet(Instructions instruction) {
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

    public static boolean isTokenValueValid(Object value, _tokenValues target) {
        if (target == _tokenValues.ValueAny) { return true; }
        else if (value instanceof String    && target == _tokenValues.ValueString)  { return true; }
        else if (value instanceof Integer   && target == _tokenValues.ValueInteger) { return true; }
        else if (value instanceof Boolean   && target == _tokenValues.ValueBoolean) { return true; }
        else if (value instanceof List      && target == _tokenValues.ValueList)    { return true; }
        else if (Character.isDigit(String.valueOf(value).charAt(0)) && String.valueOf(value).charAt(1) == 'x' && String.valueOf(value).length() == 4 && target == _tokenValues.HexadecimalAddress) {
            return true;
        } else {
            return false;
        }

    }

}

