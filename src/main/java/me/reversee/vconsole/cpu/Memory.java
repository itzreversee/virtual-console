package me.reversee.vconsole.cpu;

public interface Memory {

    // size
    int Size();

    // read byte from address and return it
    Byte readByte(int address);

    // write byte to address
    void writeByte(int address, byte value);
}
