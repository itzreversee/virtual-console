package me.reversee.vconsole.box;

public enum Instructions {
    FLG, // eg. flg 0x0A                    - sets a flag

    MOV, // eg. mov PBA, "Hello, World!"    - moves value into register
    ADD, // eg. add VAA, 10                 - adds value to register
    INC, // eg. INC VAA, 2                  - increments register by value
    DEC, // eg. DEC VAA, 2                  - decrements register by value

    INT, // eg. INT 0xA0                    - calls an interrupt

    HLT, // eg. HLT                         - halt thread
}
