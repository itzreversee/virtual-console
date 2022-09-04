package me.reversee.vconsole.cpu;

import me.reversee.vconsole.util.Logger;

import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class MemoryManager implements Memory {
    private byte[] memory; // Virtual Console Memory
    private int size;

    public MemoryManager(int size) {
        this.size = size;
        this.memory = new byte[this.size];
        for (int i = 0; i < memory.length; i++) {
            this.memory[i] = (byte) 0;
        }
    }

    @Override
    public int Size() {
        return this.memory.length; // return length of memory byte array
    }

    @Override
    public Byte readByte(int address) {
        return this.memory[address];
    }

    @Override
    public void writeByte(int address, byte value) {
        this.memory[address] = value;
    }

    // advanced functions

    // Words
    public void writeWord(int addr, byte val1, byte val2) {
        this.memory[addr] = val1;
        this.memory[addr + 1] = val2;
    }

    public byte[] readWord(int addr) {
        return new byte[]{this.memory[addr], this.memory[addr + 1]};
    }

    // Byte Arrays
    public void writeByteArray(int addr, byte[] val) { // write byte array
        int addr_it = 0;
        for(byte b : val){
            this.memory[addr + addr_it] = b;
            addr_it++;
        }
        this.memory[addr + addr_it + 1] = -3; // write ending header
    }

    public byte[] readByteArray(int addr_start, int addr_end) { // read byte array from start to end
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int addr_cur = addr_start;
        while (!(addr_cur == addr_end)) {
            out.write(this.memory[addr_cur]);
            addr_cur++;
        }
        return out.toByteArray();
    }

    public byte[] readByteArray(int addr) { // read from byte array until header
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int addr_it = addr;
        while (!(this.memory[addr_it] == -3)) {
            out.write(this.memory[addr_it]);
            addr_it++;
        }
        return out.toByteArray();
    }

    // Dump
    public byte[] dump() { return this.memory; }
    public void dumpToFile(Path path) {
        try {
            Files.write(path, this.memory, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
        } catch (Exception e) {
            Logger.log("Cannot dump memory to file!");
            e.printStackTrace();
        }
    }
    public void loadFromFile(Path memoryDump)  {
        try {
            this.memory = Files.readAllBytes(memoryDump);
        } catch (Exception e) {
            Logger.log("Cannot read memory from file!");
            e.printStackTrace();
            System.exit(1);
        }

    }

    // mass operation
    public void clear() {
        this.memory = new byte[this.size];
    }

}
