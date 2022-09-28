package me.reversee.vconsole.exceptions;

import me.reversee.vconsole.util.Logger;

public class MemoryOverflowAttempt extends Exception{
    public MemoryOverflowAttempt() {
        Logger.lldo("Memory overflow attempt! Cutting rest of data.");
    }
}
