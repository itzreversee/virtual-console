package me.reversee.vconsole.rom;

import me.reversee.vconsole.DoNothing;
import me.reversee.vconsole.box.Instructions;
import me.reversee.vconsole.box.Registers;
import me.reversee.vconsole.box._tokenValues;
import me.reversee.vconsole.util.Logger;
import me.reversee.vconsole.util.StringTool;

import java.util.*;

public class Tokenizer {

    /* eg. Turns " mov PBA, "Hello, World" "
     * into ->  "mov",          "pba",       "Hello, World!"
     *          "Instruction",  "Address",   "ValueString"
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

            if (TokenBuffer.startsWith(";")) {
                break;
            }

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
                Logger.newline(true);
                Logger.log("Check instruction " + output, Logger.logfile, true);
                compiledTokenizedString.put(_tokenValues.Instruction, output.toString().toUpperCase());
                ci = Instructions.valueOf((output.toString().toUpperCase()));
                compileSet = getInstructionCompileSet(ci);
            } else if (i > 1) {
                Logger.log("Check parameters for : " + output.toString() + ", should be : " + _tokenValues.valueOf(String.valueOf(compileSet.get(i - 2))), Logger.logfile, true );
                try {
                    Registers.valueOf((output.toString().toUpperCase()));
                    Logger.log("Valid!", Logger.logfile, true);
                    compiledTokenizedString.put(compileSet.get(i - 2), output.toString().toUpperCase());
                    continue;
                } catch (Exception ignored) {}
                if (isTokenValueValid(output, compileSet.get(i - 2))) {
                    Logger.log("Valid!", Logger.logfile, true);
                    if (isTokenValueValid(output, _tokenValues.Address)) {
                        compiledTokenizedString.put(compileSet.get(i - 2), output.toString().toUpperCase());
                    }
                    else if (compileSet.get(i - 2) == _tokenValues.VariableOrAddress) {
                        if (output.toString().length() == 3) {
                            compiledTokenizedString.put(_tokenValues.Address, output);
                        } else {
                            compiledTokenizedString.put(_tokenValues.Variable, output);
                        }
                    }
                    else if (isTokenValueValid(output, _tokenValues.ValueString) && !(compileSet.get(i - 2) == _tokenValues.ValueDebugString) && !(compileSet.get(i - 2) == _tokenValues.Variable) && !(compileSet.get(i - 2) == _tokenValues.HexadecimalAddress) && !(compileSet.get(i - 2) == _tokenValues.ValueInteger) ) {
                        String fo = output.toString();
                        if (fo.startsWith("\"")) { // format string
                            fo = StringTool.removeFirstChar(fo); // remove first quote
                            fo = StringTool.removeLastChar(fo); // remove last quote
                        }
                        fo = fo.replace("\\n", System.lineSeparator()); // \n -> newline
                        fo = fo.replace("\\t", "\t"); // \t -> tab
                        fo = fo.replace("\\b", "\b"); // \b -> backspace
                        fo = fo.replace("\\r", "\r"); // \r -> carriage return
                        compiledTokenizedString.put(_tokenValues.ValueString, fo);
                    }
                    else if (isTokenValueValid(output, _tokenValues.HexadecimalAddress)) {
                        compiledTokenizedString.put(compileSet.get(i - 2), "INT_" + output.toString().toUpperCase());
                    }
                    else {
                        compiledTokenizedString.put(compileSet.get(i - 2), output);
                    }
                } else {
                    System.out.println("Compiler error! Instruction needs " + compileSet.get(i - 2));
                    System.exit(1);
                }
            }
        }
        return compiledTokenizedString;
    }

    public static ArrayList<_tokenValues> getInstructionCompileSet(Instructions instruction) {
        ArrayList<_tokenValues> compiledTokenizedString = new ArrayList<>();

        switch (instruction) {
            case MOV -> {
                compiledTokenizedString.add(_tokenValues.ValueAny);
                compiledTokenizedString.add(_tokenValues.ValueAny);
            }
            case MVA -> {
                compiledTokenizedString.add(_tokenValues.ValueInteger);
                compiledTokenizedString.add(_tokenValues.Variable);
            }
            case MVV -> {
                compiledTokenizedString.add(_tokenValues.Variable);
                compiledTokenizedString.add(_tokenValues.ValueAny);
            }
            case ADD, INC, DEC -> {
                compiledTokenizedString.add(_tokenValues.Variable);
                compiledTokenizedString.add(_tokenValues.ValueInteger);
            }
            case LEN -> {
                compiledTokenizedString.add(_tokenValues.Variable);
                compiledTokenizedString.add(_tokenValues.Variable);
            }
            case FLG, DMP -> compiledTokenizedString.add(_tokenValues.ValueDebugString);
            case VAR -> compiledTokenizedString.add(_tokenValues.ValueString);
            case INT -> {
                compiledTokenizedString.add(_tokenValues.HexadecimalAddress);
                compiledTokenizedString.add(_tokenValues.ValueInteger);
            }
        }

        return compiledTokenizedString;
    }

    public static boolean isTokenValueValid(Object value, _tokenValues target) {
        if (target == _tokenValues.ValueAny) { return true; }
        else if ( target == _tokenValues.ValueDebugString )  {
            if (value.toString().equalsIgnoreCase("MEMORY")) {
                return true;
            } else return value.toString().equalsIgnoreCase("DBG") || (value.toString().equalsIgnoreCase("NO-DBG"));
        }
        else if (value instanceof String    && target == _tokenValues.VariableOrAddress)  { return true; }
        else if (value instanceof String    && target == _tokenValues.Variable)      { return true; }
        else if (value instanceof String    && target == _tokenValues.ValueString)  { return true; }
        else if (target == _tokenValues.ValueInteger) {
            try {
                int x = Integer.parseInt(String.valueOf(value));
                return true;
            } catch (Exception ignored) {
                return false;
            }
        }
        else if (value instanceof Boolean   && target == _tokenValues.ValueBoolean) { return true; }
        else if (value instanceof List      && target == _tokenValues.ValueList)    { return true; }
        else return String.valueOf(value).length() == 4 && Character.isDigit(String.valueOf(value).charAt(0)) && String.valueOf(value).charAt(1) == 'x' && target == _tokenValues.HexadecimalAddress;

    }

}

