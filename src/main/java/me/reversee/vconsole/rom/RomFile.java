package me.reversee.vconsole.rom;

import java.util.ArrayList;

public class RomFile {

    // Basic information
    public static ArrayList<String> metadata = new ArrayList<String>();
    public static String[] flags = null;

    // Instructions
    public static String file_path = "";
    public static String file_path_absolute = "";
    public static boolean isCMap = false;
    public static boolean useAlternativeOpCode = false;

    // Signing
    public static boolean isSigned = false;
    public static boolean ignoreSignedStatus = true;

}
