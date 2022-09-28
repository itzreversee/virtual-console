package me.reversee.vconsole.cpu;

import me.reversee.vconsole.exceptions.MemoryOverflowAttempt;

public interface Memory {

    // size
    int Size();

    // read byte from address and return it
    Byte readByte(int address) throws MemoryOverflowAttempt;

    // write byte to address
    void writeByte(int address, byte value) throws MemoryOverflowAttempt;
}
